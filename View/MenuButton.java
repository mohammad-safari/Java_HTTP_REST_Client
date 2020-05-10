package View;

import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JToggleButton;

public class MenuButton extends JToggleButton {
    /**
     *
     */
    private static final long serialVersionUID = 3635006151633276306L;
    private JPopupMenu menu;
    private String origin;

    public MenuButton(String label) {
        super(label);
        origin = label;
        menu = new JPopupMenu(label);
        super.addActionListener(e -> menu.show(this, (getWidth() / 2), getHeight() + 1));
    }

    public MenuButton(String label, int x, int y) {
        super(label);
        menu = new JPopupMenu(label);
        super.addActionListener(e -> menu.show(this, x, y));
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.drawString("\u2b9f", getWidth() - 20, (int) (getHeight() / 2) + 1);
    }

    /**
     * 
     * @param item to add to menu
     */
    public void add(JMenuItem item) {
        menu.add(item);
    }

    /**
     * 
     * @param item to remove from menu
     */
    public void remove(JMenuItem item) {
        menu.remove(item);
    }

    /**
     * @return the menu
     */
    public JPopupMenu getMenu() {
        return menu;
    }

    /**
     * setting default or primary text
     */
    public void setText() {
        setText(origin);
    }

    @Override
    public void setText(String text) {
        super.setText(text);
    }

}