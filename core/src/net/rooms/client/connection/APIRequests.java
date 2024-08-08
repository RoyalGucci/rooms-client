package net.rooms.client.connection;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.rooms.client.connection.requests.CreateRequest;
import net.rooms.client.connection.requests.RegistrationRequast;
import net.rooms.client.connection.requests.Room;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class APIRequests {

    private String jSessionID = "";

    public boolean login(String username, String password) {
        HttpRequest request;
        try {
            request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:8080/login"))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString("username=" + username + "&password=" + password))
                    .build();
        } catch (URISyntaxException e) {
            return false;
        }

        HttpResponse<String> response;
        try (HttpClient client = HttpClient.newHttpClient()) {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            return false;
        }
        String setCookieHeader = response.headers().firstValue("Set-Cookie").orElse("");
        jSessionID = setCookieHeader.split(";")[0]; // Extracts JSESSIONID
        return jSessionID != null && !jSessionID.isEmpty();

    }

    public boolean signup(String nickname, String username, String password) {
        ObjectMapper objectMapper = new ObjectMapper();
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            String jsonPayload = objectMapper.writeValueAsString(new RegistrationRequast(nickname, username, password, 0));
            HttpPost httpPost = new HttpPost("http://localhost:8080/api/v1/registration");
            StringEntity stringEntity = new StringEntity(jsonPayload, ContentType.APPLICATION_JSON);
            httpPost.setEntity(stringEntity);

            CloseableHttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            String responseBody = EntityUtils.toString(entity);
            System.out.println(responseBody);
            return responseBody.equals("success");
        } catch (IOException | ParseException e) {
            return false;
        }
    }

    public Room createRoom(String title, String description, boolean isPrivate, String password) {
        HttpRequest jsonRequest;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            CreateRequest myData = new CreateRequest(title, isPrivate, password, description);
            String jsonPayload = objectMapper.writeValueAsString(myData);
            jsonRequest = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:8080/api/v1/room/create"))
                    .header("Content-Type", "application/json")
                    .header("Cookie", jSessionID)
                    .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                    .build();
        } catch (URISyntaxException | JsonProcessingException e) {
            return null;
        }

        HttpResponse<String> jsonResponse;
        try (HttpClient client = HttpClient.newHttpClient()) {
            jsonResponse = client.send(jsonRequest, HttpResponse.BodyHandlers.ofString());
            ObjectMapper objectMapper = new ObjectMapper();
            Room room;
            try {
                room = objectMapper.readValue(jsonResponse.body(), Room.class);
            } catch (JsonProcessingException e) {
                return null;
            }
            return room;
        } catch (IOException | InterruptedException e) {
            return null;
        }
    }

    public List<Room> getRooms() {
        HttpRequest jsonRequest;
        try {
            jsonRequest = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:8080/api/v1/room/list"))
                    .header("Content-Type", "application/json")
                    .header("Cookie", jSessionID)
                    .build();
        } catch (URISyntaxException e) {
            return new ArrayList<>();
        }

        HttpResponse<String> jsonResponse;
        try (HttpClient client = HttpClient.newHttpClient()) {
            jsonResponse = client.send(jsonRequest, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            return new ArrayList<>();
        }

        if (jsonResponse.body() == null || jsonResponse.body().isEmpty()) return new ArrayList<>();

        ObjectMapper objectMapper = new ObjectMapper();
        Room[] roomsArray;
        try {
            roomsArray = objectMapper.readValue(jsonResponse.body(), Room[].class);
        } catch (JsonProcessingException e) {
            return new ArrayList<>();
        }

        return Arrays.asList(roomsArray);

    }


}
