package br.edu.ufcg.analytics.infoamazonia.model;

import java.util.List;

import org.springframework.data.repository.CrudRepository;


/**
 * @author Ricardo Ara&eacute;jo Santos - ricoaraujosantos@gmail.com
 */
public interface AlertRepository extends CrudRepository<Alert, AlertPk> {
	
	Alert findFirstByStationAndTimestamp(Station station, Long timestamp);

	Alert findFirstByStationOrderByTimestampDesc(Station station);

	List<Alert> findAllByStation(Station station);

	Long countByStation(Station station);

}