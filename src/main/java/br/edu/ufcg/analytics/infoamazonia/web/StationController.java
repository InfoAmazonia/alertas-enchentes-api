package br.edu.ufcg.analytics.infoamazonia.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ufcg.analytics.infoamazonia.service.StationService;
import br.edu.ufcg.analytics.infoamazonia.service.StationService.Result;
import br.edu.ufcg.analytics.infoamazonia.model.Alert;
import br.edu.ufcg.analytics.infoamazonia.model.StationEntry;
import br.edu.ufcg.analytics.infoamazonia.model.Summary;

@CrossOrigin
@RestController
@RequestMapping("/station")
public class StationController {

	@Autowired
	private StationService service;

	@RequestMapping("/{id}/prediction")
	public ResponseEntity<Result<StationEntry>> getRecomendationsFor(@PathVariable Long id,
			@RequestParam(value = "timestamp", defaultValue = "-1") Long timestamp) {

		if (!service.exists(id)) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		if (timestamp == -1) {
			timestamp = System.currentTimeMillis() - 300;
		}

		return new ResponseEntity<>(service.getPredictionsForStationSince(id, timestamp), HttpStatus.OK);
	}

	@RequestMapping("/{id}/history")
	public ResponseEntity<Result<Summary>> getHistory(@PathVariable Long id) {

		if (!service.exists(id)) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(service.getHistory(id), HttpStatus.OK);
	}

	@RequestMapping("/{id}/alert")
	public ResponseEntity<Alert> getAlert(@PathVariable Long id) {

		if (!service.exists(id)) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(service.getLatestAlert(id), HttpStatus.OK);
	}
}