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

	@ManyToOne(optional=false, fetch=FetchType.EAGER)
	@JoinColumn(name = "station_id", referencedColumnName = "id", insertable = false, updatable = false)
	@JsonIgnore
	public Station station;

	@Column
	public String message;
	
	
	public Alert() {
		// TODO Auto-generated constructor stub
	}
	
	public Alert(Station station, Long timestamp, String message) {
		super();
		this.station = station;
		this.timestamp = timestamp;
		this.message = message;
		this.id = new AlertPk(timestamp, station.id);
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
		return "Alert [timestamp=" + timestamp + ", station=" + station + ", message=" + message + "]";
	}
}
