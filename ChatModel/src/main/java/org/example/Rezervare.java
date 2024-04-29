package org.example;
import java.io.Serializable;
import java.util.Objects;

public class Rezervare extends Entity<Long> implements Serializable {

    private String numeClient;
    private Integer nrLocuri;
    private Long idCursa;

    public Rezervare(String numeClient, Integer nrLocuri, Long idCursa) {
        this.numeClient = numeClient;
        this.nrLocuri = nrLocuri;
        this.idCursa = idCursa;
    }

    public Long getIdCursa() {
        return idCursa;
    }

    public void setIdCursa(Long idCursa) {
        this.idCursa = idCursa;
    }

    public String getNumeClient() {
        return numeClient;
    }

    public void setNumeClient(String numeClient) {
        this.numeClient = numeClient;
    }

    public Integer getNrLocuri() {
        return nrLocuri;
    }

    public void setNrLocuri(Integer nrLocuri) {
        this.nrLocuri = nrLocuri;
    }

    @Override
    public String toString() {
        return "Rezervare{" +
                "numeClient='" + numeClient + '\'' +
                ", nrLocuri=" + nrLocuri +
                ", idCursa=" + idCursa +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Rezervare rezervare = (Rezervare) o;
        return Objects.equals(numeClient, rezervare.numeClient) && Objects.equals(nrLocuri, rezervare.nrLocuri) && Objects.equals(idCursa, rezervare.idCursa);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), numeClient, nrLocuri, idCursa);
    }
}
