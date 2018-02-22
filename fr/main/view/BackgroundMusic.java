package fr.main.view;

import java.applet.AudioClip;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JApplet;

public class BackgroundMusic {
	
	//We can use the music in asset.
	AudioClip bm = loadSound("assets/bc.wav");  
	
	public static AudioClip loadSound(String filename) {  
        URL url = null;  
        
        try {  
            url = new URL("file:" + filename);  
        }   
        catch (MalformedURLException e) {;}  
        return JApplet.newAudioClip(url);  
    }  
    public void play() {  
        
        bm.play();  
    }  
    
    public void stop() {
    	bm.stop();
    }
	 
}
