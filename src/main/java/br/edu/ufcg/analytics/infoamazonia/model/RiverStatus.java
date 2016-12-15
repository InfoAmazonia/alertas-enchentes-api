package br.edu.ufcg.analytics.infoamazonia.model;

public enum RiverStatus {
	NORMAL, ATENCAO, ALERTA, INUNDACAO, INDISPONIVEL;

	public static RiverStatus get(Long data, Long... thresholds) {
		if(data == null || thresholds == null || thresholds.length == 0){
			return INDISPONIVEL;
		}
		for (int i = 0; i < thresholds.length; i++) {
			if(data < thresholds[i]){
				return values()[i];
			}
		}
		return INUNDACAO;
	}
}
	