package br.edu.femass.dao;

import java.sql.*;

public abstract class DaoPostgres {
    protected static String ENDERECO = "localhost";
    protected static String BD = "loja_tenis";
    protected static String PORTA = "5432";
    protected static String USUARIO = "postgres";
    protected static String SENHA = "admin";

    protected Connection getConexao() throws SQLException {
        String url = "jdbc:postgresql://" + ENDERECO + ":" + PORTA + "/" + BD;
        Connection con = DriverManager.getConnection(url, USUARIO, SENHA);
        return con;
    }

    protected PreparedStatement getPreparedStatement(String sql, Boolean insercao) throws Exception {
        PreparedStatement ps = null;
        if (insercao) {
            return getConexao().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        } else {
            return getConexao().prepareStatement(sql);
        }
    }
}
