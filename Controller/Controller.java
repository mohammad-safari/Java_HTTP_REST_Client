package Controller;

import java.awt.Color;
import java.awt.Component;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.Spring;
import javax.swing.SwingWorker;
import javax.swing.text.JTextComponent;
import javax.swing.tree.DefaultMutableTreeNode;

import Model.FileSystemCollection;
import Model.Model;
import Model.Request;
import View.Frame;
import View.PairPanel;
import javafx.scene.control.Label;

public class Controller {
    private Model model;
    private Frame view;
    private FileSystemCollection<Request> root;
    private Request current;
    private String response;
    private int ping;
    private long size;

    public Controller() {
    }

    public void sendRequest() {
        // creating request
        new SwingWorker<Request, String>() {
            @Override
            protected Request doInBackground() {
                publish("status");
                /**
                 * set headers
                 * 
                 */
                try {
                    String url = view.getRequestPanel().getAddressBar().getText();
                    current = new Request(url);
                } catch (MalformedURLException e) {
                    JOptionPane.showMessageDialog(null, "Please enter proper url!", "Malformed URL",
                            JOptionPane.ERROR_MESSAGE);
                    // cancelling for malformed url
                    cancel(true);
                    publish("canceled");
                    return null;
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null, "Input / Output Error happened, check Connection!",
                            "IO Exception", JOptionPane.ERROR_MESSAGE);
                    cancel(true);
                    publish("cancel");
                    return null;

                }
                /**
                 * gathering other data for request
                 */
                Map<String, List<String>> head = new ConcurrentHashMap<String, List<String>>();
                Component[] list = view.getRequestPanel().getHeader().getComponents();
                for (Component each : list)
                    if (each instanceof PairPanel) {
                        PairPanel pair = (PairPanel) each;
                        // only active pair values are gathered
                        if (pair.isSelected()) {
                            String key = ((JTextComponent) (pair.getComponent(0))).getText();
                            String val = ((JTextComponent) (pair.getComponent(0))).getText();
                            // empty entries are ignored
                            if (!key.equals("") && !val.equals("")) {
                                List<String> v = new ArrayList<String>();
                                v.add(val);
                                head.put(key, v);
                            }
                        }
                    }
                    current.setHeaders(head);

                /**
                 * gather query of request for appending
                 */
                Map<String, String> query = new ConcurrentHashMap<String, String>() {
                    {
                        Component[] list = view.getRequestPanel().getQuery().getComponents();
                        for (Component each : list)
                            if (each instanceof PairPanel) {
                                PairPanel pair = (PairPanel) each;
                                // only active pair values are gathered
                                if (pair.isSelected()) {
                                    String key = ((JTextComponent) (pair.getComponent(0))).getText();
                                    String val = ((JTextComponent) (pair.getComponent(1))).getText();
                                    // empty entries are ignored
                                    if (!key.equals("") && !val.equals("")) {
                                        put(key, val);
                                    }
                                }
                            }
                    }
                };

                /**
                 * setting the query for request appending to url ...
                 */
                try {
                    current.setQuery(query);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Query Error happened!", "Query Parameters",
                            JOptionPane.ERROR_MESSAGE);
                    // any error happens to query set
                    cancel(true);
                    publish("cancel");
                    return null;
                }

                /**
                 * configuring the json (must be athorized by gson parser) for appending to
                 * request
                 */
                String json = view.getRequestPanel().getJsonEditor().getText();
                try {
                    current.setJSONBody(json);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Json Error happened!", "Json", JOptionPane.ERROR_MESSAGE);
                    // json syntax error happens
                    cancel(true);
                    publish("cancel");
                    return null;
                }

                /**
                 * getting data for uploading binary contents (several files can be handled)
                 */
                JFileChooser bin = view.getRequestPanel().getBinary();
                File file = bin.getSelectedFile();
                if (file != null) {
                    // for (File file : files)
                    current.addBinFile(file.getPath());
                }

                /**
                 * gathering form data from gui form
                 */
                Map<String, String> form = new ConcurrentHashMap<String, String>();
                for (Component c : view.getRequestPanel().getFormdata().getComponents())
                    if (c instanceof PairPanel) {
                        PairPanel p = (PairPanel) c;
                        String key = ((JTextComponent) (p.getComponent(0))).getText();
                        String val = ((JTextComponent) (p.getComponent(1))).getText();
                        if (p.isSelected() && key != "")
                            form.put(key, val);
                    }
                current.setBodyParameters(form);

                /**
                 * setting method of the request from combo box
                 */
                JComboBox box = (JComboBox) (view.getRequestPanel().getComponent(0));
                try {
                    current.setMethod(box.getSelectedItem().toString());
                } catch (ProtocolException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                publish("pending");

                // investigating follow redirect condition
                if (view.getMyMenuBar().getFollowRedirect().isSelected())
                    current.setFollowRedirect(true);
                else
                    current.setFollowRedirect(false);

                /**
                 * multi part wise investigate the possibility of writting content then doing it
                 */
                try {
                    current.setMultipartWise();
                    Date now = new Date();
                    response = new String(current.sendRequest(), StandardCharsets.UTF_8);
                    Date then = new Date();
                    size = response.getBytes().length;
                    ping = (int) (then.getTime() - now.getTime());

                    publish(current.getConnection().getResponseMessage() + " "
                            + current.getConnection().getResponseCode());
                } catch (Exception e) {
                    cancel(true);
                    // e.printStackTrace();
                }
                return current;
            }

            /**
             * investigating process situaion according to what happened
             */
            protected void process(List<String> chunksList) {
                JLabel label = ((JLabel) (view.getResponsePanel().getComponent(0)));
                label.setBackground(Color.WHITE);
                for (String str : chunksList)
                    if (str.equals("pending")) {
                        // disabling send button
                        ((JButton) (view.getRequestPanel().getComponent(2))).setEnabled(false);
                        label.setText("pending ...");
                    } else {
                        // renabling the send button
                        label.setText(str);
                        ((JButton) (view.getRequestPanel().getComponent(2))).setEnabled(true);
                    }
            }

            /**
             * shwoing filnal data to user
             */
            protected void done() {
                int code;
                try {
                    // reenabling send button
                    ((JButton) (view.getRequestPanel().getComponent(2))).setEnabled(true);
                    code = current.getConnection().getResponseCode();
                    // filtering color
                    float r = (code < 300 ? 0.3f : code < 400 ? 0.7f : code < 500 ? 0.9f : 0.3f),
                            g = (code < 300 ? 0.5f : code < 400 ? 0.2f : code < 500 ? 0.1f : 0.9f),
                            b = (code < 300 ? 0.9f : code < 400 ? 0.9f : code < 500 ? 0.4f : 0.6f);
                    // setting status
                    JLabel label = ((JLabel) (view.getResponsePanel().getComponent(0)));
                    label.setBackground(new Color(r, g, b));
                    label.setText(code + " " + current.getConnection().getResponseMessage());

                    // stting claculated ping
                    label = ((JLabel) (view.getResponsePanel().getComponent(1)));
                    label.setText(ping + " ms");

                    // writing accepted 
                    label = ((JLabel) (view.getResponsePanel().getComponent(2)));
                    label.setText(size + " B");
                    view.getResponsePanel().setRawText(response);
                    // view.getResponsePanel().setHTMLText(response);
                    String headers = current.getResponseHeaders().toString();
                    headers = headers.replaceAll(",", "\n\n");
                    // headers= headers.replaceAll("{", "");
                    // headers= headers.replaceAll("}", "");
                    view.getResponsePanel().getResponseHeader().setText(headers);

                    view.getResponsePanel().repaint();
                    view.getResponsePanel().revalidate();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }.execute();

    }

    public void loadRequest() {

    }

    public void setModel(Model model) {
        this.model = model;
    }

    public void setView(Frame view) {
        this.view = view;
    }

    public Model getModel() {
        return model;
    }
}