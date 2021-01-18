package de.hsharz.images.pointoperator;

import java.awt.image.BufferedImage;

import de.hsharz.images.filter.AbstractFilter;
import de.hsharz.images.filter.utils.ImageUtils;

public class DynamicColorFilter extends AbstractFilter {

	@Override
	public void transformOutputImage(BufferedImage inputImage) {
		// tranform every pixel to gray
		for (int x = 0; x < inputImage.getWidth(); x++) {
			for (int y = 0; y < inputImage.getHeight(); y++) {
				// Get Gray value of current pixel

				int rgb = inputImage.getRGB(x, y);
				int red = ImageUtils.getRedOfPixel(rgb);
				int green = ImageUtils.getGreenOfPixel(rgb);
				int blue = ImageUtils.getBlueOfPixel(rgb);

				int newRgb;
				if (red > green && red > blue) {
					newRgb = (red << 16) + (0 << 8) + 0;
				} else if (green > red && green > blue) {
					newRgb = (0 << 16) + (green << 8) + 0;
				} else if (blue > red && blue > green) {
					newRgb = (0 << 16) + (0 << 8) + blue;
				} else {
					int gray = ImageUtils.getGrayOfPixel(rgb);
					newRgb = (gray << 16) + (gray << 8) + gray;
				}

				// Update current Pixel to created gray-pixel
				getOutputImage().setRGB(x, y, newRgb);
			}
		}
	}

}
