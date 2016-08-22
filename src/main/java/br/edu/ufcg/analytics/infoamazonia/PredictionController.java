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
	public ResponseEntity<Alert> getRecomendationsFor(@PathVariable Long id,
			@RequestParam(value = "timestamp", defaultValue = "") Long timestamp) {

		List<Alert> alert = repository.find(id, timestamp);
		
		if (alert.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(alert.get(0), HttpStatus.OK);
	}

}
