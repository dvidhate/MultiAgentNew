/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package algo;

import algo.rule;
import gui.RuleTreeNode;
import java.util.ArrayList;

/**
 *
 * @author ganesh
 */
public class RuleList {

    public String[] col;
    public String[] val;
    public int[] cnt;
    public double confidence;

    public RuleList(String[] col, String[] val, double confidence, int cnt[]) {
        this.col = col;
        this.val = val;
        this.cnt = cnt;
        this.confidence = confidence;
    }
 
    public boolean equalCol(String[] col, String val[]) {
        boolean flag = false;
        if (this.col.length == col.length) {
            for (int i = 0; i < col.length; i++) {
                if (col[i].equals(this.col[i]) && !col[i].equals("Name")) {
                    if (!val[i].equals(this.val[i])) {
                        return flag;
                    }
                } else if (!col[i].equals("Name")) {
                    return flag;
                }

            }
            flag = true;
        }
        return flag;
    }

    public String[] cloneArray(String d[]) {
        String d1[] = new String[d.length];
        for (int i = 0; i < d.length; i++) {
            d1[i] = d[i];
        }
        return d1;
    }
//    @Override
//    public String toString() {
//
//        String data = "";
//        if (col != null) {
//            for (int i = 0; i < col.length - 1; i++) {
//                data += col[i] + "=" + val[i] + " " + cnt[i] + " ";
//            }
//            data += "==> ";
//
//            for (int i = col.length - 1; i < col.length; i++) {
//                data += col[i] + "=" + val[i] + " " + cnt[i] + " ";
//            }
//            data += "conf:(" + confidence + ")\n";
//        }
//        return data; //To change body of generated methods, choose Tools | Templates.
//    }

    public String toString() {

        String data = "";
        if (col != null) {
            for (int i = 0; i < col.length - 1; i++) {
                data += col[i] + "=" + val[i];
            }
            data += "==> ";

            for (int i = col.length - 1; i < col.length; i++) {
                data += col[i] + "=" + val[i];
            }
            //data += "conf:(" + confidence + ")\n";
        }
        return data; //To change body of generated methods, choose Tools | Templates.
    }

    public static ArrayList<RuleList> filter(ArrayList<RuleList> al) {
        for (int i = al.size() - 1; i >= 0; i--) {
            boolean flag = true;
            for (int j = 0; j < al.get(i).col.length; j++) {
                if (al.get(i).col.length == 1) {
                    break;
                }
                if (al.get(i).col[j].equals("Name")) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                al.remove(i);
            }
        }
        return al;

    }

    public static ArrayList<RuleList> exract(String data) {
        int i = 1;
        ArrayList<RuleList> al = new ArrayList();
        data = data.substring(data.indexOf("Best rules found:"));
        while (data.indexOf(i + ".") != -1) {
            String dl[] = data.substring(data.indexOf(i + "."), data.indexOf(")")).split(" ");
            ArrayList alrec = new ArrayList();
            try {
                double conf = 0;
                for (int ii = 0; ii < dl.length; ii++) {
                    String d1 = dl[ii];
                    if (d1.indexOf("conf:") != -1) {
                        d1 = d1.replace("conf:(", "");
                        d1 = d1.replace(")", "");
                        conf = Double.parseDouble(d1);
                    } else if (!d1.equals("==>") && d1.indexOf("=") != -1) {
                        String col1 = d1.substring(0, d1.indexOf("="));
                        String val = d1.substring(d1.indexOf("=") + 1);
                        if (dl[ii + 1].indexOf("=") == -1) {
                            ii++;
                            String cntval = dl[ii];
                            alrec.add(col1 + "," + val + "," + cntval);
                            int n = 2;
                            while (alrec.size() - n >= 0 && alrec.get(alrec.size() - n).toString().indexOf(",?") != -1) {
                                alrec.set(alrec.size() - n, alrec.get(alrec.size() - n).toString().replace(",?", "," + cntval));
                                n++;
                            }
                        } else {
                            alrec.add(col1 + "," + val + ",?");
                        }
                    }
                }
                if (alrec.size() > 0) {

                    String c[] = new String[alrec.size()];
                    String v[] = new String[alrec.size()];
                    int vc[] = new int[alrec.size()];
                    for (int j = 0; j < alrec.size(); j++) {
                        String rec[] = alrec.get(j).toString().split(",");
                        c[j] = rec[0];
                        v[j] = rec[1];
                        try {
                            vc[j] = Integer.parseInt(rec[2]);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    al.add(new RuleList(c, v, conf, vc));
                }
                data = data.substring(data.indexOf(")") + 1);

                i++;
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        //   System.out.println(al.toString());
        filter(al);
        return al;
    }

    public static void getOrderElement(RuleList r) {
        String col[] = new String[r.col.length];
        String val[] = new String[r.val.length];
        String orer[] = new String[]{"Price", "Age", "Q", "Name"};
        int k = 0;
        for (int i = 0; i < orer.length; i++) {

            for (int j = 0; j < r.col.length; j++) {
                if (r.col[j].equals(orer[i])) {
                    col[k] = r.col[j];
                    val[k] = r.val[j];
                    k++;
                    break;
                }
            }

        }
        r.col = col;
        r.val = val;

    }

    public static RuleTreeNode get(ArrayList<RuleList> r) {
        RuleTreeNode st = new RuleTreeNode("Root", "", 0);
        for (int i = 0; i < r.size(); i++) {
            RuleTreeNode st1 = st;
            getOrderElement(r.get(i));
            for (int j = 0; j < r.get(i).col.length; j++) {
                boolean flag = false;
                for (int k = 0; k < st1.al.size(); k++) {
                    if (r.get(i).col[j] == null || st1.al.get(k).col == null) {
                        System.out.println("d");
                    }
                    if (r.get(i).col[j].equals(st1.al.get(k).col)) {
                        st1 = st1.al.get(k);
                        flag = true;
                        break;

                    }
                }
                if (!flag) {
                    RuleTreeNode st2 = new RuleTreeNode(r.get(i).col[j], r.get(i).val[j], r.get(i).confidence);
                    st1.al.add(st2);
                    st1 = st2;
                }

            }
        }
        return st;
    }

    public static void main(String[] args) {
        ArrayList<RuleList> r = rule.getRule("agent1.xml", 0.2);
        RuleList.get(r);

    }
}
