package br.edu.ufcg.analytics.infoamazonia.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ufcg.analytics.infoamazonia.model.Alert;
import br.edu.ufcg.analytics.infoamazonia.model.Station;
import br.edu.ufcg.analytics.infoamazonia.model.StationEntry;
import br.edu.ufcg.analytics.infoamazonia.model.Summary;
import br.edu.ufcg.analytics.infoamazonia.service.StationService;
import br.edu.ufcg.analytics.infoamazonia.service.StationService.Result;

@CrossOrigin
@RestController
@RequestMapping("/station")
public class StationController {

	private static final int FIFTEEN_MINUTES = 900;
	@Autowired
	private StationService service;

	@RequestMapping("/{id}/prediction")
	public ResponseEntity<Result<StationEntry>> getRecomendationsFor(@PathVariable Long id,
			@RequestParam(name = "timestamp", defaultValue = "-1") Long timestamp) {

		if (!service.exists(id)) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		long queryTimestamp = timestamp == -1 ? System.currentTimeMillis() / 1000 - FIFTEEN_MINUTES : timestamp;
		
		return new ResponseEntity<>(service.getPredictionsForStationSince(id, queryTimestamp), HttpStatus.OK);
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
		
		Station station = new Station();
		
		return new ResponseEntity<>(new Alert(station, (long)(300 * Math.floor(System.currentTimeMillis()/300000)), "Alerta fake para estação de id=" + id + "!!!!!"), HttpStatus.OK);
//		return new ResponseEntity<>(service.getLatestAlert(id), HttpStatus.OK);
	}

	@RequestMapping("/{id}/now")
	public ResponseEntity<Alert> getCurrentStatus(@PathVariable Long id) {

		if (!service.exists(id)) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<>(service.getCurrentStatus(id), HttpStatus.OK);
	}
}
