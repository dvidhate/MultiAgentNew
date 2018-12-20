/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package learningalgo.util.math;

import java.util.ArrayList;

/**
 *
 * @author vision soft
 */
public class Base {

    public double[] wt = new double[]{};
    public ArrayList algo = new ArrayList();
    public String graphname;

    public void initData() {
        wt = new double[]{1, 0.9, 0.7};
        algo.add("SarsaLamda");
        algo.add("Sarsa");
        algo.add("QLearning");
    }

}
