import javax.swing.JOptionPane;
import javax.swing.UIManager;

import View.Frame;

public class Main {
    public static void main(final String[] args) {
        JOptionPane UI = new JOptionPane();
        if (JOptionPane.showConfirmDialog(UI, "windows look and feel", "look and feel",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
            // set look and feel
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (final Exception e1) {

                try {
                    UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                    System.out.println("Couldnt Set System Look and Feel!");
                } catch (final Exception e2) {
                    System.out.println("Couldnt Set NATIVE Look and Feel!");
                }
                
            }
        // assembling MVC parts
        final Frame view = new Frame();

    }
}