package de.hsharz.images.filter.highpass;

import java.awt.image.BufferedImage;

import de.hsharz.images.filter.AbstractMaskFilter;
import de.hsharz.images.filter.utils.ImageUtils;

public abstract class HighPassFilter extends AbstractMaskFilter {

	@Override
	public void transformOutputImage(BufferedImage inputImage) {
		for (int x = 1; x < inputImage.getWidth() - 1; x++) {
			for (int y = 1; y < inputImage.getHeight() - 1; y++) {

				double redTotal = 0;
				double greenTotal = 0;
				double blueTotal = 0;

				double factor = 0;
				for (int x1 = 0; x1 < mask.length; x1++) {
					for (int y1 = 0; y1 < mask[x1].length; y1++) {

						factor = mask[x1][y1];
						redTotal += ImageUtils.getRedOfPixel(
								inputImage.getRGB(x - mask.length / 2 + x1, y - mask[x1].length / 2 + y1)) * factor;
						greenTotal += ImageUtils.getGreenOfPixel(
								inputImage.getRGB(x - mask.length / 2 + x1, y - mask[x1].length / 2 + y1)) * factor;
						blueTotal += ImageUtils.getBlueOfPixel(
								inputImage.getRGB(x - mask.length / 2 + x1, y - mask[x1].length / 2 + y1)) * factor;
					}
				}

				int newColor = 0xff000000 | (ImageUtils.adjustColor(redTotal) << 16)
						| (ImageUtils.adjustColor(greenTotal) << 8) | ImageUtils.adjustColor(blueTotal);

				getOutputImage().setRGB(x, y, newColor);
			}
		}
	}

}
