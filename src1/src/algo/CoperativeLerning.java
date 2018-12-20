package algo;

 
import algo.rule;
import static algo.ReinforcementLearningAgentTest.test_utility_learning_rates;
import java.util.ArrayList;
import java.util.Map;
import learningalgo.agent.impl.AbstractEnvironment;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import learningalgo.environment.rule.Rile;
import learningalgo.environment.rule.RuleData;
import learningalgo.environment.rule.RuleDataAction;
import learningalgo.environment.rule.RuleDataFactory;
import learningalgo.learning.reinforcement.agent.QLearningAgent;
import learningalgo.learning.reinforcement.example.RuleEnvironment;
import learningalgo.probability.example.MDPFactory;
import learningalgo.util.JavaRandomizer;

public class CoperativeLerning  {
    //

    private RuleData<Double> cw = null;
    public RuleEnvironment cwe = null;
    private QLearningAgent<Rile<Double>, RuleDataAction> qla = null;

    @Before
    public void setUp() {
        cw = RuleDataFactory.createRuleDataForFig17_1();
        cwe = new RuleEnvironment(
                cw.getCellAt(1, 1),
                cw.getCells(),
                MDPFactory.createTransitionProbabilityFunctionForFigure17_1(cw),
                new JavaRandomizer());

        qla = new QLearningAgent<Rile<Double>, RuleDataAction>(MDPFactory
                .createActionsFunctionForFigure17_1(cw),
                RuleDataAction.None, 0.2, 1.0, 5, 2.0);

        cwe.addAgent(qla);
    }

    public static ArrayList<algo.RuleList> test_COPQ_learning(ArrayList<algo.RuleList> r, ArrayList<algo.RuleList> r1) { 

        ArrayList<algo.RuleList> alrule = new ArrayList(); 

        for (int i = 0; i < r.size(); i++) {
            for (int j = 0; j < r1.size(); j++) {
                if (r.get(i).equalCol(r1.get(j).col, r1.get(j).val)) {
                    String col[] = r1.get(j).cloneArray(r1.get(j).col);
                    String val[] = r1.get(j).cloneArray(r1.get(j).val);
                    for (int k = 0; k < col.length; k++) {
                        if (col[k].equals("Name")) {
                            val[k] = val[k] + ":" + r.get(i).val[k];
                        }
                    }
                    alrule.add(new algo.RuleList(col, val, (r1.get(j).confidence + r.get(i).confidence) / 2, new int[]{}));
                }
            }
        }
        return alrule;
    }

    // Note: Enable this test if you wish to generate tables for
    // creating figures, in a spreadsheet, of the learning
    // rate of the agent.
    @Ignore
    @Test
    public void test_Q_learning_rate() {
        test_utility_learning_rates(qla, 20, 10000, 500, 20);
    }

    public static void main(String[] args) {
        QLRLearningAgent  qt = new QLRLearningAgent();
        qt.setUp();
        //     qt.test_Q_learning();
    }
}
