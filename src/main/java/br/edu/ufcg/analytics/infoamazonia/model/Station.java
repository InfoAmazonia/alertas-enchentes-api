package br.edu.ufcg.analytics.infoamazonia.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Station implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2183448021591991181L;

	@Id
	public Long id;
	
	public String name;
	public Long warningThreshold;
	public Long floodThreshold;
	@JsonIgnore
	public String oldestMeasureDate;
	@JsonIgnore
	public Boolean predict;
	@JsonIgnore
	public Long lstStation;
	@Column(length=5000)
	@JsonIgnore
	public String viewState;

	public int subbacia;
	
	public Station() {
		
	}
	
	public Station(String name, long id, long warningThreshold, long floodThreshold, String oldestMeasureDate, Boolean predict, Long lstStation, String viewState, int subbacia) {
		this();
		this.name = name;
		this.id = id;
		this.warningThreshold = warningThreshold;
		this.floodThreshold = floodThreshold;
		this.oldestMeasureDate = oldestMeasureDate;
		this.predict = predict;
		this.lstStation = lstStation;
		this.viewState = viewState;
		this.subbacia = subbacia;
	}

	public String calculateStatus(long quota) {
		if(quota < warningThreshold){
			return "NORMAL";
		}else if (warningThreshold <= quota && quota < floodThreshold){
			return "ALERTA";
		}else{
			return "INUNDACAO";
		}
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
		Station other = (Station) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Station [name=" + name + ", id=" + id + ", warningThreshold=" + warningThreshold + ", floodThreshold="
				+ floodThreshold + "]";
	}
}
