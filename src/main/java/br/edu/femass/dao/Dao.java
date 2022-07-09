package br.edu.femass.dao;

import java.util.List;

public interface Dao<T> {
    public List<T> listar() throws Exception;
    public void gravar(T value) throws Exception;
    public void alterar(T value) throws Exception;
    public void excluir(T value) throws Exception;
}
