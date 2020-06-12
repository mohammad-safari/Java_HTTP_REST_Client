package Model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

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

    private String url; // desired desination of request
    private transient HttpURLConnection con; // managing connection of request
    private String method = "GET"; // method of request
    private Map<String, List<String>> headers;// properties of request(client-side headers)

    private Map<String, String> query; // parameters of request

    private transient MultipartStreamBuilder multipart;
    private Map<String, String> parameters; // parameters of request
    private String json; // json body of request
    private String text; // text body of request
    private List<String> textFiles; // text files to write in the bidy of request
    private List<String> binaryFiles; // arbitrary binary files to write in the body of request

    private String boundary; // used in multipart-form data
    private boolean multipartReady;
    private boolean followRedirect = false; // option of following redirect

    private String outputPath; // the path to response output file

    /**
     * only get an initial request url
     * 
     * @param urlAddress the initial url address
     * @throws MalformedURLException if url is malformed
     */
    public Request(String urlAddress) throws MalformedURLException, IOException {
        // validating url
        new URL(((urlAddress.substring(0, 7).equals("http://") ? "" : "http://") + urlAddress));
        // setting url address
        url = (((urlAddress.substring(0, 7).equals("http://") ? "" : "http://") + urlAddress));
        // connection get opened when created
        con = (HttpURLConnection) new URL(url).openConnection();
        // headers(properties) of connection are now saved in this map
        headers = con.getRequestProperties();
        // query parameters will be saved in this map
        query = new HashMap<String, String>();
        // parameters to be written to body are saved in here
        parameters = new HashMap<String, String>();
        // a list of the path of all chosen binary files to be written in body
        binaryFiles = new ArrayList<String>();
        // a list of the parth of all chosen text files (in UTF-8 charset) to be written
        // in body
        textFiles = new ArrayList<String>();
        // setting boundary key
        boundary = Long.toHexString(new Random().nextLong());
        multipartReady = false;
    }

    /**
     * changing the url(attention: needs to load and reopening the connection for
     * efficacy)
     * 
     * @param url request destinattion
     * @throws MalformedURLException
     * @see {@link #loadConnection()}
     */
    public void setUrl(String url) throws MalformedURLException {
        url = ((url.substring(0, 7).equals("http://") ? "" : "http://") + url);
        new URL(url);
        // setting url address
        this.url = url;
    }

    /**
     * setting(overwrites) query parameters(attention: needs to load and reopening
     * the connection for efficacy)
     * 
     * @param query list of queries to add
     * @throws Exception if query is improper
     * @see {@link #loadConnection()}
     */
    public void setQuery(Map<String, String> query) throws Exception {
        for (Map.Entry<String, String> param : this.query.entrySet()) {
            if (param.getKey() == "" || param.getValue() == "")
                throw new Exception("query key or vlaue can not be empty!");
            this.query.put(param.getKey(), param.getValue());
        }
    }

    /**
     * must be called after any changes to connection url and header or when
     * reloading(reopening) the connection
     * 
     * @throws MalformedURLException if url grow malformed
     * @throws IOException           if an I/O exception occurs for connection
     */
    public void loadConnection() throws MalformedURLException, IOException {
        String query = "";
        if (!this.query.isEmpty()) {
            query = "?";
            for (Map.Entry<String, String> param : this.query.entrySet())
                query = query + param.getKey() + "=" + param.getValue() + "&";
            query = query.substring(0, query.length() - 2); // deleting the last ampersand
        }
        con = (HttpURLConnection) new URL((url.substring(0, 6) == "http://" ? "" : "http://") + url
                + (url.charAt(url.length() - 1) == '/' ? "" : "/") + query).openConnection(); // adding http, slash and
                                                                                              // query to url
        // loading previous headers
        setHeaders(headers);
        // loading body
        multipartReady = false;
    }

    /**
     * setting request client-side headers(actually overwriting them)
     * 
     * @param head list of headers to initializes request headers
     */
    public void setHeaders(Map<String, List<String>> properties) {
        for (Map.Entry<String, List<String>> entry : properties.entrySet()) {
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
     * @param key   header key
     * @param value header value
     */
    public void setHeader(String key, String value) {
        con.setRequestProperty(key, value);
    }

    /**
     * adding value to client-side headers if exists otherwise creating one
     * 
     * @param key   header key
     * @param value header value
     */
    public void addHeader(String key, String value) {
        con.addRequestProperty(key, value);
    }

    /**
     * the POST method would mask the overriden method (default method is GET)
     * 
     * @param method the method of request
     * @throws ProtocolException if the method cannot be reset or if the requested
     *                           method isn't valid for HTTP
     */
    public void setMethod(String method) throws ProtocolException {
        if (Method.toMethod(method) == Method.OTHER) {
            con.setRequestProperty("X-HTTP-Method-Override", method);
            method = "POST";
        }
        con.setRequestMethod(method);
    }

    /**
     * the mask method must be given in this overload (default method is GET)
     * 
     * @param method the method of request
     * @throws ProtocolException if the method cannot be reset or if the requested
     *                           method isn't valid for HTTP
     */
    public void setMethod(String method, Method mask) throws ProtocolException {
        if (Method.toMethod(method) == Method.OTHER) {
            con.setRequestProperty("X-HTTP-Method-Override", method);
            if (mask != Method.OTHER)
                method = mask.toString();
            else
                method = "PUT";
        }
        con.setRequestMethod(method);
    }

    /**
     * prepare for posting pararmters-json-files(text/binaries/...), must be called
     * before adding content to multipart body
     * 
     * @throws IOException
     */
    public String prepareMultipart() throws IOException {
        con.setDoOutput(true);
        setHeader("Content-Type", "multipart/form-data; boundary=" + boundary);
        multipart = new MultipartStreamBuilder(con.getOutputStream(), boundary);
        multipartReady = true;
        return boundary;
    }

    /**
     * if multipart stream writer is not ready does nothing and must be used once
     * need reloading connection for more
     * 
     * @throws FileNotFoundException an attempt to open the file denoted by a
     *                               specified pathname has failed
     * @throws IOException           an I/O exception of some sort has occurred
     * @see {@link #prepareMultipart()}
     * @see {@link #loadConnection()}
     */
    public void writeBodyStream() throws FileNotFoundException, IOException {
        if (multipartReady) {
            for (Map.Entry<String, String> entry : parameters.entrySet())
                multipart.addParameterPart(entry.getKey(), entry.getValue());
            multipart.addJSONTextPart(json);
            multipart.addTextPart(text);
            for (String path : textFiles)
                multipart.addTextFilePart(new File(path));
            for (String path : binaryFiles)
                multipart.addBinaryFilePart(new File(path));
            multipart.writeToStream();
        }
    }

    /**
     * setting request parameters in body
     * 
     * @param param list of params to initializes request params
     */
    public void setBodyParameters(Map<String, String> param) {
        for (Map.Entry<String, String> entry : param.entrySet()) {
            // key and value list
            String key = entry.getKey();
            String val = entry.getValue();
            // checking parameters key and values
            if (key != "") {
                parameters.put(key, val);
            }
        }
    }

    /**
     * 
     * @param JSONString to write a json object in body
     * @throws JsonSyntaxException if json syntax is not regarded
     * @throws JsonParseException  if somthing happens to parser so that string
     *                             cannot be parsed
     */
    public void setJSONBody(String JSONString) throws JsonSyntaxException, JsonParseException {
        new JsonParser().parse(JSONString);
        // set it if is validate as a json string
        json = JSONString;
    }

    /**
     * 
     * @param plainText a plain text to write in body
     */
    public void setTextBody(String plainText) {
        text = plainText;
    }

    /**
     * to write a UTF-8 text file to request body
     * 
     * @param path to text file
     */
    public void addTextFile(String path) {
        new File(path);
        textFiles.add(path);
    }

    /**
     * to write an arbitrary binary to request body
     * 
     * @param path to binary file
     */
    public void addBinFile(String path) {
        new File(path);
        textFiles.add(path);
    }

    /**
     * 
     * @return response headers
     */
    public Map<String, List<String>> getResponseHeaders() {
        return con.getHeaderFields();
    }

    /**
     * setting output path for
     * 
     * @param outputPath
     */
    public void setOutputPath(String outputPath) {
        new File(outputPath);
        this.outputPath = outputPath;
    }

    public byte[] sendRequest(/* int milisec */) throws Exception {
        // con.setConnectTimeout(milisec);
        writeBodyStream();
        int code = con.getResponseCode();
        if (code >= 300 && code < 400 && followRedirect) {
            // current.getConnection().setInstanceFollowRedirects(true);
            // HttpURLConnection.setFollowRedirects(true);
            String redirectPage = con.getHeaderField("Location");
            if (redirectPage.substring(0, 8).equals("https://"))
                redirectPage = "http://" + redirectPage.substring(8);
            if (redirectPage != null && !redirectPage.equals("")) {
                setUrl(redirectPage);
                loadConnection();
                System.out.println("redirecting to " + redirectPage);
                sendRequest(/* milisec */);
            } else {
                throw new Exception("Redirect Not Found!");
            }
        }
        // returning response body
        byte[] responseBody;
        InputStream inputStream;
        if (con.getInputStream().available() > 0) {
            inputStream = con.getInputStream();
        } else {
            inputStream = con.getErrorStream();
        }
        if (inputStream == null)
            return "Done".getBytes();

        responseBody = new byte[inputStream.available()];
        inputStream.read(responseBody);
        // writing response to file
        if (!(outputPath == null || outputPath.equals(""))) {
            OutputStream bodyStream = new FileOutputStream(new File(outputPath));
            bodyStream.write(responseBody);
            bodyStream.flush();
        }

        return responseBody;
    }

    public void setFollowRedirect(boolean followRedirect) {
        this.followRedirect = followRedirect;
    }

    // temp
    public HttpURLConnection getConnection() {
        return con;
    }
}