package br.edu.ufcg.analytics.infoamazonia.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Station implements Serializable{
	
	public static final String STATUS_ENCHENTE = "INUNDACAO";
	public static final String STATUS_ALERTA = "ALERTA";
	public static final String STATUS_NORMAL = "NORMAL";
	public static final String STATUS_INDISPONIVEL = "INDISPONIVEL";

	/**
	 * 
	 */
	private static final long serialVersionUID = -2183448021591991181L;

	@Id
	public Long id;
	
	public String name;

	public String riverName;
	public String cityName;
	
	public Long warningThreshold;
	public Long floodThreshold;

	public String oldestMeasureDate;
	public Boolean predict;
	public Long lstStation;
	@Column(length=10000)
	public String viewState;

	public int bacia;
	public int subbacia;
	
	public Station() {
		
	}
	
	public Station(long id, String name, String riverName, String cityName, long warningThreshold, long floodThreshold, String oldestMeasureDate, Boolean predict, Long lstStation, String viewState, int bacia, int subbacia) {
		this();
		this.name = name;
		this.id = id;
		this.riverName = riverName;
		this.cityName = cityName;
		this.warningThreshold = warningThreshold;
		this.floodThreshold = floodThreshold;
		this.oldestMeasureDate = oldestMeasureDate;
		this.predict = predict;
		this.lstStation = lstStation;
		this.viewState = viewState;
		this.bacia = bacia;
		this.subbacia = subbacia;
	}

	public String calculateStatus(Long quota) {
		if(quota == null)
			return STATUS_INDISPONIVEL;
		if(quota < warningThreshold)
			return STATUS_NORMAL;
		if (warningThreshold <= quota && quota < floodThreshold)
			return STATUS_ALERTA;
		if (floodThreshold <= quota)
			return STATUS_ENCHENTE;
		return STATUS_INDISPONIVEL;
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
