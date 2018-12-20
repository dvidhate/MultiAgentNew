package learningalgo.agent.impl.aprog.simplerule;

import learningalgo.agent.impl.ObjectWithDynamicAttributes;

/**
 * Base abstract class for describing conditions.
 *
 *
 *
 */
public abstract class Condition {

    public abstract boolean evaluate(ObjectWithDynamicAttributes p);

    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof Condition)) {
            return super.equals(o);
        }
        return (toString().equals(((Condition) o).toString()));
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }
}
