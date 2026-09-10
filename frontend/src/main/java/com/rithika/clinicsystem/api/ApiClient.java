package com.rithika.clinicsystem.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;

import com.rithika.clinicsystem.dto.ApiErrorResponse;

public class ApiClient {

    private static final String BASE_URL = "http://localhost:8080/api";

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public ApiClient() {

        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();

        this.objectMapper = new ObjectMapper();

        this.objectMapper.registerModule(
                new JavaTimeModule()
        );
    }

    public <T> T get(
            String path,
            Class<T> responseType
    ) {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(buildUri(path))
                .GET()
                .build();

        HttpResponse<String> response =
                sendRequest(request);

        validateResponse(response);

        return readResponse(
                response.body(),
                responseType
        );
    }

    public <T> List<T> getList(
            String path,
            Class<T> elementType
    ) {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(buildUri(path))
                .GET()
                .build();

        HttpResponse<String> response =
                sendRequest(request);

        validateResponse(response);

        try {

            JavaType listType =
                    objectMapper
                            .getTypeFactory()
                            .constructCollectionType(
                                    List.class,
                                    elementType
                            );

            return objectMapper.readValue(
                    response.body(),
                    listType
            );

        } catch (JsonProcessingException exception) {

            throw new ApiException(
                    "Unable to read server response",
                    exception
            );
        }
    }

    public <T> T post(
            String path,
            Object requestBody,
            Class<T> responseType
    ) {

        HttpRequest request =
                createJsonRequest(
                        path,
                        requestBody,
                        "POST"
                );

        HttpResponse<String> response =
                sendRequest(request);

        validateResponse(response);

        return readResponse(
                response.body(),
                responseType
        );
    }

    public <T> T put(
            String path,
            Object requestBody,
            Class<T> responseType
    ) {

        HttpRequest request =
                createJsonRequest(
                        path,
                        requestBody,
                        "PUT"
                );

        HttpResponse<String> response =
                sendRequest(request);

        validateResponse(response);

        return readResponse(
                response.body(),
                responseType
        );
    }

    public <T> T patch(
            String path,
            Class<T> responseType
    ) {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(buildUri(path))
                .method(
                        "PATCH",
                        HttpRequest.BodyPublishers.noBody()
                )
                .build();

        HttpResponse<String> response =
                sendRequest(request);

        validateResponse(response);

        return readResponse(
                response.body(),
                responseType
        );
    }

    public void delete(String path) {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(buildUri(path))
                .DELETE()
                .build();

        HttpResponse<String> response =
                sendRequest(request);

        validateResponse(response);
    }

    private HttpRequest createJsonRequest(
            String path,
            Object requestBody,
            String method
    ) {

        String json;

        try {

            json = objectMapper
                    .writeValueAsString(requestBody);

        } catch (JsonProcessingException exception) {

            throw new ApiException(
                    "Unable to convert request to JSON",
                    exception
            );
        }

        return HttpRequest.newBuilder()
                .uri(buildUri(path))
                .header(
                        "Content-Type",
                        "application/json"
                )
                .method(
                        method,
                        HttpRequest.BodyPublishers.ofString(json)
                )
                .build();
    }

    private HttpResponse<String> sendRequest(
            HttpRequest request
    ) {

        try {

            return httpClient.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );

        } catch (IOException exception) {

            throw new ApiException(
                    "Unable to connect to the backend server",
                    exception
            );

        } catch (InterruptedException exception) {

            Thread.currentThread().interrupt();

            throw new ApiException(
                    "Request was interrupted",
                    exception
            );
        }
    }

    private void validateResponse(
            HttpResponse<String> response
    ) {

        int statusCode =
                response.statusCode();

        if (statusCode >= 200 &&
                statusCode < 300) {

            return;
        }

        try {

            ApiErrorResponse errorResponse =
                    objectMapper.readValue(
                            response.body(),
                            ApiErrorResponse.class
                    );

            String message =
                    buildErrorMessage(
                            errorResponse,
                            statusCode
                    );

            throw new ApiException(
                    message,
                    statusCode,
                    errorResponse.getFieldErrors()
            );

        } catch (JsonProcessingException exception) {

            throw new ApiException(
                    getDefaultErrorMessage(statusCode),
                    statusCode
            );
        }
    }
    private String buildErrorMessage(
            ApiErrorResponse errorResponse,
            int statusCode
    ) {

        if (errorResponse.getFieldErrors() != null &&
                !errorResponse.getFieldErrors().isEmpty()) {

            return String.join(
                    "\n",
                    errorResponse
                            .getFieldErrors()
                            .values()
            );
        }

        if (errorResponse.getMessage() != null &&
                !errorResponse.getMessage().isBlank()) {

            return errorResponse.getMessage();
        }

        return getDefaultErrorMessage(statusCode);
    }

    private String getDefaultErrorMessage(
            int statusCode
    ) {

        return switch (statusCode) {

            case 400 ->
                    "The request could not be processed.";

            case 404 ->
                    "The requested resource could not be found.";

            case 409 ->
                    "The operation conflicts with the current state of the resource.";

            case 500 ->
                    "The server encountered an unexpected error.";

            default ->
                    "The request failed. Please try again.";
        };
    }

    private <T> T readResponse(
            String json,
            Class<T> responseType
    ) {

        try {

            return objectMapper.readValue(
                    json,
                    responseType
            );

        } catch (JsonProcessingException exception) {

            throw new ApiException(
                    "Unable to read server response",
                    exception
            );
        }
    }

    private URI buildUri(String path) {

        if (!path.startsWith("/")) {
            path = "/" + path;
        }

        return URI.create(
                BASE_URL + path
        );
    }
}