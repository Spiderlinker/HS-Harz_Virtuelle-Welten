package de.hsharz.images.filter.lowpass;

import java.awt.image.BufferedImage;

import de.hsharz.images.filter.AbstractMaskFilter;
import de.hsharz.images.filter.utils.ImageUtils;

public abstract class LowPassFilter extends AbstractMaskFilter {

	@Override
	public void transformOutputImage(BufferedImage inputImage) {
		// tranform every pixel to gray
		for (int x = 0; x < inputImage.getWidth(); x++) {
			for (int y = 0; y < inputImage.getHeight(); y++) {

				int knownPixels = 0;
				double redPixelSum = 0;
				double greenPixelSum = 0;
				double bluePixelSum = 0;

				double factor;
				for (int x1 = -1; x1 <= 1; x1++) {
					if (ImageUtils.isOutOfBounds(inputImage.getWidth(), x + x1)) {
						continue;
					}
					for (int y1 = -1; y1 <= 1; y1++) {
						if (ImageUtils.isOutOfBounds(inputImage.getHeight(), y + y1)) {
							continue;
						}

						factor = mask[x1 + 1][y1 + 1];
						knownPixels += factor;
						redPixelSum += ImageUtils.getRedOfPixel(inputImage, x + x1, y + y1) * factor;
						greenPixelSum += ImageUtils.getGreenOfPixel(inputImage, x + x1, y + y1) * factor;
						bluePixelSum += ImageUtils.getBlueOfPixel(inputImage, x + x1, y + y1) * factor;
					}
				}

				if (knownPixels == 0) {
					System.out.println("Something went really wrong here!");
					break;
				}
				redPixelSum /= knownPixels;
				greenPixelSum /= knownPixels;
				bluePixelSum /= knownPixels;

				// create new rgb-Pixel with only gray values
				int rgb = 0xff000000 + (ImageUtils.adjustColor(redPixelSum) << 16)
						+ (ImageUtils.adjustColor(greenPixelSum) << 8) + ImageUtils.adjustColor(bluePixelSum);

				// Update current Pixel to created gray-pixel
				getOutputImage().setRGB(x, y, rgb);
			}
		}
	}

}
