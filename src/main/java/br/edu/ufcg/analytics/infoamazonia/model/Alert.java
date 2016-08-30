package br.edu.ufcg.analytics.infoamazonia.model;
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Alert implements Serializable {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 6750021246745651633L;

    @EmbeddedId
    @JsonIgnore
    public AlertPk id;

	@Column(name="timestamp", insertable=false, updatable=false)
	public Long timestamp;

	@Column
	public Long measured;

	@Column(nullable=true)
	@JsonIgnore
	public Long calculated;
	
	@Column(nullable=true)
	public Long predicted;
	
	@Column(nullable=true)
	public String measuredStatus;
	
	@Column(nullable=true)
	public String predictedStatus;
	
	@ManyToOne(optional=false, fetch=FetchType.EAGER)
	@JoinColumn(name = "station_id", referencedColumnName = "id", insertable = false, updatable = false)
	@JsonIgnore
	public Station station;

	
	public Alert() {
		// TODO Auto-generated constructor stub
	}
	
	public Alert(Station station, Long timestamp, Long measured, Long calculated, Long predicted, String measuredStatus,
			String predictedStatus) {
		super();
		this.station = station;
		this.timestamp = timestamp;
		this.id = new AlertPk(timestamp, station.id);
		this.measured = measured;
		this.calculated = calculated;
		this.predicted = predicted;
		this.measuredStatus = measuredStatus;
		this.predictedStatus = predictedStatus;
	}

	public Alert(Station station, Long timestamp, Long measured) {
		this(station, timestamp, measured, null, null, null, null);
	}
	
	public Alert(Station station, long timestamp) {
		this(station, timestamp, null, null, null, null, null);
	}

	public AlertPk getId() {
		return id;
	}


	public void registerQuota(Long quota){
		this.measured = quota;
		this.measuredStatus = this.station.calculateStatus(quota);
	}

	public void registerPrediction(Long calculated, Long predicted) {
		this.calculated = calculated;
		this.predicted = predicted;
		this.predictedStatus = this.station.calculateStatus(predicted);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Alert other = (Alert) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Alert [id=" + id + ", measured=" + measured + ", calculated=" + calculated + ", predicted="
				+ predicted + ", measuredStatus=" + measuredStatus + ", predictedStatus=" + predictedStatus
				+ "]";
	}

}
