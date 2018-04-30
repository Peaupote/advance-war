package fr.main.view.sound;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

public class MusicEngine {
	private String musicPath; 
    public volatile boolean run = true;   
    private Thread mainThread;   
      
    private AudioInputStream audioStream;  
    private AudioFormat audioFormat;  
    private SourceDataLine sourceDataLine;  
      
    public MusicEngine(String musicPath) {  
        this.musicPath = musicPath;  
        prefetch();  
    }  
      
    //preper the music data
    private void prefetch(){  
        try{   
        audioStream = AudioSystem.getAudioInputStream(new File(musicPath));  
        //get the format of the object  
        audioFormat = audioStream.getFormat();  
        DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class,  
                audioFormat,AudioSystem.NOT_SPECIFIED);  
        sourceDataLine = (SourceDataLine)AudioSystem.getLine(dataLineInfo);  
          
        sourceDataLine.open(audioFormat);  
        sourceDataLine.start();  
          
        }catch(UnsupportedAudioFileException ex){  
            ex.printStackTrace();  
        }catch(LineUnavailableException ex){  
            ex.printStackTrace();  
        }catch(IOException ex){  
            ex.printStackTrace();  
        }  
          
    }  
    
    protected void finalize() throws Throwable{  
        super.finalize();
        sourceDataLine.drain();
        sourceDataLine.close();
        audioStream.close();
    }  
       
    //the parameter loop controls if playback or not
    //if true, it will play the music until close, 
    //if false, just one time   
    public void playMusic(boolean loop) {  
        try { 
            if(loop) 
                while(true) 
                    playMusic();
            else {
                playMusic();
                sourceDataLine.drain();
                sourceDataLine.close();
                audioStream.close();
            }          
        } catch(IOException ex) {
          ex.printStackTrace();  
        }
    }

    private void playMusic(){  
        try{  
            synchronized(this){  
                run = true;  
            }  
            //AudioInputStream -> SourceDataLine;  
            audioStream = AudioSystem.getAudioInputStream(new File(musicPath));  
            int count;  
            byte tempBuff[] = new byte[1024];  
              
            while((count = audioStream.read(tempBuff,0,tempBuff.length)) != -1){  
                synchronized(this){  
                while(!run)  
                    wait();  
                }  
                sourceDataLine.write(tempBuff,0,count);  
                          
            }  
  
        }catch(UnsupportedAudioFileException ex){  
            ex.printStackTrace();  
        }catch(IOException ex){  
            ex.printStackTrace();  
        }catch(InterruptedException ex){  
            ex.printStackTrace();  
        }  
          
    }  

    private void stopMusic(){  
        synchronized(this){  
            run = false;  
            notifyAll();  
        }  
    }  

    private void continueMusic(){  
        synchronized(this){  
             run = true;  
             notifyAll();  
        }  
    }  
    
    public void playOneTime() {
    	mainThread = null;
        mainThread = new Thread(new Runnable(){  
            public void run(){  
                playMusic();  
                while(!run) {
                	mainThread.interrupt();
                	try {
						mainThread.join();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
                }
            }  
        });  
        mainThread.start();
    	
    }
 
    //start with creat a new thread
    public void start(boolean loop){
        mainThread = new Thread(() -> playMusic(loop));  
        mainThread.start();  
    }  
    
    //stop music in the thread
    public void stop(){  
        new Thread(this::stopMusic).start();  
    }  
 
    //continue the music
    public void continues(){  
        new Thread(this::continueMusic).start();  
    }  
    
}
