/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.util.ArrayList;

/**
 *
 * @author ganesh
 */
public class RuleTreeNode {

    public String val;
    public String col;
    public double wt;
    public ArrayList<RuleTreeNode> al = new ArrayList();

    public RuleTreeNode(String col, String val, double wt) {
        this.val = val;
        this.col = col;
        this.wt = wt;
    }
}
