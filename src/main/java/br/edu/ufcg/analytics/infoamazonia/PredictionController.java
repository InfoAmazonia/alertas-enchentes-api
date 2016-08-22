package br.edu.ufcg.analytics.infoamazonia;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/station")
public class PredictionController {

	@Autowired
	AlertRepository repository;

	@RequestMapping("/{id}/prediction")
	public ResponseEntity<List<Alert>> getRecomendationsFor(@PathVariable Long id,
			@RequestParam(value = "timestamp", defaultValue = "") String timestamp) {
		
		if(timestamp.isEmpty()){
			timestamp = getLastMeasuredHour();
		}
		
//		repository.getLatest(id)
		
		List<Alert> alert = repository.getBetween(id, Long.valueOf(timestamp), Long.valueOf(timestamp) + 43200);
		
		if (alert == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(alert, HttpStatus.OK);
	}

	private String getLastMeasuredHour() {
		return Long.toString(System.currentTimeMillis()/1000);
	}

}
