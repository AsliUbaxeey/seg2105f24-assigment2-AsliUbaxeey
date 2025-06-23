package edu.server.backend;

import java.io.*;
import java.util.Scanner;
import client.common.ChatIF;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import edu.server.backend.EchoServer;


public class ServerConsole implements ChatIF {
    final public static int DEFAULT_PORT = 5555;
    private EchoServer server;

    public ServerConsole(int port) {
       server = new EchoServer(port);
       
    }

    public void accept() {
        try {
            server.listen();  
        } 
        catch (Exception ex) {
            System.out.println("ERROR: Could not listen for clients");
        }

        // Start listening for user input given
        try (BufferedReader fromConsole = new BufferedReader(new InputStreamReader(System.in))) {
            String message;
            while (true) {
                message = fromConsole.readLine();// from bufferedreader library
                if (message.startsWith("#")) {
                    handleCommand(message);
                } 
                else {
                    String fullMessage = "SERVER MSG> " + message;
                    display(fullMessage);
                    server.sendToAllClients(fullMessage); 
                }
            }
        } 
        catch (Exception ex) {
            System.out.println(" ERROR while reading from console");
        }
    }

    public void display(String message) {
        System.out.println(message);
    }

    private void handleCommand(String command) {
        String[] parts = command.split(" ");
        String cmd = parts[0];

        switch (cmd) {
            case "#quit":
                try {
                    server.close();
                } 
                catch (IOException e) {
                    System.out.println("ERROR during server quit");
                }
                System.exit(0);
                break;

            case "#stop":
                server.stopListening();
                break;

            case "#close":
                try {
                    server.close();
                    display("Server closed all connections");
                } 
                catch (IOException e) {
                    System.out.println("Error while closing server");
                }
                break;

            case "#setport":
                if (parts.length == 2) {
                    if (!server.isListening()) {
                        try {
                            int newPort = Integer.parseInt(parts[1]);
                            server.setPort(newPort);
                            display("Port set to " + newPort);
                        } 
                        catch (NumberFormatException e) {
                            display("Invalid port");
                        }
                    } 
                    else {
                        display("Cannot change port while server is listening");
                    }
                }               
                break;

            case "#start":
                if (!server.isListening()) {
                    try {
                        server.listen();
                        display("Server listening...");
                    } 
                    catch (IOException e) {
                        display("ERROR: Could not start server");
                    }
                } 
                else {
                    display("Server is already listening.");
                }
                break;

            case "#getport":
                display("Current port: " + server.getPort());
                break;

        }
    }

    // Main method 
    public static void main(String[] args) {
        int port = DEFAULT_PORT;

        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } 
            catch (NumberFormatException e) {
                System.out.println("Invalid. Using default port " + DEFAULT_PORT);
            }
        }

        ServerConsole serverC = new ServerConsole(port);
        serverC.accept(); 
    }
}