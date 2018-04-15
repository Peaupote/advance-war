package fr.main.view.views;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import fr.main.view.MainFrame;
import fr.main.view.controllers.StatController;

public class StatView extends View {

  private StatController controller;

  private class StatPanel extends JPanel {

    public int[][] data;
    Color[] colors = new Color[]{
      Color.red,
      Color.blue,
      Color.green,
      Color.yellow
    };

    int max = 0;

    public StatPanel (int[][] data) {
      this.data = data;
      for (int i = 0; i < data.length; i++)
        for (int j =0; j < data[i].length; j++)
          if (max < data[i][j]) max = data[i][j];
    }

    public void paintComponent(Graphics g) {
      int x, scaleHeight = (MainFrame.height() + 1)/ max,
          gap = MainFrame.width() / data[0].length;
      for (int i = 0; i < data.length; i++) { // each player
        g.setColor(colors[i]);
        x = 0;
        for (int j = 1; j < data[i].length; j++) {
          g.drawLine(x, (max - data[i][j - 1]) * scaleHeight, x + gap, (max - data[i][j]) * scaleHeight);
          x = x + gap;
        }
      }

      g.setColor(Color.black);
      for (int day = 0; day < data[0].length; day++) {
        x = day * gap;
        g.drawLine(x, getHeight(), x, getHeight() - 10);
        g.drawString("day " + day, x, getHeight() - 10);
      }

    }

  }

  public StatView (StatController controller) {
    super(controller);
    this.controller = controller;
    
    JTabbedPane tabs = new JTabbedPane();
    tabs.addTab("Units", null, new StatPanel (controller.units), "Evolution of all players units");
    tabs.addTab("Buildings", null, new StatPanel (controller.buildings), "Evolution of all players buildings");

    setLayout(new BorderLayout());
    add(tabs, BorderLayout.CENTER);

    JButton menu = new JButton("Back to menu");
    add(menu, BorderLayout.SOUTH);

    menu.addActionListener(controller.menu);
  }

}
