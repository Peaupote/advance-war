package fr.main.view;

import java.applet.AudioClip;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JApplet;

public class BackgroundMusic {
	
	public static AudioClip loadSound(String filename) {  
        URL url = null;  
        try {  
            url = new URL("file:" + filename);  
        }   
        catch (MalformedURLException e) {;}  
        return JApplet.newAudioClip(url);  
    }  
    public void play() {  
        AudioClip christmas = loadSound("assets/bc.wav");  
        christmas.play();  
    }  
	 
}
