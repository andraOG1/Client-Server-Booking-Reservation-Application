package org.example.dto;

import org.example.Cursa;
import org.example.User;
import org.example.Rezervare;


public class DTOUtils {
    public static User getFromDTO(UserDTO usdto){
        String username = usdto.getUsername();
        String pass = usdto.getPassword();
        return new User(username, pass);

    }
    public static UserDTO getDTO(User user){
        String name=user.getUsername();
        String pass=user.getPassword();
        return new UserDTO(name, pass);
    }

    public static Cursa getFromDTO(CursaDTO cursaDTO){
        String destinatie = cursaDTO.getDestinatie();
        String data = cursaDTO.getData();
        Integer ora = cursaDTO.getOra();
        Integer locuri = cursaDTO.getLocuriDisponibile();
        return new Cursa(destinatie, data, ora, locuri);
    }

    public static CursaDTO getDTO(Cursa cursa){
        String destinatie = cursa.getDestinatia();
        String data = cursa.getDataPlecare();
        Integer ora = cursa.getOraPlecare();
        Integer locuri = cursa.getNrLocuri();
        return new CursaDTO(destinatie, data, ora, locuri);
    }

    public static Rezervare getFromDTO(RezervareDTO rezDTO)
    {
        String numeClient = rezDTO.getNumeClient();
        Integer nrLocuri = rezDTO.getNrLocuri();
        Long idCursa = rezDTO.getIdCursa();
        return new Rezervare(numeClient, nrLocuri,idCursa);
    }

    public static RezervareDTO getDTO(Rezervare rez)
    {
        String numeClient = rez.getNumeClient();
        Integer nrLocuri = rez.getNrLocuri();
        Long idCursa = rez.getIdCursa();
        return new RezervareDTO(numeClient, nrLocuri, idCursa);
    }

}
