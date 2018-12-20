package learningalgo.agent.impl.aprog.simplerule;

import learningalgo.agent.impl.ObjectWithDynamicAttributes;

/**
 * Implementation of an EQUALity condition.
 *
 *
 *
 */
public class EQUALCondition extends Condition {

    private Object key;

    private Object value;

    public EQUALCondition(Object key, Object value) {
        assert (null != key);
        assert (null != value);

        this.key = key;
        this.value = value;
    }

    @Override
    public boolean evaluate(ObjectWithDynamicAttributes p) {
        return value.equals(p.getAttribute(key));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        return sb.append(key).append("==").append(value).toString();
    }
}
