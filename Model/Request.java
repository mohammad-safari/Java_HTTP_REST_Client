package Model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

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
        if (urlAddress.length() <= 7) {
            new URL("http://" + urlAddress);
            url = "http://" + urlAddress;
        } else {
            new URL(((urlAddress.substring(0, 7).equals("http://") ? "" : "http://") + urlAddress));
            // setting url address
            url = (((urlAddress.substring(0, 7).equals("http://") ? "" : "http://") + urlAddress));
        }
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
        // validating url
        if (url.length() <= 7) {
            new URL("http://" + url);
            this.url = "http://" + url;
        } else {
            new URL(((url.substring(0, 7).equals("http://") ? "" : "http://") + url));
            // setting url address
            url = (((url.substring(0, 7).equals("http://") ? "" : "http://") + url));
        }
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
        for (Map.Entry<String, String> param : query.entrySet()) {
            if (param.getKey() == "" || param.getValue() == "")
                throw new Exception("query key or vlaue can not be empty!");
            this.query.put(param.getKey(), param.getValue());
        }
        loadConnection();
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
            query = query.substring(0, query.length() - 1); // deleting the last ampersand
        }
        con = (HttpURLConnection) new URL(url + query).openConnection(); // adding http, slash and
                                                                         // query to url
        // loading previous headers
        setHeaders(headers);
        // method
        setMethod(method);
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
        this.method = method;
        if (Method.toMethod(method) == Method.OTHER) {
            addMethod(method);
        }
        con.setRequestMethod(method);
    }

    /**
     * using reflection api to override and add other methods to list of
     * authenticated(Standard) methods of HTTPURLConnection only in RUNTIME
     * 
     * @param method the non-standard method name
     */
    private static void addMethod(String method) {
        try {

            // getting runtime methods of HTTPConnection into a reflect field
            Field methodsField = HttpURLConnection.class.getDeclaredField("methods");

            // getting runtime modifiers of field class into a reflect field
            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(methodsField, methodsField.getModifiers() & ~Modifier.FINAL);
            methodsField.setAccessible(true);

            String[] oldMethods = (String[]) methodsField.get(null);
            Set<String> methodSet = new LinkedHashSet<>(Arrays.asList(oldMethods));
            methodSet.add(method);
            String[] newMethods = methodSet.toArray(new String[0]);

            methodsField.set(null, newMethods);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
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
            if (json != null)
                if (!json.equals(""))
                    multipart.addJSONTextPart(json);
            if (text != null)
                if (!text.equals(""))
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

    public boolean getMultipartReady() {
        return multipartReady;
    }

    /**
     * designed for jurl recognizing when to prepare multipart/form-data
     * 
     * @throws IOException when an I/O exception of some sort has occurred
     */
    public void setMultipartWise() throws IOException {
        if ((parameters == null || parameters.size() == 0) && (text == null || text.equals(""))
                && (json == null || json.equals("")) && (binaryFiles == null || binaryFiles.size() == 0)
                && (textFiles == null || textFiles.size() == 0))
            return;
        prepareMultipart();
    }
}