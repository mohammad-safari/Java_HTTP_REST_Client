package View;

import java.awt.Color;

import javax.swing.JMenu;
import javax.swing.JTree;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JMenuItem;
import javax.swing.JTextField;
import javax.swing.JTabbedPane;

import Controller.Controller;

public class SidePanel extends JPanel {
    private MenuButton workspaceButton = new MenuButton("Workspace"), environmentButton = new MenuButton("Environment"),
            addButton = new MenuButton("\u2795 ");
    private JMenuItem workspaceSetting = new JMenuItem("workspace setting"),
            addWorkspace = new JMenuItem("create workspace");
    private JMenuItem environmentManagement = new JMenuItem("environment management"),
            addEnvironment = new JMenuItem("add environment");
    private JMenuItem addRequest = new JMenuItem("request"), addFolder = new JMenuItem("folder");
    private JButton cookiesButton = new JButton("Cookies");
    private JTextField reqFilter = new JTextField("Filter");
    private JTree reqTree = new JTree();
    private Controller controller;

    public SidePanel() {
        super();
        init();
    }

    private void init() {
        // left panel(side bar)
        GroupLayout glo = new GroupLayout(this);
        glo.setAutoCreateGaps(true);
        glo.setAutoCreateContainerGaps(true);
        setLayout(glo);
        glo.setHorizontalGroup(glo.createParallelGroup().addComponent(workspaceButton, 100, 200, 1500)
                .addGroup(glo.createSequentialGroup().addComponent(environmentButton, 75, 150, 1200)
                        .addComponent(cookiesButton, 25, 50, 400))
                .addGroup(glo.createSequentialGroup().addComponent(reqFilter, 85, 180, 1400).addComponent(addButton, 55,
                        55, 55))
                .addComponent(reqTree, 100, 200, 1500));
        glo.setVerticalGroup(glo.createSequentialGroup().addComponent(workspaceButton, 45, 45, 45)
                .addGap(15)
                .addGroup(glo.createParallelGroup().addComponent(environmentButton, 30, 30, 30).addComponent(cookiesButton, 30, 30, 30))
                .addGroup(glo.createParallelGroup().addComponent(reqFilter, 25, 25, 25).addComponent(addButton, 25, 25, 25))
                .addGap(15)
                .addComponent(reqTree, 200, 200, 1000));
        setBackground(new Color(.1f, .05f, .3f));
        addButton.add(addRequest);
        addButton.add(addFolder);
        environmentButton.add(addEnvironment);
        environmentButton.add(environmentManagement);
        workspaceButton.add(workspaceSetting);
        workspaceButton.add(addWorkspace);
        reqTree.setOpaque(true);
        reqTree.setRootVisible(false);
        reqTree.putClientProperty("JTree.lineStyle", "Horizontal");
        reqTree.setBorder(BorderFactory.createEtchedBorder());
        reqTree.setBackground(new Color(.45f, .5f, .9f));
    }

    /**
     * @param controller the controller to set
     */
    public void setController(Controller controller) {
        this.controller = controller;

    }

}