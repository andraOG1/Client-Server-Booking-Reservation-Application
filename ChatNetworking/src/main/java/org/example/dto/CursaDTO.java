package org.example.dto;

import java.io.Serializable;

public class CursaDTO implements Serializable {

    private String destinatie;
    private String data;
    private Integer ora;
    private Integer locuriDisponibile;

    public CursaDTO(String destinatie, String data, Integer ora, Integer locuriDisponibile) {

        this.destinatie = destinatie;
        this.data = data;
        this.ora = ora;
        this.locuriDisponibile = locuriDisponibile;
    }


    public String getDestinatie() {
        return destinatie;
    }

    public String getData() {
        return data;
    }

    public Integer getOra() {
        return ora;
    }

    public Integer getLocuriDisponibile() {
        return locuriDisponibile;
    }
}
