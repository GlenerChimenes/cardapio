package br.florinda;

import com.google.gson.Gson;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        Database db = new Database();

        List<ItemCardapio> itens = db.listaDeItensCardapio();
        Optional<ItemCardapio> optionalItem = db.itemCardapioPorId(100L);
        itens.forEach(System.out::println);

        System.out.println("-------------------------------------");

        if(optionalItem.isPresent()){
            ItemCardapio item = optionalItem.get();
            System.out.println(item);
        }else {
            System.out.println("Nao encontrado");
        }
        System.out.println("-----------------------------------");


        /*O HashSet usa um HashMap por debaixo dos panos. Então é muito rápido para inserção/alteração de registros*/
         Set<ItemCardapio.CategoriaCardapio> categorias = new HashSet<>();
        Comparator<ItemCardapio.CategoriaCardapio> comparadorPorNome = Comparator.comparing(ItemCardapio.CategoriaCardapio::name);
        //programação tradicional
         for (ItemCardapio item : itens) {
             ItemCardapio.CategoriaCardapio categoria = item.categoria();
             categorias.add(categoria);
         }

         for (ItemCardapio.CategoriaCardapio categoria : categorias) {
             System.out.println(categoria);
         }
        System.out.println("-------------------------------------");

         //programação funcional
         itens.stream()
                 .map(ItemCardapio::categoria)
                 .collect(Collectors.toSet())
                 .forEach(System.out::println);


        System.out.println("-------------------------------------");

        //programação funcional com comparator
        // Use TreeSet quando quiser manter sem repetição dos itens e ordenado. Pode ser usado Comparator. Por padrão ele vai usar a ordem natural do objeto.
        itens.stream()
                .map(ItemCardapio::categoria)
                .collect(Collectors.toCollection(() -> new TreeSet<>(comparadorPorNome)))
                .forEach(System.out::println);


        System.out.println("-------------------------------------");

        HashMap<ItemCardapio.CategoriaCardapio, Integer> itensPorCategoria = new HashMap<>();

        for (ItemCardapio item : itens) {
            ItemCardapio.CategoriaCardapio categoria = item.categoria();
            if(!itensPorCategoria.containsKey(categoria)){
                itensPorCategoria.put(categoria,1);
            }else{
                Integer quantidadeAnterior = itensPorCategoria.get(categoria);
                itensPorCategoria.put(categoria, quantidadeAnterior+1);
            }
        }
        System.out.println(itensPorCategoria);

        itens.stream()
                .collect(Collectors.groupingBy(
                        ItemCardapio::categoria,
                        TreeMap::new,
                        Collectors.counting()
                ))
                .forEach((chave, valor) -> System.out.println(chave + " - " + valor));

    }
}