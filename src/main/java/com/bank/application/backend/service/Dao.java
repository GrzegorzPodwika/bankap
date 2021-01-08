package com.bank.application.backend.service;

import java.util.List;
import java.util.Optional;

public interface Dao <T>{

    T save(T t);

    T update(T t);

    void delete(T t);

    Optional<T> get(Integer id);

    List<T> getAll();

}
