package de.hsharz.images.filter;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

import de.hsharz.images.ImageInfo;
import de.hsharz.images.utils.ImageUtils;

public class GrayScaleFilter implements Filter {

	@Override
	public ImageInfo perform(ImageInfo imageInfo) {

		// Copy BufferdImage to modify the copy and not the original iamge
		ColorModel colorModel = imageInfo.getImage().getColorModel();
		WritableRaster raster = imageInfo.getImage().copyData(null);
		boolean isAlphaPremultiplied = colorModel.isAlphaPremultiplied();
		BufferedImage imageCopy = new BufferedImage(colorModel, raster, isAlphaPremultiplied, null);

		// tranform every pixel to gray
		for (int x = 0; x < imageCopy.getWidth(); x++) {
			for (int y = 0; y < imageCopy.getHeight(); y++) {
				// Get Gray value of current pixel
				int grayOfPixel = ImageUtils.getGrayOfPixel(imageCopy.getRGB(x, y));
				// create new rgb-Pixel with only gray values
				int rgb = (grayOfPixel << 16) + (grayOfPixel << 8) + grayOfPixel;

				// Update current Pixel to created gray-pixel
				imageCopy.setRGB(x, y, rgb);
			}
		}

		return new ImageInfo(imageInfo.getImageFile(), imageCopy);
	}

}
