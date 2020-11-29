package de.hsharz.images.filter;

public abstract class AbstractMaskFilter extends AbstractFilter {

	protected double[][] mask = getMask();

	protected abstract double[][] getMask();

}
