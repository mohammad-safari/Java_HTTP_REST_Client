package Model;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class MultipartStreamBuilder {

    private transient OutputStream output; // main stream of the connection will be written at writeToStream
    private transient BufferedOutputStream bufferedOutput; // all changes happen to this stream before final write
    private transient PrintWriter writer; // used for writing String formated to bufferd stream
    private String boundary; // the key which separates parts of outstream
    private final String CRLF = "\r\n"; // CRLF character(string)

    /**
     * 
     * @param boundary needed for dividing the stream
     * @param output   get output stream which is gonna write
     */
    public MultipartStreamBuilder(OutputStream output, String boundary) {

        this.output = output;
        this.boundary = boundary;
        bufferedOutput = new BufferedOutputStream(this.output);
        writer = new PrintWriter(bufferedOutput);
    }

    /**
     * adding a parameter in form data format
     * 
     * @param key
     * @param val
     */
    public void addParameterPart(String key, String val) {
        writer.append("--" + boundary).append(CRLF);
        writer.append("Content-Disposition: form-data; name=\"" + key + "\"").append(CRLF);
        writer.append("Content-Type: text/plain; charset=" + "UTF-8").append(CRLF);
        writer.append(CRLF).append("\"" + val + "\"").append(CRLF).flush(); // end of boundary
    }

    public void addTextFilePart(File textFile) throws IOException {
        writer.append("--" + boundary).append(CRLF);
        writer.append("Content-Disposition: form-data; name=\"textFile\"; filename=\"" + textFile.getName() + "\"")
                .append(CRLF);
        writer.append("Content-Type: text/plain; charset=" + "UTF-8").append(CRLF); // The text file saved in UTF-8
                                                                                    // charset!
        writer.append(CRLF).flush();
        Files.copy(textFile.toPath(), bufferedOutput);
        bufferedOutput.flush();
        writer.append(CRLF).flush(); // pointing end of boundary.

    }

    public void addBinaryFilePart(File binaryFile) throws FileNotFoundException, IOException {
        writer.append("--" + boundary).append(CRLF);
        writer.append("Content-Disposition: form-data; name=\"binaryFile\"; filename=\"" + binaryFile.getName() + "\"")
                .append(CRLF);
        writer.append("Content-Type: " + URLConnection.guessContentTypeFromStream(new FileInputStream(binaryFile)))
                .append(CRLF);
        writer.append("Content-Transfer-Encoding: binary").append(CRLF);
        writer.append(CRLF).flush();
        Files.copy(binaryFile.toPath(), output);
        output.flush();
        writer.append(CRLF).flush();
    }

    /**
     * 
     * finally writes buffer(multipart boundary headers and bodies) and multipart
     * boundary footer
     * 
     * @throws IOException
     */
    public void writeToStream() throws IOException {
        // write all buffered to given stream
        bufferedOutput.flush();
        // ending multipart stream
        new PrintWriter(output).append("--" + boundary + "--").append(CRLF).flush();
    }

}

// class ParameterStringBuilder {
// public static String getParamsString(Map<String, String> params) throws
// UnsupportedEncodingException {
// StringBuilder result = new StringBuilder();

// for (Map.Entry<String, String> entry : params.entrySet()) {
// result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
// result.append("=");
// result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
// result.append("&");
// }

// String resultString = result.toString();
// return resultString.length() > 0 ? resultString.substring(0,
// resultString.length() - 1) : resultString;
// }
// }