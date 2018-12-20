package learningalgo.search.uninformed;

import java.util.List;

import learningalgo.agent.Action;
import learningalgo.search.framework.Metrics;
import learningalgo.search.framework.Node;
import learningalgo.search.framework.Problem;
import learningalgo.search.framework.QueueSearch;
import learningalgo.search.framework.Search;
import learningalgo.util.datastructure.LIFOQueue;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 85.<br>
 * <br>
 * Depth-first search always expands the deepest node in the current frontier of
 * the search tree. <br>
 * <br>
 * <b>Note:</b> Supports both Tree and Graph based versions by assigning an
 * instance of TreeSearch or GraphSearch to its constructor.
 *
 *
 *
 */
public class DepthFirstSearch implements Search {

    QueueSearch search;

    public DepthFirstSearch(QueueSearch search) {
        this.search = search;
    }

    public List<Action> search(Problem p) {
        return search.search(p, new LIFOQueue<Node>());
    }

    public Metrics getMetrics() {
        return search.getMetrics();
    }
}
