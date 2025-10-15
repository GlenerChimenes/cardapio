package br.florinda;

import com.google.gson.Gson;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class GeradorItensCardapioJSON {

    public static void main(String[] args) throws IOException {
        Database database = new Database();
        List<ItemCardapio> listaLtensCardapio = database.listaDeItensCardapio();

        Gson gson = new Gson();
        String json = gson.toJson(listaLtensCardapio);

        Path path = Path.of("itensCardapio.json");
        Files.writeString(path, json);

    }
    
}
