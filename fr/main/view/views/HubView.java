package fr.main.view.views;

import java.awt.*;
import javax.swing.*;
import fr.main.view.controllers.HubController;

public class HubView extends View {

  protected HubController controller;

  private class Slot extends JPanel {

    JLabel name;

    public Slot () {
      setBorder(BorderFactory.createLineBorder(Color.black));
      name = new JLabel("Empty slot");

      add(name);
    }

  }

  protected Slot[] slots;
  protected JLabel header;
  protected JButton ready;

  public HubView(HubController controller) {
    super (controller);
    this.controller = controller;
    setLayout(new GridLayout(6, 1, 0, 20));
    setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

    header = new JLabel("Joining game on address " + controller.getAddress());
    ready  = new JButton("Ready to play");

    slots = new Slot[4];
    for (int i = 0; i < slots.length; i++)
      slots[i] = new Slot();

    add (header);
    for (int i = 0; i < slots.length; i++)
      add(slots[i]);
    add (ready);

  }

  public static class Host extends HubView {

    public Host (HubController controller) {
      super(controller);
      header.setText("Hosting game on addr " + controller.getAddress());

      ready.setText("Start");
      ready.setEnabled(false);
    }


  }

}
