package br.edu.ufcg.analytics.infoamazonia.service;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.edu.ufcg.analytics.infoamazonia.model.Alert;
import br.edu.ufcg.analytics.infoamazonia.model.AlertRepository;
import br.edu.ufcg.analytics.infoamazonia.model.Station;
import br.edu.ufcg.analytics.infoamazonia.model.StationEntry;
import br.edu.ufcg.analytics.infoamazonia.model.StationEntryRepository;
import br.edu.ufcg.analytics.infoamazonia.model.StationRepository;
import br.edu.ufcg.analytics.infoamazonia.model.Summary;
import br.edu.ufcg.analytics.infoamazonia.model.SummaryRepository;

@Service
public class StationService {
	
	@Autowired
	private StationEntryRepository stationEntryRepo;

	@Autowired
	private SummaryRepository summaryRepo;

	@Autowired
	private StationRepository stationRepo;

	@Autowired
	private AlertRepository alertRepo;

	public class Result<T extends Serializable> implements Serializable {
		private static final long serialVersionUID = -1644121143775945570L;
		public Station info;
		public List<T> data;
		public T last;

		public Result(Station info, List<T> data, T last) {
			this.info = info;
			this.data = data;
			this.last = last;
		}

		public Result(Station info, List<T> data) {
			this(info, data, null);
		}
	}

	public boolean exists(Long stationId) {
		return stationRepo.exists(stationId);
	}

	@RequestMapping("/{id}/prediction")
	public ResponseEntity<Result<StationEntry>> getRecomendationsFor(@PathVariable Long id,
			@RequestParam(value = "timestamp", defaultValue = "-1") Long timestamp) {

		Station station = stationRepo.findOne(id);

		if (station == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		StationEntry lastMeasurement = stationEntryRepo
				.findFirstByStationAndMeasuredIsNotNullOrderByTimestampDesc(station);
		lastMeasurement.fillStatus();
		if (timestamp == -1) {
			timestamp = System.currentTimeMillis() / 1000;
		}

		List<StationEntry> alerts = stationEntryRepo.findAllByStationAndTimestampBetween(station, timestamp - 300,
				timestamp + 43200);
		for (StationEntry alert : alerts) {
			alert.fillStatus();
		}

		return new ResponseEntity<>(new Result<StationEntry>(station, alerts, lastMeasurement), HttpStatus.OK);
	}

	@RequestMapping("/{id}/history")
	public Result<Summary> getHistory(@PathVariable Long id) {

		Station station = stationRepo.findOne(id);

		List<Summary> history = summaryRepo.findAllByStationOrderByTimestampAsc(station);
		for (Summary summary : history) {
			summary.fillStatus();
		}

		return new Result<Summary>(station, history);
	}

	@RequestMapping("/{id}/alert")
	public Alert getLatestAlert(@PathVariable Long id) {

		Alert alert = alertRepo.findFirstByStationOrderByTimestampDesc(stationRepo.findOne(id));
		return alert;
	}

	public Result<StationEntry> getLastPredictionsForStation(Long id) {

		Station station = stationRepo.findOne(id);
		StationEntry lastMeasurement = stationEntryRepo
				.findFirstByStationAndMeasuredIsNotNullOrderByTimestampDesc(station);
		
		long timestamp = System.currentTimeMillis()/1000 - 900;
		List<StationEntry> alerts = stationEntryRepo.findAllByStationAndTimestampGreaterThanEqual(station, timestamp);

		for (StationEntry alert : alerts) {
			alert.fillStatus();
		}

		return new Result<StationEntry>(station, alerts, lastMeasurement);
	}

	
	public Result<StationEntry> getPredictionsForStationSince(Long id, Long timestamp) {

		Station station = stationRepo.findOne(id);
		int delta = 43200;//station.delta;
		StationEntry lastMeasurement = stationEntryRepo
				.findFirstByStationAndMeasuredIsNotNullOrderByTimestampDesc(station);
		List<StationEntry> alerts = stationEntryRepo.findAllByStationAndTimestampBetween(station, timestamp,
				timestamp + delta);

		for (StationEntry alert : alerts) {
			alert.fillStatus();
		}

		return new Result<StationEntry>(station, alerts, lastMeasurement);
	}

	
	public Station save(Station station) {
		return stationRepo.save(station);
	}
	

	public boolean containsHistory(Station station) {
		return summaryRepo.countByStation(station) != 0;
	}

	public Iterable<Summary> saveHistory(Iterable<Summary> history) {
		return summaryRepo.save(history);
	}
}
