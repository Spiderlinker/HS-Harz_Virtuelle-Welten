package de.hsharz.images.filter.highpass;

public class Laplace4Filter extends HighPassFilter {

	@Override
	protected double[][] getMask() {
		return new double[][] { //
				{ 0, 1, 0 }, //
				{ 1, -4, 1 }, //
				{ 0, 1, 0 } //
		};
	}

}
