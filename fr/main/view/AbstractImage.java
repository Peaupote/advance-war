//package fr.main.view;
//
//import java.awt.image.BufferedImage;
//import java.awt.image.RasterFormatException;
//
//public abstract class AbstractImage {
//	protected BufferedImage image;
//
//	public BufferedImage getSubImg(Location l, int width, int height) {
//		if(image == null) return null;
//		BufferedImage out = null;
//		try {
//			out = image.getSubimage(l.x, l.y, width, height);
//		} catch (RasterFormatException e) {
//			System.out.println("Image out of bound : origin(" + l.x + ":" + l.y + ")");
//		}
//
//		return out;
//	}
//
//	public BufferedImage getSubImg(TerrainImage.Location l) {
//		return getSubImg(l, 16, 16);
//	}
//
//	public enum Location {
//		TOP_LEFT(0, 0), TOP_RIGHT(34,0), BOTTOM_LEFT(0, 34), BOTTOM_RIGHT(34, 34),
//		TOP(17, 0), BOTTOM(17, 34), LEFT(0, 17), RIGHT(34, 17),
//		CENTER(17, 17);
//		int x, y;
//		Location(int x, int y) {
//			this.x = x;
//			this.y = y;
//		}
//	}
//}
