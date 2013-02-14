package de.gisdesign.nas.media.domain;

/**
 *
 * @author Denis Pasek
 */
public class SingleValueMetaDataCriteria extends MetaDataCriteria<String> {

    public SingleValueMetaDataCriteria(MediaFileType mediaFileType, String criteriaName) {
        super(mediaFileType, criteriaName);
    }

    @Override
    public String getValueAsString() {
        return getValue();
    }

    @Override
    public void setValueAsString(String value) {
        setValue(value);
    }

    @Override
    protected MetaDataCriteria<String> createClone(MediaFileType mediaFileType, String name) {
        return new SingleValueMetaDataCriteria(mediaFileType, name);
    }

}
