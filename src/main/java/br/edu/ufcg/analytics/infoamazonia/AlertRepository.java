package br.edu.ufcg.analytics.infoamazonia;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 * @author Ricardo Ara&eacute;jo Santos - ricoaraujosantos@gmail.com
 */
public interface AlertRepository extends CrudRepository<Alert, Long> {

	/**
	 * @param id
	 * @param timestamp
	 * @return
	 */
	@Query("SELECT a FROM Alert a WHERE id = :stationId AND timestamp = :timestamp")
	List<Alert> find(@Param("stationId") Long id, @Param("timestamp") Long timestamp);

	/**
	 * @param id
	 * @param timestamp
	 * @return
	 */
	@Query("SELECT a FROM Alert a WHERE id = :stationId ORDER BY timestamp DESC LIMIT 1")
	Alert getLatest(@Param("stationId") Long id);

}