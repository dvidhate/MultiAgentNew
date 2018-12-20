package learningalgo.learning.reinforcement.example;

import learningalgo.environment.rule.Rile;
import learningalgo.learning.reinforcement.PerceptStateReward;

/**
 * An implementation of the PerceptStateReward interface for the cell world
 * environment. Note: The getCell() and setCell() methods allow a single percept
 * to be instantiated per agent within the environment. However, if an agent
 * tracks its perceived percepts it will need to explicitly copy the relevant
 * information.
 *
 * @author oreilly
 *
 */
public class RulePercept implements PerceptStateReward<Rile<Double>> {

    private Rile<Double> cell = null;

    /**
     * Constructor.
     *
     * @param cell the cell within the environment that the percept refers to.
     */
    public RulePercept(Rile<Double> cell) {
        this.cell = cell;
    }

    /**
     *
     * @return the cell within the environment that the percept refers to.
     */
    public Rile<Double> getCell() {
        return cell;
    }

    /**
     * Set the cell within the environment that the percept refers to.
     *
     * @param cell the cell within the environment that the percept refers to.
     */
    public void setCell(Rile<Double> cell) {
        this.cell = cell;
    }

    //
    // START-PerceptStateReward
    @Override
    public double reward() {
        return cell.getContent().doubleValue();
    }

    @Override
    public Rile<Double> state() {
        return cell;
    }

	// END-PerceptStateReward
    //
}
