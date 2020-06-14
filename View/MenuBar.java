package View;

import java.awt.event.ActionEvent;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import Controller.Controller;

public class MenuBar extends JMenuBar {
        /**
         *
         */
        private static final long serialVersionUID = 293350073081096641L;

        Controller controller;

        // menus
        private JMenu appMenu = new JMenu("Application"), editMenu = new JMenu("Edit"), viewMenu = new JMenu("View"),
                        winMenu = new JMenu("Window"), toolMenu = new JMenu("Tools"), helpMenu = new JMenu("Help");
        // menu items
        // application
        private JMenuItem pref = new JMenuItem("prefernce"), chlog = new JMenuItem("changelog"),
                        happ = new JMenuItem("hide app"), hother = new JMenuItem("hide other"),
                        quit = new JMenuItem("quit");
        // edit
        private JMenuItem undo = new JMenuItem("undo"), redo = new JMenuItem("redo"), cut = new JMenuItem("cut"),
                        copy = new JMenuItem("copy"), paste = new JMenuItem("paste"),
                        selall = new JMenuItem("select all");
        // view and window
        private JMenuItem full = new JMenuItem("toggle fullscreen"), actual = new JMenuItem("actual size"),
                        zin = new JMenuItem("zoom in"), zout = new JMenuItem("zoom out"),
                        sidebar = new JMenuItem("toggle sidebar"), devtools = new JMenuItem("toggle devtools");
        // tool
        private JMenuItem mini = new JMenuItem("minimize"), plugins = new JMenuItem("reload pluginss");
        // help
        private JMenuItem about = new JMenuItem("about"), support = new JMenuItem("contact support"),
                        dataFolder = new JMenuItem("app data folder"),
                        license = new JMenuItem("show open source licenses"),
                        shortcut = new JMenuItem("keyboard shortcut"), help = new JMenuItem("app help");
        // preference
        private JCheckBox followRedirect = new JCheckBox("Follow Redirect");
        private JCheckBox hideOnExit = new JCheckBox("Hide on System Tray");

        public JCheckBox getHideOnExit() {
                return hideOnExit;
        }

        public JCheckBox getFollowRedirect() {
                return followRedirect;
        }

        public MenuBar(Controller controller) {
                setController(controller);
                setBorderPainted(false);
                add(appMenu);
                appMenu.setMnemonic(KeyEvent.VK_A);
                // appMenu
                appMenu.add(pref);
                pref.addActionListener(e -> setPreference());

                appMenu.add(chlog);

                appMenu.add(new JSeparator());

                appMenu.add(happ);
                happ.setAccelerator(KeyStroke.getKeyStroke("H"));

                appMenu.add(hother);
                hother.setAccelerator(KeyStroke.getKeyStroke("alt H"));

                appMenu.add(new JSeparator());

                appMenu.add(quit);
                quit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.CTRL_MASK));
                quit.addActionListener(e -> System.exit(0));

                add(editMenu);
                editMenu.setMnemonic(KeyEvent.VK_E);
                // editMenu
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

                add(viewMenu);
                viewMenu.setMnemonic(KeyEvent.VK_V);
                // viewMenu
                viewMenu.add(full);
                full.setAccelerator(KeyStroke.getKeyStroke("F11"));
                full.addActionListener(e -> (((Frame) (SwingUtilities.getRoot(this)))).toggleFullScreen());

                viewMenu.add(actual);
                actual.setAccelerator(KeyStroke.getKeyStroke("control 0"));
                actual.addActionListener(e -> (((Frame) (SwingUtilities.getRoot(this)))).toggleActual());

                viewMenu.add(zin);
                zin.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS, ActionEvent.CTRL_MASK));

                viewMenu.add(zout);
                zout.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, ActionEvent.CTRL_MASK));

                viewMenu.add(sidebar);
                sidebar.addActionListener(e -> ((Frame) (SwingUtilities.getRoot(this))).toggleSidePanel());

                viewMenu.add(devtools);

                add(winMenu);
                winMenu.setMnemonic(KeyEvent.VK_W);
                // winMenu
                winMenu.add(mini);

                mini.setAccelerator(KeyStroke.getKeyStroke("control M"));

                add(toolMenu);
                toolMenu.setMnemonic(KeyEvent.VK_T);
                // toolMenu
                toolMenu.add(plugins);

                add(helpMenu);
                helpMenu.setMnemonic(KeyEvent.VK_H);
                // helpMenu
                helpMenu.add(about);
                about.addActionListener(e -> new About());

                helpMenu.add(support);

                helpMenu.add(shortcut);

                helpMenu.add(dataFolder);

                helpMenu.add(license);

                helpMenu.add(help);
                help.setAccelerator(KeyStroke.getKeyStroke("F1"));

                

        }

        private void setPreference() {
                JFrame pref = new JFrame("Preferences");
                // pref.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                pref.setBounds(200, 200, 300, 200);
                JPanel panel = new JPanel();
                panel.add(followRedirect);
                panel.add(hideOnExit);
                pref.add(panel);

                pref.setVisible(true);
        }

        public void setController(Controller controller) {
                this.controller = controller;

        }

}