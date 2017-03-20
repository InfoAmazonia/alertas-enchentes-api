package br.edu.ufcg.analytics.infoamazonia.model;

public enum RiverStatus {
	NORMAL("NORMAL"), ATENCAO("ATENÇÃO"), ALERTA("ALERTA"), INUNDACAO("INUNDAÇÃO"), INDISPONIVEL("INDISPONÍVEL");
	
	private String printableName;
	
	private RiverStatus(String printableName) {
		this.printableName = printableName;
	}
	
	public String getPrintableName(){
		return this.printableName;		
	}

	public static RiverStatus get(Integer data, Integer... thresholds) {
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
				result = values()[i+1];
			}
		}
		return result;
	}
}
	