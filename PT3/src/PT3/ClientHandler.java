package PT3;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler extends Thread {
    private final Socket clientSocket;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());

            // send first message to client
            Message message = new Message(0, "Ready");
            out.writeObject(message);

            // wait for response from client (number from client)
            message = (Message) in.readObject();
            int number = message.getNumber();
            System.out.println("Client sent: " + number);

            // send 2nd message to the client
            message.setContent("Ready for messages");
            out.writeObject(message);

            //recerive number messages from client
            for(int i=0; i<number; i++) {
                message = (Message) in.readObject();
                System.out.println("Client sent: " + message.getContent());
            }

            clientSocket.close();
            System.out.println("Client disconnected: " + clientSocket.getInetAddress());
        }
        catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}