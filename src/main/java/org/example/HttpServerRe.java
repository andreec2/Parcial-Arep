package org.example;

import java.net.*;
import java.io.*;
import java.util.Arrays;

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
            System.out.println(uri.getQuery() + "esta es la query");
            if (uri.getPath().startsWith("/pcompreflex")) {
                System.out.println(uri.getQuery() + "esta es la query");
                int[] res = getNumbers(uri.getQuery());
                int[] response = bbl(res);
                out.println("HTTP/1.1 200 OK");
                out.println("Content-Type: application/json");
                out.println();
                out.println(Arrays.toString(response));
                out.close();
                in.close();
            } else if (uri.getPath().startsWith("/compreflex")){
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
    public static int[] getNumbers(String query){
        String p1 = query.split("\\(")[1].split("\\)")[0];
        String[] values = p1.split(",");
        int[] res = new int[values.length];
        for(int i = 0; i <= values.length - 1; i++){
            res[i] = Integer.parseInt(values[i]);
        }
        return res;
    }
    public static int[] bbl(int[] p){
        int aux = 0;
        for(int i = 0; i < p.length - 1; i++){
            if(p[i] > p[i+1]){
               aux = p[i];
               p[i] = p[i+1];
               p[i+1] = aux;
            } else { return p;}
        }
        return p;
    }


}