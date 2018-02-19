package main.java;

import main.java.socialServer.WallEndpoint;
import org.glassfish.tyrus.server.Server;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class App {
    private static final String HOSTNAME = "localhost";
    private static final int PORT = 3000;
    public static void main(String[] args) {
        runServer();
    }
    public static void runServer() {
        Server server = new Server(HOSTNAME, PORT, "", null, WallEndpoint.class);

        try {
            server.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Please press a key to stop the server.");
            reader.readLine();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            server.stop();
        }
    }
}
