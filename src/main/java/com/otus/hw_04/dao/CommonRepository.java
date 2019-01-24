package com.otus.hw_04.dao;

import java.util.Collection;

public interface CommonRepository<T> {

    T save(T domain);

    Iterable<T> save(Collection<T> domains);

    void delete(T domain);

    T findById(long id);

    Iterable<T> findAll();

}
