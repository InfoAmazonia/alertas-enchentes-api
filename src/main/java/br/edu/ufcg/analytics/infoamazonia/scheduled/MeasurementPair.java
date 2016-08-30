package br.edu.ufcg.analytics.infoamazonia.scheduled;

public class MeasurementPair {
	
	public Long timestamp;
	public Long quota;
	
	public MeasurementPair() {
		// TODO Auto-generated constructor stub
	}

	public MeasurementPair(Long timestamp, Long quota) {
		super();
		this.timestamp = timestamp;
		this.quota = quota;
	}
	
}
