package de.gisdesign.nas.media.domain;

/**
 *
 * @author Denis Pasek
 */
public class DiscreteValueMetaDataCriteria<T> extends MetaDataCriteria<T> {

    public DiscreteValueMetaDataCriteria(String id) {
        super(id);
    }

    @Override
    protected MetaDataCriteria<T> createClone(String id) {
        return new DiscreteValueMetaDataCriteria<T>(id);
    }

    @Override
    public String getValueAsString() {
        return getValue() != null ? String.valueOf(getValue()) : null;
    }

}
