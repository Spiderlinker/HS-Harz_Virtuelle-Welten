package de.hsharz.images.filter.morphological;

import java.awt.image.BufferedImage;

import de.hsharz.images.filter.AbstractMaskFilter;
import de.hsharz.images.filter.utils.ImageUtils;

public abstract class MorphologicalFilter extends AbstractMaskFilter {

	protected static final int WHITE = 255;
	protected static final int BLACK = 0;

	protected abstract int getColor();

	@Override
	protected void transformOutputImage(BufferedImage inputImage) {
		for (int x = 1; x < inputImage.getWidth() - 1; x++) {
			for (int y = 1; y < inputImage.getHeight() - 1; y++) {

				double factor = 0;
				boolean maskMatches = false;
				for (int x1 = 0; x1 < mask.length; x1++) {
					for (int y1 = 0; y1 < mask[x1].length; y1++) {

						factor = mask[x1][y1];
						if (ImageUtils.getGrayOfPixel(
								inputImage.getRGB(x - mask.length / 2 + x1, y - mask[x1].length / 2 + y1))
								* factor == getColor()) {
							maskMatches = true;
							break;
						}
					}
				}

				if (maskMatches) {
					int newColor = 0xff000000 | (getColor() << 16) | (getColor() << 8) | getColor();
					getOutputImage().setRGB(x, y, newColor);
				}
			}
		}
	}

}
