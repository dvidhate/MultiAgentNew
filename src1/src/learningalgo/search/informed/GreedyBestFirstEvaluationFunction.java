package learningalgo.search.informed;

import learningalgo.search.framework.EvaluationFunction;
import learningalgo.search.framework.HeuristicFunction;
import learningalgo.search.framework.Node;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 92.<br>
 * <br>
 * Greedy best-first search tries to expand the node that is closest to the
 * goal, on the grounds that this is likely to lead to a solution quickly. Thus,
 * it evaluates nodes by using just the heuristic function; that is, f(n) = h(n)
 *
 *
 *
 */
public class GreedyBestFirstEvaluationFunction implements EvaluationFunction {

    private HeuristicFunction hf = null;

    public GreedyBestFirstEvaluationFunction(HeuristicFunction hf) {
        this.hf = hf;
    }

    public double f(Node n) {
        // f(n) = h(n)
        return hf.h(n.getState());
    }
}
