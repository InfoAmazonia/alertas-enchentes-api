package br.edu.ufcg.analytics.infoamazonia.scheduled;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class UpdateRioMadeira extends UpdatePredictionsTask {

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	
	public UpdateRioMadeira() {
		super("RIO BRANCO", 13600002, dependency("01/12/201400:00:00", 13551000));
	}

	@Scheduled(fixedRate = 5000)
//	@Scheduled(fixedRate = 900000)
	@Override
	public void update() throws FileNotFoundException, ParseException {
		super.update();
	}

}
