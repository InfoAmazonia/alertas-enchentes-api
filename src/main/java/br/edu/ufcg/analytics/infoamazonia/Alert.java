package br.edu.ufcg.analytics.infoamazonia;
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
	private AlertPk id;

	@Column(nullable=true)
	private Long measured;

	@Column(nullable=true)
	private Long predicted;
	
	@Column(nullable=true)
	private Long corrected;
	
	@Column(nullable=true)
	private String measuredStatus;
	
	@Column(nullable=true)
	private String predictedStatus;
	
	public Alert() {
		// TODO Auto-generated constructor stub
	}
	
	public Alert(AlertPk id, Long measured, Long predicted, Long corrected, String measuredStatus,
			String predictedStatus) {
		super();
		this.id = id;
		this.measured = measured;
		this.predicted = predicted;
		this.corrected = corrected;
		this.measuredStatus = measuredStatus;
		this.predictedStatus = predictedStatus;
	}

	public Alert(AlertPk id, Long predicted, Long corrected, String status) {
		this(id, null, predicted, corrected, status, null);
	}

	public Alert(AlertPk id, Long measured) {
		this(id, measured, null, null, null, null);
	}
	
	public AlertPk getId() {
		return id;
	}

	public void setId(AlertPk id) {
		this.id = id;
	}

	public Long getMeasured() {
		return measured;
	}

	public void setMeasured(Long measured) {
		this.measured = measured;
	}

	public Long getPredicted() {
		return predicted;
	}

	public void setPredicted(Long predicted) {
		this.predicted = predicted;
	}

	public Long getCorrected() {
		return corrected;
	}

	public void setCorrected(Long corrected) {
		this.corrected = corrected;
	}
	
	public String getMeasuredStatus() {
		return measuredStatus;
	}

	public void setMeasuredStatus(String measuredStatus) {
		this.measuredStatus = measuredStatus;
	}

	public String getPredictedStatus() {
		return predictedStatus;
	}

	public void setPredictedStatus(String predictedStatus) {
		this.predictedStatus = predictedStatus;
	}

	@Override
	public String toString() {
		return "Alert [id=" + id + ", measured=" + measured + ", predicted=" + predicted + ", corrected=" + corrected
				+ ", measuredStatus=" + measuredStatus + ", predictedStatus=" + predictedStatus + "]";
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
}
