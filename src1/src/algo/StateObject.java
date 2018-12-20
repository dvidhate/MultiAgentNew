/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algo;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author vision soft
 */
public class StateObject {

    public String stateid;
    public HashMap<String, Integer> action = new HashMap();
    public int trancnt;
    public ArrayList<Integer> trancntall = new ArrayList();
    public ArrayList<String> prod = new ArrayList();
    int index;
    public String finalstate;

    public String getList() {
        HashMap<String, Integer> action1 = (HashMap<String, Integer>) action.clone();
        if (prod.size() > 0) {
            action1.remove(finalstate);
        }
        return action1.toString();

    }

    public StateObject(String stateid) {
        this.stateid = stateid;
    }

    public StateObject(String stateid, int index) {
        this.stateid = stateid;

        this.index = index;
    }

}
