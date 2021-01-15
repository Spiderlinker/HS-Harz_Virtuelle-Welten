package de.hsharz.images.filter.highpass;

import java.awt.image.BufferedImage;

import de.hsharz.images.filter.AbstractMaskFilter;
import de.hsharz.images.filter.utils.ImageUtils;

public abstract class HighPassFilter extends AbstractMaskFilter {

	@Override
	public void transformOutputImage(BufferedImage inputImage) {
		for (int x = 0; x < inputImage.getWidth(); x++) {
			for (int y = 0; y < inputImage.getHeight(); y++) {

				double redTotal = 0;
				double greenTotal = 0;
				double blueTotal = 0;

				double maskFactor = 0;
				for (int x1 = 0; x1 < mask.length; x1++) {
					int finX = x - mask.length / 2 + x1;
					if (ImageUtils.isOutOfBounds(inputImage.getWidth(), finX)) {
						continue;
					}
					for (int y1 = 0; y1 < mask[x1].length; y1++) {
						int finY = y - mask[x1].length / 2 + y1;
						if (ImageUtils.isOutOfBounds(inputImage.getHeight(), finY)) {
							continue;
						}

						maskFactor = mask[x1][y1];
						redTotal += ImageUtils.getRedOfPixel(inputImage.getRGB(finX, finY)) * maskFactor;
						greenTotal += ImageUtils.getGreenOfPixel(inputImage.getRGB(finX, finY)) * maskFactor;
						blueTotal += ImageUtils.getBlueOfPixel(inputImage.getRGB(finX, finY)) * maskFactor;
					}
				}

				int newColor = 0xff000000 | (ImageUtils.adjustColor(redTotal) << 16)
										  | (ImageUtils.adjustColor(greenTotal) << 8) 
										  | ImageUtils.adjustColor(blueTotal);
				getOutputImage().setRGB(x, y, newColor);
			}
		}
	}

}
