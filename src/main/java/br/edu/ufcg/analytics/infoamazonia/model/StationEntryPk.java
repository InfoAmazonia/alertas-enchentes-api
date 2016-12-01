package br.edu.ufcg.analytics.infoamazonia.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class StationEntryPk implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2175511611596730290L;

	@Column(name="timestamp")
	public Long timestamp;

	@Column(name = "station_id")
	public Long stationId;

	public StationEntryPk() {
		// TODO Auto-generated constructor stub
	}

	public StationEntryPk(Long timestamp, Long stationId) {
		this.timestamp = timestamp;
		this.stationId = stationId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((stationId == null) ? 0 : stationId.hashCode());
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
		StationEntryPk other = (StationEntryPk) obj;
		if (stationId == null) {
			if (other.stationId != null)
				return false;
		} else if (!stationId.equals(other.stationId))
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
		return "AlertPk [timestamp=" + timestamp + ", stationId=" + stationId + "]";
	}
}
