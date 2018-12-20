package learningalgo.agent.impl.aprog.simplerule;

import learningalgo.agent.impl.ObjectWithDynamicAttributes;

/**
 * Implementation of an AND condition.
 *
 *
 *
 */
public class ANDCondition extends Condition {

    private Condition left;

    private Condition right;

    public ANDCondition(Condition leftCon, Condition rightCon) {
        assert (null != leftCon);
        assert (null != rightCon);

        left = leftCon;
        right = rightCon;
    }

    @Override
    public boolean evaluate(ObjectWithDynamicAttributes p) {
        return (left.evaluate(p) && right.evaluate(p));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        return sb.append("[").append(left).append(" && ").append(right)
                .append("]").toString();
    }
}
