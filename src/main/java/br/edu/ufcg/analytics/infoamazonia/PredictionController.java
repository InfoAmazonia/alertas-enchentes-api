package br.edu.ufcg.analytics.infoamazonia;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ufcg.analytics.infoamazonia.model.Alert;
import br.edu.ufcg.analytics.infoamazonia.model.AlertRepository;
import br.edu.ufcg.analytics.infoamazonia.model.Station;
import br.edu.ufcg.analytics.infoamazonia.model.StationRepository;

@RestController
@RequestMapping("/station")
public class PredictionController {
	
	class PredictionInfo implements Serializable{
		private static final long serialVersionUID = -1644121143775945570L;
		public Station info;
		public List<Alert> data;
		public PredictionInfo(Station info, List<Alert> data) {
			this.info = info;
			this.data = data;
		}
	}

	@Autowired
	AlertRepository repository;

	@Autowired
	StationRepository stationRepository;

	@RequestMapping("/{id}/prediction")
	public ResponseEntity<PredictionInfo> getRecomendationsFor(@PathVariable Long id,
			@RequestParam(value = "timestamp", defaultValue = "") String timestamp) {
		
		if(timestamp.isEmpty()){
			timestamp = getLastMeasuredHour();
		}
		
//		repository.getLatest(id)
		
		Station station = stationRepository.findOne(id);
		
		if (station == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		List<Alert> alert = null;//repository.findAllByStationBetween(station, Long.valueOf(timestamp), Long.valueOf(timestamp) + 43200);
		
		return new ResponseEntity<>(new PredictionInfo(station, alert) , HttpStatus.OK);
	}

	private String getLastMeasuredHour() {
		return Long.toString(System.currentTimeMillis()/1000);
	}

}
