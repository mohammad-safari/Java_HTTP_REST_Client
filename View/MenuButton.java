package View;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JToggleButton;

public class MenuButton extends JToggleButton {
    /**
     *
     */
    private static final long serialVersionUID = 3635006151633276306L;
    private JPopupMenu menu;

    public MenuButton(String label) {
        super(label);
        menu = new JPopupMenu(label);
        super.addActionListener(e -> menu.show(this, getWidth(), getHeight()));
    }

    public MenuButton(String label, int x, int y) {
        super(label);
        menu = new JPopupMenu(label);
        super.addActionListener(e -> menu.show(this, x, y));
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

}