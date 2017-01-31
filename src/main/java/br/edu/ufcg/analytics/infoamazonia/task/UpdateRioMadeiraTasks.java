package br.edu.ufcg.analytics.infoamazonia.task;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import br.edu.ufcg.analytics.infoamazonia.model.EntryPk;
import br.edu.ufcg.analytics.infoamazonia.model.Station;
import br.edu.ufcg.analytics.infoamazonia.model.StationEntry;

@Component
public class UpdateRioMadeiraTasks extends UpdateTasks {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private static final long PORTOVELHO_ID = 15400000;
	private static final long ABUNA_ID = 15320002;
	private static final long MORADA_ID = 15326000;
	private static final long GUAJARA_ID = 15250000;
	
	private static final long RATE = 900000;
	private static final double A_1 = -0.115;
	private static final double A_2 = 0.994;
	private static final double A_3 = -0.014;
	private static final double A_4 = 0.023;

	public UpdateRioMadeiraTasks() {
		super(PORTOVELHO_ID, ABUNA_ID, MORADA_ID, GUAJARA_ID);
	}

//	@Scheduled(initialDelay=1000, fixedRate = RATE)
	@Override
	public void update() throws FileNotFoundException, ParseException {
		logger.info("Update Rio Madeira ");
		long time = System.currentTimeMillis();
		super.update();
		time = System.currentTimeMillis() - time;
		logger.info("Updated RioMadeira in " + time + " millis");
	}

	@Override
	protected StationEntry predict(long timestamp, Map<Long, Station> stations) {
		
		Station portoVelho = stations.get(PORTOVELHO_ID);
		Station abuna = stations.get(ABUNA_ID);
		Station morada = stations.get(MORADA_ID);
		Station guajara = stations.get(GUAJARA_ID);
		
		long predictionWindow = portoVelho.predictionWindow * HOUR_IN_SECONDS;
		
		StationEntry future = new StationEntry(portoVelho, timestamp + predictionWindow);
		
		StationEntry current = repository.findOne(new EntryPk(timestamp, portoVelho.id));
		StationEntry past = repository.findOne(new EntryPk(timestamp - predictionWindow, portoVelho.id));

		StationEntry currentAbuna = repository.findOne(new EntryPk(timestamp, abuna.id));
		StationEntry pastAbuna = repository.findOne(new EntryPk(timestamp - predictionWindow, abuna.id));

		StationEntry currentMorada = repository.findOne(new EntryPk(timestamp - predictionWindow, morada.id));
		StationEntry pastMorada = repository.findOne(new EntryPk(timestamp - 2*predictionWindow, morada.id));

		StationEntry currentGuajara = repository.findOne(new EntryPk(timestamp - 2*predictionWindow, guajara.id));
		StationEntry pastGuajara = repository.findOne(new EntryPk(timestamp - 4*predictionWindow, guajara.id));

		if (!isAnyAlertNull(current, past, currentAbuna, pastAbuna, currentMorada, pastMorada, currentGuajara, pastGuajara)) {

			long calculated  = (long) (current.measured + 
					A_1 * (current.measured - past.measured) + 
					A_2 * (currentAbuna.measured - pastAbuna.measured) + 
					A_3 * (currentMorada.measured - pastMorada.measured) + 
					A_4 * (currentGuajara.measured - pastGuajara.measured));
			long predicted = calculated + (current.calculated == 0?0:(current.measured - current.calculated));

			future.registerPrediction(calculated, predicted);
		}else{
			future.registerPrediction(0L, 0L);
		}
		
		return future;
	}
}
