package View;

import java.awt.Color;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import Controller.Controller;

public class ResponsePanel extends JPanel {
    /**
     *
     */
    private static final long serialVersionUID = -7634778642783201670L;
    private JTabbedPane resPanel = new JTabbedPane();
    private JPanel prev = new JPanel(), cookie = new JPanel(), resheader = new JPanel(), timeline = new JPanel();
    private JLabel success, ping, size;
    private Controller controller;

    public ResponsePanel() {
        super();
        init();
    }

    private void init() {
        // right panel(response panel)
        setLayout(new GridBagLayout());
        setBackground(new Color(.3f, .2f, .4f));
    }

    /**
     * @param controller the controller to set
     */
    public void setController(Controller controller) {
        this.controller = controller;

    }

}