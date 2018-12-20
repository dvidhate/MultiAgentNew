package algo;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.StringTokenizer;
import learningalgo.agent.impl.AbstractEnvironment;
import learningalgo.learning.reinforcement.example.RuleEnvironment;
import weka.associations.Apriori;
import weka.core.Instances;
import weka.core.converters.CSVLoader;

public class rule {

    public static ArrayList<QState> al1;
    public static double R[][];
    public static int state[];

    public static void init(RuleEnvironment r) {
        r.al = null;
        r.val = 0.1;
    }

    public static void init1(RuleEnvironment r) {
        r.al = null;
        r.val = 0.11;
    }

    public static ArrayList<RuleList> getRule(String file, double val) {
        try {

            Record1.writeCSV(Record1.readXML(file), file);

            CSVLoader cv = new CSVLoader();
            cv.setSource(new File("csv/" + file + ".csv"));
            Instances instances = cv.getDataSet();

            // get the FileReader from the data file
            //  Instances instances = new weka.core.Instances(fr);
// construct a WEKA Apriori associator object
            Apriori aprioriObj = null;
            aprioriObj = new weka.associations.Apriori();
            aprioriObj.setOptions(new String[]{"-M", String.valueOf(val), "-C", "0.01", "-N", "100"});
            aprioriObj.buildAssociations(instances);
// get the extracted association rules

            String aprioriRules = aprioriObj.toString();
            // send the extracted rules to the Console
            System.out.println(aprioriRules);
            //ArrayList(Al)
            ArrayList<RuleList> al = RuleList.exract(aprioriRules);

            Hashtable ht = new Hashtable();
            al1 = new ArrayList();
            int cnt = 0;
            for (int i = 0; i < al.size(); i++) {
                String key = "";
                for (int j = 0; j < al.get(i).col.length; j++) {
                    String key1 = al.get(i).col[j] + "=" + al.get(i).val[j];
                    if (ht.get(key1) == null) {
                        ht.put(key1, String.valueOf(ht.size()));
                        al1.add(new QState(key1, 1, ((double) al.get(i).cnt[j])
                                / instances.numInstances()));
                        key += key1 + ";";
                        cnt++;
                    }

                }
                key = "";
                for (int j = 0; j < al.get(i).col.length; j++) {
                    key += al.get(i).col[j] + "=" + al.get(i).val[j] + ";";
                }
                if (ht.get(key) == null && al.get(i).col.length >= 2) {
                    al1.add(new QState(key, al.get(i).col.length, al.get(i).confidence));
                    ht.put(key, String.valueOf(ht.size()));
                }
            }
            Collections.sort(al1);
            R = new double[al1.size() + 1][al1.size() + 1];
            for (int i = 0; i < R.length; i++) {
                for (int j = 0; j < R[i].length - 1; j++) {
                    R[i][j] = -1;
                }
            }

            state = new int[cnt];
            int k = 0;
            for (int i = 0; i < al1.size(); i++) {
             
                String key = al1.get(i).name;
                if (al1.get(i).cnt == 1) {
                       state[k] = i;k++;
                    
                }
                if (key.indexOf(";") != -1) {
                    StringTokenizer st = new StringTokenizer(key, ";");
                    if (st.countTokens() == 2) {
                        while (st.hasMoreTokens()) {
                            int n1 = Integer.parseInt(ht.get(st.nextToken()).toString());
                            int n2 = Integer.parseInt(ht.get(st.nextToken()).toString());
                            R[n1][n2] = al1.get(i).wt;
                            R[Integer.parseInt(ht.get(key).toString())][n1] = al1.get(i).wt;
                            R[Integer.parseInt(ht.get(key).toString())][n2] = al1.get(i).wt;
                            R[n1][Integer.parseInt(ht.get(key).toString())] = al1.get(i).wt;
                            R[n2][Integer.parseInt(ht.get(key).toString())] = al1.get(i).wt;
                        }
                    } else if (st.countTokens() == 1) {
                        int n1 = Integer.parseInt(ht.get(st.nextToken()).toString());
                        R[n1][n1] = al1.get(n1).wt;
                    }
                    R[Integer.parseInt(ht.get(key).toString())][Integer.parseInt(ht.get(key).toString())] = al1.get(i).wt;

                }
            }

            System.out.println(ht);
            R[R.length - 1][R.length - 1] = 100;
            R[R.length - 1][R.length - 5] = 100;
            return al;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        //    Apriori.main(new String[]{});
        //new weka.associations.Apriori();
        String file = "agent1.xml";
        getRule(file, 0.05);

        QLearningRule q = new QLearningRule(R.length, state, R);
        q.train();

        for (int i = 0; i < R.length; i++) {
            System.out.println("");
            for (int j = 0; j < R[i].length; j++) {
                System.out.print(R[i][j] + ",");
            }
        }
        for (int i = 0; i < q.q.length; i++) {
            System.out.println("");
            for (int j = 0; j < q.q[i].length; j++) {
                System.out.print(q.q[i][j] + ",");
            }
        }
        q.test();
//        file = "agent2.xml";
        //  getRule(file, 0.1);
    }
}
