package br.edu.femass.dao;

import br.edu.femass.model.Cliente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClienteDao extends DaoPostgres implements Dao<Cliente>{
    @Override
    public List<Cliente> listar() throws Exception {
        String sql = "SELECT * FROM cliente ORDER BY nome";
        PreparedStatement ps = getPreparedStatement(sql, false);
        ResultSet rs = ps.executeQuery();

        List<Cliente> clientes = new ArrayList<>();

        while (rs.next()) {
            Cliente cliente = new Cliente();
            cliente.setNome(rs.getString("nome"));
            cliente.setId(rs.getLong("id"));
            cliente.setCpf(rs.getString("cpf"));
            clientes.add(cliente);
        }
        return clientes;
    }

    @Override
    public void gravar(Cliente value) throws Exception {
        String sql = "INSERT INTO cliente (nome, cpf) VALUES (?,?)";
        PreparedStatement ps = getPreparedStatement(sql, true);
        ps.setString(1, value.getNome());
        ps.setString(2, value.getCpf());
        ps.executeUpdate();

        ResultSet rs = ps.getGeneratedKeys();
        rs.next();
        value.setId(rs.getLong(1));
    }

    @Override
    public void alterar(Cliente value) throws Exception {
        String sql = "UPDATE cliente SET nome = ?, cpf = ? WHERE id = ?";
        PreparedStatement ps = getPreparedStatement(sql, false);
        ps.setString(1, value.getNome());
        ps.setString(2, value.getCpf());
        ps.setLong(3, value.getId());
        ps.executeUpdate();
    }

    @Override
    public void excluir(Cliente value) throws Exception {
        Connection conexao = getConexao();
        conexao.setAutoCommit(false);
        try {
            String sql = "DELETE FROM cliente WHERE id = ?";
            PreparedStatement ps1 = conexao.prepareStatement(sql);
            ps1.setLong(1, value.getId());
            ps1.executeUpdate();
            conexao.commit();
        } catch (SQLException exception) {
            conexao.rollback();
            throw exception;
        }
    }
}
