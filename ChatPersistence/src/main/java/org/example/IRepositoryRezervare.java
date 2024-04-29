package org.example;

import org.example.Rezervare;

public interface IRepositoryRezervare extends IRepository<Long, Rezervare>{

    void save(Rezervare entity);

    void delete(Long id);

    void update(Rezervare entity);

    Rezervare findOne(Long id);
    Rezervare findOneByNumeNrLocuri(String nume, Integer nrLocuri);

    Iterable<Rezervare> findAll();
    Iterable<Rezervare> findAllByCursa(Long id);

}
