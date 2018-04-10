package fr.main.view.views;

import java.awt.*;
import javax.swing.*;
import fr.main.view.controllers.*;

public class ConnectionView extends View {

  /**
	 * Add Connection UID
	 */
	private static final long serialVersionUID = -5016640791074575870L;
protected ConnectionController controller;
  protected JLabel header;
  protected InputPanel addr, port;
  protected JButton submit;

  protected class InputPanel extends JPanel {

    /**
	 * 
	 */
	private static final long serialVersionUID = 6468276623404782198L;
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
