package fr.main.view.render.sprites;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class cutImage {
	
	@SuppressWarnings("unused")
	private ScaleRect scaleRect;
	private Sprite sprite;
	
	public cutImage(String str) {
		this.sprite = new Sprite(str);
	}
	
	public static void main(String[] args) throws IOException {
		cutImage cutImage = new cutImage("./assets/GameUI.png");	
	    ImageIO.write(cutImage.sprite.getImage(1090, 1205, 121, 121), "png", new File("./assets/button/border01.png")); 
	}

}
