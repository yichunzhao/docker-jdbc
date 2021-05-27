package com.ynz.demo.dockerjdbc.dao;

public interface CrudeMethods<T, ID> {

    T findById(ID id);

    int insert(T entity);

    T update(ID id, T entity);

    void deleteById(ID id);

}
