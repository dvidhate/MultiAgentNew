package learningalgo.search.framework;

import learningalgo.agent.impl.DynamicAction;

/**
 * A NoOp action that indicates a CutOff has occurred in a search. Used
 * primarily by DepthLimited and IterativeDeepening search routines.
 *
 *
 */
public class CutOffIndicatorAction extends DynamicAction {

    public static final CutOffIndicatorAction CUT_OFF = new CutOffIndicatorAction();

    //
    // START-Action
    public boolean isNoOp() {
        return true;
    }

    // END-Action
    //
    private CutOffIndicatorAction() {
        super("CutOff");
    }
}
