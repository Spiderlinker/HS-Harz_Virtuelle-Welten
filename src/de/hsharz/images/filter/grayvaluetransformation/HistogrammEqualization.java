package de.hsharz.images.filter.grayvaluetransformation;

import java.awt.image.BufferedImage;

import de.hsharz.images.filter.AbstractFilter;
import de.hsharz.images.filter.utils.ImageUtils;
import de.hsharz.images.ui.ImageInfo;
import de.hsharz.images.ui.ImageInfo.ImageColor;

public class HistogrammEqualization extends AbstractFilter {

	private ImageColor color;

	public HistogrammEqualization(ImageColor color) {
		this.color = color;
	}

	@Override
	public ImageInfo perform(ImageInfo imageInfo) {
		// Create a copy of the input image, transform it and return as result
		BufferedImage inputImage = imageInfo.getImage();
		BufferedImage outputImage = ImageUtils.getCopyOf(imageInfo.getImage());

		int[] adjustedValues = new int[imageInfo.getColorValueCount(color).length];

		int sum = 0;
		for (int i = 0; i < 255; i++) {
			sum += imageInfo.getColorValueCount(color)[i];
			adjustedValues[i] = sum * 255 / imageInfo.getPixelCount();
		}

		// tranform every pixel to gray
		for (int x = 0; x < inputImage.getWidth(); x++) {
			for (int y = 0; y < inputImage.getHeight(); y++) {

				// Update current Pixel to created gray-pixel
				outputImage.setRGB(x, y,
						adjustedValues[ImageUtils.getColorValueOfPixel(color, inputImage.getRGB(x, y))]);

			}
		}
		return new ImageInfo(imageInfo.getImageFile(), outputImage);
	}

	@Override
	public void transformOutputImage(BufferedImage inputImage) {
		// all work already done in perform method
	}

}
