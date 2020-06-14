package View;

import java.awt.Color;
import java.awt.Insets;
import java.awt.PopupMenu;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.AbstractButton;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.MouseInputAdapter;
import javax.swing.JTabbedPane;

import Controller.Controller;
import javax.swing.Box;

public class RequestPanel extends JPanel {
    /**
     *
     */
    private static final long serialVersionUID = -152062743165019082L;
    private String[] types = new String[] { "GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS", "HEAD",
            "Custom Method" };
    private JComboBox reqBox = new JComboBox<String>(types);
    private JTextField addressBar = new JTextField("8.8.8.8");
    private JButton sendButton = new JButton("SEND");
    private TabbedPanel reqPanel = new TabbedPanel();
    private JPanel body = new JPanel(), auth = new JPanel(), query = new JPanel(), header = new JPanel(),
            docs = new JPanel();
    private JButton addHeaderButton = new JButton("add Header");
    private JButton addQueryButton = new JButton("add Query");
    private JButton addFormButton = new JButton("add Form Data");

    private JTextPane jsonEditor = new JTextPane();
    private JTextPane textEditor = new JTextPane();
    private JPanel formdata = new JPanel();
    private JFileChooser binary = new JFileChooser();

    private Controller controller;

    public RequestPanel(Controller controller) {
        super();
        setController(controller);
        sendButton.addActionListener(e -> controller.sendRequest());
        init();
    }

    private void init() {
        // center panel(request panel)
        GroupLayout glo = new GroupLayout(this);
        glo.setAutoCreateGaps(true);
        glo.setAutoCreateContainerGaps(true);
        setLayout(glo);
        glo.setHorizontalGroup(glo.createParallelGroup()
                .addGroup(glo.createSequentialGroup().addComponent(reqBox, 100, 100, 100)
                        .addComponent(addressBar, 50, 100, 1000).addComponent(sendButton, 65, 65, 65))
                .addComponent(reqPanel, 200, 200, 1500));
        glo.setVerticalGroup(glo.createSequentialGroup()
                .addGroup(glo.createParallelGroup().addComponent(reqBox, 40, 40, 40)
                        .addComponent(addressBar, 40, 40, 40).addComponent(sendButton, 40, 40, 40))
                .addGap(20, 20, 20).addComponent(reqPanel, 350, 350, 1000));
        reqPanel.addTab("Body", body);
        reqPanel.addTab("Auth", auth);
        reqPanel.addTab("Query", query);
        reqPanel.addTab("Header", header);
        reqPanel.addTab("Docs", docs);
        setBackground(new Color(.3f, .2f, .4f));

        body.setBackground(new Color(.0f, .2f, .4f));
        query.setBackground(new Color(.1f, .0f, .4f));
        header.setBackground(new Color(.0f, .0f, .1f));

        JMenuItem FD = new JMenuItem("Form Data"), JS = new JMenuItem("JSON"), BIN = new JMenuItem("Binary");

        reqPanel.getButton("Body").setMenued(true);
        reqPanel.getButton("Body").add(new JSeparator());
        reqPanel.getButton("Body").add(FD);
        reqPanel.getButton("Body").add(new JSeparator());
        reqPanel.getButton("Body").add(JS);
        reqPanel.getButton("Body").add(new JSeparator());
        reqPanel.getButton("Body").add(BIN);

        FD.addActionListener(e -> {
            reqPanel.getContainer("Body").removeAll();
            reqPanel.getContainer("Body").add(formdata);
            repaint();
            revalidate();
        });
        JS.addActionListener(e -> {
            reqPanel.getContainer("Body").removeAll();
            reqPanel.getContainer("Body").add(jsonEditor);
            repaint();
            revalidate();
        });
        BIN.addActionListener(e -> {
            reqPanel.getContainer("Body").removeAll();
            reqPanel.getContainer("Body").add(binary);
            repaint();
            revalidate();
        });

        reqPanel.getButton("Auth").setMenued(true);
        reqPanel.getButton("Auth").add(new JSeparator());
        reqPanel.getButton("Auth").add(new JMenuItem("Bearer"));
        reqPanel.getButton("Auth").add(new JSeparator());
        reqPanel.getButton("Auth").add(new JMenuItem("No Authentication"));

        initHeaderTab();
        initQueryTab();
        initFormDataTab();
    }

    private void initFormDataTab() {
        formdata.setLayout(new BoxLayout(formdata, BoxLayout.Y_AXIS));
        formdata.add(Box.createRigidArea(new Dimension(0, 20)));
        formdata.add(addFormButton);
        addFormButton.setAlignmentX(CENTER_ALIGNMENT);
        addFormButton.addActionListener(e -> addFormPair());
    }

    public void initHeaderTab() {
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.add(Box.createRigidArea(new Dimension(0, 20)));
        header.add(addHeaderButton);
        addHeaderButton.setAlignmentX(CENTER_ALIGNMENT);
        addHeaderButton.addActionListener(e -> addHeaderPair());
    }

    public void initQueryTab() {
        query.setLayout(new BoxLayout(query, BoxLayout.Y_AXIS));
        query.add(Box.createRigidArea(new Dimension(0, 20)));
        query.add(addQueryButton);
        addQueryButton.setAlignmentX(CENTER_ALIGNMENT);
        addQueryButton.addActionListener(e -> addQueryPair());
    }

    private void addHeaderPair() {
        header.add(new PairPanel());
        header.revalidate();
        header.repaint();
    }

    private void addQueryPair() {
        query.add(new PairPanel());
        query.revalidate();
        query.repaint();
    }

    private void addFormPair() {
        formdata.add(new PairPanel());
        formdata.revalidate();
        formdata.repaint();
    }

    /**
     * @param controller the controller to set
     */
    public void setController(Controller controller) {
        this.controller = controller;

    }

    public JTextField getAddressBar() {
        return addressBar;
    }

    public JPanel getHeader() {
        return header;
    }

    public JPanel getQuery() {
        return query;
    }

    public JTextPane getJsonEditor() {
        return jsonEditor;
    }

    public JFileChooser getBinary() {
        return binary;
    }

    public JTextPane getTextEditor() {
        return textEditor;
    }

    public JPanel getFormdata() {
        return formdata;
    }
}