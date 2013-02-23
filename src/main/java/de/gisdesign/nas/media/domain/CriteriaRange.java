package de.gisdesign.nas.media.domain;

/**
 * Encapsulation of a value range.
 * @param <T> The type of the range values.
 * @author Denis Pasek
 */
public class CriteriaRange<T extends Comparable<T>> {

    /**
     * The min value.
     */
    private T minValue;

    /**
     * The max value.
     */
    private T maxValue;

    /**
     * Constructor.
     */
    public CriteriaRange() {
    }

    /**
     * Constructor.
     * @param minValue The min value.
     * @param maxValue The max value.
     */
    public CriteriaRange(T minValue, T maxValue) {
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    public T getMinValue() {
        return minValue;
    }

    public void setMinValue(T minValue) {
        this.minValue = minValue;
    }

    public T getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(T maxValue) {
        this.maxValue = maxValue;
    }
}
