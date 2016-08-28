package br.edu.ufcg.analytics.infoamazonia.model;
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
public class Alert implements Serializable {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 6750021246745651633L;

    @EmbeddedId
    public AlertPk id;

	@Column
	public Long measured;

	@Column(nullable=true)
	public Long calculated;
	
	@Column(nullable=true)
	public Long predicted;
	
	@Column(nullable=true)
	public String measuredStatus;
	
	@Column(nullable=true)
	public String predictedStatus;
	
	public Alert() {
		// TODO Auto-generated constructor stub
	}
	
	public Alert(Station station, Long timestamp, Long measured, Long calculated, Long predicted, String measuredStatus,
			String predictedStatus) {
		super();
		this.id = new AlertPk(timestamp, station);
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
		this.measuredStatus = this.id.station.calculateStatus(quota);
	}

	public void registerPrediction(Long calculated, Long predicted) {
		this.calculated = calculated;
		this.predicted = predicted;
		this.predictedStatus = this.id.station.calculateStatus(predicted);
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
