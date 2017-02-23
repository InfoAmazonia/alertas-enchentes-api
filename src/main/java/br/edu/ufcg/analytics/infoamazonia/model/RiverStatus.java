package br.edu.ufcg.analytics.infoamazonia.model;

public enum RiverStatus {
	NORMAL, ATENCAO, ALERTA, INUNDACAO, INDISPONIVEL;

	public static RiverStatus get(Long data, Long... thresholds) {
		if(data == null || thresholds == null || thresholds.length == 0){
			return INDISPONIVEL;
		}
		RiverStatus result = NORMAL;
		for (int i = 0; i < thresholds.length; i++) {
			if(thresholds[i] == null){
				continue;
			}
			
			if(data < thresholds[i]){
				return result;
			}else{
				result = values()[i];
			}
		}
		return result;
	}
}
	