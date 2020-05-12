package View;

import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.JToggleButton;

public class MenuButton extends JToggleButton {
    /**
     *
     */
    private static final long serialVersionUID = 3635006151633276306L;
    protected Boolean menued = true;
    protected String origin;
    protected JPopupMenu menu;
    private int arrow = 1;

    public MenuButton(String text) {
        super(text);
        origin = text;
        menu = new JPopupMenu(text);
        super.addActionListener(e -> {
            if (menued) {
                menu.show(this, (getWidth() / 2), getHeight() + 1);
                setSelected(false);
            }
        });
    }

    public MenuButton(String text, int x, int y) {
        super(text);
        menu = new JPopupMenu(text);
        super.addActionListener(e -> menu.show(this, x, y));
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (arrow == 1)
            g.drawString("\u2b9f", getWidth() - 20, (int) (getHeight() / 2) + 1);
        else if (arrow == 2)
            g.drawString("\u2b9b", getWidth() - 20, (int) (getHeight() / 2) + 1);

    }

    /**
     * 
     * @param item to add to menu
     */
    public void add(JMenuItem item) {
        arrow = 1;
        menu.add(item);
    }

    public void add(JSeparator sep) {
        menu.add(sep);
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

    /**
     * @param menued the menued to set
     */
    public void setMenued(Boolean menued) {
        this.menued = menued;
    }

    /**
     * @param arrow the arrow to set
     */
    public void setArrow(int arrow) {
        this.arrow = arrow;
    }

}