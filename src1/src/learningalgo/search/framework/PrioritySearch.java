package learningalgo.search.framework;

import java.util.Comparator;
import java.util.List;

import learningalgo.agent.Action;
import learningalgo.util.datastructure.PriorityQueue;

/**
 *
 * @author Ruediger Lunde
 */
public class PrioritySearch implements Search {

    private final QueueSearch search;
    private final Comparator<Node> comparator;

    public PrioritySearch(QueueSearch search, Comparator<Node> comparator) {
        this.search = search;
        this.comparator = comparator;
        if (search instanceof GraphSearch) {
            ((GraphSearch) search)
                    .setReplaceFrontierNodeAtStateCostFunction(comparator);
        }
    }

    public List<Action> search(Problem p) throws Exception {
        return search.search(p, new PriorityQueue<Node>(5, comparator));
    }

    public Metrics getMetrics() {
        return search.getMetrics();
    }
}
