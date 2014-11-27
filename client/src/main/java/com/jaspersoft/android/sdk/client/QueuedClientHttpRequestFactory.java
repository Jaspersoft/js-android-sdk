/*
 * Copyright (c) 2014. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.jaspersoft.android.sdk.client;

import org.springframework.http.client.SimpleClientHttpRequestFactory;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayDeque;
import java.util.Queue;

/**
 * @author Tom Koptel
 * @since 1.9
 */
public class QueuedClientHttpRequestFactory extends SimpleClientHttpRequestFactory {
    private static final String TAG = QueuedClientHttpRequestFactory.class.getSimpleName();
    private final Queue<HttpURLConnection> connectionPool = new ArrayDeque<HttpURLConnection>();

    /**
     * Template method for preparing the given {@link HttpURLConnection}.
     * <p/>
     * This implementation inherit default one. Prepares the connection for input and output,
     * and sets the HTTP method. In addition caches all {@link HttpURLConnection} instances inside
     * queue for later use.
     *
     * @param connection the connection to prepare
     * @param httpMethod the HTTP request method ({@code GET}, {@code POST}, etc.)
     * @throws IOException in case of I/O errors
     */
    protected void prepareConnection(HttpURLConnection connection, String httpMethod) throws IOException {
        super.prepareConnection(connection, httpMethod);
        connectionPool.offer(connection);
    }

    /**
     * Disconnect all {@link HttpURLConnection} already registered within poll of connections.
     */
    public void disconnectAll() {
        HttpURLConnection connection;
        while ((connection = connectionPool.poll()) != null) {
            connection.disconnect();
        }
    }

}
