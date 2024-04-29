package org.example.dto;

import java.io.Serializable;

public class RezervareDTO implements Serializable {

    private String numeClient;
    private Integer nrLocuri;
    private Long idCursa;

    public RezervareDTO(String numeClient, Integer nrLocuri, Long idCursa) {
        this.numeClient = numeClient;
        this.nrLocuri = nrLocuri;
        this.idCursa = idCursa;
    }

    public String getNumeClient() {
        return numeClient;
    }

    public Integer getNrLocuri() {
        return nrLocuri;
    }

    public Long getIdCursa() {
        return idCursa;
    }
}
