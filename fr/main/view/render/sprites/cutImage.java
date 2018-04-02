package fr.main.view.render.sprites;

import java.awt.Button;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class cutImage {
	
	private ScaleRect scaleRect;
	private Sprite sprite;
	
	public cutImage(String str) {
		this.sprite = new Sprite(str);
	}
	
	public static void main(String[] args) throws IOException {
		cutImage cutImage = new cutImage("./assets/boutton.png");	
	    ImageIO.write(cutImage.sprite.getImage(561, 1410, 160, 160), "png", new File("./assets/button/border.png")); 
	}

}
