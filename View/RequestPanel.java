package View;

import java.awt.Color;
import java.awt.Insets;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JTabbedPane;

import Controller.Controller;

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
    private JTabbedPane reqPanel = new JTabbedPane();
    private JPanel body = new JPanel(), auth = new JPanel(), query = new JPanel(), header = new JPanel(),
            docs = new JPanel();
    private Controller controller;

    public RequestPanel() {
        super();
        init();
    }

    private void init() {
        // center panel(request panel)
        GridBagConstraints c = new GridBagConstraints();
        setLayout(new GridBagLayout());
        c.insets = new Insets(10, 10, 10, 10);
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.NORTHWEST;
        c.gridx = 0;
        c.gridy = 0;
        c.gridheight = 1;
        c.gridwidth = 1;
        c.weightx = 0.25;
        c.weighty = 0.25;
        add(reqBox, c);
        c.gridx = 1;
        c.gridy = 0;
        c.gridheight = 1;
        c.gridwidth = 4;
        c.weightx = 1;
        add(addressBar, c);
        c.gridx = 5;
        c.gridy = 0;
        c.gridheight = 1;
        c.gridwidth = 1;
        c.weightx = 0.25;
        add(sendButton, c);
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.gridheight = GridBagConstraints.REMAINDER;
        c.weighty = 5;
        add(reqPanel, c);
        reqPanel.addTab("Body "+'\u2b9b', body);
        reqPanel.addTab("Auth "+'\u2b9b', auth);
        reqPanel.addTab("Query", query);
        reqPanel.addTab("Header", header);
        reqPanel.addTab("Docs", docs);
        setBackground(new Color(.3f, .2f, .4f));
    }

    /**
     * @param controller the controller to set
     */
    public void setController(Controller controller) {
        this.controller = controller;

    }
}