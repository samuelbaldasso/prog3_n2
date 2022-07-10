package br.edu.femass.dao;


import br.edu.femass.model.ItemVenda;
import br.edu.femass.model.Tenis;
import br.edu.femass.model.Venda;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class VendaDao extends DaoPostgres implements Dao<Venda> {
    private final ProdutoDao produtoDao = new ProdutoDao();

    @Override
    public List<Venda> listar() throws Exception {
        String sql = "select * from venda order by data desc";
        PreparedStatement ps = getPreparedStatement(sql, true);
        ResultSet rs = ps.executeQuery();

        List<Venda> vendas = new ArrayList<>();

        while (rs.next()) {
            Venda venda = new Venda();
            venda.setValorTotal(rs.getFloat("valor_total"));
            venda.setId(rs.getLong("id"));
            venda.setData(rs.getDate("data"));
            vendas.add(venda);
        }
        return vendas;
    }

    public Float consultarValor(String data) throws Exception {
        String sql = "SELECT * FROM venda WHERE data = '" + data + "'";
        PreparedStatement ps = getPreparedStatement(sql, true);
        ResultSet rs = ps.executeQuery();
        Float totalVendido = 0F;

        while (rs.next()) {
            totalVendido = totalVendido + rs.getFloat("valor_total");
        }

        return totalVendido;
    }

    @Override
    public void gravar(Venda value) throws Exception {
        Connection conexao = getConexao();
        conexao.setAutoCommit(false);

        String date = LocalDateTime.now().getYear() + "-" + LocalDateTime.now().getMonthValue() + "-" + LocalDateTime.now().getDayOfMonth();
        try {
            String sql = "INSERT INTO venda (data, valor_total, id_cliente) VALUES (?,?,?)";
            PreparedStatement ps = getPreparedStatement(sql, true);
            ps.setDate(1, Date.valueOf(date));
            ps.setFloat(2, value.getValorTotal());
            ps.setLong(3, value.getCliente().getId());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            value.setId(rs.getLong(1));

            for (ItemVenda itemVendido : value.getItensVendidos()) {
                sql = "select estoque from produto where id = ?";
                PreparedStatement ps2 = getPreparedStatement(sql, false);
                ps2.setLong(1, itemVendido.getTenis().getId());

                ResultSet aux = ps2.executeQuery();
                while(aux.next()) {
                    itemVendido.getTenis().setEstoque(aux.getInt("estoque"));
                }

                sql = "INSERT INTO item_venda (qtd, preco_venda, id_venda, id_produto) VALUES (?,?,?,?)";
                PreparedStatement ps3 = getPreparedStatement(sql, true);
                ps3.setInt(1, itemVendido.getQtd());
                ps3.setFloat(2, itemVendido.getPrecoVenda());
                ps3.setLong(3, value.getId());
                ps3.setLong(4, itemVendido.getTenis().getId());
                ps3.executeUpdate();

                produtoDao.alterarProdutoVenda(itemVendido);

                if (itemVendido.equals(value.getItensVendidos().size())) {
                    conexao.commit();
                }
            }

        } catch (SQLException exception) {
            conexao.rollback();
            throw exception;
        }
    }

    @Override
    public void alterar(Venda value) throws Exception {
        //Este conjunto de dados não possui alteração
    }

    @Override
    public void excluir(Venda value) throws Exception {
        //Este conjunto de dados não possui exclusão
    }
}
