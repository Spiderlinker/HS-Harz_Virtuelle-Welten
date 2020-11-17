package de.hsharz.images.filter.highpass;

import java.awt.image.BufferedImage;

import de.hsharz.images.filter.AbstractFilter;
import de.hsharz.images.filter.utils.ImageUtils;

public class SobelFilter extends AbstractFilter {

	private int[][] hmask = new int[][] { //
			{ -1, -2, -1 }, //
			{ 0, 0, 0 }, //
			{ 1, 2, 1 } //
	};

	private int[][] vmask = new int[][] { //
			{ -1, 0, 1 }, //
			{ -2, 0, 2 }, //
			{ -1, 0, 1 } //
	};

	@Override
	public void transformOutputImage(BufferedImage inputImage) {

		int[][] edgeRedColors = new int[inputImage.getWidth()][inputImage.getHeight()];
		int[][] edgeGreenColors = new int[inputImage.getWidth()][inputImage.getHeight()];
		int[][] edgeBlueColors = new int[inputImage.getWidth()][inputImage.getHeight()];
		int maxGradient = -1;

		for (int x = 1; x < inputImage.getWidth() - 1; x++) {
			for (int y = 1; y < inputImage.getHeight() - 1; y++) {

//				double hTotal = 0;
//				double vTotal = 0;
				double hRedTotal = 0;
				double vRedTotal = 0;
				double hGreenTotal = 0;
				double vGreenTotal = 0;
				double hBlueTotal = 0;
				double vBlueTotal = 0;

				for (int x1 = -1; x1 <= 1; x1++) {
					for (int y1 = -1; y1 <= 1; y1++) {
						hRedTotal += ImageUtils.getRedOfPixel(inputImage, x + x1, y + y1) * hmask[x1 + 1][y1 + 1];
						vRedTotal += ImageUtils.getRedOfPixel(inputImage, x + x1, y + y1) * vmask[x1 + 1][y1 + 1];
						hGreenTotal += ImageUtils.getGreenOfPixel(inputImage, x + x1, y + y1) * hmask[x1 + 1][y1 + 1];
						vGreenTotal += ImageUtils.getGreenOfPixel(inputImage, x + x1, y + y1) * vmask[x1 + 1][y1 + 1];
						hBlueTotal += ImageUtils.getBlueOfPixel(inputImage, x + x1, y + y1) * hmask[x1 + 1][y1 + 1];
						vBlueTotal += ImageUtils.getBlueOfPixel(inputImage, x + x1, y + y1) * vmask[x1 + 1][y1 + 1];
					}
				}

				int newRedValue = (int) Math.sqrt((hRedTotal * hRedTotal) + (vRedTotal * vRedTotal));
				int newGreenValue = (int) Math.sqrt((hGreenTotal * hGreenTotal) + (vGreenTotal * vGreenTotal));
				int newBlueValue = (int) Math.sqrt((hBlueTotal * hBlueTotal) + (vBlueTotal * vBlueTotal));
				edgeRedColors[x][y] = newRedValue;
				edgeGreenColors[x][y] = newGreenValue;
				edgeBlueColors[x][y] = newBlueValue;

				maxGradient = Math.max(maxGradient, newRedValue);
				maxGradient = Math.max(maxGradient, newGreenValue);
				maxGradient = Math.max(maxGradient, newBlueValue);
			}
		}

		double scale = 255.0 / maxGradient;

		for (int i = 1; i < inputImage.getWidth() - 1; i++) {
			for (int j = 1; j < inputImage.getHeight() - 1; j++) {
				int rgb = 0xff000000 + ((int) Math.round(edgeRedColors[i][j] * scale) << 16)
						+ ((int) Math.round(edgeGreenColors[i][j] * scale) << 8)
						+ (int) Math.round(edgeBlueColors[i][j] * scale);

				getOutputImage().setRGB(i, j, rgb);
			}
		}
	}

}
