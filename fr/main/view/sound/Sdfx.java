package fr.main.view.sound;

import javax.sound.sampled.*;
import java.io.File;
import java.util.*;

public enum Sdfx {
  EXPLOSION("song016.wav"),
  NEW_DAY("song218.wav", "song208.wav", "song207.wav", "song204.wav"),
  SELECT("song124.wav");
  
  private static Random rand = new Random();

  private ArrayList<Clip> sdfxs;

  private Sdfx (String... paths) {
    sdfxs = new ArrayList<>();
    for (String path: paths)
      sdfxs.add(getClip(path));
  }

  private Clip getClip (String path) {
    Clip sdfx = null;

    try {
      AudioInputStream ais = AudioSystem.getAudioInputStream(new File("./assets/sound/" + path));
      sdfx = AudioSystem.getClip ();
      sdfx.open(ais);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return sdfx;
  }

  public void play () {
    Clip sdfx = sdfxs.get(rand.nextInt(sdfxs.size()));
    if (sdfx.isRunning()) sdfx.stop();
    sdfx.setFramePosition(0);
    sdfx.start();
  }

}
