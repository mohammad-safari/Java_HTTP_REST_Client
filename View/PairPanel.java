package View;

import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;

/**
 * a class that implements panels for pair key and value using panels
 * 
 * @author M.safari
 */

public class PairPanel extends JPanel {
    /**
     *
     */
    private static final long serialVersionUID = 4072187796899221624L;
    private JTextField key = new JTextField(), value = new JTextField();
    private JCheckBox selectButton = new JCheckBox("");
    private JButton deleteButton = new JButton(new ImageIcon("./Resources/trash.jpg"));

    public PairPanel() {
        super();
        GroupLayout glo = new GroupLayout(this);
        glo.setAutoCreateGaps(true);
        glo.setAutoCreateContainerGaps(true);
        setLayout(glo);
        glo.setHorizontalGroup(glo.createSequentialGroup().addComponent(key, 85, 85, 100)
                .addComponent(value, 85, 85, 100).addComponent(selectButton).addComponent(deleteButton, 15, 15, 15));
        glo.setVerticalGroup(glo.createParallelGroup().addComponent(key, 20, 20, 20).addComponent(value, 20, 20, 20)
                .addComponent(selectButton, 20, 20, 20).addComponent(deleteButton, 20, 20, 20));
        setOpaque(false);
        deleteButton.setBorderPainted(false);
        key.setPreferredSize(new Dimension(85, 25));
        value.setPreferredSize(new Dimension(85, 25));
        selectButton.setSelected(true);
        deleteButton.addActionListener(e -> {
            Container parent = getParent();
            try {
                parent.remove(this);
                parent.revalidate();
                parent.repaint();
            } catch (Exception ex) {
            }
        });
        selectButton.addChangeListener(e -> {
            Container parent = getParent();
            if (selectButton.isSelected()) {
                key.setEnabled(true);
                value.setEnabled(true);
            } else {
                key.setEnabled(false);
                value.setEnabled(false);
            }
            parent.revalidate();
            parent.repaint();
        });
    }

    public boolean isSelected() {
        return (selectButton.isSelected());
    }
}
