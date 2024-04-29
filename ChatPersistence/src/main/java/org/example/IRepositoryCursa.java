package org.example;

import org.example.Cursa;

public interface IRepositoryCursa extends IRepository<Long, Cursa>{

    void save(Cursa entity);

    void delete(Long id);

    void update(Cursa entity);

    Cursa findOne(Long id);

    Cursa findOneByDestDataOra(String dest, String Data, Integer Ora);

    Iterable<Cursa> findAll();
}
