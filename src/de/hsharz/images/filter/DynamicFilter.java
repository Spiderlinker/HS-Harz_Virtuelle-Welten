package de.hsharz.images.filter;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

import de.hsharz.images.ImageInfo;
import de.hsharz.images.utils.ImageUtils;

public class DynamicFilter implements Filter {

	private final int amountGrayValues;

	public DynamicFilter(int amountGrayValues) {
		this.amountGrayValues = amountGrayValues;
	}

	@Override
	public ImageInfo perform(ImageInfo imageInfo) {
		double stepSize = ImageInfo.MAX_COLOR_VALUE / (double) amountGrayValues;
		double halfStepSize = stepSize / 2;
		int[] newGrayValues = new int[amountGrayValues + 1];
		for (int i = 0; i < newGrayValues.length; i++) {
			newGrayValues[i] = (int) Math.round(i * stepSize);
		}

		// Copy BufferdImage to modify the copy and not the original iamge
		ColorModel colorModel = imageInfo.getImage().getColorModel();
		WritableRaster raster = imageInfo.getImage().copyData(null);
		boolean isAlphaPremultiplied = colorModel.isAlphaPremultiplied();
		BufferedImage imageCopy = new BufferedImage(colorModel, raster, isAlphaPremultiplied, null);

//		System.out.println("Amount of Values: " + amountGrayValues);
//		System.out.println("StepSize:" + stepSize);
//		System.out.println("Half: " + halfStepSize);

		// tranform every pixel to gray
		for (int x = 0; x < imageCopy.getWidth(); x++) {
			for (int y = 0; y < imageCopy.getHeight(); y++) {
				// Get Gray value of current pixel
				int grayOfPixel = ImageUtils.getGrayOfPixel(imageCopy.getRGB(x, y));

//				System.out.println("Original Gray Value: " + grayOfPixel);
				int index = (int) Math.round((grayOfPixel / halfStepSize) / 2);
				int newGrayValue = newGrayValues[index];
//				System.out.println("Belongs to: [" + index + "]: " + newGrayValue);

				// create new rgb-Pixel with only gray values
				int rgb = (newGrayValue << 16) + (newGrayValue << 8) + newGrayValue;

				// Update current Pixel to created gray-pixel
				imageCopy.setRGB(x, y, rgb);
			}
		}

		return new ImageInfo(imageInfo.getImageFile(), imageCopy);
	}

}
