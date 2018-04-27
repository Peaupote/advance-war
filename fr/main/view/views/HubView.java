package fr.main.view.views;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import fr.main.view.controllers.HubController;

@SuppressWarnings("serial")
public class HubView extends View {

    protected HubController controller;

    @SuppressWarnings("serial")
    private class Slot extends JPanel {

        JLabel label;

        public Slot () {
            setBorder(BorderFactory.createLineBorder(Color.black));
            label = new JLabel("Empty slot");

            add(label);
        }

        public void set (fr.main.network.Slot slot) {
            if (slot == null) label.setText("Empty slot");
            else {
                if (slot.ready) label.setForeground(Color.green);
                else label.setForeground(Color.red);
                label.setText("Player " + (slot.id + 1) + ": " + slot.name);
            }
        }

    }

    @SuppressWarnings("serial")
    private class MutableSlot extends Slot {

        private JTextField name;
        
        public MutableSlot () {
            super();
            HubView.this.clientSlot = this;
            label.setText("Player name: ");
            name = new JTextField(30);
            name.addActionListener(controller.send);

            add(name);
        }

    }

    protected Slot[] slots;
    protected MutableSlot clientSlot;
    protected JLabel header;
    protected JButton ready;

    public HubView(HubController controller) {
        super (controller);
        this.controller = controller;
        setLayout(new GridLayout(6, 1, 0, 20));
        setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        header = new JLabel("Joining game on address " + controller.getAddress());
        ready  = new JButton("Ready to play");
        ready.addActionListener(controller.readyAction);
        ready.addActionListener(controller.send);

        slots = new Slot[4];
        for (int i = 0; i < slots.length; i++)
            if (i == controller.getID()) slots[i] = new MutableSlot();
            else slots[i] = new Slot();

        add (header);
        for (int i = 0; i < slots.length; i++)
            add(slots[i]);
        add (ready);

    }

    @SuppressWarnings("serial")
    public static class Host extends HubView {

        public Host (HubController controller) {
            super(controller);
            header.setText("Hosting game on addr " + controller.getAddress());

            ready.setText("Start");
            ready.setEnabled(false);
        }

        public void update () {
            boolean allReady = true;
            // host ID = 0
            for (int i = 1; i < slots.length; i++) {
                slots[i].set(controller.slots[i]);
                allReady = allReady && (controller.slots[i] == null || controller.slots[i].ready);
            }

            ready.setEnabled(allReady);
        }

    }

    public void update () {
        // TODO: not necessary to loop
        for (int i = 0; i < slots.length; i++)
            slots[i].set(controller.slots[i]);
    }

    public String getName () {
        return clientSlot.name.getText();
    }

}
