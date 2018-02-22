package fr.main.view;

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
    private volatile boolean run = true;   
    private Thread mainThread;   
      
    private AudioInputStream audioStream;  
    private AudioFormat audioFormat;  
    private SourceDataLine sourceDataLine;  
      
    public MusicEngine(String musicPath) {  
        this.musicPath = musicPath;  
        prefetch();  
    }  
      
    //数据准备  
    private void prefetch(){  
        try{  
        //获取音频输入流  
        audioStream = AudioSystem.getAudioInputStream(new File(musicPath));  
        //获取音频的编码对象  
        audioFormat = audioStream.getFormat();  
        //包装音频信息  
        DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class,  
                audioFormat,AudioSystem.NOT_SPECIFIED);  
        //使用包装音频信息后的Info类创建源数据行，充当混频器的源  
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
       
    private void playMusic(boolean loop)throws InterruptedException {  
        try{  
                if(loop){  
                    while(true){  
                        playMusic();  
                    }  
                }else{  
                    playMusic();  
                    sourceDataLine.drain();  
                    sourceDataLine.close();  
                    audioStream.close();  
                }  
              
        }catch(IOException ex){  
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
 
    public void start(boolean loop){  
        mainThread = new Thread(new Runnable(){  
            public void run(){  
                try {  
                    playMusic(loop);  
                } catch (InterruptedException e) {  
                    e.printStackTrace();  
                }  
            }  
        });  
        mainThread.start();  
    }  
      
    public void stop(){  
        new Thread(new Runnable(){  
            public void run(){  
                stopMusic();  
                  
            }  
        }).start();  
    }  
 
    public void continues(){  
        new Thread(new Runnable(){  
            public void run(){  
                continueMusic();  
            }  
        }).start();  
    }  
    
}
