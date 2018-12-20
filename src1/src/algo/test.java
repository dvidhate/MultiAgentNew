/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algo;

import algo.Record1;
import algo.rule;
import java.util.ArrayList;

/**
 *
 * @author vision soft
 */
public class test {

    public static void main(String[] args) {
        ArrayList al = Record1.readXML("agent.xml");
        ArrayList al1 = Record1.readXML("agent1.xml");
        for (int i = 0; i < al1.size(); i++) {
            al.add(al1.get(i));

        }
        Record1.writeXML(al, "temp.xml");
        ArrayList<algo.RuleList> r = rule.getRule("agent.xml", 0.02);
        ArrayList<algo.RuleList> alrule = new ArrayList();
        ArrayList<algo.RuleList> r1 = rule.getRule("agent1.xml", 0.02);
        for (int i = 0; i < r.size(); i++) {
            for (int j = 0; j < r1.size(); j++) {
                if (r.get(i).equalCol(r1.get(j).col, r1.get(j).val)) {
                    String col[] = r1.get(j).cloneArray(r1.get(j).col);
                    String val[] = r1.get(j).cloneArray(r1.get(j).val);
                    for (int k = 0; k < col.length; k++) {
                        if (col[k].equals("Name")) {
                            val[k] = val[k] + ":" + r.get(i).val[k];
                        }
                    }
                    alrule.add(new algo.RuleList(col, val, (r1.get(j).confidence + r.get(i).confidence) / 2, new int[]{}));
                }
            }
        }
        for (int i = 0; i < alrule.size(); i++) {
            System.out.println((i + 1) + ":" + alrule.get(i).toString() + "\n");
        }
//        for (int i = 0; i < r1.size(); i++) {
//            System.out.println((i + 1) + ":" + r.get(i).toString() + "\n");
//        }
    }
}
