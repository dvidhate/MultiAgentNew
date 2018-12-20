package learningalgo.probability.mdp;

import java.util.Set;

import learningalgo.agent.Action;

/**
 * An interface for MDP action functions.
 *
 * @param <S> the state type.
 * @param <A> the action type.
 *
 *
 *
 */
public interface ActionsFunction<S, A extends Action> {

    /**
     * Get the set of actions for state s.
     *
     * @param s the state.
     * @return the set of actions for state s.
     */
    Set<A> actions(S s);
}
