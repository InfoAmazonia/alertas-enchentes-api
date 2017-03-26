package br.edu.ufcg.analytics.infoamazonia.web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ufcg.analytics.infoamazonia.model.Alert;
import br.edu.ufcg.analytics.infoamazonia.model.StationEntry;
import br.edu.ufcg.analytics.infoamazonia.model.Summary;
import br.edu.ufcg.analytics.infoamazonia.service.StationService;
import br.edu.ufcg.analytics.infoamazonia.service.StationService.Result;

@CrossOrigin
@RestController
@RequestMapping("/station")
public class StationController {

	private static final long RIO_BRANCO_CPRM_ID = 13600010;
	private static final long RIO_BRANCO_ID = 13600002;
	
	@Autowired
	private StationService service;

	@RequestMapping("/{id}/prediction")
	public ResponseEntity<Result<StationEntry>> getRecomendationsFor(@PathVariable Long id,
			@RequestParam(name = "timestamp", required=false) Long timestamp,
			@RequestParam(name = "forcecprm", required=false, defaultValue="false") Boolean forceCPRM) {

		if (!service.exists(id)) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		Long stationId = extracted(id, forceCPRM);
		
		return new ResponseEntity<>(service.getPredictionsForStationSince(stationId, timestamp), HttpStatus.OK);
	}

	private Long extracted(Long id, Boolean forceCPRM) {
		return (id.longValue() == RIO_BRANCO_CPRM_ID && ! forceCPRM)? RIO_BRANCO_ID: id;
	}

	@RequestMapping("/{id}/history")
	public ResponseEntity<Result<Summary>> getHistory(@PathVariable Long id,
			@RequestParam(name = "forcecprm", required=false, defaultValue="false") Boolean forceCPRM) {

		if (!service.exists(id)) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		Long stationId = extracted(id, forceCPRM);

		return new ResponseEntity<>(service.getHistory(stationId), HttpStatus.OK);
	}

	@RequestMapping(value="/{id}/csvhistory", method=RequestMethod.GET)
	public void getHistoryInCSV(@PathVariable Long id, HttpServletResponse response,
			@RequestParam(name = "forcecprm", required=false, defaultValue="false") Boolean forceCPRM) throws IOException {

		if (!service.exists(id)) {
			return;
		}

		Long stationId = extracted(id, forceCPRM);

		response.addHeader("Content-disposition", "attachment;filename=" + stationId + ".csv");
		response.setContentType("txt/plain; charset=utf-8");
		
		PrintWriter writer = response.getWriter();
		Result<Summary> history = service.getHistory(stationId);
		writer.write("\"timestamp\",\"measured\",\"measured_status\"\n");
		for (Summary item : history.data) {
			writer.write(item.toCSV());
		}
		
		
		
	}

	@RequestMapping("/{id}/alert")
	public ResponseEntity<Alert> getAlert(@PathVariable Long id, @RequestParam(name = "timestamp", defaultValue = "-1") Long timestamp,
			@RequestParam(name = "forcecprm", required=false, defaultValue="false") Boolean forceCPRM) {

		if (!service.exists(id)) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		Long stationId = extracted(id, forceCPRM);

		if(timestamp != -1){
			return new ResponseEntity<>(service.getFirstAlertAfter(stationId, timestamp), HttpStatus.OK);
		}
		
		return new ResponseEntity<>(service.getLatestAlert(stationId), HttpStatus.OK);
	}

	@RequestMapping("/{id}/now")
	public ResponseEntity<Alert> getCurrentStatus(@PathVariable Long id,
			@RequestParam(name = "forcecprm", required=false, defaultValue="false") Boolean forceCPRM) {

		if (!service.exists(id)) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		Long stationId = extracted(id, forceCPRM);

		return new ResponseEntity<>(service.getCurrentStatus(stationId), HttpStatus.OK);
	}
}
