package fr.main.view.render.units;

public class UnitAnimationManager {

  private static int animationOffset = 0;

  static {
    new Thread(() -> {
      while (true) {
        animationOffset = (animationOffset + 1) % 5;

        try {
          Thread.sleep(100);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }).start();
  }

  public static int getOffset () {
    return animationOffset;
  }
}

