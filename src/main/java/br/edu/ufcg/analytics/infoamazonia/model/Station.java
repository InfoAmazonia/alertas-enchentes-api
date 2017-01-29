package br.edu.ufcg.analytics.infoamazonia.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Station implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2183448021591991181L;

	@Id
	public Long id;
	
	public String name;

	public String riverName;
	public String cityName;
	
	public String oldestMeasureDate;
	public Boolean predict;
	public Long lstStation;
	
	@Column(length=10000)
	public String viewState;

	public int bacia;
	public int subbacia;
	
	public Long warningThreshold;
	public Long attentionThreshold;
	public Long floodThreshold;
	
	public String timezone;
	
	public Long predictionWindow;
	
	public RiverStatus calculateStatus(Long quota) {
		return RiverStatus.get(quota, attentionThreshold, warningThreshold, floodThreshold);
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
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
		return id == ((Station) obj).id;
	}

	@Override
	public String toString() {
		return "Station [name=" + name + ", id=" + id + "]";
	}
}
