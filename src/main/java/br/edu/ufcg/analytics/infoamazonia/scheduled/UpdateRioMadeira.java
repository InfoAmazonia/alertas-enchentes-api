package br.edu.ufcg.analytics.infoamazonia.scheduled;

import java.io.FileNotFoundException;
import java.text.ParseException;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import br.edu.ufcg.analytics.infoamazonia.Alert;
import br.edu.ufcg.analytics.infoamazonia.AlertPk;

@Component
public class UpdateRioMadeira extends UpdatePredictionsTask {

	private static final long XAPURI_ID = 13551000L;
	private static final int DELTA = 900000;
	private static final long LIMIAR_ALERTA = 1350;
	private static final long LIMIAR_INUNDACAO = 1400;
	private static final double ALPHA = 0.717395738210093;
	private static final double BETA = 0.151170920309919;
	
	public UpdateRioMadeira() {
		super("01/12/201400:00:00", 13600002L, dependency("01/12/201400:00:00", XAPURI_ID));
	}

	@Scheduled(fixedRate = DELTA)
	@Override
	public void update() throws FileNotFoundException, ParseException {
		System.out.println("UpdateRioMadeira.update()");
		super.update();
	}

	@Override
	protected Alert predict(long id, long timestamp) {
		
		Alert currentXapuri = repository.findOne(new AlertPk(XAPURI_ID, timestamp));
		Alert pastXapuri = repository.findOne(new AlertPk(13551000L, timestamp-DELTA));

		Alert current = repository.findOne(new AlertPk(id, timestamp));
		Alert past = repository.findOne(new AlertPk(id, timestamp-DELTA));
		Alert future = new Alert();
		future.setId(new AlertPk(id, timestamp + DELTA));
		
		if(current == null || past == null || currentXapuri == null || pastXapuri == null){
			future.setPredicted(current.getMeasured());
			future.setCorrected(current.getMeasured());
		}else if(past.getMeasured() != null && currentXapuri.getMeasured() != null && pastXapuri.getMeasured() != null){
			long predicted  = (long) (current.getMeasured() + ALPHA * (current.getMeasured() - past.getMeasured()) + BETA * (currentXapuri.getMeasured() - pastXapuri.getMeasured()));
			long corrected = predicted + (current.getMeasured() - current.getPredicted());
			future.setPredicted(predicted);
			future.setCorrected(corrected);
		}else{
			future.setPredicted(current.getMeasured());
			future.setCorrected(current.getMeasured());
		}
		
		future.setPredictedStatus(calculateStatus(future.getCorrected(), LIMIAR_ALERTA, LIMIAR_INUNDACAO));
		return future;
	}
	
	@Override
	protected Alert setMeasuredStatus(long cota, Alert alert) {
		alert.setMeasured(cota);
		alert.setMeasuredStatus(calculateStatus(cota, LIMIAR_ALERTA, LIMIAR_INUNDACAO));
		return alert;
	}

}
