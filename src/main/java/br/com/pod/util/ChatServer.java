package br.com.pod.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author filipe
 */
public class ChatServer {

    ArrayList<PrintWriter> writers = new ArrayList<PrintWriter>();

    public ChatServer() {
        ServerSocket server;
        try {
            server = new ServerSocket(9999);
            while (true) {
                Socket socket = server.accept();
                new Thread(new EscutaCliente(socket)).start();
                PrintWriter p = new PrintWriter(socket.getOutputStream());
                writers.add(p);
            }
        } catch (IOException ex) {
            Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void encaminhaMsg(String msg) {
        for (PrintWriter printWriter : writers) {
            printWriter.println(msg);
            printWriter.flush();
        }

    }

    private class EscutaCliente implements Runnable {

        Scanner write;

        public EscutaCliente(Socket socket) {
            try {
                write = new Scanner(socket.getInputStream());
            } catch (IOException ex) {
                Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        public void run() {
            String msg;
            while ((msg = write.nextLine()) != null) {
                System.out.println(msg);
                encaminhaMsg(msg);
            }
        }
    }

    public static void main(String[] args) {
        new ChatServer();
    }
}
