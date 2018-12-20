package algolearning;

import algo.AgentBoolean;
//import learningalgo.environment.rule.*;

/**
 *
 *
 *
 */
public class RuleDataFactory {

    /**
     * Create the cell world as defined in Figure 17.1 in AIMA3e. (a) A simple 4
     * x 3 environment that presents the agent with a sequential decision
     * problem.
     *
     * @return a cell world representation of Fig 17.1 in AIMA3e.
     */
    public static RuleData<Double> createRuleDataForFig17_1(AgentBoolean rec) {
        RuleData<Double> cw = new RuleData<Double>(rec.flag[0].length, rec.flag[0].length + 2, -0.04,rec);

        //    cw.removeCell(2, 2);
        for (int i = 1; i <= rec.flag[0].length; i++) {
            cw.getCellAt(i, rec.flag[0].length+1).setContent(1.0);
            cw.getCellAt(i, rec.flag[0].length + 2).setContent(-1.0);
        }
//        for (int i = 0; i < rec.flag.length; i++) {
//            cw.getCellAt( rec.flag.length - 2,i).setContent(1.0);
//            cw.getCellAt( rec.flag.length - 1,i).setContent(-1.0);
//        }
//
//        cw.getCellAt(4, 3).setContent(1.0);
//        cw.getCellAt(4, 2).setContent(-1.0);

        return cw;
    }
}
