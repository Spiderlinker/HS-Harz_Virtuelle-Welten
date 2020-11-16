package de.hsharz.images.filter.utils;

import static de.hsharz.images.filter.utils.ImageUtils.colorToRgb;
import static de.hsharz.images.filter.utils.ImageUtils.imageToColorArray;
import static de.hsharz.images.filter.utils.ImageUtils.isOutOfBounds;

import java.awt.image.BufferedImage;

import de.hsharz.images.ui.ImageInfo.ImageColor;

public class Convolution {

	public static double singlePixelConvolution(double[][] image, int x, int y, double[][] kernel) {
		double output = 0;
		for (int i = 0; i < kernel.length; i++) {
			int x1 = x + i - kernel.length / 2;
			if (isOutOfBounds(image.length, x1)) {
				continue;
			}
			for (int j = 0; j < kernel[i].length; j++) {
				int y1 = y + j - kernel[i].length / 2;
				if (isOutOfBounds(image[x1].length, y1)) {
					continue;
				}
				output += image[x1][y1] * kernel[i][j];
			}
		}
		return output;
	}

	public static double singlePixelConvolutionWithCalculatedFactor(double[][] image, int x, int y,
			double[][] kernel) {
		double output = 0;
		int processedPixel = 0;
		for (int i = 0; i < kernel.length; i++) {
			int x1 = x + i - kernel.length / 2;
			if (isOutOfBounds(image.length, x1))
				continue;
			for (int j = 0; j < kernel[i].length; j++) {
				int y1 = y + j - kernel[i].length / 2;
				if (isOutOfBounds(image[x1].length, y1))
					continue;
				processedPixel++;
				output += image[x1][y1] * kernel[i][j];
			}
		}
		processedPixel = Math.max(1, processedPixel);
		return output / processedPixel;
	}

	public static double[][] convolute(double[][] image, double[][] kernel, double factor) {
		double[][] output = new double[image.length][];

		for (int x = 0; x < image.length; x++) {
			output[x] = new double[image[x].length];
			for (int y = 0; y < image[x].length; y++) {
				output[x][y] = singlePixelConvolution(image, x, y, kernel) * factor;
			}
		}

		return output;
	}

	public static double[][] convoluteWithCalculatedFactor(double[][] image, double[][] kernel) {
		double[][] output = new double[image.length][];

		for (int x = 0; x < image.length; x++) {
			output[x] = new double[image[x].length];
			for (int y = 0; y < image[x].length; y++) {
				output[x][y] = singlePixelConvolutionWithCalculatedFactor(image, x, y, kernel);
			}
		}

		return output;
	}

	public static double[][] convolutePadded(double[][] image, double[][] kernel, double factor) {
		int kernelSize = kernel.length / 2;
		double[][] output = new double[image.length - kernelSize + 1][];

		for (int x = 0; x < image.length; x++) {
			output[x] = new double[image[x].length - kernelSize + 1];
			if (isOutOfBounds(image.length, x - kernelSize, x + kernelSize)) {
				continue;
			}
			for (int y = 0; y < image[x].length; y++) {
				if (isOutOfBounds(image[x].length, y - kernelSize, y + kernelSize)) {
					continue;
				}
				output[x][y] = singlePixelConvolution(image, x, y, kernel) * factor;
			}
		}

		return output;
	}

	public static BufferedImage convolute(BufferedImage image, double[][] kernel, double factor) {
		BufferedImage imageCopy = ImageUtils.getCopyOf(image);
		double[][] output = convolute(imageToColorArray(image, ImageColor.RED), kernel, factor);
		for (int x = 0; x < output.length; x++) {
			for (int y = 0; y < output[x].length; y++) {
				imageCopy.setRGB(x, y, colorToRgb(output[x][y]));
			}
		}

		return imageCopy;
	}

	public static BufferedImage convolutePadded(BufferedImage image, double[][] kernel,
			double factor) {
		BufferedImage imageCopy = ImageUtils.getCopyOf(image);
		double[][] output = convolutePadded(imageToColorArray(image, ImageColor.RED), kernel,
				factor);
		for (int x = 0; x < output.length; x++) {
			for (int y = 0; y < output[x].length; y++) {
				imageCopy.setRGB(x, y, colorToRgb(output[x][y]));
			}
		}

		return imageCopy;
	}

	public static BufferedImage convoluteWithCalculatedFactor(BufferedImage image,
			double[][] kernel) {
		BufferedImage imageCopy = ImageUtils.getCopyOf(image);
		double[][] output = convoluteWithCalculatedFactor(imageToColorArray(image, ImageColor.RED),
				kernel);
		for (int x = 0; x < output.length; x++) {
			for (int y = 0; y < output[x].length; y++) {
				imageCopy.setRGB(x, y, colorToRgb(output[x][y]));
			}
		}

		return imageCopy;
	}

//	private static void print(double[][] arr) {
//		for (int i = 0; i < arr.length; i++) {
//			if (arr[i] == null) {
//				System.out.println("----");
//				continue;
//			}
//			for (int j = 0; j < arr[i].length; j++) {
//				System.out.print(arr[i][j] + "\t");
//			}
//			System.out.println();
//		}
//	}

}
