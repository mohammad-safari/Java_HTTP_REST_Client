package View;

import java.util.HashMap;
import java.awt.event.ActionEvent;
import java.awt.event.*;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Container;

import javax.swing.JPanel;
import javax.swing.JToggleButton;

public class TabbedPanel extends JPanel {
    /**
     *
     */
    private static final long serialVersionUID = 1605326250837491085L;
    private CardLayout layout = new CardLayout();
    private HashMap<MenuButton, Container> tabs = new HashMap<MenuButton, Container>();
    private JPanel buttonPanel = new JPanel(), panel = new JPanel();
    private GridBagConstraints c = new GridBagConstraints();
    private ActionListener TabButtonHandler = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            MenuButton btn = ((MenuButton) e.getSource());
            // if a tab button clicked more than once
            if (!btn.isSelected()) {
                if (btn.menued)
                    btn.menu.show(btn, (btn.getWidth() / 2), btn.getHeight() + 5);
            } else {
                layout.show(panel, btn.origin);
            }
            AllButtonPressedOut();
            btn.setSelected(true);
    }
    };

    public TabbedPanel() {
        super(new BorderLayout());
        panel.setLayout(layout);
        buttonPanel.setLayout(new GridBagLayout());
        add(buttonPanel, BorderLayout.NORTH);
        buttonPanel.setPreferredSize(new Dimension(buttonPanel.getWidth(), 40));
        add(panel, BorderLayout.CENTER);
        c.anchor = GridBagConstraints.NORTHWEST;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.weighty = 1;
    }

    public MenuButton addTab(String text, Container tab) {
        MenuButton btn = new MenuButton(text);
        btn.setArrow(0);
        btn.setMenued(false);
        btn.removeActionListener(btn.getActionListeners()[0]);
        btn.addActionListener(TabButtonHandler);
        c.gridx = tabs.size() % 5;
        c.gridy = (int) (tabs.size() / 5);
        buttonPanel.add(btn, c);
        panel.add(tab, text);
        tabs.put(btn, tab);
        if(tabs.size() == 1)
        btn.setSelected(true);
        return btn;
    }

    public void AllButtonPressedOut() {
        for (JToggleButton btn : tabs.keySet()) {
            btn.setSelected(false);
        }
    }

    public MenuButton getButton(String text) {
        for (MenuButton btn : tabs.keySet()) {
            if (btn.origin == text)
                return btn;
        }
        return (MenuButton) tabs.keySet().toArray()[0];
    }

    public Container getContainer(String text) {
        return tabs.get(getButton(text));
    }

}