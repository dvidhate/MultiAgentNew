/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algo;

/**
 *
 * @author vision soft
 */
public class QState implements Comparable<QState> {

    String name;
    int cnt;
    double wt;

    public QState(String name, int cnt) {
        this.name = name;
        this.cnt = cnt;
    }

    public QState(String name, int cnt, double wt) {
        this.name = name;
        this.cnt = cnt;
        this.wt = wt;
    }

    @Override
    public int compareTo(QState o) {
        return cnt - o.cnt;
    }

}
