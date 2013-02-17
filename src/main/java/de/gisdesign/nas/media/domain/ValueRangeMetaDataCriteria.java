package de.gisdesign.nas.media.domain;

/**
 *
 * @author Denis Pasek
 */
public class ValueRangeMetaDataCriteria<T> extends MetaDataCriteria<CriteriaRange<T>> {

    public ValueRangeMetaDataCriteria(String id) {
        super(id);
    }

    @Override
    public String getValueAsString() {
        CriteriaRange<T> range = getValue();
        return range != null ? buildRangeString(range) : null;
    }

    @Override
    protected MetaDataCriteria<CriteriaRange<T>> createClone(String id) {
        return new ValueRangeMetaDataCriteria<T>(getId());
    }

    private String buildRangeString(CriteriaRange<T> range) {
        StringBuilder sb = new StringBuilder();
        if (range.getMinValue() != null)  {
            sb.append(range.getMinValue());
        }
        sb.append("-");
        if (range.getMaxValue() != null)  {
            sb.append(range.getMaxValue());
        }
        return sb.toString();
    }
}
