package de.hsharz.images.filter.grayvaluetransformation;

import java.awt.image.BufferedImage;

import de.hsharz.images.filter.AbstractFilter;
import de.hsharz.images.filter.utils.ImageUtils;

public class BinaryFilter extends  AbstractFilter {

	private final int schwellwert;

	public BinaryFilter(int schwellwert) {
		this.schwellwert = schwellwert;
	}

	@Override
	public void transformOutputImage(BufferedImage inputImage) {
		// tranform every pixel to gray
		for (int x = 0; x < inputImage.getWidth(); x++) {
			for (int y = 0; y < inputImage.getHeight(); y++) {
				// Get Gray value of current pixel
				int grayOfPixel = ImageUtils.getGrayOfPixel(inputImage, x, y);
				int newPixelRgb;
				if (grayOfPixel <= schwellwert) {
					newPixelRgb = 0;
				} else {
					newPixelRgb = (255 << 16) + (255 << 8) + 255;
				}

				// Update current Pixel to created gray-pixel
				getOutputImage().setRGB(x, y, newPixelRgb);
			}
		}
	}

}
