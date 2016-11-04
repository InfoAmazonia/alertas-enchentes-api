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
public class Summary implements Serializable {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 6750021246745651633L;

    @EmbeddedId
    @JsonIgnore
    public SummaryPk id;

	@Column(name="timestamp", insertable=false, updatable=false)
	public String timestamp;

	@Column
	public Long measured;

	@Transient
	public String measuredStatus;
	
	@ManyToOne(optional=false, fetch=FetchType.EAGER)
	@JoinColumn(name = "station_id", referencedColumnName = "id", insertable = false, updatable = false)
	@JsonIgnore
	public Station station;

	
	public Summary() {
		// TODO Auto-generated constructor stub
	}
	
	public Summary(Station station, String timestamp, Long measured) {
		super();
		this.station = station;
		this.timestamp = timestamp;
		this.id = new SummaryPk(timestamp, station.id);
		this.measured = measured;
		this.measuredStatus = "";
	}

	public Summary(Station station, String timestamp) {
		this(station, timestamp, null);
	}

	public SummaryPk getId() {
		return id;
	}


	public void registerQuota(Long quota){
		this.measured = quota;
		this.measuredStatus = this.station.calculateStatus(quota);
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
		Summary other = (Summary) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "AlertSummary [id=" + id + ", timestamp=" + timestamp + ", measured=" + measured + ", measuredStatus="
				+ measuredStatus + ", station=" + station + "]";
	}

	public void fillStatus() {
		this.measuredStatus = station.calculateStatus(this.measured);
	}
}
