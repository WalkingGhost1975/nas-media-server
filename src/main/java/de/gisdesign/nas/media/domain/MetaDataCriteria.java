package de.gisdesign.nas.media.domain;

import org.apache.commons.lang.Validate;

/**
 * Represents a node in a hierarchy of criteria used to match certain media files.
 * @param <T> The type of value supported.
 * @author Denis Pasek
 */
public abstract class MetaDataCriteria<T> {

    /**
     * The target {@link MediaFileType} of this criteria.
     */
    private MediaFileType mediaFileType;

    /**
     * The name of the criteria.
     */
    private String name;

    /**
     * The value of the criteria.
     */
    private T value;

    /**
     * The parent criteria.
     */
    private MetaDataCriteria<?> parent;

    /**
     * The child criteria.
     */
    private MetaDataCriteria<?> childCriteria;

    /**
     * Constructor.
     * @param mediaFileType The target {@link MediaFileType}.
     * @param criteriaName The name of the criteria.
     */
    public MetaDataCriteria(MediaFileType mediaFileType, String criteriaName) {
        Validate.notNull(mediaFileType, "MediaFileType is null.");
        Validate.notNull(criteriaName, "MetaDataCriteria name is null.");
        this.mediaFileType = mediaFileType;
        this.name = criteriaName;
    }

    public MediaFileType getMediaFileType() {
        return mediaFileType;
    }

    public String getName() {
        return name;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value)  {
        this.value = value;
    }

    public abstract String getValueAsString();

    public abstract void setValueAsString(String value);

    public MetaDataCriteria<?> getParent() {
        return parent;
    }

    public void setParent(MetaDataCriteria<?> parent) {
        this.parent = parent;
    }

    public MetaDataCriteria<?> getChildCriteria() {
        return childCriteria;
    }

    public void setChildCriteria(MetaDataCriteria<?> childCriteria) {
        this.childCriteria = childCriteria;
        this.childCriteria.setParent(this);
    }

    /**
     * Checks whether this criteria is a leaf criteria. Leaf criteria can be used
     * to match a set of media files while non-leaf criteria can used to match
     * a list of possible criteria options.
     * @return <code>true</code> if this criteria is leaf criteria.
     */
    public boolean isLeaf() {
        return (value != null && childCriteria == null);
    }

    /**
     * Performs a deep copy of this criteria and all the referenced child criteria.
     * @return The deep copy of this {@link MetaDataCriteria}.
     */
    public MetaDataCriteria<T> copy() {
        MetaDataCriteria<T> clone = createClone(mediaFileType, name);
        clone.value = value;
        clone.childCriteria = (childCriteria != null) ? childCriteria.copy() : null;
        return clone;
    }

    /**
     * Dumps the hoerachy of the {@link MetaDataCriteria} into a human readble form.
     * Only used for debugging purposes.
     * @return The string representation of the {@link MetaDataCriteria} hierachy.
     */
    public String dumpHierarchy()  {
        MetaDataCriteria<?> currentCriteria = this;
        StringBuilder sb = new StringBuilder();
        int level = 0;
        while (currentCriteria != null) {
            StringBuilder criteria = new StringBuilder();
            criteria.append(currentCriteria.getName()).append(":").append(currentCriteria.getValue());
            if (level > 0) {
                criteria.append("; ");
            }
            sb.insert(0, criteria);
            currentCriteria = currentCriteria.getParent();
            level++;
        }
        return sb.toString();
    }

    /**
     * Template method to be implemented by subclass. Should instantiate a clone.
     * It is not necessary to copy parent
     * @param mediaFileType The {@link MediaFileType}.
     * @param name The name of the criteria.
     * @return The prepared clone.
     */
    protected abstract MetaDataCriteria<T> createClone(MediaFileType mediaFileType, String name);
}
