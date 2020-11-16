package de.hsharz.images.filter.lowpass;

public class RectangularFilter extends LowPassFilter {

	@Override
	protected double[][] getMask() {
		return new double[][] { //
				{ 1, 1, 1 }, //
				{ 1, 1, 1 }, //
				{ 1, 1, 1 } //
		};
	}

}
