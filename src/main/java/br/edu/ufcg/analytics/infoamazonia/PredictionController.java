package br.edu.ufcg.analytics.infoamazonia;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ufcg.analytics.infoamazonia.model.Station;
import br.edu.ufcg.analytics.infoamazonia.model.StationEntry;
import br.edu.ufcg.analytics.infoamazonia.model.StationEntryRepository;
import br.edu.ufcg.analytics.infoamazonia.model.StationRepository;
import br.edu.ufcg.analytics.infoamazonia.model.Summary;
import br.edu.ufcg.analytics.infoamazonia.model.SummaryRepository;

@CrossOrigin
@RestController
@RequestMapping("/station")
public class PredictionController {
	

	class PredictionInfo<T extends Serializable> implements Serializable{
		private static final long serialVersionUID = -1644121143775945570L;
		public Station info;
		public List<T> data;
		public T last;
		public PredictionInfo(Station info, List<T> data, T last) {
			this.info = info;
			this.data = data;
			this.last = last;
		}
		public PredictionInfo(Station info, List<T> data) {
			this(info, data, null);
		}
	}

	@Autowired
	StationEntryRepository repository;

	@Autowired
	SummaryRepository summaryRepository;

	@Autowired
	StationRepository stationRepository;

	@RequestMapping("/{id}/prediction")
	public ResponseEntity<PredictionInfo<StationEntry>> getRecomendationsFor(@PathVariable Long id,
			@RequestParam(value = "timestamp", defaultValue = "-1") Long timestamp) {
		
		Station station = stationRepository.findOne(id);
		
		if (station == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		StationEntry lastMeasurement = repository.findFirstByStationAndMeasuredIsNotNullOrderByTimestampDesc(station);
		if(timestamp == -1){
			if(lastMeasurement == null){
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			timestamp = lastMeasurement.timestamp;
		}

		List<StationEntry> alerts = repository.findAllByStationAndTimestampBetween(station, timestamp-300, timestamp + 43200);
		for (StationEntry alert : alerts) {
			alert.fillStatus();
		}
		
		if(alerts.get(0).measured != null){
			lastMeasurement = alerts.get(0); 
		}
		
		return new ResponseEntity<>(new PredictionInfo<StationEntry>(station, alerts, lastMeasurement) , HttpStatus.OK);
	}

	@RequestMapping("/{id}/history")
	public ResponseEntity<PredictionInfo<Summary>> getHistory(@PathVariable Long id) {
		
		Station station = stationRepository.findOne(id);
		
		if (station == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		List<Summary> history = summaryRepository.findAllByStationOrderByTimestampAsc(station);
		for (Summary summary : history) {
			summary.fillStatus();
		}
		
		return new ResponseEntity<>(new PredictionInfo<Summary>(station, history) , HttpStatus.OK);
	}

}
