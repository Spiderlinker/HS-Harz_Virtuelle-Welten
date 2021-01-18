package de.hsharz.images.pointoperator;

import java.awt.image.BufferedImage;

import de.hsharz.images.filter.AbstractFilter;
import de.hsharz.images.filter.utils.ImageUtils;
import de.hsharz.images.ui.ImageInfo.ImageColor;

public class HistogramEqualization extends AbstractFilter {

	private ImageColor color;

	public HistogramEqualization(ImageColor color) {
		this.color = color;
	}

	private int insertRgbInOriginalRgb(int origRgb, int rgb) {
		int mask;
		int shift;
		switch (color) {
		case RED:
			mask = 0xff00ffff;
			shift = 16;
			break;
		case GREEN:
			mask = 0xffff00ff;
			shift = 8;
			break;
		case BLUE:
			mask = 0xffffff00;
			shift = 0;
			break;
		default:
			mask = 1;
			shift = 0;
		}
		return (origRgb & mask) | (rgb << shift);
	}

	@Override
	public void transformOutputImage(BufferedImage inputImage) {
		for (int x = 0; x < inputImage.getWidth(); x++) {
			for (int y = 0; y < inputImage.getHeight(); y++) {
				int currentPixelVal = ImageUtils.getColorValueOfPixel(color, inputImage.getRGB(x, y));
				float newPixelVal = 0;
				for (int j = 0; j < (currentPixelVal + 1); j++) {
					newPixelVal += getImageInfo().getColorValueCount(color)[j]
							/ (double) getImageInfo().getPixelCount();
				}
				int result = (int) (newPixelVal * 255);

				int origRgb = inputImage.getRGB(x, y);
				int newRgb = insertRgbInOriginalRgb(origRgb, result);

				getOutputImage().setRGB(x, y, newRgb);
			}
		}
	}

}

