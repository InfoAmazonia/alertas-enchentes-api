package br.edu.ufcg.analytics.infoamazonia;
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Alert implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String stationName;
	private Long stationID;
	private Long timestamp;
	private Long current;

	@Column(nullable=true)
	private Long calculated;
	
	@Column(nullable=true)
	private Long prediction;
	@Column(nullable=true)
	private String status;
	
	public Alert() {
		// TODO Auto-generated constructor stub
	}

	public Alert(String stationName, Long stationID, Long timestamp,
			Long current, Long calculated, Long prediction, String status) {
		super();
		this.stationName = stationName;
		this.stationID = stationID;
		this.timestamp = timestamp;
		this.current = current;
		this.calculated = calculated;
		this.prediction = prediction;
		this.status = status;
	}

	public Alert(String stationName, Long stationID, Long timestamp,
			Long current) {
		super();
		this.stationName = stationName;
		this.stationID = stationID;
		this.timestamp = timestamp;
		this.current = current;
	}

	public String getStationName() {
		return stationName;
	}

	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	public Long getCalculated() {
		return calculated;
	}

	public void setCalculated(Long calculated) {
		this.calculated = calculated;
	}

	public Long getStationID() {
		return stationID;
	}

	public void setStationID(Long stationID) {
		this.stationID = stationID;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public Long getCurrent() {
		return current;
	}

	public void setCurrent(Long current) {
		this.current = current;
	}

	public Long getPrediction() {
		return prediction;
	}

	public void setPrediction(Long prediction) {
		this.prediction = prediction;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Alert [id=" + id + ", stationName=" + stationName
				+ ", stationID=" + stationID + ", timestamp=" + timestamp
				+ ", current=" + current + ", calculated=" + calculated
				+ ", prediction=" + prediction + ", status=" + status + "]";
	}
	
	
	

}
