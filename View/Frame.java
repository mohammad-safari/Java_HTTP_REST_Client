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
import javax.swing.JFrame;
import javax.swing.JSplitPane;

import Controller.Controller;

public class Frame extends JFrame {
    /**
     *
     */
    private static final long serialVersionUID = -2257792327102504528L;
    Controller controller;

    // components of main frame
    SidePanel sidePanel = new SidePanel();
    // response panel
    private final MenuBar menuBar = new MenuBar();
    // request panel
    RequestPanel requestPanel = new RequestPanel();
    // side panel
    ResponsePanel responsePanel = new ResponsePanel();
    // panes and panels of main frame
    private final JSplitPane right = new JSplitPane(), left = new JSplitPane();

    public Frame() {
        // frame initialization
        super("HTTP Request Debug App");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000, 600);

        initGUI();

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                if (menuBar.hide.isSelected()) {
                    setExtendedState(JFrame.ICONIFIED);
                    setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                    /**
                     * //Check the SystemTray is supported
        if (!SystemTray.isSupported()) {
            System.out.println("SystemTray is not supported");
            return;
        }
        final PopupMenu popup = new PopupMenu();
        final TrayIcon trayIcon =
                new TrayIcon(createImage("images/bulb.gif", "tray icon"));
        final SystemTray tray = SystemTray.getSystemTray();
       
        // Create a pop-up menu components
        MenuItem aboutItem = new MenuItem("About");
        CheckboxMenuItem cb1 = new CheckboxMenuItem("Set auto size");
        CheckboxMenuItem cb2 = new CheckboxMenuItem("Set tooltip");
        Menu displayMenu = new Menu("Display");
        MenuItem errorItem = new MenuItem("Error");
        MenuItem warningItem = new MenuItem("Warning");
        MenuItem infoItem = new MenuItem("Info");
        MenuItem noneItem = new MenuItem("None");
        MenuItem exitItem = new MenuItem("Exit");
       
        //Add components to pop-up menu
        popup.add(aboutItem);
        popup.addSeparator();
        popup.add(cb1);
        popup.add(cb2);
        popup.addSeparator();
        popup.add(displayMenu);
        displayMenu.add(errorItem);
        displayMenu.add(warningItem);
        displayMenu.add(infoItem);
        displayMenu.add(noneItem);
        popup.add(exitItem);
       
        trayIcon.setPopupMenu(popup);
       
        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            System.out.println("TrayIcon could not be added.");
        }
                     */

                } else {
                    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                }
            }
        });

        // setting visible
        setVisible(true);
        // pack();
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
            setUndecorated(true);
            setVisible(true);
            revalidate();
            repaint();
        } else {
            toggleActual();
        }
    }

    public void toggleActual() {
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
        else
            sidePanel.setVisible(true);
        sidePanel.revalidate();
        sidePanel.repaint();
    }

    /**
     * @param controller the controller to set
     */
    public void setController(final Controller controller) {
        this.controller = controller;

    }
}