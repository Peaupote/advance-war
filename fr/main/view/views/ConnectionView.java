package fr.main.view.views;

import java.awt.*;
import javax.swing.*;
import fr.main.view.controllers.*;

@SuppressWarnings("serial")
public class ConnectionView extends View {

    protected ConnectionController controller;
    protected JLabel header;
    protected InputPanel addr, port;
    protected JButton submit;

    @SuppressWarnings("serial")
    protected class InputPanel extends JPanel {

    final JTextField input;

        public InputPanel (String text) {
            input = new JTextField(50);

            add(new JLabel (text));
            add(input);
        }

    }

    public ConnectionView (ConnectionController controller) {
        super(controller);
        this.controller = controller;

        header = new JLabel("Join a game");
        addr   = new InputPanel("Address: ");
        port   = new InputPanel("Port: ");
        submit = new JButton ("Connect");

        setLayout(new GridLayout(4, 1));

        add(header);
        add(addr);
        add(port);
        add(submit);

        submit.addActionListener(new ConnectionController.Submit(addr.input, port.input));
    }
}
