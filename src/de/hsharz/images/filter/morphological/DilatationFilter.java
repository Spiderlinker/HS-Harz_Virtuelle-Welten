package de.hsharz.images.filter.morphological;

public class DilatationFilter extends MorphologicalFilter {

	@Override
	protected double[][] getMask() {
		return new double[][] { //
				{ 1, 1, 1 }, //
				{ 1, 1, 1 }, //
				{ 1, 1, 1 } //
		};
	}

	@Override
	protected int getColor() {
		return BLACK;
	}

}
