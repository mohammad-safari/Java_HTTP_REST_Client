
import Controller.Controller;
import Model.Model;
import View.Frame;

public class Main {
    // Pieces of MVC
    private static Model model;
    private static Controller controller;
    private static Frame view;

    public static void main(final String[] args) {
        // look and feel dialogue
        javax.swing.JOptionPane UI = new javax.swing.JOptionPane();
        {
            // set system look and feel
            if (javax.swing.JOptionPane.showConfirmDialog(UI, "System Look and Feel", "Look and Feel",
                    javax.swing.JOptionPane.YES_NO_OPTION) == javax.swing.JOptionPane.YES_OPTION)
                try {
                    javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
                } catch (final Exception e1) {
                    System.out.println("Couldnt Set System Look and Feel!");
                }
            // set other look and feel
            else
                try {
                    javax.swing.UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
                } catch (final Exception e2) {
                    System.out.println("Couldnt Set Other Look and Feel!");
                }
        }

        // assembling MVC peices
        controller = new Controller();
        model = new Model(controller, "DefaultWorkspace");
        controller.setModel(model);
        view = new Frame(controller);
        controller.setView(view);
    }
}