package br.florinda;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServidorItensCardapioCOmSocket {

    public static void main(String[] args) throws Exception{

        try(ServerSocket serverSocket = new ServerSocket(8000)){
            System.out.println("Subiu servidor");

            try(Socket clientSocket = serverSocket.accept()){
                InputStream clientIS = clientSocket.getInputStream();

                StringBuffer requestBuilder = new StringBuffer();
                int data;
                do {
                    data = clientIS.read();
                    requestBuilder.append((char)data);
                } while(clientIS.available() > 0);

                String request = requestBuilder.toString();
                System.out.println(request);

                OutputStream clientOS = clientSocket.getOutputStream();
                PrintStream clientOut = new PrintStream(clientOS);
                clientOut.println("HTTP/1.1 200 OK");
                clientOut.println("Content-Type: application/json; charset=utf-8");
                clientOut.println();
              //  clientOut.println(json);
        }
    }
}
}
