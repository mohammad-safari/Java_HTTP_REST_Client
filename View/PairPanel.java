package View;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.Dimension;
import java.awt.FlowLayout;

/**
 * a class that implements panels for pair key and value using panels
 * 
 * @author M.safari
 */

public class PairPanel extends JPanel {
    private JTextField key = new JTextField(), value = new JTextField();
    private JCheckBox selectButton = new JCheckBox("");
    private JButton deleteButton = new JButton(new ImageIcon(".\\src\\trash.jpg"));

    public PairPanel() {
        super(new FlowLayout(FlowLayout.CENTER, 10, 10));
        add(key);
        add(value);
        add(selectButton);
        add(deleteButton);
        setOpaque(false);
        deleteButton.setBorderPainted(false);
        key.setPreferredSize(new Dimension(85, 25));
        value.setPreferredSize(new Dimension(85, 25));
        selectButton.setSelected(true);
        deleteButton.addActionListener(e -> {
            try {
                getParent().remove(this);
                getParent().revalidate();
                getParent().repaint();
            } catch (Exception ex) {
            }
        });
        selectButton.addChangeListener(e -> {
            if (selectButton.isSelected()) {
                key.setEnabled(true);
                value.setEnabled(true);
            } else {
                key.setEnabled(false);
                value.setEnabled(false);
            }
            getParent().revalidate();
            getParent().repaint();
        });
    }
}
