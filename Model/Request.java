package Model;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

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
                return "OPTIONS";
            case PATCH:
                return "PATCH";
            default:
                return "OTHER";
        }
    }
    public void send(URLConnection con){}
}

public class Request {
    private URL url;
    private Method method;
    private String messageBody;
    private boolean followRedirect;
    private HashMap<String, String> headers = new HashMap<String, String>();
    /**
     * 
     */
    public void saveBody() {
    }
    /**
     * 
     */
    public void saveRequest() {
    }

    /**
     * 
     * @param path
     */
    public void uploadFile(Path path) {
    }

    /**
     * prepare
     */
    public void sendRequest() {
        HttpURLConnection connection;
        // opening connection
        try {
            connection = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        // setting request method
        try {
            connection.setRequestMethod(method.toString());
        } catch (ProtocolException e) {
            e.printStackTrace();
            return;
        }
        // setting headers
        for (Map.Entry<String, String> header : headers.entrySet()) {
            connection.setRequestProperty(header.getKey(), header.getKey());
        }
        
    }
    public static void main(String[] args) {
         
    }
}