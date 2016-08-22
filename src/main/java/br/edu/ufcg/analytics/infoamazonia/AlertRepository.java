package br.edu.ufcg.analytics.infoamazonia;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 * @author Ricardo Ara&eacute;jo Santos - ricoaraujosantos@gmail.com
 */
public interface AlertRepository extends CrudRepository<Alert, AlertPk> {
	
	@Query("SELECT a FROM Alert a WHERE a.id.stationId = :stationId ORDER BY a.id.timestamp DESC")
	List<Alert> getLatest(@Param("stationId") Long id);

	@Query("SELECT a FROM Alert a WHERE a.id.stationId = :stationId AND a.id.timestamp >= :start and a.id.timestamp <= :end ORDER BY a.id.timestamp ASC")
	List<Alert> getBetween(@Param("stationId") Long id, @Param("start") Long start, @Param("end") Long end);

}