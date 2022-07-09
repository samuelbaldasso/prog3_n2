package br.edu.femass.dao;

import br.edu.femass.model.Fornecedor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FornecedorDao extends DaoPostgres implements Dao<Fornecedor>{
    @Override
    public List<Fornecedor> listar() throws Exception {
        String sql = "SELECT * FROM fornecedor ORDER BY nome";
        PreparedStatement ps = getPreparedStatement(sql, false);
        ResultSet rs = ps.executeQuery();

        List<Fornecedor> forncededores = new ArrayList<>();

        while (rs.next()) {
            Fornecedor fornecedor = new Fornecedor();
            fornecedor.setNome(rs.getString("nome"));
            fornecedor.setId(rs.getLong("id"));
            fornecedor.setCnpj(rs.getString("cnpj"));
            forncededores.add(fornecedor);
        }
        return forncededores;
    }

    @Override
    public void gravar(Fornecedor value) throws Exception {
        String sql = "INSERT INTO fornecedor (nome, cnpj) VALUES (?,?)";
        PreparedStatement ps = getPreparedStatement(sql, true);
        ps.setString(1, value.getNome());
        ps.setString(2, value.getCnpj());

        ps.executeUpdate();

        ResultSet rs = ps.getGeneratedKeys();
        rs.next();
        value.setId(rs.getLong(1));

    }

    @Override
    public void alterar(Fornecedor value) throws Exception {
        String sql = "update fornecedor set nome = ?, cnpj = ? where id = ?";
        PreparedStatement ps = getPreparedStatement(sql, false);
        ps.setString(1, value.getNome());
        ps.setString(2, value.getCnpj());
        ps.setLong(3, value.getId());
        ps.executeUpdate();
    }

    @Override
    public void excluir(Fornecedor value) throws Exception {
        Connection conexao = getConexao();
        conexao.setAutoCommit(false);
        try {
            String sql = "delete from fornecedor where id = ?";
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
