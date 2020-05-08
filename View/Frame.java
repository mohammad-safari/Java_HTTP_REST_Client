package View;

import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.GridBagConstraints;
import java.awt.BorderLayout;
import java.awt.event.*;
import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.*;

import Controller.*;
public class Frame extends JFrame {
    Controller controller;
    /**
     *
     */
    private static final long serialVersionUID = -2257792327102504528L;

    // components of main frame
    private JMenuBar menuBar = new JMenuBar();
    private JMenu appMenu = new JMenu("Application"), editMenu = new JMenu("Edit"), viewMenu = new JMenu("View"),
            winMenu = new JMenu("Window"), toolMenu = new JMenu("Tools"), helpMenu = new JMenu("Help");
    // frame menu items
    private JMenuItem pref = new JMenuItem("prefernce"), chlog = new JMenuItem("changelog"), happ = new JMenuItem("hide app"), hother = new JMenuItem("hide other"), quit = new JMenuItem("quit");
    private JMenuItem undo = new JMenuItem("undo"), redo = new JMenuItem("redo"), cut = new JMenuItem("cut"), copy = new JMenuItem("copy"), paste = new JMenuItem("paste"), selall = new JMenuItem("select all");
    private JMenuItem full = new JMenuItem("toggle fullscreen"), actual = new JMenuItem("actual size"), zin = new JMenuItem("zoom in"), zout = new JMenuItem("zout"), sidebar = new JMenuItem("toggle sidebar"), devtools = new JMenuItem("toggle devtools");
    private JMenuItem mini = new JMenuItem("minimize"), plugins = new JMenuItem("reload pluginss");
    private JMenuItem about = new JMenuItem("about"), support = new JMenuItem("contact support"), dataFolder = new JMenuItem("app data folder"), license = new JMenuItem("show open source licenses"), shortcut = new JMenuItem("keyboard shortcut"), help = new JMenuItem("app help");
    // panes and panels of main frame
    private JSplitPane right = new JSplitPane(), left = new JSplitPane();
    private JPanel rightPanel = new JPanel(), centerPanel = new JPanel(), sidePanel = new JPanel();

    // components of left panel(side bar)

    // components of center panel

    // components of right panel

    public Frame() {
        // frame initialization
        super("HTTP Request Debug App");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBounds(100, 100, 1000, 600);

        // adding components to main frame
        add(menuBar, BorderLayout.NORTH);
        add(left, BorderLayout.CENTER);

        GridBagConstraints c = new GridBagConstraints();

        // adding components to left panel
        
        // adding components to center panel

        // adding components to right panel

        // modifying components
        // menuBar
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
        viewMenu.add(actual);
        viewMenu.add(zin);
        viewMenu.add(zout);
        viewMenu.add(sidebar);
        viewMenu.add(devtools);
        menuBar.add(winMenu);
        winMenu.setMnemonic(KeyEvent.VK_W);
        winMenu.add(mini);
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
        // left panel(side bar)
        sidePanel.setBackground(new Color(.1f, .05f, .3f));
        // center panel
        centerPanel.setBackground(new Color(.3f, .2f, .4f));
        // right panel
        rightPanel.setBackground(new Color(.3f, .2f, .4f));

        // setting visible
        setVisible(true);
    }

    /**
     * @param controller the controller to set
     */
    public void setController(Controller controller) {
        this.controller = controller;
    } 
}