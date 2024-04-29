package org.example;

import java.io.Serializable;

public class Cursa extends Entity<Long> implements Serializable {

    private String destinatia;
    private String dataPlecare;
    private Integer oraPlecare;
    private Integer nrLocuri;

    public Cursa(String destinatia, String dataPlecare, Integer oraPlecare, Integer nrLocuri) {
        this.destinatia = destinatia;
        this.dataPlecare = dataPlecare;
        this.oraPlecare = oraPlecare;
        this.nrLocuri = nrLocuri;
    }

    public String getDestinatia() {
        return destinatia;
    }

    public void setDestinatia(String destinatia) {
        this.destinatia = destinatia;
    }

    public String getDataPlecare() {
        return dataPlecare;
    }

    public void setDataPlecare(String dataPlecare) {
        this.dataPlecare = dataPlecare;
    }

    public Integer getOraPlecare() {
        return oraPlecare;
    }

    public void setOraPlecare(Integer oraPlecare) {
        this.oraPlecare = oraPlecare;
    }

    public Integer getNrLocuri() {
        return nrLocuri;
    }

    public void setNrLocuri(Integer nrLocuri) {
        this.nrLocuri = nrLocuri;
    }
}
