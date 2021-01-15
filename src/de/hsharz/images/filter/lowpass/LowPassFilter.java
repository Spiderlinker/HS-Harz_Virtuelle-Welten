package de.hsharz.images.filter.lowpass;

import java.awt.image.BufferedImage;

import de.hsharz.images.filter.AbstractMaskFilter;
import de.hsharz.images.filter.utils.ImageUtils;

public abstract class LowPassFilter extends AbstractMaskFilter {

	@Override
	public void transformOutputImage(BufferedImage inputImage) {
		// tranform every pixel to gray

		// Durch jeden Pixel im Bild iterieren
		for (int x = 0; x < inputImage.getWidth(); x++) {
			for (int y = 0; y < inputImage.getHeight(); y++) {

				int knownPixels = 0;
				double redPixelSum = 0;
				double greenPixelSum = 0;
				double bluePixelSum = 0;

				double maskFactor;
				// Für jedes Pixel des Bildes soll die komplette Filtermaske durchlaufen werden
				// Da jede Größe von Masken unterstützt werden sollen, wird von 0 bis zur
				// mask.length iteriert
				for (int x1 = 0; x1 < mask.length; x1++) {
					// Die Berechnung der nun betrachteten x-Koordinate gestaltet sich hierdurch
					// allerdings etwas schwieriger. Hierbei wird nun der aktuell betrachtete Pixel
					// des Bildes genommen, die Hälfte der Länge der Filtermaske von diesem Pixel
					// abgezogen und die aktuelle Stelle (index) in der Filtermaske aufaddiert.
					int finX = x - mask.length / 2 + x1; // x-Koordinate im Bild berechnen
					if (ImageUtils.isOutOfBounds(inputImage.getWidth(), finX)) {
						// Die aktuelle x-Koordinate liegt außerhalb des Bildes.
						// Mit nächstem Wert der Filtermaske weitermachen
						continue;
					}
					for (int y1 = 0; y1 < mask[x1].length; y1++) {
						int finY = y - mask[x1].length / 2 + y1;
						if (ImageUtils.isOutOfBounds(inputImage.getHeight(), finY)) {
							// Die aktuelle y-Koordinate liegt außerhalb des Bildes.
							// Mit nächstem Wert der Filtermaske weitermachen
							continue;
						}

						maskFactor = mask[x1][y1]; // Aktuellen Wert aus der Mmaske holen
						knownPixels += maskFactor;
						redPixelSum += ImageUtils.getRedOfPixel(inputImage, finX, finY) * maskFactor;
						greenPixelSum += ImageUtils.getGreenOfPixel(inputImage, finX, finY) * maskFactor;
						bluePixelSum += ImageUtils.getBlueOfPixel(inputImage, finX, finY) * maskFactor;
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
