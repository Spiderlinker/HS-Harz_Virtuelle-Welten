package de.hsharz.images.filter;

import java.awt.image.BufferedImage;

import de.hsharz.images.filter.utils.ImageUtils;
import de.hsharz.images.ui.ImageInfo;

public abstract class AbstractFilter implements Filter {

	private ImageInfo imageInfo;
	private BufferedImage outputImage;

	protected BufferedImage getOutputImage() {
		if (outputImage == null) {
			outputImage = ImageUtils.getCopyOf(imageInfo.getImage());
		}
		return outputImage;
	}
	
	protected ImageInfo getImageInfo() {
		return imageInfo;
	}

	@Override
	public ImageInfo perform(ImageInfo imageInfo) {
		this.imageInfo = imageInfo;
		transformOutputImage(imageInfo.getImage());
		return new ImageInfo(imageInfo.getImageFile(), getOutputImage());
	}

	protected abstract void transformOutputImage(BufferedImage inputImage);

}
