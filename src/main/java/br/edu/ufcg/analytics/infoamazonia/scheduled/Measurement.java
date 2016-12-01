package br.edu.ufcg.analytics.infoamazonia.scheduled;

public class Measurement {
	
	public Long timestamp;
	public Long quota;
	
	public Measurement() {
		// TODO Auto-generated constructor stub
	}

	public Measurement(Long timestamp, Long quota) {
		super();
		this.timestamp = timestamp;
		this.quota = quota;
	}
	
}
