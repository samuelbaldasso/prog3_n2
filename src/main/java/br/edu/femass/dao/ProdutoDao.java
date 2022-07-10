package br.edu.femass.dao;

import br.edu.femass.model.ItemCompra;
import br.edu.femass.model.ItemVenda;
import br.edu.femass.model.Tenis;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDao extends DaoPostgres implements Dao<Tenis>{
    @Override
    public List<Tenis> listar() throws Exception {
        String sql = "select * from produto order by id";
        PreparedStatement ps = getPreparedStatement(sql, false);
        ResultSet rs = ps.executeQuery();

        List<Tenis> tenis = new ArrayList<>();
        while (rs.next()) {
            Tenis t = new Tenis();
            t.setNome(rs.getString("nome"));
            t.setId(rs.getLong("id"));
            t.setEstoque(rs.getInt("estoque"));
            tenis.add(t);
        }
        return tenis;
    }

    @Override
    public void gravar(Tenis value) throws Exception {
        try {
            String sql = "INSERT INTO produto (nome, preco_venda, estoque) VALUES (?,?,?)";
            PreparedStatement ps = getPreparedStatement(sql, true);
            ps.setString(1, value.getNome());
            ps.setFloat(2, value.getPrecoVenda());
            ps.setInt(3, value.getEstoque());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            value.setId(rs.getLong(1));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void alterar(Tenis value) throws Exception {
        String sql = "UPDATE produto set nome = ? where id = ?";
        PreparedStatement ps = getPreparedStatement(sql, false);
        ps.setString(1, value.getNome());
        ps.setLong(2, value.getId());
     //   ps.setFloat(2, value.getPrecoVenda());
     //   ps.setInt(3, value.getEstoque());

        ps.executeUpdate();
    }

    public void alterarProdutoVenda(ItemVenda value) throws Exception {
        try{
            String sql = "UPDATE produto SET estoque = ?, preco_venda = ? WHERE id = ?";
            PreparedStatement ps = getPreparedStatement(sql, false);
            ps.setInt(1, value.getTenis().getEstoque() - value.getQtd());
            ps.setFloat(2, value.getPrecoVenda());
            ps.setLong(3, value.getTenis().getId());
            ps.executeUpdate();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void alterarProdutoCompra(ItemCompra value) {
        try{
            String sql = "UPDATE produto SET estoque = ? WHERE id = ?";
            PreparedStatement ps = getPreparedStatement(sql, false);
            ps.setInt(1,  value.getTenis().getEstoque() + value.getQtd());
            ps.setLong(2, value.getTenis().getId());
            ps.executeUpdate();

        }catch(Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void excluir(Tenis value) throws Exception {
        Connection conexao = getConexao();
        conexao.setAutoCommit(false);
        try {
            String sql = "DELETE FROM produto WHERE id = ?";
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
