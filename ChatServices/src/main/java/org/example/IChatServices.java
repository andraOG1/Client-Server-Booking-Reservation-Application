package org.example;

import org.example.User;

public interface IChatServices {
    void login(User user, IChatObserver client) throws ChatException;
    void logout(User user, IChatObserver client) throws ChatException;

    Iterable<Cursa> getAllCurse() throws ChatException; ;
    Cursa findByDestDataOra(String dest, String Data, Integer Ora) throws ChatException;
    User findOneUserByUsername(String username);
    Iterable<Rezervare> getAllRezervari();
    //Iterable<Rezervare> getAllRezervariByCursa(Long id) throws ChatException;

    void saveRezervare(Object  rezervare) throws ChatException;

    Cursa findOneCursa(Long id) throws ChatException;
    Iterable<Rezervare> listaRezByCursa(Cursa c) throws ChatException;



}
