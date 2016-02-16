package echoserver;

import Presentation.ChatGUI;
import echoclient.ClientThread;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import shared.ProtocolStrings;

public class EchoServer extends Thread{

    private static boolean keepRunning = true;
    private static ServerSocket serverSocket;
    private String ip;
    private int port;

    public static void stopServer() {
        keepRunning = false;
    }

    private void runServer(String ip, int port) {
        this.port = port;
        this.ip = ip;

        System.out.println("Sever started. Listening on: " + port + ", bound to: " + ip);
        Logger.getLogger(Log.LOG_NAME).log(Level.INFO, "Server started. Listening on: " + port + ", bound to " + ip);
        
        try {
            serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress(ip, port));
            do {
                Socket socket = serverSocket.accept(); //Important Blocking call - venter på en client tilkobler
                System.out.println("Connected to a client");
                Logger.getLogger(Log.LOG_NAME).log(Level.INFO, "Connected to a client");
                //handleClient(socket);
                new ClientThread(socket).start();
            } while (keepRunning);
        } catch (IOException ex) {
            Logger.getLogger(Log.LOG_NAME).log(Level.SEVERE, null, ex);
        }
    }
    @Override
     public void run(){
         try {
            Log.setLogFile("logFile.txt", "ServerLog");
            //String ip = "localhost"; // ændret her -----------------!!!!!!!!!!!!!!!!!!!!!
            //int port = 9999;
            new EchoServer().runServer(ChatGUI.allIp, ChatGUI.allPort);
        } finally {
            Log.closeLogger();
        }
     }

//    public static void main(String[] args) {
//        try {
//            Log.setLogFile("logFile.txt", "ServerLog");
//            //String ip = args[0];
//            //int port = Integer.parseInt(args[1]);
//            String ip = "localhost"; // ændret her -----------------!!!!!!!!!!!!!!!!!!!!!
//            int port = 9999;
//            new EchoServer().runServer(ip, port);
//        } finally {
//            Log.closeLogger();
//        }
//    }
}
