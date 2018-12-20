/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algo;

import static algo.rule.R;
import static algo.rule.state;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;
import weka.core.Instances;
import weka.core.converters.CSVLoader;

/**
 *
 * @author vision soft
 */
public class MapState {

    public HashMap<String, StateObject> ht;
    double R[][];
    int cnt;
    public double supp = 0.02;
    public double conf = 0.35;

    public ArrayList<String> state = new ArrayList();
    public ArrayList<String> prod = new ArrayList();
    public double max1 = 0;

    public double[][] getState(String file) {
        max1 = 0;
        try {
            ht = new HashMap<String, StateObject>();

            ArrayList<Record> al = Record.readXML(file);
            cnt = al.size();
            int totaltran = 0;
            boolean totaltranflag[] = new boolean[al.size()];
            for (int i = 0; i < al.size(); i++) {
                totaltranflag[ al.get(i).getTransationno()] = true;
            }
            for (int i = 0; i < al.size(); i++) {
                if (totaltranflag[i]) {
                    totaltran++;
                }
            }
//            double minsu = min * al.size();

            SimpleDateFormat sd = new SimpleDateFormat("MM");
            for (int i = 0; i < al.size(); i++) {
                String key = al.get(i).getAgeflag() + ":" + al.get(i).getPriceflag() + ":" + sd.format(al.get(i).getDt());
                if (state.indexOf(key) == -1) {
                    state.add(key);
                    ht.put(key, new StateObject(key, ht.size()));
                }
                if (ht.get(key).action.get(al.get(i).getName()) == null) {
                    ht.get(key).action.put(al.get(i).getName(), 1);
                } else {
                    ht.get(key).action.put(al.get(i).getName(), ht.get(key).action.get(al.get(i).getName()) + 1);
                }
//                if (prod.indexOf(al.get(i).getName()) == -1) {
//                    prod.add(al.get(i).getName());
//                }
                if (ht.get(key).trancntall.indexOf(al.get(i).getTransationno()) == -1) {
                    ht.get(key).trancnt++;
                    ht.get(key).trancntall.add(al.get(i).getTransationno());
                }
            }
            for (int i = state.size() - 1; i >= 0; i--) {
                double wt = ht.get(state.get(i)).trancnt / (double) totaltran;
                //  System.out.println(wt);
                if (wt < supp) {
                    ht.remove(state.get(i).toString());
                    state.remove(i);
                }
            }

            ArrayList<String> prodall = new ArrayList();
            for (int i = 0; i < state.size(); i++) {
                Iterator it = ht.get(state.get(i)).action.keySet().iterator();
                ArrayList<String> prod1 = new ArrayList();
                int min = -1;
                while (it.hasNext()) {
                    String key = it.next().toString();
                    double wt = ht.get(state.get(i)).action.get(key).intValue() / (double) ht.get(state.get(i)).trancnt;
                    //  System.out.println(wt);
                    if (wt < conf) {
                        prod1.add(key);
                    } else {
                        if (min == -1) {
                            min = ht.get(state.get(i)).action.get(key).intValue();
                        } else {
                            min = Math.min(min, ht.get(state.get(i)).action.get(key).intValue());
                        }
                        max1 = Math.min(max1, ht.get(state.get(i)).action.get(key).intValue());
                        if (prodall.indexOf(key) == -1) {
                            prodall.add(key);
                            //prodall.add(key)
                        }
                    }
                }
                for (int j = 0; j < prod1.size(); j++) {
                    ht.get(state.get(i)).action.remove(prod1.get(j));
                }
                it = ht.get(state.get(i)).action.keySet().iterator();
                while (it.hasNext()) {
                    String key = it.next().toString();
                    ht.get(state.get(i)).prod.add(key);
                }
                if (ht.get(state.get(i)).prod.size() > 0) {
                    Collections.sort(ht.get(state.get(i)).prod);
                    ht.get(state.get(i)).finalstate = ht.get(state.get(i)).prod.toString();
                    ht.get(state.get(i)).action.put(ht.get(state.get(i)).prod.toString(), min);
                    // ht.get(state.get(i)).finalstate = ht.get(state.get(i)).getList();
                }
            }
            for (int i = 0; i < state.size(); i++) {
                prodall.add(ht.get(state.get(i)).prod.toString());
            }
            prod = prodall;

            Iterator ir = ht.keySet().iterator();
            String key = "";
            R = new double[ht.size() + prod.size()][ht.size() + prod.size()];
//            while (ir.hasNext()) {
//                key = ir.next().toString();
//                Iterator ir1 = ht.get(key).action.keySet().iterator();
//                while (ir1.hasNext()) {
//                    String key1 = ir1.next().toString();
//                    // if (ht.get(key).action.get(key1).intValue() > minsu) {
//                    R[ht.get(key).index][ht.size() + prod.indexOf(key1)] = ((double) ht.get(key).action.get(key1).intValue()) / al.size();
//                    //}
//                }
//
//            }
            for (int i = 0; i < state.size(); i++) {
                for (int j = 0; j < state.size(); j++) {
                    if (i != j) {
                        R[i][j] = calwt(ht.get(state.get(i).toString()).action, ht.get(state.get(j).toString()).action);
                    }
                }
            }
            double max = 0;
            for (int i = 0; i < R.length; i++) {
                for (int j = 0; j < R[i].length; j++) {
                    max = Math.max(max, R[i][j]);
                }

            }
//            for (int i = state.size(); i < R.length; i++) {
//                for (int j = 0; j < R[i].length; j++) {
//                    R[i][j] = max;
//                }
//            }
            for (int i = 0; i < R.length; i++) {
                for (int j = ht.size(); j < R[i].length; j++) {
                    if (i < state.size()) {
                        String key1 = state.get(i).toString();
                        if (ht.get(key1).action.get(prod.get(j - ht.size()).toString()) != null) {
                            double wt = ht.get(key1).action.get(prod.get(j - ht.size()).toString());

                            if (wt > 0.0) {
                                if (ht.get(key1).finalstate.equals(prod.get(j - ht.size()).toString())) {
                                    R[i][j] += (max1 * ht.get(key1).prod.size()) + wt;
                                } else {
                                    R[i][j] += max + wt;
                                }
                            }

                        }

                    }
                }
            }
            max1 = max;
            return R;
        } catch (Exception ex) {
            Logger.getLogger(MapState.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void cslearning(MapState s) {
        for (int i = 0; i < state.size(); i++) {
            for (int j = 0; j < state.size(); j++) {
//                if (i != j) {
                //if(ht.get(state.get(i).toString()))
                if (s.state.indexOf(state.get(i).toString()) != -1 && s.state.indexOf(state.get(j).toString()) != -1) {
                    int is = s.state.indexOf(state.get(i).toString());
                    int js = s.state.indexOf(state.get(i).toString());
                    R[i][j] = R[i][j] + s.R[is][js];
                }
                // R[i][j] = calwt(ht.get(state.get(i).toString()).action, ht.get(state.get(j).toString()).action);
                //}
            }
        }
    }

    public double calwt(HashMap<String, Integer> action1, HashMap<String, Integer> action2) {
        double wt = 0;
        HashMap<String, Integer> action = new HashMap<String, Integer>();
        Iterator ir = action1.keySet().iterator();
        while (ir.hasNext()) {
            String key = ir.next().toString();
            if (action.get(key) == null) {
                action.put(key, 0);
            }
            action.put(key, action.get(key) + action1.get(key));

        }
        ir = action2.keySet().iterator();
        while (ir.hasNext()) {
            String key = ir.next().toString();
            if (action.get(key) == null) {
                action.put(key, 0);
            }
            action.put(key, action.get(key) + action2.get(key));

        }
        ir = action.keySet().iterator();
        while (ir.hasNext()) {
            String key = ir.next().toString();
            if (action.get(key) == null) {
                action.put(key, 0);
            }
            if (action2.get(key) != null) {
                wt += action2.get(key).intValue() / (double) action.get(key).intValue();
            }
        }

        return wt;
    }
    public double q[][];

    public void start(Graph q1) {
        q1.mt = this;
        int[] state = new int[ht.size()];
        for (int i = 0; i < state.length; i++) {
            state[i] = i;
        }
//        for (int i = 0; i < R.length; i++) {
//            System.out.println("");
//            for (int j = 0; j < R[i].length; j++) {
//                System.out.print(R[i][j] + ",");
//            }
//        }
        q1.maxwt = max1;
        q1.init(R.length, state, R);
        q1.train();
        q = q1.q;
//        for (int i = 0; i < q1.q.length; i++) {
//            System.out.println("");
//            for (int j = 0; j < q1.q[i].length; j++) {
//                System.out.print(q1.q[i][j] + ",");
//            }
//        }
    }

    public void startSarsa() {
        int[] state = new int[ht.size()];
        for (int i = 0; i < state.length; i++) {
            state[i] = i;
        }
//        for (int i = 0; i < R.length; i++) {
//            System.out.println("");
//            for (int j = 0; j < R[i].length; j++) {
//                System.out.print(R[i][j] + ",");
//            }
//        }
        SarasaRule q1 = new SarasaRule(R.length, state, R);
        q1.train();
        q = q1.q;
//        for (int i = 0; i < q1.q.length; i++) {
//            System.out.println("");
//            for (int j = 0; j < q1.q[i].length; j++) {
//                System.out.print(q1.q[i][j] + ",");
//            }
//        }
        q1.test();
    }

    public DefaultTableModel getTableR() {

        String data[][] = new String[R.length][R.length + 1];

        String head[] = new String[R.length + 1];
        for (int i = 0; i < state.size(); i++) {
            head[i + 1] = state.get(i).toString();
            data[i][0] = state.get(i).toString();
        }
        for (int i = 0; i < prod.size(); i++) {
            head[state.size() + i + 1] = prod.get(i).toString();
        }

        for (int i = 0; i < R.length; i++) {

            for (int j = 0; j < R.length; j++) {
                data[i][j + 1] = String.valueOf(R[i][j]);
            }
        }
        DefaultTableModel dm = new DefaultTableModel(data, head);
        return dm;
    }

    public DefaultTableModel getTableState() {

        String head[] = new String[4];

        head[0] = "Age";
        head[1] = "Price";
        head[2] = "Month";
        head[3] = "Product";
        String data[][] = new String[state.size()][4];
        for (int i = 0; i < state.size(); i++) {
            String key[] = state.get(i).split(":");
            data[i][0] = key[0];
            data[i][1] = key[1];
            data[i][2] = key[2];
            ht.get(state.get(i).toString());

            data[i][ 3] = ht.get(state.get(i).toString()).getList();

        }
        DefaultTableModel dm = new DefaultTableModel(data, head);

        return dm;
    }
   public  String data[][];

    public DefaultTableModel getTableQ() {
        data = new String[state.size()][R.length + 1 - state.size()];

        String head[] = new String[R.length + 1];
        for (int i = 0; i < state.size(); i++) {
            // head[i + 1] = state.get(i).toString();
            data[i][0] = state.get(i).toString();
        }
        for (int i = 0; i < prod.size(); i++) {
            head[ i + 1] = prod.get(i).toString();
        }

        for (int i = 0; i < state.size(); i++) {

            for (int j = state.size(); j < q[i].length; j++) {
                data[i][j + 1 - state.size()] = String.valueOf(q[i][j]);
            }
        }
        DefaultTableModel dm = new DefaultTableModel(data, head);

        return dm;
    }

    public static void main(String[] args) {
//        MapState m = new MapState();
//        m.getState("agent.xml");
//        MapState m1 = new MapState();
//        m1.getState("agent1.xml");
//        m.cslearning(m1);
//        m.start();
    }
}
