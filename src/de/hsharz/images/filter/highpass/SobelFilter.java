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

		int[][] edgeColors = new int[inputImage.getWidth()][inputImage.getHeight()];
		int maxGradient = -1;

		for (int x = 1; x < inputImage.getWidth() - 1; x++) {
			for (int y = 1; y < inputImage.getHeight() - 1; y++) {

				double hTotal = 0;
				double vTotal = 0;

				for (int x1 = -1; x1 <= 1; x1++) {
					for (int y1 = -1; y1 <= 1; y1++) {
						hTotal += ImageUtils.getGrayOfPixel(inputImage, x + x1, y + y1) * hmask[x1 + 1][y1 + 1];
						vTotal += ImageUtils.getGrayOfPixel(inputImage, x + x1, y + y1) * vmask[x1 + 1][y1 + 1];
					}
				}

				int newGrayValue = (int) Math.sqrt((hTotal * hTotal) + (vTotal * vTotal));
				edgeColors[x][y] = newGrayValue;

				maxGradient = Math.max(maxGradient, newGrayValue);
			}
		}

		double scale = 255.0 / maxGradient;

		for (int i = 1; i < inputImage.getWidth() - 1; i++) {
			for (int j = 1; j < inputImage.getHeight() - 1; j++) {
				double edgeColor = edgeColors[i][j] * scale;
				getOutputImage().setRGB(i, j, ImageUtils.colorToRgbWithAlpha(edgeColor));
			}
		}
	}

}
