/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package algo;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Random;

/**
 *
 * @author ganesh
 */
public class Record implements Cloneable, Serializable, Comparable<Record> {

    /**
     * @return the maxpprize
     */
    public static double getMaxpprize() {
        return maxpprize;
    }

    /**
     * @param aMaxpprize the maxpprize to set
     */
    public static void setMaxpprize(double aMaxpprize) {
        maxpprize = aMaxpprize;
    }
    private String name;
    private int qty;
    private int transationno;
    private double price;
    private int age;
    private Date dt;
    private String priceflag;
    private String ageflag;
    private int profit;
    private String qflag;
    private static double maxpprize = 0;

    public void setAgeflag() {
        this.setAgeflag(getAge() < 21 ? "Y" : getAge() < 45 ? "M" : "O");
    }

    public void setPriceflag() {
        if (getPrice() < getMaxpprize()) {
            setPriceflag("LOW");
        } else if (getPrice() < 2 * getMaxpprize()) {
            setPriceflag("MEDIUM");
        } else {
            setPriceflag("HIGH");
        }

    }

    public Record(String name, int qty, double price, int age, Date dt, int profit, int transactionid) {
        this.name = name;
        this.qty = qty;
        this.price = price;
        this.age = age;
        this.dt = dt;
        this.profit = profit;
        this.transationno = transactionid;
        setAgeflag();
        setPriceflag();
        qflag = dt.getMonth() <= 3 ? "Q1" : dt.getMonth() <= 6 ? "Q2" : dt.getMonth() <= 9 ? "Q3" : "Q4";
    }

    public static void random(int noprod, int noofrecord, String agent) {
        Random rn = new Random();
        int tran = noofrecord / (noprod);
        tran = tran * 3;
        double price[] = new double[noprod];
        Hashtable<String, Double> ht = new Hashtable();
        Hashtable<String, String> id = new Hashtable();
        Hashtable<String, Integer> idage = new Hashtable();
        Hashtable<String, Date> tdt = new Hashtable();
        Hashtable<String, Double> ht1 = new Hashtable();
        double pr[] = new double[]{100, 150, 250};
        double profit[] = new double[]{10, 15, 25};
        double profit1[] = new double[noprod];
        for (int i = 0; i < noprod; i++) {
            int k = rn.nextInt(pr.length);
            price[i] = pr[k] + 500;
            profit1[i] = profit[k];
            if (price[i] > getMaxpprize()) {
                setMaxpprize(price[i]);
            }
        }
        profit = profit1;
        setMaxpprize(getMaxpprize() / 3);
        ArrayList<Record> hs = new ArrayList();
        ArrayList<Integer> prod = new ArrayList();
        ArrayList<Integer> mostprod = new ArrayList();
        for (int i = 0; i < noprod; i++) {
            // int k = rn.nextInt(noprod);
            prod.add(i);
            if (rn.nextBoolean()) {
                mostprod.add(i);
            }
            int l = rn.nextInt(noprod);
            ht.put("P" + i, price[i]);
            ht1.put("P" + i, profit[i]);
        }
        for (int i = 0; i < noofrecord; i++) {
            Date dt = new Date(new Date().getYear(), rn.nextInt(12) + 1, rn.nextInt(30));
            Record rd = null;
            int p = prod.get(rn.nextInt(prod.size()));
            int tran1 = rn.nextInt(tran);
            int age = rn.nextInt(70);
            do {

                if (id.get("T" + tran1) == null) {
                    id.put("T" + tran1, "," + p + ";");
                    if (tdt.get("T" + tran1) == null) {
                        tdt.put("T" + tran1, dt);
                        idage.put("T" + tran1, age);
                    } else {
                        dt = tdt.get("T" + tran1);
                        age = idage.get("T" + tran1);
                    }
                    break;
                } else if (id.get("T" + tran1).indexOf("," + p + ";") == -1) {
                    id.put("T" + tran1, id.get("T" + tran1) + "," + p + ";");
                    if (tdt.get("T" + tran1) == null) {
                        tdt.put("T" + tran1, dt);
                        idage.put("T" + tran1, age);
                    } else {
                        dt = tdt.get("T" + tran1);
                        age = idage.get("T" + tran1);
                    }
                    break;
                }

                tran1 = rn.nextInt(tran);
            } while (true);
            if ((mostprod.indexOf(p)) == -1) {
                rd = new Record(agent + "_P" + p, rn.nextInt(3) + 1, ht.get("P" + p).doubleValue(), age, dt, ht1.get("P" + p).intValue(), tran1);
            } else {
                rd = new Record(agent + "_P" + p, rn.nextInt(5) + 5, ht.get("P" + p).doubleValue(), age, dt, ht1.get("P" + p).intValue(), tran1);
            }
            hs.add(rd);
        }
        Collections.sort(hs);
        writeXML(hs, agent + ".xml");
    }

    public void setQflag(String qflag) {
        this.qflag = qflag;
    }

    @Override
    public int compareTo(Record o) {
        if (transationno == o.transationno) {
            return name.compareTo(o.name);
        }
        return transationno - o.transationno;
    }

    public static ArrayList readXML(String filename) {
        ArrayList<Record> hs = new ArrayList();

        try {

            XStream xstream = new XStream(new DomDriver());
            xstream.alias("ArrayList", HashMap.class);
            xstream.alias("Record", Record.class);

            FileInputStream fis = new FileInputStream("db/" + filename);
            InputStreamReader isr = new InputStreamReader(fis);
            ObjectInputStream in = xstream.createObjectInputStream(isr);
            hs = (ArrayList) in.readObject();

            in.close();
            Collections.sort(hs);
            return hs;
        } catch (Exception e) {

            System.out.println("Exception in XMLToStream.xmlDToStream() " + e.toString());

        }

        return hs;
    }

    public static boolean writeXML(ArrayList al, String filename) {
        try {

            XStream xstream = new XStream();

            xstream.alias("ArrayList", ArrayList.class);
            xstream.alias("Record", Record.class);

            FileOutputStream fos = new FileOutputStream("db/" + filename);
            OutputStreamWriter osw = new OutputStreamWriter(fos);

            ObjectOutputStream out = xstream.createObjectOutputStream(osw);
            out.writeObject(al);
            out.close();
            return true;
        } catch (Exception e) {
            System.out.println("StreamToXML File DStreamToXML() : " + e);

        }
        return false;
    }

    public static boolean writeXML1(ArrayList al, String filename) {
        try {

            XStream xstream = new XStream();

            xstream.alias("ArrayList", ArrayList.class);
            xstream.alias("Record", Record.class);

            FileOutputStream fos = new FileOutputStream(filename);
            OutputStreamWriter osw = new OutputStreamWriter(fos);

            ObjectOutputStream out = xstream.createObjectOutputStream(osw);
            out.writeObject(al);
            out.close();
            return true;
        } catch (Exception e) {
            System.out.println("StreamToXML File DStreamToXML() : " + e);

        }
        return false;
    }

    public static void writeCSV(ArrayList<Record> al, String filename) {
        try {

            FileWriter fr = new FileWriter("csv/" + filename + ".csv");
            fr.write("Name,Price,Q,Age\n");
            for (int i = 0; i < al.size(); i++) {
                fr.write(al.get(i).name + "," + al.get(i).priceflag + "," + al.get(i).qflag + "," + al.get(i).ageflag + "\n");
            }
            fr.close();
        } catch (Exception e) {
            System.out.println("StreamToXML File DStreamToXML() : " + e);

        }

    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the qty
     */
    public int getQty() {
        return qty;
    }

    /**
     * @param qty the qty to set
     */
    public void setQty(int qty) {
        this.qty = qty;
    }

    /**
     * @return the price
     */
    public double getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * @return the age
     */
    public int getAge() {
        return age;
    }

    /**
     * @param age the age to set
     */
    public void setAge(int age) {
        this.age = age;
    }

    /**
     * @return the dt
     */
    public Date getDt() {
        return dt;
    }

    /**
     * @param dt the dt to set
     */
    public void setDt(Date dt) {
        this.dt = dt;
    }

    /**
     * @return the priceflag
     */
    public String getPriceflag() {
        return priceflag;
    }

    /**
     * @param priceflag the priceflag to set
     */
    public void setPriceflag(String priceflag) {
        this.priceflag = priceflag;
    }

    /**
     * @return the ageflag
     */
    public String getAgeflag() {
        return ageflag;
    }

    /**
     * @param ageflag the ageflag to set
     */
    public void setAgeflag(String ageflag) {
        this.ageflag = ageflag;
    }

    public static void main(String[] args) {

        for (int i = 1; i <= 2; i++) {
            Record.random(20, 1000, "agent" + i);
        }
    }

    /**
     * @return the profit
     */
    public int getProfit() {
        return profit;
    }

    /**
     * @param profit the profit to set
     */
    public void setProfit(int profit) {
        this.profit = profit;
    }

    /**
     * @return the transationno
     */
    public int getTransationno() {
        return transationno;
    }

    /**
     * @param transationno the transationno to set
     */
    public void setTransationno(int transationno) {
        this.transationno = transationno;
    }
}
