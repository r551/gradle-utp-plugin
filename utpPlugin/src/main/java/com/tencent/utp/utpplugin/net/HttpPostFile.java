package com.tencent.utp.utpplugin.net;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;

public class HttpPostFile {

    private final static char[] MULTIPART_CHARS = "-_1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
            .toCharArray();

    private URL url;
    private HttpURLConnection conn;
    private String boundary = null;
    private Map<String, String> textParams = new HashMap<String, String>();
    private Map<String, File> fileparams = new HashMap<String, File>();

    public HttpPostFile(String url) throws Exception {
        this.url = new URL(url);
    }

    public void setUrl(String url) throws Exception {
        this.url = new URL(url);
    }

    public void addTextParameter(String name, String value) {
        textParams.put(name, value);
    }

    public void addFileParameter(String name, File value) {
        fileparams.put(name, value);
    }

    public void clearAllParameters() {
        textParams.clear();
        fileparams.clear();
    }

    public byte[] send() throws Exception {
        initConnection();
        try {
            conn.connect();
        } catch (SocketTimeoutException e) {
            throw new Exception(e);
        }

        OutputStream connOutStream = new DataOutputStream(
                conn.getOutputStream());

        writeFileParams(connOutStream);
        writeStringParams(connOutStream);
        writesEnd(connOutStream);

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

    private void initConnection() throws Exception {
        StringBuffer buf = new StringBuffer("----");
        Random rand = new Random();
        for (int i = 0; i < 15; i++) {
            buf.append(MULTIPART_CHARS[rand.nextInt(MULTIPART_CHARS.length)]);
        }
        this.boundary = buf.toString();

        conn = (HttpURLConnection) this.url.openConnection();
        conn.setDoOutput(true);
        conn.setUseCaches(false);
        conn.setConnectTimeout(3 * 60 * 1000);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type",
                "multipart/form-data; boundary=" + boundary);
    }

    private void writeStringParams(OutputStream out) throws Exception {
        Set<String> keySet = textParams.keySet();
        for (Iterator<String> it = keySet.iterator(); it.hasNext();) {
            String name = it.next();
            String value = textParams.get(name);

            out.write(("--" + boundary + "\r\n").getBytes());
            out.write(("Content-Disposition: form-data; name=\"" + name + "\"\r\n")
                    .getBytes());
            out.write(("\r\n").getBytes());
            out.write((encode(value) + "\r\n").getBytes());
        }
    }

    private void writeFileParams(OutputStream out) throws Exception {
        Set<String> keySet = fileparams.keySet();
        for (Iterator<String> it = keySet.iterator(); it.hasNext();) {
            String name = it.next();
            File value = fileparams.get(name);

            out.write(("--" + boundary + "\r\n").getBytes());
            out.write(("Content-Disposition: form-data; name=\"" + name
                    + "\"; filename=\"" + encode(value.getName()) + "\"\r\n")
                    .getBytes());
            out.write(("Content-Type: " + getContentType(value) + "\r\n")
                    .getBytes());
            out.write(("Content-Transfer-Encoding: " + "binary" + "\r\n")
                    .getBytes());

            out.write(("\r\n").getBytes());

            FileInputStream inStream = new FileInputStream(value);
            int bytes = 0;
            byte[] bufferByte = new byte[1024];
            while ((bytes = inStream.read(bufferByte)) != -1) {
                out.write(bufferByte, 0, bytes);
            }
            inStream.close();

            out.write(("\r\n").getBytes());
        }
    }

    private void writesEnd(OutputStream out) throws Exception {
        out.write(("--" + boundary + "--" + "\r\n").getBytes());
        out.write(("\r\n").getBytes());
    }

    private String getContentType(File f) throws Exception {
        String fileName = f.getName();
        if (fileName.endsWith(".jpg")) {
            return "image/jpeg";
        } else if (fileName.endsWith(".png")) {
            return "image/png";
        }
        return "application/octet-stream";
    }

    private String encode(String value) throws Exception {
        return URLEncoder.encode(value, "UTF-8");
    }

} 