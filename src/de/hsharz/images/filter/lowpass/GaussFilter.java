package de.hsharz.images.filter.lowpass;

public class GaussFilter extends LowPassFilter {

	@Override
	protected double[][] getMask() {
		return new double[][] { //
				{ 1, 2, 1 }, //
				{ 2, 4, 2 }, //
				{ 1, 2, 1 } //
		};
	}

}
