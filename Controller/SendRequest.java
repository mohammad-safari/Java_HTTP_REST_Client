package Controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLConnection;
import java.net.UnknownHostException;

import javax.swing.SwingWorker;

import Model.Request;

public class SendRequest extends SwingWorker<URLConnection, String> {

    Request current;

    public SendRequest(Request req) {
        req = current;
    }

    @Override
    protected URLConnection doInBackground() throws Exception {
            // sending request
            try {
                current.setMultipartWise();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            try {
                // writing if multipart stream builder id prepared
                System.out.println(current.getConnection().getRequestMethod());
                ByteArrayInputStream responseStream = new ByteArrayInputStream(current.sendRequest(/* 5000 */));
                char[] reader = new char[responseStream.available()];
                new InputStreamReader(responseStream).read(reader);
                System.out.println(reader);
            } catch (UnknownHostException e) {
                // TODO Auto-generated catch block
                // e.printStackTrace();
                System.out.println("Unfortunaltely can not resolve the Host!");
            } catch (IOException e) {
                // TODO Auto-generated catch block
                // e.printStackTrace();
                try {
                    if (current.getConnection().getResponseCode() >= 500)
                        System.out.println("Server does not Provide Service");
                } catch (IOException ioe) {
                    // TODO Auto-generated catch block
                    // e.printStackTrace();
                    System.out.println(ioe);
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                // e.printStackTrace();
                System.out.println("some thing unknown happened to connection!");
            }
            return current.getConnection();    
    }

}