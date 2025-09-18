package com.example.teste.app.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Enumeration;

@Component
public class HttpLoggingInterceptor implements HandlerInterceptor {

    private final ElasticsearchOperations elasticsearchOperations;
    private final ObjectMapper objectMapper;

    public HttpLoggingInterceptor(ElasticsearchOperations elasticsearchOperations) {
        this.elasticsearchOperations = elasticsearchOperations;
        this.objectMapper = new ObjectMapper();
    }

    // Store request ID in thread local to link request and response
    private final ThreadLocal<String> requestIdThreadLocal = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Wrap the request and response for caching content
        ContentCachingRequestWrapper cachingRequest;
        if (request instanceof ContentCachingRequestWrapper) {
            cachingRequest = (ContentCachingRequestWrapper) request;
        } else {
            cachingRequest = new ContentCachingRequestWrapper(request);
        }

        if (!(response instanceof ContentCachingResponseWrapper)) {
            response = new ContentCachingResponseWrapper(response);
        }

        Map<String, Object> logMap = new HashMap<>();
        logMap.put("path", request.getRequestURI());
        logMap.put("method", request.getMethod());

        // Body da request
        String body = new String(cachingRequest.getContentAsByteArray(), request.getCharacterEncoding());
        logMap.put("body", body);

        // Headers
        Map<String, String> headers = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames != null && headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            headers.put(headerName, request.getHeader(headerName));
        }

        logMap.put("headers", headers);

        // Campos extras
        logMap.put("timestamp", System.currentTimeMillis());
        logMap.put("clientIp", request.getRemoteAddr());

        // Indexa no Elasticsearch
        String json = objectMapper.writeValueAsString(logMap);
        IndexQuery indexQuery = new IndexQueryBuilder()
                .withSource(json)
                .build();
        elasticsearchOperations.index(indexQuery, IndexCoordinates.of("httplogs"));

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        if (response instanceof ContentCachingResponseWrapper) {
            ContentCachingResponseWrapper responseWrapper = (ContentCachingResponseWrapper) response;

            // Create a map for logging the response
            Map<String, Object> logMap = new HashMap<>();
            logMap.put("path", request.getRequestURI());
            logMap.put("method", request.getMethod());
            logMap.put("status", response.getStatus());

            // Headers
            Map<String, String> headers = new HashMap<>();
            Enumeration<String> headerNames = request.getHeaderNames();
            while (headerNames != null && headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();
                headers.put(headerName, request.getHeader(headerName));
            }
            logMap.put("headers", headers);

            // Get response body
            byte[] responseBody = responseWrapper.getContentAsByteArray();
            if (responseBody.length > 0) {
                String responseContent = new String(responseBody, response.getCharacterEncoding());
                logMap.put("body", responseContent);
            } else {
                logMap.put("body", "");
            }

            // Add timestamp
            logMap.put("timestamp", System.currentTimeMillis());
            logMap.put("clientIp", request.getRemoteAddr());

            // Index in Elasticsearch - use the same index as preHandle
            String json = objectMapper.writeValueAsString(logMap);
            IndexQuery indexQuery = new IndexQueryBuilder()
                    .withSource(json)
                    .build();
            elasticsearchOperations.index(indexQuery, IndexCoordinates.of("httplogs"));

            // Important: copy content to the original response
            responseWrapper.copyBodyToResponse();
        }
    }

}
