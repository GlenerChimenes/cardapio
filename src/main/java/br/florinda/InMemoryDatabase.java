package br.florinda;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static br.florinda.ItemCardapio.CategoriaCardapio.*;

public class InMemoryDatabase implements Database {

    private final Map<Long, ItemCardapio> itensPorId = new ConcurrentHashMap<>();
    private final Map<ItemCardapio, BigDecimal> auditoriaPrecos = new HashMap<>();


    public InMemoryDatabase() {
        var refrescoDoChaves = new ItemCardapio(1L, "Refresco do Chaves",
                "Suco de limão, que parece tamarindo mas tem gosto de graselha",
                BEBIDAS, new BigDecimal("2.99"), null);
        itensPorId.put(1L, refrescoDoChaves);

        var cafeDaDonaFlorinda = new ItemCardapio(1L, "Café da Dona Florinda",
                "Café forte para começar o dia com energia.",
                BEBIDAS, new BigDecimal("1.99"), null);
        itensPorId.put(2L, cafeDaDonaFlorinda);

        var churrosDoChaves = new ItemCardapio(1L, "Churros do Chaves",
                "Churros recheados com dice de leite.",
                SOBREMESA, new BigDecimal("4.99"), null);
        itensPorId.put(3L, churrosDoChaves);

        var sanduicheDoChaves = new ItemCardapio(1L, "Chanduíche de Presunto do Chaves",
                "sanduíche de presunto simples, mas feito com muito amor.",
                PRATOS_PRINCIPAIS, new BigDecimal("3.50"), null);
        itensPorId.put(4L, sanduicheDoChaves);

        var tortaDaDonaFlorinda = new ItemCardapio(1L, "Torta de frango Dona Florinda",
                "Torta de frango com recheio cremoso e massa crocante",
                PRATOS_PRINCIPAIS, new BigDecimal("5.0"), null);
        itensPorId.put(5L, tortaDaDonaFlorinda);

        var pipocaDoQuico = new ItemCardapio(1L, "Pipoca do Quico",
                "Balde de pipoca com manteiga.",
                PRATOS_PRINCIPAIS, new BigDecimal("10.0"), null);
        itensPorId.put(6L, pipocaDoQuico);

    }

    @Override
    public boolean alteraPrecoItemCardapio(Long id, BigDecimal novoPreco) {
        ItemCardapio item = itensPorId.get(id);
        if (item == null) return false;
        ItemCardapio itemPrecoAlterado = item.alteraPreco(novoPreco);
        itensPorId.put(id, itemPrecoAlterado);
        auditoriaPrecos.put(item, novoPreco);
        return true;
    }

    @Override
    public boolean removeItemCardapio(Long id) {
        ItemCardapio removido = itensPorId.get(id);
        return removido !=null;
    }

    public void rastroAuditoriaPrecos() {
        System.out.println("\nAuditoria de preços:");
        auditoriaPrecos.forEach((ItemCardapio item, BigDecimal preco) ->
                System.out.printf(" - %s: %s => %s%n", item.nome(), item.preco(), preco));
        System.out.println();
    }

    @Override
    public List<ItemCardapio> listaDeItensCardapio() {
        return itensPorId.values().stream().toList();
    }

    @Override
    public Optional<ItemCardapio> itemCardapioPorId(Long itemId) {
        return Optional.ofNullable(itensPorId.get(itemId));
    }

    @Override
    public void adicionaItem(ItemCardapio item) {
        itensPorId.put(item.id(), item);
    }

    @Override
    public int totalItemCardapio() {
        return itensPorId.size();
    }
}
