package com.tencent.utp.utpplugin.net;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.*;

public class HttpPostJson {
    private URL url;
    private HttpURLConnection conn;
    private UtpSubmitReq jsonParams;

    public UtpSubmitReq getJsonParams() {
        return jsonParams;
    }

    public HttpPostJson(String url) throws Exception {
        this.url = new URL(url);
        jsonParams = new UtpSubmitReq();
    }

    public void setUrl(String url) throws Exception {
        this.url = new URL(url);
    }

    public byte[] send(String body) throws Exception {
        initJsonConnection();
        try {
            conn.connect();
        } catch (SocketTimeoutException e) {
            throw new Exception(e);
        }

        OutputStream connOutStream = new DataOutputStream(
                conn.getOutputStream());
        connOutStream.write(body.getBytes());

        InputStream responseInStream = conn.getInputStream();
        ByteArrayOutputStream responseOutStream = new ByteArrayOutputStream();
        int len;
        byte[] bufferByte = new byte[1024];
        while ((len = responseInStream.read(bufferByte)) != -1) {
            responseOutStream.write(bufferByte, 0, len);
        }
        responseInStream.close();
        connOutStream.close();

        conn.disconnect();
        byte[] resultByte = responseOutStream.toByteArray();
        responseOutStream.close();
        return resultByte;
    }

    public byte[] send() throws Exception {
        initJsonConnection();
        try {
            conn.connect();
        } catch (SocketTimeoutException e) {
            throw new Exception(e);
        }

        OutputStream connOutStream = new DataOutputStream(
                conn.getOutputStream());
        writeJsonParams(connOutStream);

        InputStream responseInStream = conn.getInputStream();
        ByteArrayOutputStream responseOutStream = new ByteArrayOutputStream();
        int len;
        byte[] bufferByte = new byte[1024];
        while ((len = responseInStream.read(bufferByte)) != -1) {
            responseOutStream.write(bufferByte, 0, len);
        }
        responseInStream.close();
        connOutStream.close();

        conn.disconnect();
        byte[] resultByte = responseOutStream.toByteArray();
        responseOutStream.close();
        return resultByte;
    }

    private void initJsonConnection() throws Exception {
        conn = (HttpURLConnection) this.url.openConnection();
        conn.setDoOutput(true);
        conn.setUseCaches(false);
        conn.setConnectTimeout(3 * 60 * 1000);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        conn.setRequestProperty("Accept", "application/json");
    }

    private void writeJsonParams(OutputStream out) throws Exception {
        jsonParams.productId = 22L; // default map
        out.write((jsonParams.toString()).getBytes());
    }
} 