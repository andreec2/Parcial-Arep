package org.example;

import java.net.*;
import java.io.*;

public class HttpServerRe {
    public static void main(String[] args) throws IOException, URISyntaxException {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(3000);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 3000.");
            System.exit(1);
        }
        while (true) {
            Socket clientSocket = null;
            try {
                System.out.println("Listo para recibir ...");
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String inputLine, outputLine, firstLine = "";
            boolean isFirstLine = true;
            while ((inputLine = in.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    firstLine = inputLine;
                }
                System.out.println("Recibí: " + inputLine);
                if (!in.ready()) {
                    break;
                }
            }
            URI uri = new URI(firstLine.split(" ")[1]);
            if (uri.getPath().startsWith("/compreflex")) {
                String res = getindex();
                out.println("HTTP/1.1 200 OK");
                out.println("Content-Type: text/html");
                out.println("Content-Length: " +  res.length());
                out.println();
                out.println(res);
                out.close();
                in.close();
            }
        }
    }
    public static String getindex(){
        String response = "<!DOCTYPE html>\n" +
                "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<meta charset=\"UTF-8\">" +
                "<title>Title of the document</title>\n" +
                "</head>" +
                "<body>" +
                "<h1>Mi propio mensaje</h1>" +
                "</body>" +
                "</html>";
        return response;
    }
}