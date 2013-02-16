package de.gisdesign.nas.media.domain;

/**
 *
 * @author Denis Pasek
 */
public class CriteriaRange<T> {

    private T minValue;

    private T maxValue;

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
