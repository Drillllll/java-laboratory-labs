package PT3;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Socket socket = new Socket("localhost", 1236);
        System.out.println("Connected to server: " + socket.getInetAddress());

        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

        // receive first message from server
        Message message = (Message) in.readObject();
        System.out.println("message from server: " + message.getContent());

        // read number from user
        Scanner scanner = new Scanner(System.in);
        int number = scanner.nextInt();
        message.setNumber(number);

        // send message (number) to server
        out.writeObject(message);

        // receive message from server (ready for messages)
        message = (Message) in.readObject();
        System.out.println("message from server: " + message.getContent());

        for(int i=0; i<number; i++) {
            message = new Message(i, "my " + i + " message");
            out.writeObject(message);
        }

        socket.close();
        System.out.println("Disconnected from server.");
    }
}