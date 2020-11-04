package de.hsharz.images.filter;

import java.awt.image.BufferedImage;

public interface Filter {

	BufferedImage perform(BufferedImage img);
	
}
