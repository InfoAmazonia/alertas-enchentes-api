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

	@Scheduled(initialDelay=30000, fixedRate = RATE)
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
		
		StationEntry portovelhoNext = new StationEntry(portoVelho, timestamp + predictionWindow);
		
		StationEntry portoVelhoNow = repository.findOne(new EntryPk(timestamp, portoVelho.id));
		StationEntry portoVelhoPast = repository.findOne(new EntryPk(timestamp - predictionWindow, portoVelho.id));

		StationEntry abunaNow = repository.findOne(new EntryPk(timestamp, abuna.id));
		StationEntry abunaPast = repository.findOne(new EntryPk(timestamp - predictionWindow, abuna.id));

		StationEntry moradaNow = repository.findOne(new EntryPk(timestamp - predictionWindow, morada.id));
		StationEntry moradaPast = repository.findOne(new EntryPk(timestamp - 2*predictionWindow, morada.id));

		StationEntry guajaraNow = repository.findOne(new EntryPk(timestamp - 2*predictionWindow, guajara.id));
		StationEntry guajaraPast = repository.findOne(new EntryPk(timestamp - 4*predictionWindow, guajara.id));

		if (!isAnyAlertNull(portoVelhoNow, portoVelhoPast, abunaNow, abunaPast, moradaNow, moradaPast, guajaraNow, guajaraPast)) {

			int predicted  = (int) (portoVelhoNow.measured + 
					A_1 * (portoVelhoNow.measured - portoVelhoPast.measured) + 
					A_2 * (abunaNow.measured - abunaPast.measured) + 
					A_3 * (moradaNow.measured - moradaPast.measured) + 
					A_4 * (guajaraNow.measured - guajaraPast.measured));

			predicted = Math.max(0, predicted);

			portovelhoNext.registerPrediction(predicted);
		}else{
			portovelhoNext.registerPrediction(null);
		}
		
		return portovelhoNext;
	}
}
