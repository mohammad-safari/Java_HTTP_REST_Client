package Model;

import java.io.IOException;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Path;
import java.security.KeyStore.Entry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
}

public class Request implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 499498293015481510L;

    private URL url; // desired desination of request
    private HttpURLConnection con; // managing connection of request
    private Method method = Method.GET; // method of request
    private String messageBody; // message body would be a form, or a json, or or any file
    private Map<String, String> parameters = new HashMap<>(); // parameters of request
    // private Map<String, List<String>> headers = new HashMap<String, List<String>>();// properties of request(client-side
    // headers)
    private boolean followRedirect = false; // option of following redirect

    /**
     * opening a request connection
     * 
     * @param urlAddress destination of request
     * @throws MalformedURLException
     * @throws IOException
     */
    public void init(String urlAddress) throws MalformedURLException, IOException {
        url = new URL(urlAddress);
        con = (HttpURLConnection) url.openConnection();
        // System.out.println("Malformed Url, please enter url in correct format");
        // System.out.println("Exception in connection input happened!");
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

    public void prepareGetMethod() {
    }

    public void preparePostMethod() {
    }

    public static void main(String[] args) {
        // new Request().init("");
    }

}