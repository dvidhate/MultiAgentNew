package learningalgo.learning.framework;

/**
 *
 *
 */
public interface AttributeSpecification {

    boolean isValid(String string);

    String getAttributeName();

    Attribute createAttribute(String rawValue);
}
