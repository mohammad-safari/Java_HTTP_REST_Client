package View;

import java.awt.PopupMenu;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.CheckboxMenuItem;
import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.event.*;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.AWTException;
import java.awt.Component;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JSplitPane;

import Controller.Controller;

public class Frame extends JFrame {
    /**
     *
     */
    private static final long serialVersionUID = -2257792327102504528L;
    Controller controller;

    // components of main frame
    SidePanel sidePanel;
    // response panel
    private final MenuBar menuBar;
    // request panel
    RequestPanel requestPanel;
    // side panel
    ResponsePanel responsePanel;
    // panes and panels of main frame
    private final JSplitPane right = new JSplitPane(), left = new JSplitPane();

    public Frame(Controller controller) {
        // frame initialization
        super("HTTP Client App");
        setController(controller);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBounds(100, 100, 1200, 600);

        sidePanel = new SidePanel(controller);
        menuBar = new MenuBar(controller);
        requestPanel = new RequestPanel(controller);
        responsePanel = new ResponsePanel(controller);
        initGUI();

        /**
         * configuring close operation for window when window event happens
         */
        WindowAdapter windowAdapter = new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                // check whether hideOnExit is selected
                if (menuBar.getHideOnExit().isSelected()) {
                    // Check whether the SystemTray is supported
                    if (!SystemTray.isSupported()) {
                        JOptionPane message = new JOptionPane();
                        JOptionPane.showConfirmDialog(message, "SystemTray is not supported", "Hide on Exit",
                                JOptionPane.OK_OPTION);
                        menuBar.getHideOnExit().setSelected(false);
                        return;
                    }

                    // setting Extended State
                    setExtendedState(JFrame.ICONIFIED);
                    setVisible(false);
                    // setting close operation
                    setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

                    final SystemTray tray = SystemTray.getSystemTray();
                    final PopupMenu popup = new PopupMenu();
                    final TrayIcon trayIcon = new TrayIcon(getToolkit().getImage("./Resources/cup.png"));

                    // Create a pop-up menu components
                    MenuItem aboutItem = new MenuItem("About");
                    // showing about frame
                    aboutItem.addActionListener(e -> new About());
                    CheckboxMenuItem size = new CheckboxMenuItem("Set auto size");
                    CheckboxMenuItem tooltip = new CheckboxMenuItem("Set tooltip");
                    MenuItem displayItem = new MenuItem("Display");
                    // displaying app gui
                    displayItem.addActionListener(e -> {
                        setVisible(true);
                        setExtendedState(JFrame.NORMAL);
                    });
                    // exitting the app
                    MenuItem exitItem = new MenuItem("Exit");
                    exitItem.addActionListener(e -> {
                        System.exit(0);
                    });

                    // adding pop-up menu for tray icon
                    trayIcon.setPopupMenu(popup);

                    // Add components to pop-up menu
                    popup.add(aboutItem);
                    popup.addSeparator();
                    popup.add(size);
                    popup.add(tooltip);
                    popup.addSeparator();
                    popup.add(displayItem);
                    popup.add(exitItem);

                    try {
                        tray.add(trayIcon);
                    } catch (AWTException e) {
                        JOptionPane message = new JOptionPane();
                        JOptionPane.showConfirmDialog(message, "TrayIcon could not be added", "Hide on Exit",
                                JOptionPane.OK_OPTION);
                    }

                } else {
                    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                }
            }
        };

        addWindowListener(windowAdapter);

        // setting visible
        setVisible(true);
    }

    public void initGUI() {
        // adding components
        // adding components to main frame
        add(menuBar, BorderLayout.NORTH);
        add(left, BorderLayout.CENTER);

        // left pane
        left.setBorder(null);
        left.setOpaque(true);
        left.setDividerSize(4);
        left.setDividerLocation(300);
        left.setRightComponent(right);
        left.setLeftComponent(sidePanel);
        left.setBackground(new Color(.3f, .2f, .4f));

        // right pane
        right.setBorder(null);
        right.setOpaque(true);
        right.setDividerSize(4);
        right.setDividerLocation(375);
        right.setLeftComponent(requestPanel);
        right.setRightComponent(responsePanel);
    }

    public void toggleFullScreen() {
        if (getExtendedState() != JFrame.MAXIMIZED_BOTH) {
            setExtendedState(JFrame.MAXIMIZED_BOTH);
            // setUndecorated(true);
            revalidate();
            repaint();
        } else {
            toggleActual();
            // setUndecorated(true);
        }
    }

    public void toggleActual() {
        if (getExtendedState() == JFrame.NORMAL)
            setSize(1200, 600);
        setExtendedState(JFrame.NORMAL);
        revalidate();
        repaint();
    }

    /**
     * toggles sidebar(panel)
     */
    public void toggleSidePanel() {
        if (sidePanel.isVisible())
            sidePanel.setVisible(false);
        else {
            sidePanel.setVisible(true);
            left.setDividerLocation(250);
        }
        sidePanel.revalidate();
        sidePanel.repaint();
    }

    /**
     * @param controller the controller to set
     */
    public void setController(final Controller controller) {
        this.controller = controller;

    }

    public RequestPanel getRequestPanel() {
        return requestPanel;
    }

    public ResponsePanel getResponsePanel() {
        return responsePanel;
    }

    public SidePanel getSidePanel() {
        return sidePanel;
    }

    public MenuBar getMyMenuBar() {
        return menuBar;
    }
}