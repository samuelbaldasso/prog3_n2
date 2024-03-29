package br.edu.femass.dao;

import br.edu.femass.model.Compra;
import br.edu.femass.model.ItemCompra;
import br.edu.femass.model.Tenis;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CompraDao extends DaoPostgres implements Dao<Compra> {
    private final ProdutoDao produtoDao = new ProdutoDao();

    @Override
    public List<Compra> listar() throws Exception {
        String sql = "select * from compra order by data desc";
        PreparedStatement ps = getPreparedStatement(sql, true);
        ResultSet rs = ps.executeQuery();

        List<Compra> compras = new ArrayList<>();

        while (rs.next()) {
            Compra compra = new Compra();
            compra.setValorTotal(rs.getFloat("valor_total"));
            compra.setId(rs.getLong("id"));
            compra.setData(rs.getDate("data"));
            compras.add(compra);
        }
        return compras;
    }

    public List<ItemCompra> listarItemCompra(Tenis tenis) throws Exception {
        String sql = "select * from item_compra where id_produto = ?";
        PreparedStatement ps = getPreparedStatement(sql, true);
        ResultSet rs = ps.executeQuery();

        List<ItemCompra> itemCompras = new ArrayList<>();

        while (rs.next()) {
            ItemCompra itemCompra = new ItemCompra();
            itemCompra.setQtd(rs.getInt("qtd"));
            itemCompra.setId(rs.getLong("id"));
            itemCompra.setPrecoCompra(rs.getFloat("preco_compra"));
            itemCompra.setTenis(tenis);

            itemCompras.add(itemCompra);
        }
        return itemCompras;
    }

    public Float consultarValor(String data) throws Exception {
        Float totalComprado = 0F;
        String sql = "SELECT * FROM compra WHERE data = '" + data + "'";
        PreparedStatement ps = getPreparedStatement(sql, true);
        ResultSet rs = ps.executeQuery();
        try {
            while (rs.next()) {
                totalComprado = totalComprado + rs.getFloat("valor_total");
            }
        } catch (Exception e) {

        }

        return totalComprado;
    }

    @Override
    public void gravar(Compra value) throws Exception {

        Connection conexao = getConexao();
        conexao.setAutoCommit(false);

        String date = LocalDateTime.now().getYear() + "-" + LocalDateTime.now().getMonthValue() + "-" + LocalDateTime.now().getDayOfMonth();
        try {
            String sql = "INSERT INTO compra (data, valor_total, id_fornecedor) VALUES (?,?,?)";
            PreparedStatement ps = getPreparedStatement(sql, true);
            ps.setDate(1, Date.valueOf(date));
            ps.setFloat(2, value.getValorTotal());
            ps.setLong(3, value.getFornecedor().getId());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            value.setId(rs.getLong(1));

            for (ItemCompra itemComprado : value.getItensComprados()) {
                sql = "select estoque from produto where id = ?";
                PreparedStatement ps2 = getPreparedStatement(sql, false);
                ps2.setLong(1, itemComprado.getTenis().getId());

                ResultSet aux = ps2.executeQuery();
                while (aux.next()) {
                    itemComprado.getTenis().setEstoque(aux.getInt("estoque"));
                }

                sql = "INSERT INTO item_compra (qtd, preco_compra, id_compra, id_produto) VALUES (?,?,?,?)";
                PreparedStatement ps3 = getPreparedStatement(sql, true);
                ps3.setInt(1, itemComprado.getQtd());
                ps3.setFloat(2, itemComprado.getPrecoCompra());
                ps3.setLong(3, value.getId());
                ps3.setLong(4, itemComprado.getTenis().getId());
                ps3.executeUpdate();


                produtoDao.alterarProdutoCompra(itemComprado);

                if (itemComprado.equals(value.getItensComprados().size())) {
                    conexao.commit();
                }
            }
        } catch (SQLException exception) {
            conexao.rollback();
            throw exception;
        }
    }

    @Override
    public void alterar(Compra value) throws Exception {
        //Este conjunto de dados não possui alteração
    }

    @Override
    public void excluir(Compra value) throws Exception {
        //Este conjunto de dados não possui exclusão
    }
}
