package br.edu.ufcg.analytics.infoamazonia.model;

import java.util.List;

import org.springframework.data.repository.CrudRepository;


/**
 * @author Ricardo Ara&eacute;jo Santos - ricoaraujosantos@gmail.com
 */
public interface AlertRepository extends CrudRepository<Alert, AlertPk> {
	
	Alert findFirstByStationAndTimestamp(Station station, Long timestamp);

	Alert findFirstByStationAndMeasuredIsNotNullOrderByTimestampDesc(Station station);

	List<Alert> findAllByStationAndTimestampBetween(Station station, Long start, Long end);

	Long countByStation(Station station);

}