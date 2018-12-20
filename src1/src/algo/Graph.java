package algo;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;
import learningalgo.util.math.Base;

/**
 *
 * @author vision soft
 */
public class Graph extends Base {

    Hashtable<Integer, GraphCount> rewardvsitreation = new Hashtable();
    public int iteration;
    static double epslon = 0.1;
    public int Q_SIZE;
    public double GAMMA = 0.8;
    public double lr = 0.1;
    public double alpha = 0.3;
    public double lamda = 0.9;
    public int ITERATIONS = 500;
    public int NUM_INITIALS = 6;
    public int GOAL_STATE = -1;
    public int INITIAL_STATES[];
    public double maxwt;
    public double q[][];
    public double revard;
    public double revardNrm;
    public double revardPos;
    public double revardNag;
    public double revardAbs;
    public boolean cslearning;
    public ArrayList<Graph> alcslear = new ArrayList();
    public int index;

    public class GraphCount {

        double val;
        int count;

        public GraphCount(double val, int count) {
            this.val = val;
            this.count = count;
        }

    }

    public void train() {

    }

    public void initcslearing(int i) {
        index = i;

    }

    public void addLearning(Graph g) {
        alcslear.add(g);
        g.index = alcslear.size() - 1;

    }

    public void init(int Q_SIZE, int[] INITIAL_STATES, double[][] R) {

    }

    public void updateVal(double val) {
        if (rewardvsitreation.get(iteration) == null) {
            rewardvsitreation.put(iteration, new GraphCount(val, 1));
        } else {
            rewardvsitreation.get(iteration).count++;
            if (val > 0) {
                rewardvsitreation.get(iteration).val += val;
            }
        }
    }

    public void toGraph(boolean flag) {
        FileWriter fr = null;
        try {
            fr = new FileWriter(new File(cslearning == false ? "iteartionvsrevrd.txt" : "csiteartionvsrevrd.txt"), flag);
            for (int i = 0; i < ITERATIONS; i += 50) {
                //    rewardvsitreation.get(i).val = rewardvsitreation.get(i).val / rewardvsitreation.get(i).count;
                fr.write(i + "," + getRoeuntWT(rewardvsitreation.get(i).val) + "," + (  graphname) + "\n");
            }
        } catch (Exception ex) {
            Logger.getLogger(Graph.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fr.close();
            } catch (IOException ex) {
                Logger.getLogger(Graph.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void toGraphLR(boolean flag) {
        FileWriter fr = null;
        try {
            fr = new FileWriter(new File(cslearning == false ? "lrrevrd.txt" : "cslrrevrd.txt"), flag);
            //for (int i = 0; i < ITERATIONS; i += 50) {
            //    rewardvsitreation.get(i).val = rewardvsitreation.get(i).val / rewardvsitreation.get(i).count;
            fr.write(lr + "," + getRoeuntWT(rewardvsitreation.get(ITERATIONS - 1).val) + "," + (cslearning == false ? graphname : "CS_" + graphname) + "\n");
            //}
        } catch (Exception ex) {
            Logger.getLogger(Graph.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fr.close();
            } catch (IOException ex) {
                Logger.getLogger(Graph.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void toGraphGAMMA(boolean flag) {
        FileWriter fr = null;
        try {
            fr = new FileWriter(new File(cslearning == false ? "gammarevrd.txt" : "cdgammarevrd.txt"), flag);
            //for (int i = 0; i < ITERATIONS; i += 50) {
            //    rewardvsitreation.get(i).val = rewardvsitreation.get(i).val / rewardvsitreation.get(i).count;
            fr.write(GAMMA + "," + getRoeuntWT(rewardvsitreation.get(ITERATIONS - 1).val) + "," + (cslearning == false ? graphname : "CL_" + graphname) + "\n");
            //}
        } catch (Exception ex) {
            Logger.getLogger(Graph.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fr.close();
            } catch (IOException ex) {
                Logger.getLogger(Graph.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void expertnessMeasures(double rewrd) {
        revardNrm += revard;
        revardAbs += Math.abs(revard);
        revardNag += revard < 0 ? rewrd : 0;
        revardPos += revard > 0 ? rewrd : 0;

    }

    public void toProductGraph(MapState m) {
        FileWriter fr = null;
        try {
            fr = new FileWriter(new File("product.txt"));
            for (int i = 0; i < m.state.size(); i++) {
                for (int j = 0; j < m.ht.get(m.state.get(i)).prod.size(); j++) {
                    fr.write(m.state.get(i) + "," + getRoeuntWT(m.ht.get(m.state.get(i)).action.get(m.ht.get(m.state.get(i)).prod.get(j)).intValue()) + "," + m.ht.get(m.state.get(i)).prod.get(j) + "\n");
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(Graph.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fr.close();
            } catch (IOException ex) {
                Logger.getLogger(Graph.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void toExcute(MapState m, Graph g) {

    }

    public static void creteCSGraph(String file1, String file2) {

        for (double i = 0.1; i < 0.8; i += 0.1) {
            CSLearningRule cs = new CSLearningRule();
            ArrayList<Graph> al = new ArrayList();
            for (int j = 0; j < 4; j++) {
                QLearningRule r1 = new QLearningRule();
                r1.lr = i;
                al.add(r1);
            }
            cs.cslearn(file1, file2, al);

            if (i == 0.1) {
                cs.r2.toGraphLR(false);
            } else {
                cs.r2.toGraphLR(true);
            }
            if (i == 0.1) {
                cs.r2.toGraph(false);
            }
        }
        for (double i = 0.1; i < 0.8; i += 0.1) {

            CSLearningRule cs = new CSLearningRule();
            ArrayList<Graph> al = new ArrayList();
            for (int j = 0; j < 4; j++) {
                QLearningRule r1 = new QLearningRule();
                r1.GAMMA = i;
                al.add(r1);
            }
            cs.cslearn(file1, file2, al);

            if (i == 0.1) {
                cs.r2.toGraphGAMMA(false);
            } else {
                cs.r2.toGraphGAMMA(true);
            }
        }

        {
            for (double i = 0.1; i < 0.8; i += 0.1) {

                CSLearningRule cs = new CSLearningRule();
                ArrayList<Graph> al = new ArrayList();
                for (int j = 0; j < 4; j++) {
                    SarasaRule r1 = new SarasaRule();
                    r1.lr = i;
                    al.add(r1);
                }
                cs.cslearn(file1, file2, al);

                cs.r2.toGraphLR(true);
                if (i == 0.1) {
                    cs.r2.toGraph(false);
                }
            }
            for (double i = 0.1; i < 0.8; i += 0.1) {

                CSLearningRule cs = new CSLearningRule();
                ArrayList<Graph> al = new ArrayList();
                for (int j = 0; j < 4; j++) {
                    SarasaRule r1 = new SarasaRule();
                    r1.GAMMA = i;
                    al.add(r1);
                }
                cs.cslearn(file1, file2, al);

                cs.r2.toGraphLR(true);

                cs.r2.toGraphGAMMA(true);

            }

        }
        {
            for (double i = 0.1; i < 0.8; i += 0.1) {

                CSLearningRule cs = new CSLearningRule();
                ArrayList<Graph> al = new ArrayList();
                for (int j = 0; j < 4; j++) {
                    SarasaRuleLamda r1 = new SarasaRuleLamda();
                    r1.lr = i;
                    al.add(r1);
                }
                cs.cslearn(file1, file2, al);

                cs.r2.toGraphLR(true);
                if (i == 0.1) {
                    cs.r2.toGraph(false);
                }
            }
            for (double i = 0.1; i < 0.8; i += 0.1) {

                CSLearningRule cs = new CSLearningRule();
                ArrayList<Graph> al = new ArrayList();
                for (int j = 0; j < 4; j++) {
                    SarasaRuleLamda r1 = new SarasaRuleLamda();
                    r1.GAMMA = i;
                    al.add(r1);
                }
                cs.cslearn(file1, file2, al);

                cs.r2.toGraphLR(true);

                cs.r2.toGraphGAMMA(true);

            }
        }

        {
            for (double i = 0.1; i < 0.8; i += 0.1) {

                CSLearningRule cs = new CSLearningRule();
                ArrayList<Graph> al = new ArrayList();
                for (int j = 0; j < 4; j++) {
                    QLearningRuleLamda r1 = new QLearningRuleLamda();
                    r1.lr = i;
                    al.add(r1);
                }
                cs.cslearn(file1, file2, al);

                cs.r2.toGraphLR(true);
                if (i == 0.1) {
                    cs.r2.toGraph(false);
                }
            }
            for (double i = 0.1; i < 0.8; i += 0.1) {

                CSLearningRule cs = new CSLearningRule();
                ArrayList<Graph> al = new ArrayList();
                for (int j = 0; j < 4; j++) {
                    QLearningRuleLamda r1 = new QLearningRuleLamda();
                    r1.GAMMA = i;
                    al.add(r1);
                }
                cs.cslearn(file1, file2, al);

                cs.r2.toGraphLR(true);

                cs.r2.toGraphGAMMA(true);

            }
        }

    }

    public static void creteGraph(String file) {

        MapState m = new MapState();
        m.getState(file);
        QLearningRule r = new QLearningRule();
        m.start(r);
        for (double i = 0.1; i < 0.8; i += 0.1) {

            QLearningRule r1 = new QLearningRule();
            r1.lr = i;
            m.start(r1);
            if (i == 0.1) {
                r1.toGraphLR(false);
            } else {
                r1.toGraphLR(true);
            }
        }
        for (double i = 0.1; i < 0.8; i += 0.1) {

            QLearningRule r1 = new QLearningRule();
            r1.GAMMA = i;
            m.start(r1);
            if (i == 0.1) {
                r1.toGraphGAMMA(false);
            } else {
                r1.toGraphGAMMA(true);
            }
        }

        r.toGraph(false);
        r.toProductGraph(m);
        m = new MapState();
        m.getState(file);
        {
            SarasaRule r1 = new SarasaRule();
            m.start(r1);
            for (double i = 0.1; i < 0.8; i += 0.1) {

                SarasaRule r2 = new SarasaRule();
                r2.lr = i;
                m.start(r2);

                r2.toGraphLR(true);

            }
            for (double i = 0.1; i < 0.8; i += 0.1) {

                SarasaRule r2 = new SarasaRule();
                r2.GAMMA = i;
                m.start(r2);

                r2.toGraphGAMMA(true);

            }
            r1.toGraph(true);

        }
        {
            m = new MapState();
            m.getState(file);
            SarasaRuleLamda r1 = new SarasaRuleLamda();
            m.start(r1);
            for (double i = 0.1; i < 0.8; i += 0.1) {

                SarasaRuleLamda r2 = new SarasaRuleLamda();
                r2.lr = i;
                m.start(r2);

                r2.toGraphLR(true);

            }
            for (double i = 0.1; i < 0.8; i += 0.1) {

                SarasaRuleLamda r2 = new SarasaRuleLamda();
                r2.GAMMA = i;
                m.start(r2);

                r2.toGraphGAMMA(true);

            }
            r1.toGraph(true);
        }

    }

    public double getRoeuntWT(double r) {
        //System.out.println(algo.indexOf(graphname) +":"+graphname);
        return (int) (wt[algo.indexOf(graphname)] * r);
    }

    public static void main(String[] args) {
        creteCSGraph("agent.xml", "agent1.xml");
        creteGraph("agent.xml");
    }
}
