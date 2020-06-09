package Model;

import java.io.IOException;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

enum Method {
    GET, HEAD, POST, PUT, DELETE, CONNECT, OPTIONS, TRACE, PATCH, OTHER;

    @Override
    public String toString() {
        switch (this) {
            case GET:
                return "GET";
            case HEAD:
                return "HEAD";
            case POST:
                return "POST";
            case PUT:
                return "PUT";
            case DELETE:
                return "DELETE";
            case CONNECT:
                return "CONNECT";
            case OPTIONS:
                return "OPTIONS";
            case TRACE:
                return "TRACE";
            case PATCH:
                return "PATCH";
            default:
                return "OTHER";
        }
    }

    public static Method toMethod(String method) {
        switch (method) {
            case "GET":
                return GET;
            case "HEAD":
                return HEAD;
            case "POST":
                return POST;
            case "PUT":
                return PUT;
            case "DELETE":
                return DELETE;
            case "CONNECT":
                return CONNECT;
            case "OPTIONS":
                return OPTIONS;
            case "TRACE":
                return TRACE;
            default:
                return OTHER;
        }
    }
}

public class Request implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 499498293015481510L;

    private URL url; // desired desination of request
    private transient HttpURLConnection con; // managing connection of request
    private String method = "GET"; // method of request
    private transient MultipartStreamBuilder multipart;
    private Map<String, String> parameters; // parameters of request
    private Map<String, List<String>> headers;// properties of request(client-side headers)
    private boolean followRedirect = false; // option of following redirect
    private String boundary; // used in multipart-form data

    /**
     * 
     * @param urlAddress
     * @throws MalformedURLException
     * @throws IOException
     */
    public Request(String urlAddress) throws MalformedURLException, IOException {
        // destination url should be set when created
        url = new URL(urlAddress.substring(0, 6) == "http://" ? urlAddress : "http://" + urlAddress);
        // connection get opened when created
        con = (HttpURLConnection) url.openConnection();
        // headers(properties) of connection are now saved in this map
        headers = con.getRequestProperties();
        // setting boundary key
        boundary = Long.toHexString(new Random().nextLong()); 
    }

    /**
     * reopening a request connection after loaded or closed
     * 
     * @param urlAddress destination of request
     * @throws MalformedURLException
     * @throws IOException
     */
    public void init() throws IOException {
        // connection get reopened
        con = (HttpURLConnection) url.openConnection();
        // headers(properties) of connection are reloaded
        setHedears((HashMap<String, List<String>>) headers);
        // rewriting output stream generally!
    }

    /**
     * setting request client-side headers(actually overwriting them)
     * 
     * @param headers list of headers to initializes request headers
     */
    public void setHedears(HashMap<String, List<String>> headers) {
        for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
            // key and value list
            String key = entry.getKey();
            List<String> otherValues = entry.getValue();
            // setting value index 0 of the key
            con.setRequestProperty(key, otherValues.get(0));

            // setting other values of the key
            otherValues.remove(0);
            for (String value : otherValues) {
                con.addRequestProperty(key, value);
            }
        }
    }

    /**
     * defining new client-side header(key and value) otherwise overwrite its value
     * 
     * @param key
     * @param value
     */
    public void setHeader(String key, String value) {
        con.setRequestProperty(key, value);
    }

    /**
     * adding value to client-side headers if exists otherwise creating one
     * 
     * @param key
     * @param value
     */
    public void addHeader(String key, String value) {
        con.addRequestProperty(key, value);
    }

    /**
     * prepare for posting pararmters-json-binary
     * 
     * @throws IOException
     */
    public String prepareMultpart() throws IOException {
        con.setDoOutput(true);
        setHeader("Content-Type", "multipart/form-data; boundary=" + boundary);
        multipart = new MultipartStreamBuilder(con.getOutputStream(), boundary);
        return boundary;
    }

    /**
     * 
     * @param method
     * @throws ProtocolException
     */
    public void setMethod(String method) throws ProtocolException {
        if (Method.toMethod(method) == Method.OTHER) {
            con.setRequestProperty("X-HTTP-Method-Override", method);
            method = "GET";
        }
        con.setRequestMethod(method);
    }

    public Map<String, List<String>> getResponseHeaders() {
        return con.getHeaderFields();
    }

    // temp
    public HttpURLConnection getConnection() {
        return con;
    }
}