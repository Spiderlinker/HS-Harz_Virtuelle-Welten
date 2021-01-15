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
		for (int x = 0; x < inputImage.getWidth(); x++) {
			for (int y = 0; y < inputImage.getHeight(); y++) {

				double maskFactor = 0;
				
				boolean maskMatches = false;
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
						if (ImageUtils.getGrayOfPixel(inputImage.getRGB(finX, finY)) * maskFactor == getColor()) {
							maskMatches = true;
							break;
						}
					}
				}

				if (maskMatches) {
					int newColor = ImageUtils.colorToRgbWithAlpha(getColor());
					getOutputImage().setRGB(x, y, newColor);
				}
			}
		}
	}

}
