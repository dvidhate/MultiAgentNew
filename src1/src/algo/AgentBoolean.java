/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algo;

import java.util.ArrayList;

/**
 *
 * @author vision soft
 */
public class AgentBoolean {

    public boolean flag[][];
    public ArrayList<String> attr = new ArrayList();
    public double minsupp = 0.2;

    public void load(String file) {
        ArrayList<algo.Record1> al = algo.Record1.readXML(file);
        for (int i = 0; i < al.size(); i++) {
            String key = al.get(i).getName();
            if (attr.indexOf(key) == -1) {
                attr.add(key);
            }
        }
        for (int i = 0; i < al.size(); i++) {
            String key = al.get(i).getAgeflag();
            if (attr.indexOf(key) == -1) {
                attr.add(key);
            }
        }

        for (int i = 0; i < al.size(); i++) {
            String key = al.get(i).getPriceflag();
            if (attr.indexOf(key) == -1) {
                attr.add(key);
            }
        }
        for (int i = 0; i < al.size(); i++) {
            String key = al.get(i).getQflag();
            if (attr.indexOf(key) == -1) {
                attr.add(key);
            }
        }
        flag = new boolean[al.size()][attr.size()];
        for (int i = 0; i < al.size(); i++) {
            String key = al.get(i).getAgeflag();
            flag[i][attr.indexOf(key)] = true;
            key = al.get(i).getName();
            flag[i][attr.indexOf(key)] = true;
            key = al.get(i).getPriceflag();
            flag[i][attr.indexOf(key)] = true;
            key = al.get(i).getQflag();
            flag[i][attr.indexOf(key)] = true;
        }
        col = new boolean[flag.length];
    }

    public void initCol() {
        for (int i = 0; i < col.length; i++) {
            col[i] = true;
        }
    }
    boolean col[];
   // boolean colpre[];
    public boolean isAction(int k) {

        int cnt = 0;
        for (int i = 0; i < flag.length; i++) {
           // colpre[i]=col[i];
            col[i] = col[i] & flag[i][k];
            if (col[i]) {
                cnt++;
            }
        }
        double support = (double) cnt / flag.length;
        if (support >= this.minsupp) {
            return true;
        }else{
        //    colpre[i]=col[i];
        }
        return false;
    }
}
