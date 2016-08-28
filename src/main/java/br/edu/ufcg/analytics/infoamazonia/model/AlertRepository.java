package br.edu.ufcg.analytics.infoamazonia.model;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;


/**
 * @author Ricardo Ara&eacute;jo Santos - ricoaraujosantos@gmail.com
 */
public interface AlertRepository extends CrudRepository<Alert, AlertPk> {
	
	@Query("SELECT a FROM Alert a WHERE a.id.station = :station AND a.id.timestamp = :timestamp")
	List<Alert> findOneFromStationIdAndTimestamp(@Param("station") Station station, @Param("timestamp") Long timestamp);

	@Query("SELECT a FROM Alert a WHERE a.id.station = :station ORDER BY a.id.timestamp DESC")
	List<Alert> getLatestFromStation(@Param("station") Station station);

	@Query("SELECT a FROM Alert a WHERE a.id.station = :station AND a.id.timestamp BETWEEN :start AND :end ORDER BY a.id.timestamp ASC")
	List<Alert> getSliceFromStation(@Param("station") Station station, @Param("start") Long start, @Param("end") Long end);

	@Query("SELECT a FROM Alert a WHERE a.id.station = :station AND a.id.timestamp BETWEEN max(a.id.timestamp)-43200 AND max(a.id.timestamp) ORDER BY a.id.timestamp ASC")
	List<Alert> getLatestSliceFromStation(@Param("station") Station station);

}