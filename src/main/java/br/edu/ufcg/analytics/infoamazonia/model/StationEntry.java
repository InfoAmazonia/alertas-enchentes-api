package br.edu.ufcg.analytics.infoamazonia.model;
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class StationEntry implements Serializable {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 6750021246745651633L;

    @EmbeddedId
    @JsonIgnore
    public EntryPk id;

	@Column(name="timestamp", insertable=false, updatable=false)
	public Long timestamp;

	@Column
	public Long measured;

	@Column(nullable=true, updatable=false)
	@JsonIgnore
	public Long calculated;
	
	@Column(nullable=true, updatable=false)
	public Long predicted;
	
	@Transient
	public RiverStatus measuredStatus;
	
	@Transient
	public RiverStatus predictedStatus;
	
	@ManyToOne(optional=false, fetch=FetchType.EAGER)
	@JoinColumn(name = "station_id", referencedColumnName = "id", insertable = false, updatable = false)
	@JsonIgnore
	public Station station;

	
	public StationEntry() {
		// TODO Auto-generated constructor stub
	}
	
	public StationEntry(Station station, Long timestamp, Long measured, Long calculated, Long predicted) {
		super();
		this.station = station;
		this.timestamp = timestamp;
		this.id = new EntryPk(timestamp, station.id);
		this.measured = measured;
		this.calculated = calculated;
		this.predicted = predicted;
	}

	public StationEntry(Station station, Long timestamp, Long measured) {
		this(station, timestamp, measured, null, null);
	}
	
	public StationEntry(Station station, long timestamp) {
		this(station, timestamp, null, null, null);
	}

	public EntryPk getId() {
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
		StationEntry other = (StationEntry) obj;
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

	public void fillStatus() {
		this.measuredStatus = station.calculateStatus(this.measured);
		this.predictedStatus = station.calculateStatus(this.predicted);
	}

}