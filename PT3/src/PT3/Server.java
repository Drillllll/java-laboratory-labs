package PT3;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(1236);
        System.out.println("Server started. Waiting for clients");

        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("New client connected, address: " + clientSocket.getInetAddress());

            ClientHandler clientHandler = new ClientHandler(clientSocket);
            clientHandler.start();
        }
    }


}