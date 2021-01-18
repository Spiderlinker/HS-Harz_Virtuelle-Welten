package de.hsharz.images.filter.grayvaluetransformation;

import java.awt.image.BufferedImage;

import de.hsharz.images.filter.AbstractFilter;
import de.hsharz.images.filter.utils.ImageUtils;
import de.hsharz.images.ui.ImageInfo;

public class DynamicFilter extends AbstractFilter {

	private final int amountGrayValues;

	public DynamicFilter(int amountGrayValues) {
		this.amountGrayValues = amountGrayValues;
	}

	@Override
	public void transformOutputImage(BufferedImage inputImage) {
		double stepSize = ImageInfo.MAX_COLOR_VALUE / (double) amountGrayValues;
		double halfStepSize = stepSize / 2;
		int[] newGrayValues = new int[amountGrayValues + 1];
		for (int i = 0; i < newGrayValues.length; i++) {
			newGrayValues[i] = (int) Math.round(i * stepSize);
		}

		// tranform every pixel to gray
		for (int x = 0; x < inputImage.getWidth(); x++) {
			for (int y = 0; y < inputImage.getHeight(); y++) {
				// Get Gray value of current pixel
				int grayOfPixel = ImageUtils.getGrayOfPixel(inputImage, x, y);
				int index = (int) Math.round((grayOfPixel / halfStepSize) / 2);

				// Update current Pixel to created gray-pixel
				getOutputImage().setRGB(x, y, ImageUtils.colorToRgb(newGrayValues[index]));
			}
		}
	}

}
