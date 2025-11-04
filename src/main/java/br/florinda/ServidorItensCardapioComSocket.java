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
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServidorItensCardapioComSocket {

    private static final Logger logger = Logger.getLogger(ServidorItensCardapioComSocket.class.getName());

    private static final Database database = new SQLDatabase();

    public static void main(String[] args) throws Exception {

        try (ExecutorService executorService = Executors.newFixedThreadPool(50)) {

            try (ServerSocket serverSocket = new ServerSocket(8000)) {
                logger.info("Subiu servidor");

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
            logger.finest(request);
            logger.fine("\n\nChegou um novo request");

            String[] requestChuncks = request.split("\r\n\r\n");
            String requestLineAndHeaders = requestChuncks[0];
            String[] requestLineAndHeadersChuncks = requestLineAndHeaders.split("\r\n");
            String requestLine = requestLineAndHeadersChuncks[0];
            String[] requestLineChuncks = requestLine.split(" ");
            String method = requestLineChuncks[0];
            String requestURI = requestLineChuncks[1];
            String httpVersion = requestLineChuncks[2];

            logger.finer(() -> "Method: " + method);
            logger.finer(() -> "Request URI: " + requestURI);
            logger.finer(() -> "HTTP Version: " + httpVersion);

            Thread.sleep(250);

            OutputStream clientOS = clientSocket.getOutputStream();
            PrintStream clientOut = new PrintStream(clientOS);

            try {

                if (method.equals("GET") && requestURI.equals("/itensCardapio.json")) {
                    logger.fine("Chamou arquivo json");
                    Path path = Path.of("itensCardapio.json");
                    String json = Files.readString(path);

                    clientOut.println("HTTP/1.1 200 OK");
                    clientOut.println("Content-Type: application/json; charset=utf-8");
                    clientOut.println();
                    clientOut.println(json);
                } else if (method.equals("GET") && requestURI.equals("/itens-cardapio")) {
                    logger.fine("Chamou listagem de itens de cardápio");
                    List<ItemCardapio> listaItensCardapio = database.listaDeItensCardapio();

                    Gson gson = new Gson();
                    String json = gson.toJson(listaItensCardapio);

                    clientOut.println("HTTP/1.1 200 OK");
                    clientOut.println("Content-Type: application/json; charset=utf-8");
                    clientOut.println();
                    clientOut.println(json);
                } else if (method.equals("GET") && requestURI.equals("/itens-cardapio/total")) {
                    logger.fine("Chamou listagem de itens de cardápio total");
                    List<ItemCardapio> listaItensCardapio = database.listaDeItensCardapio();

                    clientOut.println("HTTP/1.1 200 OK");
                    clientOut.println("Content-Type: application/json; charset=utf-8");
                    clientOut.println();
                    clientOut.println(listaItensCardapio.size());
                } else if (method.equals("POST") && requestURI.equals("/itens-cardapio")) {
                    logger.fine("Chamou adição de item cardápio");

                    if (requestChuncks.length != 1) {
                        clientOut.println("HTTP/1.1 400 Bad Request");
                        return;
                    }

                    String body = requestChuncks[1];

                    Gson gson = new Gson();
                    ItemCardapio novoItemCardapio = new Gson().fromJson(body, ItemCardapio.class);

                    System.out.println(novoItemCardapio);

                    database.adicionaItem(novoItemCardapio);

                    clientOut.println("HTTP/1.1 201 Created");

                } else {
                    logger.warning(() -> "URI não encontrada: " + requestURI);
                    clientOut.println("HTTP/1.1 404 Not Found");
                }
            } catch (Exception ex) {
                logger.log(Level.SEVERE, ex, () -> "Erro ao tratar " + method + " " + requestURI);
                clientOut.println("HTTP/1.1 500 Internal Server Error");
                clientOut.println();
                clientOut.println(ex.getMessage());
            }
        } catch (Exception e) {
            //logger.severe(e.getMessage());
            logger.log(Level.SEVERE, "Erro no servidor", e);
            throw new RuntimeException(e);
        }
    }
}
