package de.hsharz.images.utils;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

public class ImageUtils {

	private ImageUtils() {
		// Utility Class
	}

	/**
	 * Liefert den Grauwert des (in RGB-Form) �bergebenen Pixels
	 *
	 * @param rgb Pixel in RGB-Form, dessen Grauwert berechnet werden soll
	 * @return Grauwert des angegebenen Pixels
	 */
	public static int getGrayOfPixel(final int rgb) {

		// Pixel ist aufgebaut: (int = 4Byte = 32Bit)

		// Meta --- Red ---- Green -- Blue
		// 00000000 00000000 00000000 00000000
		// --
		// rgb >> 16 & 0xFF
		// Rot wird nach 'ganz rechts geschoben'
		// und dann mit Maske 0xFF (nur die letzten 8 Bit übernehmen)
		// UND-Verknüpft, damit bleiben nur die Rotwerte übrig

		int r = getRedOfPixel(rgb); // Rot extrahieren
		int g = getGreenOfPixel(rgb); // Gr�n extrahieren
		int b = getBlueOfPixel(rgb); // Blau extrahieren

		// Grauwert berechnen
		// Rot + Gr�n + Blau / Anzahl an Werten (3)
		return (r + g + b) / 3;
	}

	public static int getRedOfPixel(int rgb) {
		return (rgb >> 16) & 0xFF;
	}

	public static int getGreenOfPixel(int rgb) {
		return (rgb >> 8) & 0xFF;
	}

	public static int getBlueOfPixel(int rgb) {
		return rgb & 0xFF;
	}

	public static BufferedImage resize(BufferedImage img, int fitWidth, int fitHeight) {

		int imgWidth;
		int imgHeight;

		if (img.getWidth() / fitWidth > img.getHeight() / (double) fitHeight) {
			double scale = img.getWidth() / fitWidth;
			imgWidth = (int) (img.getWidth() / scale);
			imgHeight = (int) (img.getHeight() / scale);
		} else {
			double scale = img.getHeight() / (double) fitHeight;
			imgWidth = (int) (img.getWidth() / scale);
			imgHeight = (int) (img.getHeight() / scale);
		}

		Image tmp = img.getScaledInstance(imgWidth, imgHeight, Image.SCALE_SMOOTH);
		BufferedImage dimg = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB);

		Graphics2D g2d = dimg.createGraphics();
		g2d.drawImage(tmp, 0, 0, null);
		g2d.dispose();

		return dimg;
	}

}
