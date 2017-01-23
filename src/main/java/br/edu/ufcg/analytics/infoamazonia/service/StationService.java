package br.edu.ufcg.analytics.infoamazonia.service;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

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
	
	private static final int HOUR_IN_SECONDS = 3600;

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

	public Result<Summary> getHistory(@PathVariable Long id) {

		Station station = stationRepo.findOne(id);

		List<Summary> history = summaryRepo.findAllByStationOrderByTimestampAsc(station);
		for (Summary summary : history) {
			summary.fillStatus();
		}

		return new Result<Summary>(station, history);
	}

	public Alert getLatestAlert(@PathVariable Long id) {

		Alert alert = alertRepo.findFirstByStationOrderByTimestampDesc(stationRepo.findOne(id));
		return alert;
	}

	public Result<StationEntry> getPredictionsForStationSince(Long id, Long timestamp) {

		Station station = stationRepo.findOne(id);
		StationEntry lastMeasurement = stationEntryRepo
				.findFirstByStationAndMeasuredIsNotNullOrderByTimestampDesc(station);
		
		List<StationEntry> alerts = stationEntryRepo.findAllByStationAndTimestampBetween(station, timestamp - station.predictionWindow * HOUR_IN_SECONDS,
				timestamp + station.predictionWindow * HOUR_IN_SECONDS);

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

	public boolean containsAlerts(Station station) {
		return alertRepo.countByStation(station) != 0;
	}

	public void initAlerts(Station station) {
		alertRepo.save(new Alert(station, 0L, "Serviço indisponível."));
	}

	public Alert getCurrentStatus(Long stationID) {
		Station station = stationRepo.findOne(stationID);
		StationEntry measurement = stationEntryRepo.findFirstByStationAndMeasuredIsNotNullOrderByTimestampDesc(station );
		StationEntry prediction = stationEntryRepo.findFirstByStationOrderByTimestampDesc(station);
		
		long timestamp = (long)(300 * Math.floor(System.currentTimeMillis()/300000));
		if(measurement == null || prediction == null){
			return new Alert(station, timestamp, "Não foi possível consultar o estado corrente do " + station.riverName + ". Tente novamente em alguns minutos!");
		}
		
		measurement.fillStatus();
		prediction.fillStatus();
		
		return new Alert(station, timestamp, StationEntry.buildAlertMessage(measurement, prediction));
	}
}
