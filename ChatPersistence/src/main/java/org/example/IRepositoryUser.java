package org.example;

import org.example.User;

public interface IRepositoryUser extends IRepository<Long, User> {

    void save(User entity);

    void delete(Long id);

    void update(User entity);

    User findOne(Long id);

    User findOneByUsername(String username);

    Iterable<User> findAll();
}
