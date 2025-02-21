package org.example;

import java.net.*;
import java.io.*;

public class HttpServer {
    public static void main(String[] args) throws IOException, URISyntaxException {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(8080);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 8080.");
            System.exit(1);
        }
        while(true){
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
            if(isFirstLine){
                isFirstLine = false;
                firstLine = inputLine;
            }
            System.out.println("Recibí: " + inputLine);
            if (!in.ready()) {break; }
        }
        URI uri = new URI(firstLine.split(" ")[1]);
        if(uri.getPath().startsWith("/calculadora")){
            String res = getindex();
            out.println("HTTP/1.1 200 OK");
            out.println("Content-Type: text/html");
            out.println("Content-Length: " +  res.length());
            out.println();
            out.println(res);
            out.close();
            in.close();
        } else if (uri.getPath().startsWith("/calcu")) {
            String res = HttpConnectionExample.getResponse("/compreflex" + uri.getQuery());
            out.println("HTTP/1.1 200 OK");
            out.println("Content-Type: application/json");
            out.println();
            out.println(res);
            out.close();
            in.close();
        } else {
            String res = getNoFound();
            out.println("HTTP/1.1 404 OK");
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
                "<html>\n" +
                "    <head>\n" +
                "        <title>Form Example</title>\n" +
                "        <meta charset=\"UTF-8\">\n" +
                "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    </head>\n" +
                "    <body>\n" +
                "        <h1>Form with GET</h1>\n" +
                "        <form action=\"/hello\">\n" +
                "            <label for=\"name\">Name:</label><br>\n" +
                "            <input type=\"text\" id=\"name\" name=\"name\" value=\"John\"><br><br>\n" +
                "            <input type=\"button\" value=\"Submit\" onclick=\"loadGetMsg()\">\n" +
                "        </form> \n" +
                "        <div id=\"getrespmsg\"></div>\n" +
                "\n" +
                "        <script>\n" +
                "            function loadGetMsg() {\n" +
                "                let nameVar = document.getElementById(\"name\").value;\n" +
                "                const xhttp = new XMLHttpRequest();\n" +
                "                xhttp.onload = function() {\n" +
                "                    document.getElementById(\"getrespmsg\").innerHTML =\n" +
                "                    this.responseText;\n" +
                "                }\n" +
                "                xhttp.open(\"GET\", \"/calcu?name=\"+nameVar);\n" +
                "                xhttp.send();\n" +
                "            }\n" +
                "        </script>\n" +
                "    </body>\n" +
                "</html>";
        return response;
    }

    public static String getNoFound(){
        String response = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "    <head>\n" +
                "        <title>Form Example</title>\n" +
                "        <meta charset=\"UTF-8\">\n" +
                "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    </head>\n" +
                "    <body>\n" +
                "        <h1>El endpoint consultado no existe :)</h1>\n" +
                "    </body>\n" +
                "</html>";
        return response;
    }
}