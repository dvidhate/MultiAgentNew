package algolearning;

import algo.*;
import algo.rule;
import java.util.Map;
import learningalgo.agent.impl.AbstractEnvironment;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import learningalgo.learning.reinforcement.agent.QLearningAgent;
 
import learningalgo.util.JavaRandomizer;

public class QLRLearningAgent extends ReinforcementLearningAgentTest {
    //

    private RuleData<Double> cw = null;
    public RuleEnvironment cwe = null;
    private QLearningAgent<Cell<Double>, RuleDataAction> qla = null;
    
    @Before
    public void setUp(String file) {
           ac.load(file);
        cw = RuleDataFactory.createRuleDataForFig17_1(ac);
        cwe = new RuleEnvironment(
                cw.getCellAt(1, 1), 
                cw.getCells(),
                MDPFactory.createTransitionProbabilityFunctionForFigure17_1(cw),
                new JavaRandomizer(),ac);
        
        qla = new QLearningAgent<Cell<Double>, RuleDataAction>(MDPFactory
                .createActionsFunctionForFigure17_1(cw),
                RuleDataAction.None, 0.2, 1.0, 5, 2.0);
        
        cwe.addAgent(qla);
    }
        AgentBoolean ac = new AgentBoolean();
    @Test
    public void test_Q_learning( boolean flag) {
    
     
        qla.reset();        
      
        System.out.println("Start Qlearning Trails ...");
        //    rule.init(cwe);
        cwe.executeTrials(10000);
        
        Map<Cell<Double>, Double> U = qla.getUtility();
        
        Assert.assertNotNull(U.get(cw.getCellAt(1, 1)));

        // Note:
        // As the Q-Learning Agent is not using a fixed
        // policy it should with a reasonable number
        // of iterations observe and calculate an
        // approximate utility for all of the states.
        Assert.assertEquals(11, U.size());
        //  System.out.println(U.toString());
//        for (int i = 0; i < U.size(); i++) {
//            for (int j = 0; j < ; j++) {
//                
//            }
//        }
        // Note: Due to stochastic nature of environment,
        // will not test the individual utilities calculated
        // as this will take a fair amount of time.
        // Instead we will check if the RMS error in utility
        // for 1,1 is below a reasonable threshold.\
        if (flag) {
           // cwe = test_RMSeiu_for_1_1(qla, 20, 10000, 0.2, cwe);
        }
    }

    // Note: Enable this test if you wish to generate tables for
    // creating figures, in a spreadsheet, of the learning
    // rate of the agent.
    @Ignore
    @Test
    public void test_Q_learning_rate() {
       // test_utility_learning_rates(qla, 20, 10000, 500, 20);
    }
    
    public static void main(String[] args) {
        QLRLearningAgent qt = new QLRLearningAgent();
        qt.setUp("agent.xml");
        qt.test_Q_learning( true);
    }
}
