package br.florinda;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class ClienteItensCardapioComSocket {

    public static void main(String[] args) throws Exception {

        try (Socket socket = new Socket("localhost", 8000)) {
            OutputStream clientOS = socket.getOutputStream();
            PrintStream clientOut = new PrintStream(clientOS);
            clientOut.println("GET /itensCardapio.json HTTP/1.1");
            clientOut.println();

            InputStream clientIS = socket.getInputStream();
            Scanner scanner = new Scanner(clientIS);
            while (scanner.hasNextLine()) {
                System.out.println(scanner.nextLine());
            }
        }

    }
}
