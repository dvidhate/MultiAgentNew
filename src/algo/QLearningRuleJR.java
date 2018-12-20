/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algo;

import java.util.Random;

public class QLearningRuleJR extends Graph {

    public double R[][];

    public int currentState = 0;

    public QLearningRuleJR() {
        initData();
    }

    public QLearningRuleJR(int Q_SIZE, int[] INITIAL_STATES, double[][] R) {
        this.Q_SIZE = Q_SIZE + 1;
        GOAL_STATE = this.Q_SIZE;
        this.INITIAL_STATES = INITIAL_STATES;
        this.R = R;
        this.q = new double[Q_SIZE][Q_SIZE];
        NUM_INITIALS = INITIAL_STATES.length;

        graphname = "QLearning";

        initData();

    }

    public void init(int Q_SIZE, int[] INITIAL_STATES, double[][] R) {
        this.Q_SIZE = Q_SIZE;
        this.INITIAL_STATES = INITIAL_STATES;
        this.R = R;
        this.q = new double[Q_SIZE][Q_SIZE];
        NUM_INITIALS = INITIAL_STATES.length;
        graphname = "QLearning";
        csrevard = new double[R.length][R[0].length];
    }

    @Override
    public void train() {
        initialize();

        // Perform training, starting at all initial states.
        for (int j = 0; j < ITERATIONS; j++) {
            iteration = j;
            for (int i = 0; i < NUM_INITIALS; i++) {
                episode(INITIAL_STATES[i]);
            }
        }

        // Print out Q Matrix
//        System.out.println("Q Matrix values:");
//        for(int i = 0; i < Q_SIZE; i++)
//        {
//            for(int j = 0; j < Q_SIZE; j++)
//            {
//                System.out.print(q[i][j] + ",\t");
//            }
//            System.out.print("\n");
//        }
//        System.out.print("\n");
        return;
    }

    public void test() {
        int newState = 0;

        // Perform tests, starting at all initial states.
        System.out.println("  initial states:");
        for (int i = 0; i < NUM_INITIALS; i++) {
            currentState = INITIAL_STATES[i];
            newState = 0;
            do {
                newState = (int) maximum(currentState, true);
                System.out.print(currentState + ", ");
                currentState = newState;
            } while (currentState < GOAL_STATE); //Loop Until currentState = GOAL_STATE
            System.out.print(GOAL_STATE + "\n");
        }
        return;
    }

    public void episode(final int initialState) {
        currentState = initialState;

        // Travel from state to state until goal state is reached.
        do {
            chooseAnAction();
        } while (currentState == GOAL_STATE); // Loop Until currentState = GOAL_STATE

        // When currentState = GOAL_STATE, Run through the set once more to
        // for convergence.
        for (int i = 0; i < Q_SIZE; i++) {
            chooseAnAction();
        }
        return;
    }

    public void chooseAnAction() {
        int possibleAction = 0;

        // Randomly choose a possible action connected to the current state.
        possibleAction = getRandomAction(Q_SIZE);

        if (R[currentState][possibleAction] >= 0) {
            q[currentState][possibleAction] = qupdate(possibleAction);
            currentState = possibleAction;
        }
        return;
    }

    public int getRandomAction(final int upperBound) {
        int action = 0;
        boolean choiceIsValid = false;

        // Randomly choose a possible action connected to the current state.
        while (choiceIsValid == false) {
            // Get a random value between 0(inclusive) and UpperBound(exclusive).
            action = new Random().nextInt(upperBound);
            if (R[currentState][action] > -1) {
                choiceIsValid = true;
            }

        }
        return action;
    }

    public void initialize() {
        for (int i = 0; i < Q_SIZE; i++) {
            for (int j = 0; j < Q_SIZE; j++) {
                q[i][j] = 0;
            }
        }
        return;
    }

    public double maximum(final int state, final boolean returnIndexOnly) {
        // if(ReturnIndexOnly = true, the Q matrix index is returned.
        // if(ReturnIndexOnly = false, the Q matrix value is returned.
        int winner = 0;
        boolean foundNewWinner = false;
        boolean done = false;

        while (!done) {
            foundNewWinner = false;
            for (int i = 0; i < Q_SIZE; i++) {
                if (i != winner) {             // Avoid self-comparison.
                    if (q[state][i] > q[state][winner]) {
                        winner = i;
                        foundNewWinner = true;
                    }
                }
            }

            if (foundNewWinner == false) {
                done = true;
            }
        }

        if (returnIndexOnly == true) {
            return winner;
        } else {
            return q[state][winner];
        }
    }

    private double randomPolicy(final int state, final boolean returnIndexOnly) {
        // if(ReturnIndexOnly = true, the Q matrix index is returned.
        // if(ReturnIndexOnly = false, the Q matrix value is returned.
        int winner = 0;
        boolean foundNewWinner = false;
        boolean done = false;
        Random rnd = new Random(java.util.UUID.randomUUID().hashCode());
        winner = rnd.nextInt(Q_SIZE);
//        while (!done) {
//            foundNewWinner = false;
//            for (int i = 0; i < Q_SIZE; i++) {
//                if (i != winner) {             // Avoid self-comparison.
//                    if (q[state][i] > q[state][winner]) {
//                        winner = i;
//                        foundNewWinner = true;
//                    }
//                }
//            }
//
//            if (foundNewWinner == false) {
//                done = true;
//            }
//        }

        if (returnIndexOnly == true) {
            return winner;
        } else {
            return q[state][winner];
        }
    }

    public double selectAction(final int state, final boolean returnIndexOnly) {
        Random rnd = new Random(java.util.UUID.randomUUID().hashCode());

        if (rnd.nextFloat() > epslon) {
            return maximum(state, returnIndexOnly);
        } else {
            return randomPolicy(state, returnIndexOnly);
        }

    }

    public double qupdate(final int action) {
        double newstate = selectAction(action, false);
        double qupdate = (R[currentState][action] + (lr * ((GAMMA * (R[currentState][action] - newstate)))));
        if (!cslearning) {
            expertnessMeasures(qupdate, currentState, action);
        }
        if (cslearning) {
            return findJoindReward(qupdate, currentState, action);
        } else {
            updateVal(qupdate);
            return (qupdate);
        }

    }

//    public void main(String[] args) {
//        train();
//        for (int i = 0; i < q.length; i++) {
//            System.out.println("");
//            for (int j = 0; j < q[i].length; j++) {
//                System.out.print(q[i][j] + ",");
//            }
//        }
//        test();
//        return;
//    }
}
