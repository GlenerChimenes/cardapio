package br.florinda;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SQLDatabase implements Database{

    @Override
    public List<ItemCardapio> listaDeItensCardapio() {
        List<ItemCardapio> itens = new ArrayList<>();

        String sql = "select id, nome, descricao, categoria, preco, preco_promocional from item_cardapio";

        try(Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/cardapio", "root", "senha123");
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Long id = rs.getLong("id");
                String nome = rs.getString("nome");
                String descricao = rs.getString("descricao");
                String categoriaStr = rs.getString("categoria");
                BigDecimal preco = rs.getBigDecimal("preco");
                BigDecimal precoPromocional = rs.getBigDecimal("preco_promocional");

                ItemCardapio.CategoriaCardapio categoria = ItemCardapio.CategoriaCardapio.valueOf(categoriaStr);

                ItemCardapio itemCardapio = new ItemCardapio(id, nome, descricao, categoria, preco, precoPromocional);

                itens.add(itemCardapio);
            }
            return itens;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void adicionaItem(ItemCardapio item) {
        String sql = "INSERT INTO item_cardapio (id, nome, descricao, categoria, preco, preco_promocional)\n" +
                "values(?, ?, ?, ?, ?, ?)";

        try(Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/cardapio", "root", "senha123");
            PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, item.id());
            ps.setString(2, item.nome());
            ps.setString(3, item.descricao());
            ps.setString(4, item.categoria().name());
            ps.setBigDecimal(5, item.preco());
            ps.setBigDecimal(6, item.precoComDesconto());

            ps.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int totalItemCardapio() {
        String sql = "select count(*) from item_cardapio";

        try(Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/cardapio", "root", "senha123");
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {

            int total = 0;
            if (rs.next()) {
                 total = rs.getInt(1);

            }
            return total;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean alteraPrecoItemCardapio(Long id, BigDecimal novoPreco) {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public boolean removeItemCardapio(Long id) {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public Optional<ItemCardapio> itemCardapioPorId(Long itemId) {
        throw new UnsupportedOperationException("TODO");
    }
}
