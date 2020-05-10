package View;

import java.awt.Color;
import java.awt.BorderLayout;

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
    private MenuBar menuBar = new MenuBar();
    // request panel
    RequestPanel requestPanel = new RequestPanel();
    // side panel
    ResponsePanel responsePanel = new ResponsePanel();
    // panes and panels of main frame
    private JSplitPane right = new JSplitPane(), left = new JSplitPane();

    public Frame() {
        // frame initialization
        super("HTTP Request Debug App");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBounds(100, 100, 1000, 600);

        // adding components
        // adding components to main frame
        add(menuBar, BorderLayout.NORTH);
        add(left, BorderLayout.CENTER);

        // left pane
        left.setBorder(null);
        left.setOpaque(true);
        left.setDividerSize(1);
        left.setDividerLocation(300);
        left.setRightComponent(right);
        left.setLeftComponent(sidePanel);
        left.setBackground(new Color(.3f, .2f, .4f));

        // right pane
        right.setBorder(null);
        right.setOpaque(true);
        right.setDividerSize(1);
        right.setDividerLocation(375);
        right.setLeftComponent(requestPanel);
        right.setRightComponent(responsePanel);

        // setting visible
        setVisible(true);
        // pack();
    }

    /**
     * @param controller the controller to set
     */
    public void setController(Controller controller) {
        this.controller = controller;

    }
}