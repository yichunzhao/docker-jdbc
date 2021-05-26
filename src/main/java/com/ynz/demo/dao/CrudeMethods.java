package com.ynz.demo.dao;

public interface CrudeMethods<T, ID> {

    T findById(ID id);

    T save(T entity);

    T update(ID id, T entity);

    void deleteById(ID id);

}
