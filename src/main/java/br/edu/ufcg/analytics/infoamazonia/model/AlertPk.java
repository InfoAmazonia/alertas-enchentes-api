package br.edu.ufcg.analytics.infoamazonia.model;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Embeddable
public class AlertPk implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 207124017780543304L;

	public Long timestamp;

	@ManyToOne
	@JoinColumn(name = "station_id")
	@JsonIgnore
	public Station station;

	public AlertPk() {
		// TODO Auto-generated constructor stub
	}

	public AlertPk(Long timestamp, Station station) {
		super();
		this.timestamp = timestamp;
		this.station = station;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((station == null) ? 0 : station.hashCode());
		result = prime * result + ((timestamp == null) ? 0 : timestamp.hashCode());
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
		AlertPk other = (AlertPk) obj;
		if (station == null) {
			if (other.station != null)
				return false;
		} else if (!station.equals(other.station))
			return false;
		if (timestamp == null) {
			if (other.timestamp != null)
				return false;
		} else if (!timestamp.equals(other.timestamp))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "AlertPk [timestamp=" + timestamp + ", station=" + station + "]";
	}
}
