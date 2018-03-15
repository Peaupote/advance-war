package fr.main.view;

import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.LinkedList;

import javax.imageio.ImageIO;

import fr.main.view.render.sprites.SpriteList;

public class changeSizeImage {
 
    public static void main(String[] args) throws Exception {
    	
    	SpriteList sprite;
    	LinkedList<Rectangle> t = new LinkedList<Rectangle>(); 
		t.add(new Rectangle(0, 330, 710, 250));
		sprite = new SpriteList("./assets/button/button.png",t);
        BufferedImage tag = new BufferedImage(170, 60, BufferedImage.TYPE_INT_RGB);
        tag.getGraphics().drawImage(sprite.get(0), 0, 0,170, 60, null);
        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream("./assets/button/button01.png"));
        //JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
        //encoder.encode(tag);
        ImageIO.write(tag, "PNG",out);
//ByteArrayOutputStream out = new ByteArrayOutputStream();

//ImageIO.write(tag, "PNG",out);
//InputStream is = new ByteArrayInputStream(out.toByteArray());
        }
}