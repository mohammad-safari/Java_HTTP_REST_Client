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
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;

/**
 * a class that implements more dynaminc tabbedpanel not pane with
 * buttons(toggle) as tabs with the help of menu button
 * 
 * @author M.Safari
 */

public class TabbedPanel extends JPanel {
    /**
     *
     */
    private static final long serialVersionUID = 1605326250837491085L;
    private CardLayout layout = new CardLayout(); // layput manager which manages the scrollpanes
    private HashMap<MenuButton, Container> tabs = new HashMap<MenuButton, Container>(); // collection of scrollpanes
    private JPanel buttonPanel = new JPanel(), panel = new JPanel(); // two panel one for tab buttons and one for given
                                                                     // panel with card layout to show scrollpanes
    private GridBagConstraints c = new GridBagConstraints(); // layout manager constraint for two recent panels
    private ActionListener TabButtonHandler = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            MenuButton btn = ((MenuButton) e.getSource());
            // if a tab button clicked when selected open menu if owns one
            if (!btn.isSelected()) {
                if (btn.menued)
                    btn.menu.show(btn, (btn.getWidth() / 2), btn.getHeight() + 5);
            } else {
                //
                layout.show(panel, btn.origin);
            }
            AllButtonPressedOut();
            btn.setSelected(true);
        }
    };

    /**
     * prepares the two inside panels for this panel
     */
    public TabbedPanel() {
        super(new BorderLayout());
        // layouting and adding button panel
        buttonPanel.setLayout(new GridBagLayout());
        add(buttonPanel, BorderLayout.NORTH);
        buttonPanel.setPreferredSize(new Dimension(buttonPanel.getWidth(), 40));
        // layouting and adding the show panel
        panel.setLayout(layout);
        add(panel, BorderLayout.CENTER);
        c.anchor = GridBagConstraints.NORTHWEST;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.weighty = 1;
    }

    /**
     * creating scrollpane and adding given panel to that
     * 
     * @param text tab title
     * @param tab  the given panel
     * @return the button to show the panel
     */
    public MenuButton addTab(String text, Container tab) {
        // preparing button
        MenuButton btn = new MenuButton(text);
        btn.setArrow(0);
        btn.setMenued(false);
        btn.removeActionListener(btn.getActionListeners()[0]);
        btn.addActionListener(TabButtonHandler);
        c.gridx = tabs.size() % 5;
        c.gridy = (int) (tabs.size() / 5);
        // preparing container jscrollpane
        JScrollPane pane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        pane.setViewportView(tab);
        // affecting the button panel and show panel
        buttonPanel.add(btn, c);
        panel.add(pane, text);
        tabs.put(btn, tab);
        if (tabs.size() == 1)
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