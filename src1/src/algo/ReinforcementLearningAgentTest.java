package algo;

import algo.rule;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
 
import org.junit.Assert;

import learningalgo.environment.rule.Rile;
import learningalgo.environment.rule.RuleData;
import learningalgo.environment.rule.RuleDataAction;
import learningalgo.environment.rule.RuleDataFactory;
import learningalgo.learning.reinforcement.agent.ReinforcementAgent;
import learningalgo.learning.reinforcement.example.RuleEnvironment;
import learningalgo.probability.example.MDPFactory;
import learningalgo.util.JavaRandomizer;

public abstract class ReinforcementLearningAgentTest {

    public static RuleEnvironment test_RMSeiu_for_1_1(
            ReinforcementAgent<Rile<Double>, RuleDataAction> reinforcementAgent,
            int numRuns, int numTrialsPerRun,
            double expectedErrorLessThan, RuleEnvironment cwe1) {
        RuleData<Double> cw = RuleDataFactory.createRuleDataForFig17_1();
        RuleEnvironment cwe = new RuleEnvironment(
                cw.getCellAt(1, 1),
                cw.getCells(),
                MDPFactory.createTransitionProbabilityFunctionForFigure17_1(cw),
                new JavaRandomizer());
        cwe.file = cwe1.file;

        cwe.addAgent(reinforcementAgent);
        System.out.println("Start Reinforcement LearningAgent Trails ...");
        Map<Integer, Map<Rile<Double>, Double>> runs = new HashMap<Integer, Map<Rile<Double>, Double>>();
        rule.init1(cwe);

        for (int r = 0; r < numRuns; r++) {
            reinforcementAgent.reset();

            cwe.executeTrials(numTrialsPerRun);
            runs.put(r, reinforcementAgent.getUtility());
        }

        // Calculate the Root Mean Square Error for utility of 1,1
        // for this trial# across all runs
        double xSsquared = 0;
        for (int r = 0; r < numRuns; r++) {
            Map<Rile<Double>, Double> u = runs.get(r);
            Double val1_1 = u.get(cw.getCellAt(1, 1));
            //     System.out.println(u.toString());
            if (null == val1_1) {
                throw new IllegalStateException("U(1,1,) is not present: r=" + r + ", u=" + u);
            }
            xSsquared += Math.pow(0.705 - val1_1, 2);
        }
        double rmse = Math.sqrt(xSsquared / runs.size());
        // System.out.println("" + rmse + " is not < " + expectedErrorLessThan + ":" + (rmse < expectedErrorLessThan));
        return cwe;
    }

    public static void test_utility_learning_rates(
            ReinforcementAgent<Rile<Double>, RuleDataAction> reinforcementAgent,
            int numRuns, int numTrialsPerRun, int rmseTrialsToReport,
            int reportEveryN) {

        if (rmseTrialsToReport > (numTrialsPerRun / reportEveryN)) {
            throw new IllegalArgumentException(
                    "Requesting to report too many RMSE trials, max allowed for args is "
                    + (numTrialsPerRun / reportEveryN));
        }

        RuleData<Double> cw = RuleDataFactory.createRuleDataForFig17_1();
        RuleEnvironment cwe = new RuleEnvironment(
                cw.getCellAt(1, 1),
                cw.getCells(),
                MDPFactory.createTransitionProbabilityFunctionForFigure17_1(cw),
                new JavaRandomizer());

        cwe.addAgent(reinforcementAgent);

        Map<Integer, List<Map<Rile<Double>, Double>>> runs = new HashMap<Integer, List<Map<Rile<Double>, Double>>>();
        for (int r = 0; r < numRuns; r++) {
            reinforcementAgent.reset();
            List<Map<Rile<Double>, Double>> trials = new ArrayList<Map<Rile<Double>, Double>>();
            for (int t = 0; t < numTrialsPerRun; t++) {
                cwe.executeTrial();
                if (0 == t % reportEveryN) {
                    Map<Rile<Double>, Double> u = reinforcementAgent.getUtility();
                    if (null == u.get(cw.getCellAt(1, 1))) {
                        throw new IllegalStateException("Bad Utility State Encountered: r=" + r + ", t=" + t + ", u=" + u);
                    }
                    trials.add(u);
                }
            }
            runs.put(r, trials);
        }

        StringBuilder v4_3 = new StringBuilder();
        StringBuilder v3_3 = new StringBuilder();
        StringBuilder v1_3 = new StringBuilder();
        StringBuilder v1_1 = new StringBuilder();
        StringBuilder v3_2 = new StringBuilder();
        StringBuilder v2_1 = new StringBuilder();
        for (int t = 0; t < (numTrialsPerRun / reportEveryN); t++) {
            // Use the last run
            Map<Rile<Double>, Double> u = runs.get(numRuns - 1).get(t);
            v4_3.append((u.containsKey(cw.getCellAt(4, 3)) ? u.get(cw
                    .getCellAt(4, 3)) : 0.0)
                    + "\t");
            v3_3.append((u.containsKey(cw.getCellAt(3, 3)) ? u.get(cw
                    .getCellAt(3, 3)) : 0.0)
                    + "\t");
            v1_3.append((u.containsKey(cw.getCellAt(1, 3)) ? u.get(cw
                    .getCellAt(1, 3)) : 0.0)
                    + "\t");
            v1_1.append((u.containsKey(cw.getCellAt(1, 1)) ? u.get(cw
                    .getCellAt(1, 1)) : 0.0)
                    + "\t");
            v3_2.append((u.containsKey(cw.getCellAt(3, 2)) ? u.get(cw
                    .getCellAt(3, 2)) : 0.0)
                    + "\t");
            v2_1.append((u.containsKey(cw.getCellAt(2, 1)) ? u.get(cw
                    .getCellAt(2, 1)) : 0.0)
                    + "\t");
        }
//        System.out.println("(4,3)" + "\t" + v4_3);
//        System.out.println("(3,3)" + "\t" + v3_3);
//        System.out.println("(1,3)" + "\t" + v1_3);
//        System.out.println("(1,1)" + "\t" + v1_1);
//        System.out.println("(3,2)" + "\t" + v3_2);
//        System.out.println("(2,1)" + "\t" + v2_1);

        StringBuilder rmseValues = new StringBuilder();
        for (int t = 0; t < rmseTrialsToReport; t++) {
            // Calculate the Root Mean Square Error for utility of 1,1
            // for this trial# across all runs
            double xSsquared = 0;
            for (int r = 0; r < numRuns; r++) {
                Map<Rile<Double>, Double> u = runs.get(r).get(t);
                Double val1_1 = u.get(cw.getCellAt(1, 1));
                if (null == val1_1) {
                    throw new IllegalStateException("U(1,1,) is not present: r=" + r + ", t=" + t + ", runs.size=" + runs.size() + ", runs(r).size()=" + runs.get(r).size() + ", u=" + u);
                }
                xSsquared += Math.pow(0.705 - val1_1, 2);
            }
            double rmse = Math.sqrt(xSsquared / runs.size());
            rmseValues.append(rmse);
            rmseValues.append("\t");
        }
        System.out.println("RMSeiu" + "\t" + rmseValues);
    }
}
