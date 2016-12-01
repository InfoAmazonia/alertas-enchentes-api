package br.edu.ufcg.analytics.infoamazonia.model;

import java.util.List;

import org.springframework.data.repository.CrudRepository;


/**
 * @author Ricardo Ara&eacute;jo Santos - ricoaraujosantos@gmail.com
 */
public interface SummaryRepository extends CrudRepository<Summary, SummaryPk> {
	
	List<Summary> findAllByStationOrderByTimestampAsc(Station station);

	long countByStation(Station station);
}