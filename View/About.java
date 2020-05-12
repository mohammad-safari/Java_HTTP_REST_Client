package View;

import java.awt.Color;
import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class About extends JFrame {

    private JTextPane text = new JTextPane();
    
    public About() {
        super("About");
        text.setText("");
        JScrollPane pane = new JScrollPane();
        StyledDocument doc = text.getStyledDocument();
        SimpleAttributeSet attr = new SimpleAttributeSet();
        StyleConstants.setBold(attr, true);
        StyleConstants.setForeground(attr, Color.red);
        try {
            doc.insertString(doc.getLength(), "Mohammad Safari", attr);
        } catch (BadLocationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        StyleConstants.setBold(attr, true);
        StyleConstants.setForeground(attr, Color.MAGENTA);
        StyleConstants.setItalic(attr, true);
        try {
            doc.insertString(doc.getLength(), "\n9831138", attr);
        } catch (BadLocationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        StyleConstants.setBold(attr, false);
        StyleConstants.setUnderline(attr, true);
        StyleConstants.setForeground(attr, Color.blue);
        try {
            doc.insertString(doc.getLength(), "\nGit Hub: https://github.com/Mohammad-Safari/", attr);
        } catch (BadLocationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        text.setEditable(false);
        text.setBackground(Color.LIGHT_GRAY);
        pane.add(text);
        pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        add(text, BorderLayout.CENTER);
        setBounds(300, 300, 250, 200);
        setVisible(true);
    }
}
