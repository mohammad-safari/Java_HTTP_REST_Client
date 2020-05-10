package View;

import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JTree;

import Controller.Controller;

public class SidePanel extends JPanel {
    private MenuButton workspaceBox = new MenuButton("Workspace"), envBox = new MenuButton("Environment");
    private JMenuItem workspaceSetting = new JMenuItem("workspace setting"), addWorkspace = new JMenu("add workspace");
    private JButton cookiesButton = new JButton("Cookies"), addRequestButton = new JButton("Add");
    private JTextField reqFilter = new JTextField("Filter");
    private JTree reqTree = new JTree();
    private Controller controller;

    public SidePanel() {
        super();
        init();
    }

    private void init() {
        // left panel(side bar)
        GridBagConstraints c = new GridBagConstraints();
        setLayout(new GridBagLayout());
        c.insets = new Insets(10, 10, 10, 10);
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.NORTHWEST;
        c.gridx = 0;
        c.gridy = 0;
        c.gridheight = 1;
        c.gridwidth = 5;
        c.weightx = 5;
        c.weighty = 0.25;
        add(workspaceBox, c);
        workspaceBox.add(workspaceSetting);
        c.gridx = 0;
        c.gridy = 1;
        c.gridheight = 1;
        c.gridwidth = 3;
        c.weightx = 0.5;
        c.weighty = 0.25;
        add(envBox, c);
        c.gridx = 3;
        c.gridy = 1;
        c.gridheight = 1;
        c.gridwidth = 2;
        c.weightx = 0.25;
        add(cookiesButton, c);
        c.gridx = 0;
        c.gridy = 2;
        c.gridheight = 1;
        c.gridwidth = 4;
        c.weightx = 1;
        c.weighty = 0.25;
        add(reqFilter, c);
        c.gridx = 4;
        c.gridy = 2;
        c.gridheight = 1;
        c.gridwidth = 1;
        c.weightx = 0.25;
        add(addRequestButton, c);
        c.gridx = 0;
        c.gridy = 3;
        c.gridheight = GridBagConstraints.REMAINDER;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.weightx = 5;
        c.weighty = 5;
        add(reqTree, c);
        setBackground(new Color(.1f, .05f, .3f));
    }
    /**
     * @param controller the controller to set
     */
    public void setController(Controller controller) {
        this.controller = controller;
        
    }

}