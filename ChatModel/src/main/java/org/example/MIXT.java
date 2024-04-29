package org.example;

public class MIXT {

    private Integer nrLoc;
    private String numeClient;

    public MIXT(Integer nrLoc, String numeClient) {
        this.nrLoc = nrLoc;
        this.numeClient = numeClient;
    }

    public Integer getNrLoc() {
        return nrLoc;
    }

    public void setNrLoc(Integer nrLoc) {
        this.nrLoc = nrLoc;
    }

    public String getNumeClient() {
        return numeClient;
    }

    public void setNumeClient(String numeClient) {
        this.numeClient = numeClient;
    }
}
