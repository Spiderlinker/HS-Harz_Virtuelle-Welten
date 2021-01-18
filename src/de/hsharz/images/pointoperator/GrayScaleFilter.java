package de.hsharz.images.pointoperator;

import java.awt.image.BufferedImage;

import de.hsharz.images.filter.AbstractFilter;
import de.hsharz.images.filter.utils.ImageUtils;

public class GrayScaleFilter extends AbstractFilter {

	@Override
	public void transformOutputImage(BufferedImage inputImage) {
		// tranform every pixel to gray
		for (int x = 0; x < inputImage.getWidth(); x++) {
			for (int y = 0; y < inputImage.getHeight(); y++) {
				// Get Gray value of current pixel
				int grayOfPixel = ImageUtils.getGrayOfPixel(inputImage, x, y);
				// Update current Pixel to created gray-pixel
				getOutputImage().setRGB(x, y, ImageUtils.colorToRgb(grayOfPixel));
			}
		}
	}

}
