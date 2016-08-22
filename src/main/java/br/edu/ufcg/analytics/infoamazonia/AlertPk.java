package br.edu.ufcg.analytics.infoamazonia;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class AlertPk implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3207661025341458400L;
	@Column
	private Long stationId;
	@Column
	private Long timestamp;
	
	public AlertPk() {
		// TODO Auto-generated constructor stub
	}
	
	public AlertPk(Long stationId, Long timestamp) {
		super();
		this.stationId = stationId;
		this.timestamp = timestamp;
	}

	public Long getStationId() {
		return stationId;
	}

	public void setStationId(Long stationId) {
		this.stationId = stationId;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
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
		AlertPk other = (AlertPk) obj;
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
		return "AlertPk [stationID=" + stationId + ", timestamp=" + timestamp + "]";
	}

}
