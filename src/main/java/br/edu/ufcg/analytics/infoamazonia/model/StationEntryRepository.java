package br.edu.ufcg.analytics.infoamazonia.model;

import java.util.List;

import org.springframework.data.repository.CrudRepository;


/**
 * @author Ricardo Ara&eacute;jo Santos - ricoaraujosantos@gmail.com
 */
public interface StationEntryRepository extends CrudRepository<StationEntry, EntryPk> {
	
	StationEntry findFirstByStationAndTimestamp(Station station, Long timestamp);

	StationEntry findFirstByStationAndMeasuredIsNotNullOrderByTimestampDesc(Station station);

	List<StationEntry> findFirst2ByStationAndMeasuredIsNotNullOrderByTimestampDesc(Station station);

	List<StationEntry> findAllByStationAndTimestampBetween(Station station, Long start, Long end);

	List<StationEntry> findAllByStationAndTimestampGreaterThanEqual(Station station, Long start);

	Long countByStation(Station station);

	StationEntry findFirstByStationOrderByTimestampDesc(Station station);

}