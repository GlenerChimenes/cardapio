package br.florinda;

import java.math.BigDecimal;
import java.util.*;

import static br.florinda.ItemCardapio.CategoriaCardapio.*;

public class Database {

    private Map<Long, ItemCardapio> itensPorId = new HashMap<>();

    public Database() {
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

    public List<ItemCardapio> listaDeItensCardapio() {
        return itensPorId.values().stream().toList();
    }

    public Optional<ItemCardapio> itemCardapioPorId(Long itemId) {
        return Optional.ofNullable(itensPorId.get(itemId));
    }

    public void adicionaItem(ItemCardapio item) {
        itensPorId.put(item.id(), item);
    }
}
