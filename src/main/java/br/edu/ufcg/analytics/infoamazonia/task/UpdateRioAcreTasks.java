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
public class UpdateRioAcreTasks extends UpdateTasks {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private static final long RIOBRANCO_ID = 13600002L;
	private static final long CAPIXABA_ID = 13568000L;
	private static final long RIOROLA_ID = 13578000L;
	private static final long RATE = 900000;
	private static final double A_1 = 0.677;
	private static final double A_2 = 0.029;
	private static final double A_3 = 0.313;
	
	
	public UpdateRioAcreTasks() {
		super(RIOBRANCO_ID, CAPIXABA_ID, RIOROLA_ID);
	}

//	@Scheduled(initialDelay=1000, fixedRate = RATE)
	@Override
	public void update() throws FileNotFoundException, ParseException {
		logger.info("Updating Rio Acre");
		long time = System.currentTimeMillis();
		super.update();
		time = System.currentTimeMillis() - time;
		logger.info("Updated Rio Acre in " + time + " millis");
	}

	@Override
	protected StationEntry predict(long timestamp, Map<Long, Station> stations) {
		
		Station stationRioBranco = stations.get(RIOBRANCO_ID);
		Station stationCapixaba = stations.get(CAPIXABA_ID);
		Station stationRioRola = stations.get(RIOROLA_ID);
		
		long predictionWindow = stationRioBranco.predictionWindow * HOUR_IN_SECONDS;
		
		StationEntry riobrancoNext = new StationEntry(stationRioBranco, timestamp + predictionWindow );

		StationEntry riobrancoNow = repository.findOne(new EntryPk(timestamp,  stationRioBranco.id));
		StationEntry riobrancoPast = repository.findOne(new EntryPk(timestamp - predictionWindow,  stationRioBranco.id));
		
		StationEntry capixabaNow = repository.findOne(new EntryPk(timestamp,  stationCapixaba.id));
		StationEntry capixabaPast = repository.findOne(new EntryPk(timestamp - predictionWindow,  stationCapixaba.id));
		
		StationEntry riorolaNow = repository.findOne(new EntryPk(timestamp,  stationRioRola.id));
		StationEntry riorolaPast = repository.findOne(new EntryPk(timestamp - predictionWindow,  stationRioRola.id));
		
		if(!isAnyAlertNull(riobrancoNow, riobrancoPast, capixabaNow, capixabaPast, riorolaNow, riorolaPast)){
			long calculated  = (long) (riobrancoNow.measured + 
					A_1 * (riobrancoNow.measured - riobrancoPast.measured) + 
					A_2 * (capixabaNow.measured - capixabaPast.measured) + 
					A_3 * (riorolaNow.measured - riorolaPast.measured));
			
			long predicted = Math.max(0, calculated);

			riobrancoNext.registerPrediction(calculated, predicted);
		}else{
			riobrancoNext.registerPrediction(null, null);
		}
		return riobrancoNext;
	}
}
