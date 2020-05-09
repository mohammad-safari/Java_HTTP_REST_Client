package View;

import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.GridBagConstraints;
import java.awt.BorderLayout;
import java.awt.event.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.*;

import Controller.*;

public class Frame extends JFrame {
    /**
     *
     */
    private static final long serialVersionUID = -2257792327102504528L;
    Controller controller;

    // components of main frame
    private JMenuBar menuBar = new JMenuBar();
    private JMenu appMenu = new JMenu("Application"), editMenu = new JMenu("Edit"), viewMenu = new JMenu("View"),
            winMenu = new JMenu("Window"), toolMenu = new JMenu("Tools"), helpMenu = new JMenu("Help");
    // frame menu items
    private JMenuItem pref = new JMenuItem("prefernce"), chlog = new JMenuItem("changelog"),
            happ = new JMenuItem("hide app"), hother = new JMenuItem("hide other"), quit = new JMenuItem("quit");
    private JMenuItem undo = new JMenuItem("undo"), redo = new JMenuItem("redo"), cut = new JMenuItem("cut"),
            copy = new JMenuItem("copy"), paste = new JMenuItem("paste"), selall = new JMenuItem("select all");
    private JMenuItem full = new JMenuItem("toggle fullscreen"), actual = new JMenuItem("actual size"),
            zin = new JMenuItem("zoom in"), zout = new JMenuItem("zoom out"), sidebar = new JMenuItem("toggle sidebar"),
            devtools = new JMenuItem("toggle devtools");
    private JMenuItem mini = new JMenuItem("minimize"), plugins = new JMenuItem("reload pluginss");
    private JMenuItem about = new JMenuItem("about"), support = new JMenuItem("contact support"),
            dataFolder = new JMenuItem("app data folder"), license = new JMenuItem("show open source licenses"),
            shortcut = new JMenuItem("keyboard shortcut"), help = new JMenuItem("app help");
    // panes and panels of main frame
    private JSplitPane right = new JSplitPane(), left = new JSplitPane();
    private JPanel rightPanel = new JPanel(), centerPanel = new JPanel(), sidePanel = new JPanel();
    // request panel
    private String[] types = new String[] { "GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS", "HEAD",
            "Custom Method" };
    private JComboBox reqBox = new JComboBox<String>(types);
    private JTextField addressBar = new JTextField("8.8.8.8");
    private JButton sendButton = new JButton("SEND");
    private JTabbedPane reqPanel = new JTabbedPane();
    private JPanel body = new JPanel(), auth = new JPanel(), query = new JPanel(), header = new JPanel(),
            docs = new JPanel();

    public Frame() {
        // frame initialization
        super("HTTP Request Debug App");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBounds(100, 100, 1000, 600);

        // adding components
        // adding components to main frame
        add(menuBar, BorderLayout.NORTH);

        add(left, BorderLayout.CENTER);

        initMenuBar();

        // left pane
        left.setBorder(null);
        left.setDividerSize(1);
        left.setDividerLocation(200);
        left.setOpaque(true);
        left.setBackground(new Color(.3f, .2f, .4f));
        left.setRightComponent(right);
        left.setLeftComponent(sidePanel);

        // right pane
        right.setBorder(null);
        right.setDividerSize(1);
        right.setDividerLocation(350);
        right.setOpaque(true);
        right.setLeftComponent(centerPanel);
        right.setRightComponent(rightPanel);

        initSideBar();

        initRequestPanel();

        initReponsePanel();

        // setting visible
        setVisible(true);
    }

    private void initReponsePanel() {
        // right panel(response panel)
        rightPanel.setLayout(new GridBagLayout());
        rightPanel.setBackground(new Color(.3f, .2f, .4f));
    }

    private void initRequestPanel() {
        // center panel(request panel)
        GridBagConstraints c = new GridBagConstraints();
        centerPanel.setLayout(new GridBagLayout());
        c.insets = new Insets(10, 10, 10, 10);
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.NORTHWEST;
        c.gridx = 0;
        c.gridy = 0;
        c.gridheight = 1;
        c.gridwidth = 1;
        c.weightx = 0.25;
        c.weighty = 0.25;
        centerPanel.add(reqBox, c);
        c.gridx = 1;
        c.gridy = 0;
        c.gridheight = 1;
        c.gridwidth = 4;
        c.weightx = 1;
        centerPanel.add(addressBar, c);
        c.gridx = 5;
        c.gridy = 0;
        c.gridheight = 1;
        c.gridwidth = 1;
        c.weightx = 0.25;
        centerPanel.add(sendButton, c);
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.gridheight = GridBagConstraints.REMAINDER;
        c.weighty = 5;
        centerPanel.add(reqPanel, c);
        reqPanel.addTab("Body", body);
        reqPanel.addTab("Auth", auth);
        reqPanel.addTab("Query", query);
        reqPanel.addTab("Header", header);
        reqPanel.addTab("Docs", docs);
        centerPanel.setBackground(new Color(.3f, .2f, .4f));
    }

    private void initSideBar() {
        // left panel(side bar)
        sidePanel.setLayout(new GridBagLayout());
        sidePanel.setBackground(new Color(.1f, .05f, .3f));
    }

    /**
     * initializing menubar
     */
    private void initMenuBar() {
        menuBar.setBorderPainted(false);
        menuBar.add(appMenu);
        appMenu.setMnemonic(KeyEvent.VK_A);
        appMenu.add(pref);
        appMenu.add(chlog);
        appMenu.add(new JSeparator());
        appMenu.add(happ);
        happ.setAccelerator(KeyStroke.getKeyStroke("H"));
        appMenu.add(hother);
        hother.setAccelerator(KeyStroke.getKeyStroke("alt H"));
        appMenu.add(new JSeparator());
        appMenu.add(quit);
        quit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.CTRL_MASK));
        menuBar.add(editMenu);
        editMenu.setMnemonic(KeyEvent.VK_E);
        editMenu.add(undo);
        undo.setAccelerator(KeyStroke.getKeyStroke("control Z"));
        editMenu.add(redo);
        redo.setAccelerator(KeyStroke.getKeyStroke("control shift Z"));
        editMenu.add(new JSeparator());
        editMenu.add(cut);
        cut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
        editMenu.add(copy);
        copy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
        editMenu.add(paste);
        paste.setAccelerator(KeyStroke.getKeyStroke("control V"));
        editMenu.add(selall);
        selall.setAccelerator(KeyStroke.getKeyStroke("control A"));
        menuBar.add(viewMenu);
        viewMenu.setMnemonic(KeyEvent.VK_V);
        viewMenu.add(full);
        full.setAccelerator(KeyStroke.getKeyStroke("F11"));
        viewMenu.add(actual);
        actual.setAccelerator(KeyStroke.getKeyStroke("control 0"));
        viewMenu.add(zin);
        zin.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS, ActionEvent.CTRL_MASK));
        viewMenu.add(zout);
        zout.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, ActionEvent.CTRL_MASK));
        viewMenu.add(sidebar);
        viewMenu.add(devtools);
        menuBar.add(winMenu);
        winMenu.setMnemonic(KeyEvent.VK_W);
        winMenu.add(mini);
        mini.setAccelerator(KeyStroke.getKeyStroke("control M"));
        menuBar.add(toolMenu);
        toolMenu.setMnemonic(KeyEvent.VK_T);
        toolMenu.add(plugins);
        menuBar.add(helpMenu);
        helpMenu.setMnemonic(KeyEvent.VK_H);
        helpMenu.add(about);
        helpMenu.add(support);
        helpMenu.add(shortcut);
        helpMenu.add(dataFolder);
        helpMenu.add(license);
        helpMenu.add(help);
        help.setAccelerator(KeyStroke.getKeyStroke("F1"));
    }

    /**
     * @param controller the controller to set
     */
    public void setController(Controller controller) {
        this.controller = controller;
    }
}