package br.florinda;

import com.google.gson.Gson;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServidorItensCardapioComSocket {

    private static final Database database = new Database();

    public static void main(String[] args) throws Exception {

        try (ExecutorService executorService = Executors.newFixedThreadPool(50)) {

            try (ServerSocket serverSocket = new ServerSocket(8000)) {
                System.out.println("Subiu servidor");

                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    executorService.execute(() -> trataRequisicao(clientSocket));
                }
            }
        }
    }

    private static void trataRequisicao(Socket clientSocket) {
        try (clientSocket) {
            InputStream clientIS = clientSocket.getInputStream();

            StringBuilder requestBuilder = new StringBuilder();
            int data;
            do {
                data = clientIS.read();
                requestBuilder.append((char) data);
            } while (clientIS.available() > 0);

            String request = requestBuilder.toString();
            System.out.println("---------------------------------");
            System.out.println(request);
            System.out.println("\n\nChegou um novo request");

            Thread.sleep(250);

            String[] requestChuncks = request.split("\r\n\r\n");
            String requestLineAndHeaders = requestChuncks[0];
            String[] requestLineAndHeadersChuncks = requestLineAndHeaders.split("\r\n");
            String requestLine = requestLineAndHeadersChuncks[0];
            String[] requestLineChuncks = requestLine.split(" ");

            String method = requestLineChuncks[0];
            String requestURI = requestLineChuncks[1];

            System.out.println("------------------------------------");
            System.out.println(method);
            System.out.println(requestURI);

            OutputStream clientOS = clientSocket.getOutputStream();
            PrintStream clientOut = new PrintStream(clientOS);

            if(method.equals("GET") && requestURI.equals("/itensCardapio.json")){
                System.out.println("Chamou arquivo json");
                Path path = Path.of("itensCardapio.json");
                String json = Files.readString(path);

                clientOut.println("HTTP/1.1 200 OK");
                clientOut.println("Content-Type: application/json; charset=utf-8");
                clientOut.println();
                clientOut.println(json);
            } else if (method.equals("GET") && requestURI.equals("/itens-cardapio")) {
                System.out.println("Chamou listagem de itens de cardápio");
                List<ItemCardapio> listaItensCardapio = database.listaDeItensCardapio();

                Gson gson = new Gson();
                String json = gson.toJson(listaItensCardapio);

                clientOut.println("HTTP/1.1 200 OK");
                clientOut.println("Content-Type: application/json; charset=utf-8");
                clientOut.println();
                clientOut.println(json);
            }else if (method.equals("GET") && requestURI.equals("/itens-cardapio/total")) {
                System.out.println("Chamou listagem de itens de cardápio total");
                List<ItemCardapio> listaItensCardapio = database.listaDeItensCardapio();

                clientOut.println("HTTP/1.1 200 OK");
                clientOut.println("Content-Type: application/json; charset=utf-8");
                clientOut.println();
                clientOut.println(listaItensCardapio.size());
            }else if (method.equals("POST") && requestURI.equals("/itens-cardapio")) {
                System.out.println("Chamou adição de item cardápio");

                if(requestChuncks.length != 1){
                    clientOut.println("HTTP/1.1 400 Bad Request");
                    return;
                }

                String body = requestChuncks[1] ;

                Gson gson = new Gson();
                ItemCardapio novoItemCardapio = new Gson().fromJson(body, ItemCardapio.class);

                database.adicionaItem(novoItemCardapio);

                clientOut.println("HTTP/1.1 201 Created");

            }else {
                System.out.println("URI não encontrada: " + requestURI);
                clientOut.println("HTTP/1.1 404 Not Found");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
