
package com.sms.student_management;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

public class server {

    private static final Gson gson = new Gson();

    public static void main(String[] args) {

        try {

            HttpServer server = HttpServer.create(
                new InetSocketAddress(8080), 0
            );

            server.createContext("/studentlogin", exchange -> {

                try {
                    if (!exchange.getRequestMethod().equalsIgnoreCase("POST")) {
                        sendJson(exchange, 405,
                                new logininfoResult(false, "Only POST method is allowed"));
                        return;
                    }

                    byte[] body = exchange.getRequestBody().readNBytes(10241);

                    if (body.length > 10240) {
                        sendJson(exchange, 413,
                                new logininfoResult(false, "Request is too large"));
                        return;
                    }

                    String json = new String(body, StandardCharsets.UTF_8);

                    newlogin request = gson.fromJson(json, newlogin.class);

                    logininfo result = loginpage.login(request);

                    sendJson(exchange, 200, result);

                } catch (JsonSyntaxException e) {

                    sendJson(exchange, 400,
                            new logininfoResult(false, "Invalid JSON format"));

                } catch (Exception e) {

                    e.printStackTrace();

                    sendJson(exchange, 500,
                            new logininfoResult(false, "Server error"));

                }
            });
            
            
            
           
      server.createContext("/firstscreen", exchange -> {

                if (!exchange.getRequestMethod().equals("GET")) {

                    sendJson(exchange, 405,
                        new logininfoResult(false,
                            "Only GET method is allowed"));

                    return;
                }

                try (InputStream input = getClassResource()) {

                    if (input == null) {

                        sendJson(exchange, 404,
                            new logininfoResult(false,
                                "Login HTML page not found"));

                        return;
                    }

                    byte[] html = input.readAllBytes();

            exchange.getResponseHeaders().set(
                        "Content-Type",
                        "text/html; charset=UTF-8"
                    );

                    exchange.getResponseHeaders().set(
                        "Cache-Control", "no-store"
                    );

                    exchange.sendResponseHeaders(
                        200, html.length
                    );

                    try (OutputStream output =
                            exchange.getResponseBody()) {

                        output.write(html);
                    }

                } catch (Exception e) {

                    e.printStackTrace();

            try {
                        sendJson(exchange, 500,
                            new logininfoResult(false,
                                "Unable to load login page"));
                    } catch (IOException ignored) {
                    }
                }
            });

            
            
            
            server.createContext("/dashboard", exchange -> {

                if (!exchange.getRequestMethod().equalsIgnoreCase("GET")) {
                    exchange.sendResponseHeaders(405, -1);
                    exchange.close();
                    return;
                }

                try (InputStream input = server.class.getClassLoader()
                        .getResourceAsStream("pages/mainpage.html")) {

                    if (input == null) {
                        exchange.sendResponseHeaders(404, -1);
                        exchange.close();
                        return;
                    }

                    byte[] html = input.readAllBytes();

                    exchange.getResponseHeaders().set(
                        "Content-Type", "text/html; charset=UTF-8"
                    );

                    exchange.sendResponseHeaders(200, html.length);

                    try (OutputStream output = exchange.getResponseBody()) {
                        output.write(html);
                    }
                }
            });
            
  
            
            
            
            
            server.createContext("/getstudents", exchange -> {

                if (!exchange.getRequestMethod().equalsIgnoreCase("GET")) {
                    sendJson(exchange, 405,
                            new logininfoResult(false, "Only GET method is allowed"));
                    return;
                }

                try {

                    List<dbgetstudentdet> students = getstudent.getAllStudents();

                    sendJson(exchange, 200, students);

                } catch (Exception e) {

                    e.printStackTrace();

                    sendJson(exchange, 500,
                            new logininfoResult(false, "Unable to retrieve students"));
                }
            });
            
            
            
      
            
    

server.createContext("/getsections", exchange -> {

    if (!exchange.getRequestMethod().equalsIgnoreCase("GET")) {
        sendJson(exchange, 405,
                new logininfoResult(false, "Only GET method is allowed"));
        return;
    }

    try {
        sendJson(exchange, 200, getsec.getAllSections());
    } catch (Exception e) {
        e.printStackTrace();
        sendJson(exchange, 500,
                new logininfoResult(false, "Unable to retrieve sections"));
    }
});


server.createContext("/addstudent", exchange -> {

    if (!exchange.getRequestMethod().equalsIgnoreCase("POST")) {
        sendJson(exchange, 405,
                new logininfoResult(false, "Only POST method is allowed"));
        return;
    }

    try {
        String body = new String(
                exchange.getRequestBody().readAllBytes(),
                StandardCharsets.UTF_8);

        dbgetstudentdet student =
                gson.fromJson(body, dbgetstudentdet.class);

        if (student == null ||
            student.student_code == null ||
         student.name == null ||
            student.email == null ||
            student.dob == null ||
            student.admission_date == null ||
         student.class_section_id <= 0) {

            sendJson(exchange, 400,
                    new logininfoResult(false, "Please fill all required fields"));
            return;
        }

        boolean saved = addnewstudent.addStudent(student);
       if (saved) {
            sendJson(exchange, 200,
                    new logininfoResult(true, "Student added successfully"));
        } else {
            sendJson(exchange, 400,
                    new logininfoResult(false, "Unable to add student. Check duplicate values and class section."));
        }

    } catch (Exception e) {
        e.printStackTrace();
        sendJson(exchange, 400,
                new logininfoResult(false, "Invalid student details"));
    }
});











server.createContext("/update", exchange -> {

    if (!exchange.getRequestMethod().equalsIgnoreCase("POST")) {
        sendJson(exchange, 405,
                new logininfoResult(false, "Only POST method is allowed"));
        return;
    }

    try {

        String body = new String(
                exchange.getRequestBody().readAllBytes(),
                StandardCharsets.UTF_8);

        dbgetstudentdet student =
                gson.fromJson(body, dbgetstudentdet.class);

        if (student == null ||
            student.id <= 0 ||
            student.student_code == null ||
            student.name == null ||
            student.email == null ||
            student.dob == null ||
            student.admission_date == null ||
            student.class_section_id <= 0 ||
            student.status == null ||
            !(student.status.equals("ACTIVE") ||
              student.status.equals("INACTIVE"))) {

            sendJson(exchange, 400,
                    new logininfoResult(false, "Invalid student details"));
            return;
        }

        boolean updated = update.updateStudent(student);

        if (updated) {
            sendJson(exchange, 200,
                    new logininfoResult(true, "Student updated successfully"));
        } else {
            sendJson(exchange, 400,
                    new logininfoResult(false, "Unable to update student"));
        }

    } catch (Exception e) {
        e.printStackTrace();
        sendJson(exchange, 400,
                new logininfoResult(false, "Invalid student details"));
    }
});




server.createContext("/delete", exchange -> {

    if (!exchange.getRequestMethod().equalsIgnoreCase("POST")) {
        sendJson(exchange, 405,
                new logininfoResult(false, "Method not allowed"));
        return;
    }

    try {
        String body = new String(
                exchange.getRequestBody().readAllBytes(),
                StandardCharsets.UTF_8
        );

        dbgetstudentdet student =
                gson.fromJson(body, dbgetstudentdet.class);

        if (student == null || student.id <= 0) {
            sendJson(exchange, 400,
                    new logininfoResult(false, "Invalid student ID"));
            return;
        }

        
        boolean deleted = delete.deletestu(student.id);

        if (deleted) {
            sendJson(exchange, 200,
                    new logininfoResult(true, "Student deleted successfully"));
        } else {
            sendJson(exchange, 404,
                    new logininfoResult(false, "Student not found"));
        }

    } catch (Exception e) {
        e.printStackTrace();

        sendJson(exchange, 500,
                new logininfoResult(false, "Delete failed"));
    }
});




server.createContext("/register", exchange -> {

    if (!exchange.getRequestMethod().equalsIgnoreCase("POST")) {
        sendJson(exchange, 405,
                new logininfoResult(false, "Method not allowed"));
        return;
    }

    try {
        String body = new String(
                exchange.getRequestBody().readAllBytes(),
                StandardCharsets.UTF_8
        );

        newlogin request = gson.fromJson(body, newlogin.class);

        if (request == null ||
            request.email == null ||
            request.password == null ||
            request.email.trim().isEmpty() ||
            request.password.trim().isEmpty()) {

            sendJson(exchange, 400,
                    new logininfoResult(false, "Email and password are required"));
            return;
        }

        if (!request.email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            sendJson(exchange, 400,
                    new logininfoResult(false, "Enter a valid email address"));
            return;
        }

        if (request.password.length() < 8 ||
            request.password.length() > 72) {
            sendJson(exchange, 400,
                    new logininfoResult(false, "Password must be 8 to 72 characters"));
            return;
        }

        boolean created = registeruser.createUser(
                request.email.trim(),
                request.password
        );

        if (created) {
            sendJson(exchange, 200,
                    new logininfoResult(true, "Account created successfully"));
        } else {
            sendJson(exchange, 409,
                    new logininfoResult(false, "Email already exists or registration failed"));
        }

    } catch (Exception e) {
        e.printStackTrace();
        sendJson(exchange, 500,
                new logininfoResult(false, "Registration failed"));
    }
});



            server.start();

            System.out.println("SMS Server Started");
            System.out.println("Port: 8080");
            System.out.println(
                "Login Page: http://localhost:8080/firstscreen"
            );
            
        } catch (Exception e) {

            System.out.println("Server failed to start!");
            e.printStackTrace();
        }
    }


    
    private static InputStream getClassResource() {

        return server.class.getClassLoader()
            .getResourceAsStream("pages/firstscreen.html");
    }


    private static void sendJson(
        HttpExchange exchange,
        int status,
        Object response
    ) throws IOException {

        String json = gson.toJson(response);

        byte[] data = json.getBytes(
            StandardCharsets.UTF_8
        );

        exchange.getResponseHeaders().set(
            "Content-Type",
            "application/json; charset=UTF-8"
        );

        exchange.getResponseHeaders().set(
            "Cache-Control", "no-store"
        );

        exchange.sendResponseHeaders(
            status, data.length
        );

        try (OutputStream os = exchange.getResponseBody()) {

            os.write(data);
        }
    }

    static class logininfoResult {

        boolean success;
        String message;

        logininfoResult(boolean success, String message) {

            this.success = success;
            this.message = message;
        }
    }
}
