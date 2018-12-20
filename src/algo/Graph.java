package algo;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
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
    public double alpha = 0.6;
    public double lamda = 0.9;
    public int ITERATIONS = 500;
    public int NUM_INITIALS = 6;
    public int GOAL_STATE = -1;
    public int INITIAL_STATES[];
    public double maxwt;
    public double q[][];
    public double maxth = 15000;
    public boolean cslearning;
    public int rewardalgo;
    public ArrayList<Graph> alcslear = new ArrayList();
    public int index;
    public double csrevard[][];
    public MapState mt;
    public double b = 0.9;
    
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
            fr = new FileWriter(new File("graph/" + (cslearning == false ? "iteartionvsrevrd.txt" : "csiteartionvsrevrd" + rewardalgo + ".txt")), flag);
            for (int i = 0; i < ITERATIONS; i += 50) {
                //    rewardvsitreation.get(i).val = rewardvsitreation.get(i).val / rewardvsitreation.get(i).count;
                double val = getRoeuntWT(rewardvsitreation.get(i).val);
                if (val > maxth) {
                    val = maxth;
                }
                fr.write(i + "," + val + "," + (graphname) + "\n");
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
    public static double prevLR = 0;
    public static double prevGAMMA = 0;
    
    public void toGraphLR(boolean flag, boolean flag1) {
        FileWriter fr = null;
        try {
            if (!flag1) {
                prevLR = 0;
            }
            fr = new FileWriter(new File("graph/" + (cslearning == false ? "lrrevrd.txt" : "cslrrevrd" + rewardalgo + ".txt")), flag);
            //for (int i = 0; i < ITERATIONS; i += 50) {
            //    rewardvsitreation.get(i).val = rewardvsitreation.get(i).val / rewardvsitreation.get(i).count;
            double val = getRoeuntWT(rewardvsitreation.get(ITERATIONS - 1).val);
            if (val > maxth) {
                val = maxth;
            }
            if (prevLR != 0) {
                if (Math.abs(val - prevLR) > 2000) {
                    if (prevLR < val) {
                        val = prevLR + 2000;
                    } else {
                        val = val - 2000;
                    }
                }
            }
            prevLR = val;
            fr.write(lr + "," + val + "," + (cslearning == false ? graphname : "CS_" + graphname) + "\n");
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
    
    public void toGraphGAMMA(boolean flag, boolean flag1) {
        FileWriter fr = null;
        try {
            
            if (!flag1) {
                prevGAMMA = 0;
            }
            fr = new FileWriter(new File("graph/" + (cslearning == false ? "gammarevrd.txt" : "cdgammarevrd" + rewardalgo + ".txt")), flag);
            //for (int i = 0; i < ITERATIONS; i += 50) {
            //    rewardvsitreation.get(i).val = rewardvsitreation.get(i).val / rewardvsitreation.get(i).count;
            double val = getRoeuntWT(rewardvsitreation.get(ITERATIONS - 1).val);
            if (val > maxth) {
                val = maxth;
            }
            if (prevGAMMA != 0) {
                if (Math.abs(val - prevGAMMA) > 2000) {
                    if (prevGAMMA < val) {
                        val = prevGAMMA + 200;
                    } else {
                        val = val - 200;
                    }
                }
            }
            prevGAMMA = val;
            
            fr.write(GAMMA + "," + val + "," + (cslearning == false ? graphname : "CL_" + graphname) + "\n");
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
    
    public void expertnessMeasures(double rewrd, int current, int action) {
//        revardNrm += rewrd;
//        revardAbs += Math.abs(rewrd);
//        revardNag += rewrd < 0 ? rewrd : 0;
//        revardPos += rewrd > 0 ? rewrd : 0;
        if (rewardalgo == 0) {
            this.csrevard[current][action] += rewrd;
        } else if (rewardalgo == 1) {
            this.csrevard[current][action] += Math.abs(rewrd);;
        } else if (rewardalgo == 2) {
            this.csrevard[current][action] += rewrd > 0 ? rewrd : 0;;
        } else if (rewardalgo == 3) {
            this.csrevard[current][action] += rewrd < 0 ? rewrd : 0;;
        } else if (rewardalgo == 4) {
            double all = 0;
            for (int i = 0; i < csrevard[0].length; i++) {
                all += csrevard[current][i];
            }
            this.csrevard[current][action] += rewrd / all;
        } else if (rewardalgo == 5) {
            double all = 0;
            for (int i = 0; i < csrevard[0].length; i++) {
                all += csrevard[current][i] * Math.log(csrevard[current][i]);
            }
            
            this.csrevard[current][action] +=all;
        }
        
    }
    
    public double findWij(double rewrd, int current, int action) {
//        revardNrm += rewrd;
//        revardAbs += Math.abs(rewrd);
//        revardNag += rewrd < 0 ? rewrd : 0;
//        revardPos += rewrd > 0 ? rewrd : 0;

        double wt = 0, qnew = 0;
        String cu = "", act = "";
        if (current < mt.state.size() && action < mt.state.size()) {
            cu = mt.state.get(current).toString();
            act = mt.state.get(action).toString();
            // int   current1 = mt.ht.get(mt.state.get(current));

            for (int i = 0; i < alcslear.size(); i++) {
                int current1 = alcslear.get(i).mt.ht.get(cu) == null ? -1 : alcslear.get(i).mt.ht.get(cu).index;
                int action1 = alcslear.get(i).mt.ht.get(act) == null ? -1 : alcslear.get(i).mt.ht.get(act).index;
                if (current1 != -1 && action1 != -1) {
                    
                    if (index == alcslear.get(i).index) {
                        wt = 1 - alpha;
                    } else if (alcslear.get(i).csrevard[current1][action1] > alcslear.get(index).csrevard[current][action]) {
                        wt = (alcslear.get(i).csrevard[current1][action1] - alcslear.get(index).csrevard[current][action]);
                        double wt1 = 0;
                        for (int j = 0; j < alcslear.size(); j++) {
                            int current2 = alcslear.get(j).mt.ht.get(cu) == null ? -1 : alcslear.get(j).mt.ht.get(cu).index;
                            int action2 = alcslear.get(j).mt.ht.get(act) == null ? -1 : alcslear.get(j).mt.ht.get(act).index;
                            if (current2 != -1 && action2 != -1) {
                                wt1 += alcslear.get(j).csrevard[current2][action2] - alcslear.get(index).csrevard[current][action];
                            }
                        }
                        wt = (wt / wt1);
                        wt = alpha * wt;
                    }
                }
                qnew += wt * rewrd;
            }
            
            updateVal(qnew);
            
            return (qnew);
        }
        return rewrd;
    }
    
    public double findSimleSTWij(double rewrd, int current, int action) {
//        revardNrm += rewrd;
//        revardAbs += Math.abs(rewrd);
//        revardNag += rewrd < 0 ? rewrd : 0;
//        revardPos += rewrd > 0 ? rewrd : 0;

        double wt = 0, qnew = 0;
        String cu = "", act = "";
        if (current < mt.state.size() && action < mt.state.size()) {
            cu = mt.state.get(current).toString();
            act = mt.state.get(action).toString();
            // int   current1 = mt.ht.get(mt.state.get(current));

            for (int i = 0; i < alcslear.size(); i++) {
                int current1 = alcslear.get(i).mt.ht.get(cu) == null ? -1 : alcslear.get(i).mt.ht.get(cu).index;
                int action1 = alcslear.get(i).mt.ht.get(act) == null ? -1 : alcslear.get(i).mt.ht.get(act).index;
                if (current1 != -1 && action1 != -1) {
                    qnew += alcslear.get(i).csrevard[current1][action1];
//                    if (index == alcslear.get(i).index) {
//                        wt = 1 - alpha;
//                    } else if (alcslear.get(i).csrevard[current1][action1] > alcslear.get(index).csrevard[current][action]) {
//                        wt = (alcslear.get(i).csrevard[current1][action1] - alcslear.get(index).csrevard[current][action]);
//                        double wt1 = 0;
//                        for (int j = 0; j < alcslear.size(); j++) {
//                            int current2 = alcslear.get(j).mt.ht.get(cu) == null ? -1 : alcslear.get(j).mt.ht.get(cu).index;
//                            int action2 = alcslear.get(j).mt.ht.get(act) == null ? -1 : alcslear.get(j).mt.ht.get(act).index;
//                            if (current2 != -1 && action2 != -1) {
//                                wt1 += alcslear.get(j).csrevard[current2][action2] - alcslear.get(index).csrevard[current][action];
//                            }
//                        }
//                        wt = (wt / wt1);
//                        wt = alpha * wt;
//                    }
                }
                
            }
            qnew /= alcslear.size();
            updateVal(qnew);
            
            return (qnew);
        }
        return rewrd;
    }
    
    public double findJoindReward(double rewrd, int current, int action) {
//        revardNrm += rewrd;
//        revardAbs += Math.abs(rewrd);
//        revardNag += rewrd < 0 ? rewrd : 0;
//        revardPos += rewrd > 0 ? rewrd : 0;

        double wt = 0, qnew = 0;
        String cu = "", act = "";
        if (current < mt.state.size() && action < mt.state.size()) {
            cu = mt.state.get(current).toString();
            act = mt.state.get(action).toString();
            // int   current1 = mt.ht.get(mt.state.get(current));

            for (int i = 0; i < alcslear.size(); i++) {
                int current1 = alcslear.get(i).mt.ht.get(cu) == null ? -1 : alcslear.get(i).mt.ht.get(cu).index;
                int action1 = alcslear.get(i).mt.ht.get(act) == null ? -1 : alcslear.get(i).mt.ht.get(act).index;
                if (current1 != -1 && action1 != -1) {
                    qnew += ((QLearningRuleJR) alcslear.get(i)).R[current1][action1];
//                    if (index == alcslear.get(i).index) {
//                        wt = 1 - alpha;
//                    } else if (alcslear.get(i).csrevard[current1][action1] > alcslear.get(index).csrevard[current][action]) {
//                        wt = (alcslear.get(i).csrevard[current1][action1] - alcslear.get(index).csrevard[current][action]);
//                        double wt1 = 0;
//                        for (int j = 0; j < alcslear.size(); j++) {
//                            int current2 = alcslear.get(j).mt.ht.get(cu) == null ? -1 : alcslear.get(j).mt.ht.get(cu).index;
//                            int action2 = alcslear.get(j).mt.ht.get(act) == null ? -1 : alcslear.get(j).mt.ht.get(act).index;
//                            if (current2 != -1 && action2 != -1) {
//                                wt1 += alcslear.get(j).csrevard[current2][action2] - alcslear.get(index).csrevard[current][action];
//                            }
//                        }
//                        wt = (wt / wt1);
//                        wt = alpha * wt;
//                    }
                }
                
            }
            qnew /= alcslear.size();
            
            double jointreward = b * rewrd + (1 - b) * (qnew);
            updateVal(jointreward);
            
            return (jointreward);
        }
        return rewrd;
    }
    
    public void toProductGraph(MapState m) {
        FileWriter fr = null;
        try {
            fr = new FileWriter(new File("graph/product.txt"));
            for (int i = 0; i < m.state.size(); i++) {
                for (int j = 0; j < m.ht.get(m.state.get(i)).prod.size(); j++) {
                    fr.write(m.state.get(i) + "," + getRoeuntWT(m.ht.get(m.state.get(i)).action.get(m.ht.get(m.state.get(i)).prod.get(j)).intValue()) + "," + m.ht.get(m.state.get(i)).prod.get(j) + "\n");
                    
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(Graph.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fr.close();
                
            } catch (IOException ex) {
                Logger.getLogger(Graph.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public static void toExcute(MapState m, Graph g) {
        
    }
    
    public static void creteCSGraph(String file1, String file2, int rewaralgo) {
        {
            for (double i = 0.1; i <= 0.5; i += 0.1) {
                System.out.println("LR SarasaRuleLamda : " + i);
                CSLearningRule cs = new CSLearningRule();
                ArrayList<Graph> al = new ArrayList();
                for (int j = 0; j < 4; j++) {
                    SarasaRuleLamda r1 = new SarasaRuleLamda();
                    r1.lr = i;
                    al.add(r1);
                }
                cs.cslearn1(file1, file2, al, rewaralgo);
                if (i == 0.1) {
                    cs.r2.toGraphLR(false, false);
                } else {
                    cs.r2.toGraphLR(true, true);
                }
                if (i == 0.1) {
                    
                    cs.r2.graphname = "CS_" + cs.r2.graphname;
                    cs.r2.toGraph(false);
                    
                }
            }
            for (double i = 0.1; i <= 0.5; i += 0.1) {
                System.out.println("GAMMA SarasaRuleLamda : " + i);
                CSLearningRule cs = new CSLearningRule();
                ArrayList<Graph> al = new ArrayList();
                for (int j = 0; j < 4; j++) {
                    SarasaRuleLamda r1 = new SarasaRuleLamda();
                    r1.GAMMA = i;
                    al.add(r1);
                }
                cs.cslearn1(file1, file2, al, rewaralgo);

                //        cs.r2.toGraphLR(true);
                if (i == 0.1) {
                    cs.r2.toGraphGAMMA(false, false);
                } else {
                    cs.r2.toGraphGAMMA(true, true);
                }
                
            }
        }
        {
            for (double i = 0.1; i <= 0.5; i += 0.1) {
                System.out.println("LR QLearningRuleLamda : " + i);
                CSLearningRule cs = new CSLearningRule();
                ArrayList<Graph> al = new ArrayList();
                for (int j = 0; j < 4; j++) {
                    QLearningRuleLamda r1 = new QLearningRuleLamda();
                    r1.lr = i;
                    al.add(r1);
                }
                cs.cslearn1(file1, file2, al, rewaralgo);
                
                cs.r2.toGraphLR(true, false);
                if (i == 0.1) {
                    cs.r2.graphname = "CS_" + cs.r2.graphname;
                    cs.r2.toGraph(true);
                    
                }
            }
            for (double i = 0.1; i <= 0.5; i += 0.1) {
                System.out.println("GAMMA QLearningRuleLamda : " + i);
                CSLearningRule cs = new CSLearningRule();
                ArrayList<Graph> al = new ArrayList();
                for (int j = 0; j < 4; j++) {
                    QLearningRuleLamda r1 = new QLearningRuleLamda();
                    r1.GAMMA = i;
                    al.add(r1);
                }
                cs.cslearn1(file1, file2, al, rewaralgo);

                //       cs.r2.toGraphLR(true);
                if (i == 0.1) {
                    cs.r2.toGraphGAMMA(true, false);
                } else {
                    cs.r2.toGraphGAMMA(true, true);
                }
                
            }
        }
        {
            for (double i = 0.1; i <= 0.5; i += 0.1) {
                System.out.println("LR SarasaRule : " + i);
                CSLearningRule cs = new CSLearningRule();
                ArrayList<Graph> al = new ArrayList();
                for (int j = 0; j < 4; j++) {
                    SarasaRule r1 = new SarasaRule();
                    r1.lr = i;
                    al.add(r1);
                }
                cs.cslearn1(file1, file2, al, rewaralgo);
                
                if (i == 0.1) {
                    cs.r2.toGraphLR(true, false);
                    cs.r2.graphname = "CS_" + cs.r2.graphname;
                    cs.r2.toGraph(true);
                } else {
                    cs.r2.toGraphLR(true, true);
                }
            }
            for (double i = 0.1; i <= 0.5; i += 0.1) {
                System.out.println("GAMMA SarasaRule : " + i);
                CSLearningRule cs = new CSLearningRule();
                ArrayList<Graph> al = new ArrayList();
                for (int j = 0; j < 4; j++) {
                    SarasaRule r1 = new SarasaRule();
                    r1.GAMMA = i;
                    al.add(r1);
                }
                cs.cslearn1(file1, file2, al, rewaralgo);

                //  cs.r2.toGraphLR(true);
                if (i == 0.1) {
                    cs.r2.toGraphGAMMA(true, false);
                } else {
                    cs.r2.toGraphGAMMA(true, true);
                }
                
            }
            
        }
        for (double i = 0.1; i <= 0.5; i += 0.1) {
            System.out.println("LR QLearningRule : " + i);
            CSLearningRule cs = new CSLearningRule();
            ArrayList<Graph> al = new ArrayList();
            for (int j = 0; j < 4; j++) {
                QLearningRule r1 = new QLearningRule();
                r1.lr = i;
                al.add(r1);
            }
            cs.cslearn1(file1, file2, al, rewaralgo);
            
            if (i == 0.1) {
                cs.r2.toGraphLR(true, false);
            } else {
                cs.r2.toGraphLR(true, true);
            }
            if (i == 0.1) {
                cs.r2.graphname = "CS_" + cs.r2.graphname;
                cs.r2.toGraph(true);
            }
        }
        for (double i = 0.1; i <= 0.5; i += 0.1) {
            System.out.println("GAMMA QLearningRule : " + i);
            CSLearningRule cs = new CSLearningRule();
            ArrayList<Graph> al = new ArrayList();
            for (int j = 0; j < 4; j++) {
                QLearningRule r1 = new QLearningRule();
                r1.GAMMA = i;
                al.add(r1);
            }
            cs.cslearn1(file1, file2, al, rewaralgo);
            
            if (i == 0.1) {
                cs.r2.toGraphGAMMA(true, false);
            } else {
                cs.r2.toGraphGAMMA(true, true);
            }
        }
        
    }
    
    public static void creteGraph(String file) {
        
        MapState m = new MapState();
        m.getState(file);
        QLearningRule r = new QLearningRule();
        m.start(r);
        for (double i = 0.1; i <= 0.5; i += 0.1) {
            
            QLearningRule r1 = new QLearningRule();
            r1.lr = i;
            m.start(r1);
            if (i == 0.1) {
                r1.toGraphLR(false, false);
            } else {
                r1.toGraphLR(true, true);
            }
        }
        for (double i = 0.1; i <= 0.5; i += 0.1) {
            
            QLearningRule r1 = new QLearningRule();
            r1.GAMMA = i;
            m.start(r1);
            if (i == 0.1) {
                r1.toGraphGAMMA(false, false);
            } else {
                r1.toGraphGAMMA(true, true);
            }
        }
        
        r.toGraph(false);
        r.toProductGraph(m);
        m = new MapState();
        m.getState(file);
        {
            SarasaRule r1 = new SarasaRule();
            m.start(r1);
            for (double i = 0.1; i <= 0.5; i += 0.1) {
                
                SarasaRule r2 = new SarasaRule();
                r2.lr = i;
                m.start(r2);
                if (i == 0.1) {
                    r2.toGraphLR(true, false);
                } else {
                    r2.toGraphLR(true, true);
                }
                // r2.toGraphLR(true);

            }
            for (double i = 0.1; i <= 0.5; i += 0.1) {
                
                SarasaRule r2 = new SarasaRule();
                r2.GAMMA = i;
                m.start(r2);
                if (i == 0.1) {
                    r2.toGraphGAMMA(true, false);
                } else {
                    r2.toGraphGAMMA(true, true);
                }
                //   r2.toGraphGAMMA(true);

            }
            r1.toGraph(true);
            
        }
        {
            m = new MapState();
            m.getState(file);
            SarasaRuleLamda r1 = new SarasaRuleLamda();
            m.start(r1);
            for (double i = 0.1; i <= 0.5; i += 0.1) {
                
                SarasaRuleLamda r2 = new SarasaRuleLamda();
                r2.lr = i;
                m.start(r2);
                if (i == 0.1) {
                    r2.toGraphLR(true, false);
                } else {
                    r2.toGraphLR(true, true);
                }
                //    r2.toGraphLR(true);

            }
            for (double i = 0.1; i <= 0.5; i += 0.1) {
                
                SarasaRuleLamda r2 = new SarasaRuleLamda();
                r2.GAMMA = i;
                m.start(r2);
                if (i == 0.1) {
                    r2.toGraphGAMMA(true, false);
                } else {
                    r2.toGraphGAMMA(true, true);
                }
                //   r2.toGraphGAMMA(true);

            }
            r1.toGraph(true);
        }
        
        {
            m = new MapState();
            m.getState(file);
            QLearningRuleLamda r1 = new QLearningRuleLamda();
            m.start(r1);
            for (double i = 0.1; i <= 0.5; i += 0.1) {
                
                QLearningRuleLamda r2 = new QLearningRuleLamda();
                r2.lr = i;
                m.start(r2);
                if (i == 0.1) {
                    r2.toGraphLR(true, false);
                } else {
                    r2.toGraphLR(true, true);
                }
                //    r2.toGraphLR(true);

            }
            for (double i = 0.1; i <= 0.5; i += 0.1) {
                
                QLearningRuleLamda r2 = new QLearningRuleLamda();
                r2.GAMMA = i;
                m.start(r2);
                if (i == 0.1) {
                    r2.toGraphGAMMA(true, false);
                } else {
                    r2.toGraphGAMMA(true, true);
                }
                //   r2.toGraphGAMMA(true);

            }
            r1.toGraph(true);
        }
        
    }
    
    public double getRoeuntWT(double r) {
        //System.out.println(algo.indexOf(graphname) +":"+graphname);
        return (int) (wt[algo.indexOf(graphname)] * r);
    }
    
    public static void main(String[] args) {
        Date dt = new Date();
//        creteCSGraph("agent.xml", "agent1.xml", 0);
        //  creteGraph("agent.xml");
//        creteCSGraph("agent.xml", "agent1.xml", 0);
//
////      
//        creteCSGraph("agent.xml", "agent1.xml", 1);
//        creteCSGraph("agent.xml", "agent1.xml", 2);
//        creteCSGraph("agent.xml", "agent1.xml", 3);
        System.out.println("Time:" + ((new Date().getTime() - dt.getTime()) / (1000 * 60 * 60)) + " M");
    }
}
