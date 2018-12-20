package learningalgo.agent.impl.aprog.simplerule;

import learningalgo.agent.impl.ObjectWithDynamicAttributes;

/**
 * Implementation of a NOT condition.
 *
 *
 *
 */
public class NOTCondition extends Condition {

    private Condition con;

    public NOTCondition(Condition con) {
        assert (null != con);

        this.con = con;
    }

    @Override
    public boolean evaluate(ObjectWithDynamicAttributes p) {
        return (!con.evaluate(p));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        return sb.append("![").append(con).append("]").toString();
    }
}
