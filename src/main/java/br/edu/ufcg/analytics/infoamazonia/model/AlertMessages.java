package br.edu.ufcg.analytics.infoamazonia.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AlertMessages {

    @Value("${infoamazonia.alert.scenario.d}")
    public String d;

}