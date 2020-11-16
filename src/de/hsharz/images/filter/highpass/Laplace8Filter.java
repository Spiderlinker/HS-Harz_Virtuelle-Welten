package de.hsharz.images.filter.highpass;

public class Laplace8Filter extends HighPassFilter {

	@Override
	protected double[][] getMask() {
		return new double[][] { //
				{ 1, 1, 1 }, //
				{ 1, -8, 1 }, //
				{ 1, 1, 1 } //
		};
	}

}
