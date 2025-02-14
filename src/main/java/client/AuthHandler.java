package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Scanner;

public class AuthHandler {
    private DataInputStream in;
    private DataOutputStream out;
    private Scanner scanner;

    public AuthHandler(DataInputStream in, DataOutputStream out, Scanner scanner) {
        this.in = in;
        this.out = out;
        this.scanner = scanner;
    }

    public boolean authenticate() throws IOException {
        System.out.print(in.readUTF() + " ");
        String username = scanner.nextLine();
        out.writeUTF(username);
        out.flush();

        System.out.print(in.readUTF() + " ");
        String password = scanner.nextLine();
        out.writeUTF(password);
        out.flush();

        String response = in.readUTF();
        System.out.println(response);

        return !response.startsWith("ERROR");
    }
}