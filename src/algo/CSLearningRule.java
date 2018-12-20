/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algo;

import java.util.ArrayList;
import java.util.Random;

public class CSLearningRule {

    public MapState m = new MapState();
    public MapState m1 = new MapState();
    public MapState m2 = new MapState();
    public MapState m3 = new MapState();
    public Graph r2 = null;

    public void cslearn(String file1, String file2, ArrayList<Graph> al, int algo) {

        m.getState(file1);
        Graph r = al.get(0);
        r.rewardalgo = algo;
        m.start(r);

        m1.getState(file2);
        Graph r1 = al.get(1);
        r1.rewardalgo = algo;
        m1.start(r1);

        m2.getState(file1);
        r2 = al.get(2);
        r2.cslearning = true;
        r2.addLearning(r);
        r.index = 0;
        r2.addLearning(r1);
        r1.index = 1;
        r2.index = 0;
        m2.start(r2);

        m3.getState(file2);
        Graph r3 = al.get(3);
        r3.cslearning = true;
        r3.rewardalgo = algo;
        r3.addLearning(r);
        r.index = 0;
        r3.addLearning(r1);
        r1.index = 1;
        r3.index = 1;
        m3.start(r3);

    }

    public void cslearn1(String file1, String file2, ArrayList<Graph> al, int algo) {
        Graph r = al.get(0);
        Graph r1 = al.get(1);

        m.getState(file1);

        r.rewardalgo = algo;
        m.start(r);

        m1.getState(file2);

        r1.rewardalgo = algo;
        m1.start(r1);

        m2.getState(file1);
        r2 = al.get(2);
        r2.cslearning = true;
        r2.rewardalgo = algo;
        r2.addLearning(r);
        r.index = 0;
        r2.addLearning(r1);
        r1.index = 1;
        r2.index = 0;
        m2.start(r2);

  
    }

    public static void main(String[] args) {
     }
 
}
