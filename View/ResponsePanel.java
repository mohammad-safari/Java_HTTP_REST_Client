package View;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.GroupLayout.Alignment;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.StyledDocument;
import javax.swing.text.StyledEditorKit;
import javax.swing.text.html.HTMLEditorKit;

import Controller.Controller;

public class ResponsePanel extends JPanel {
    /**
     *
     */
    private static final long serialVersionUID = -7634778642783201670L;
    private TabbedPanel resPanel = new TabbedPanel();
    private MenuButton historyButton = new MenuButton("History");
    private JTextPane previewTab = new JTextPane(), responseHeader = new JTextPane();
    private JPanel prev = new JPanel(), cookie = new JPanel(), header = new JPanel(), timeline = new JPanel();
    private JLabel state = new JLabel(), ping = new JLabel(), size = new JLabel();

    private JMenuItem rawText = new JMenuItem("Raw Style"), HTMLStyle = new JMenuItem("HTML Style");

    private Controller controller;

    public ResponsePanel(Controller controller) {
        super();
        setController(controller);
        init();
    }

    private void init() {
        // right panel(response panel)
        GroupLayout glo = new GroupLayout(this);
        glo.setAutoCreateGaps(true);
        glo.setAutoCreateContainerGaps(true);
        setLayout(glo);
        glo.setHorizontalGroup(glo.createParallelGroup()
                .addGroup(glo.createParallelGroup()
                        .addGroup(glo.createSequentialGroup().addComponent(state).addComponent(ping).addComponent(size)
                                .addGap(10, 75, 600).addComponent(historyButton, 75, 120, 150)))
                .addComponent(resPanel));
        glo.setVerticalGroup(glo.createSequentialGroup().addGroup(glo.createParallelGroup().addComponent(state)
                .addComponent(ping).addComponent(size).addComponent(historyButton, 40, 40, 40)).addComponent(resPanel));
        setBackground(new Color(.3f, .2f, .4f));
        historyButton.setPreferredSize(new Dimension(50, 30));
        state.setText("state");
        state.setOpaque(true);
        state.setBackground(Color.WHITE);
        state.setBorder(new EmptyBorder(10, 10, 10, 10));
        ping.setText("000 ms");
        ping.setOpaque(true);
        ping.setBackground(Color.WHITE);
        ping.setBorder(new EmptyBorder(10, 10, 10, 10));
        size.setText("000 B");
        size.setOpaque(true);
        size.setBackground(Color.WHITE);
        size.setBorder(new EmptyBorder(10, 10, 10, 10));
        resPanel.addTab("Preview", previewTab);
        resPanel.addTab("Header", header);
        resPanel.addTab("Cookie", cookie);
        resPanel.addTab("TimeLine", timeline);

        resPanel.getButton("Preview").setMenued(true);
        
        resPanel.getButton("Preview").add(rawText);
        resPanel.getButton("Preview").add(HTMLStyle);
        rawText.addActionListener(e -> setRawText(previewTab.getText()));
        HTMLStyle.addActionListener(e -> setHTMLText(previewTab.getText()));
        setHTMLText(previewTab.getText());
        previewTab.setText(
                "<p></br></br></br>this is a <b>html</b> text<p></br></br><a href=\"https://www.google.com\">Here!</a>");
        responseHeader.setText("nothing yet");
        header.add(responseHeader);

    }

    /**
     * @param controller the controller to set
     */
    public void setController(Controller controller) {
        this.controller = controller;

    }

    public void setRawText(String text) {
        previewTab.setEditorKit(new StyledEditorKit());
        previewTab.setText(text);
        HTMLStyle.setEnabled(true);
        rawText.setEnabled(false);
        revalidate();
        repaint();
    }

    public void setHTMLText(String text) {
        previewTab.setEditorKit(new HTMLEditorKit());
        previewTab.setText(text);
        HTMLStyle.setEnabled(false);
        rawText.setEnabled(true);
        revalidate();
        repaint();
    }
    public JTextPane getResponseHeader() {
        return responseHeader;
    }
}