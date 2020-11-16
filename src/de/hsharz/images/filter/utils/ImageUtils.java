package de.hsharz.images.filter.utils;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.util.function.BiConsumer;

import de.hsharz.images.ui.ImageInfo.ImageColor;

public class ImageUtils {

	private ImageUtils() {
		// Utility Class
	}

	public static void runThroughImage(BufferedImage image, BiConsumer<Integer, Integer> pixelConsumer) {
		for (int x = 0; x < image.getWidth(); x++) {
			for (int y = 0; y < image.getHeight(); y++) {
				pixelConsumer.accept(x, y);
			}
		}
	}

	public static void runThroughImagePadded(BufferedImage image, BiConsumer<Integer, Integer> pixelConsumer) {
		for (int x = 1; x < image.getWidth() - 1; x++) {
			for (int y = 1; y < image.getHeight() - 1; y++) {
				pixelConsumer.accept(x, y);
			}
		}
	}

	/**
	 * Liefert den Grauwert des Pixels an der angegebenen Stelle (x, y).
	 *
	 * @param x x-Koordinate des Pixels
	 * @param y y-Koordinate des Pixels
	 * @return Grauwert des angegebenen Pixels
	 */
	public static int getColorValueOfPixel(ImageColor color, int rgb) {
		switch (color) {
		case GRAY:
			return ImageUtils.getGrayOfPixel(rgb);
		case RED:
			return ImageUtils.getRedOfPixel(rgb);
		case GREEN:
			return ImageUtils.getGreenOfPixel(rgb);
		case BLUE:
			return ImageUtils.getBlueOfPixel(rgb);
		}
		return -1;
	}

	public static int getGrayOfPixel(BufferedImage image, int x, int y) {
		return getGrayOfPixel(image.getRGB(x, y));
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

	public static int getAlphaOfPixel(BufferedImage image, int x, int y) {
		return getAlphaOfPixel(image.getRGB(x, y));
	}

	public static int getAlphaOfPixel(int rgb) {
		return (rgb >> 24) & 0xFF;
	}

	public static int getRedOfPixel(BufferedImage image, int x, int y) {
		return getRedOfPixel(image.getRGB(x, y));
	}

	public static int getRedOfPixel(int rgb) {
		return (rgb >> 16) & 0xFF;
	}

	public static int getGreenOfPixel(BufferedImage image, int x, int y) {
		return getGreenOfPixel(image.getRGB(x, y));
	}

	public static int getGreenOfPixel(int rgb) {
		return (rgb >> 8) & 0xFF;
	}

	public static int getBlueOfPixel(BufferedImage image, int x, int y) {
		return getBlueOfPixel(image.getRGB(x, y));
	}

	public static int getBlueOfPixel(int rgb) {
		return rgb & 0xFF;
	}

	public static boolean isOutOfBounds(int arrayLength, int index) {
		return isOutOfBounds(arrayLength, index, index);
	}

	public static boolean isOutOfBounds(int arrayLength, int min, int max) {
		return min < 0 || max >= arrayLength;
	}

	public static int colorToRgb(double value) {
		int adjustedColor = adjustColor(value);
		return (adjustedColor << 16) + (adjustedColor << 8) + adjustedColor;
	}

	public static int colorToRgbWithAlpha(double value) {
		int adjustedColor = adjustColor(value);
		return (255 << 24) + (adjustedColor << 16) + (adjustedColor << 8) + adjustedColor;
	}

	public static int adjustColor(double value) {
		int color = (int) Math.round(value);
		return Math.max(0, Math.min(255, color));
	}

	public static double[][] imageToColorArray(BufferedImage image, ImageColor color) {
		double[][] output = new double[image.getWidth()][image.getHeight()];
		for (int x = 0; x < image.getWidth(); x++) {
			for (int y = 0; y < image.getHeight(); y++) {
				output[x][y] = ImageUtils.getColorValueOfPixel(color, image.getRGB(x, y));
			}
		}
		return output;
	}

	public static double[][][] imageToColorArrayWithColor(BufferedImage image) {
		double[][][] output = new double[3][image.getWidth()][image.getHeight()];
		for (int x = 0; x < image.getWidth(); x++) {
			for (int y = 0; y < image.getHeight(); y++) {
				output[0][x][y] = ImageUtils.getRedOfPixel(image, x, y);
				output[1][x][y] = ImageUtils.getGreenOfPixel(image, x, y);
				output[2][x][y] = ImageUtils.getBlueOfPixel(image, x, y);
			}
		}
		return output;
	}

	public static void colorArrayToImage(double[][][] imageAsArray, BufferedImage outputImage) {
		for (int x = 0; x < imageAsArray[0].length; x++) {
			for (int y = 0; y < imageAsArray[0][x].length; y++) {
				outputImage.setRGB(x, y,
						(ImageUtils.adjustColor(imageAsArray[0][x][y]) << 16)
								+ (ImageUtils.adjustColor(imageAsArray[1][x][y]) << 8)
										+ ImageUtils.adjustColor(imageAsArray[2][x][y]));
			}
		}
	}

	public static double[][] imageToArray(BufferedImage image) {
		double[][] output = new double[image.getWidth()][image.getHeight()];
		for (int x = 0; x < image.getWidth(); x++) {
			for (int y = 0; y < image.getHeight(); y++) {
				output[x][y] = image.getRGB(x, y);
			}
		}
		return output;
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

	public static BufferedImage getCopyOf(BufferedImage imageToCopy) {
		// Copy BufferdImage to modify the copy and not the original iamge
		ColorModel colorModel = imageToCopy.getColorModel();
		WritableRaster raster = imageToCopy.copyData(null);
		boolean isAlphaPremultiplied = colorModel.isAlphaPremultiplied();
		return new BufferedImage(colorModel, raster, isAlphaPremultiplied, null);
	}

}
