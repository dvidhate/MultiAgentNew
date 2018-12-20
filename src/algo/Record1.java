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
import java.util.Random;

/**
 *
 * @author ganesh
 */
public class Record1 implements Cloneable, Serializable, Comparable<Record1> {

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
    private double price;
    private int age;
    private Date dt;
    private String priceflag;
    private String ageflag;
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

    public Record1(String name, int qty, double price, int age, Date dt) {
        this.name = name;
        this.qty = qty;
        this.price = price;
        this.age = age;
        this.dt = dt;
        setAgeflag();
        setPriceflag();
        qflag = dt.getMonth() <= 3 ? "Q1" : dt.getMonth() <= 6 ? "Q2" : dt.getMonth() <= 9 ? "Q3" : "Q4";
    }

    public static void random(int noprod, int noofrecord, String agent) {
        Random rn = new Random();
        double price[] = new double[noprod];
        double pr[] = new double[]{100, 200, 300, 400, 500, 1000, 750, 850, 700, 800};
        for (int i = 0; i < noprod; i++) {
            price[i] = pr[rn.nextInt(pr.length)] + 500;
            if (price[i] > getMaxpprize()) {
                setMaxpprize(price[i]);
            }
        }
        setMaxpprize(getMaxpprize() / 3);
        ArrayList<Record1> hs = new ArrayList();
        ArrayList<Integer> prod = new ArrayList();
        for (int i = 0; i < noprod / 2; i++) {
            prod.add(rn.nextInt(noprod));
        }
        for (int i = 0; i < noofrecord; i++) {
            Date dt = new Date(new Date().getYear(), rn.nextInt(12) + 1, rn.nextInt(30));
            Record1 rd = null;
            if ((i % 3) == 0) {
                rd = new Record1(agent + "_P" + rn.nextInt(noprod), rn.nextInt(5) + 1, price[rn.nextInt(noprod)], rn.nextInt(70), dt);
            } else {
                rd = new Record1(agent + "_P" + prod.get(rn.nextInt(prod.size())), rn.nextInt(5) + 1, price[rn.nextInt(noprod)], rn.nextInt(70), dt);
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
    public int compareTo(Record1 o) {
        return getDt().compareTo(o.getDt());
    }

    public static ArrayList readXML(String filename) {
        ArrayList<Record1> hs = new ArrayList();

        try {

            XStream xstream = new XStream(new DomDriver());
            xstream.alias("ArrayList", HashMap.class);
            xstream.alias("Job", Record1.class);

            FileInputStream fis = new FileInputStream("db/" + filename);
            InputStreamReader isr = new InputStreamReader(fis);
            ObjectInputStream in = xstream.createObjectInputStream(isr);
            hs = (ArrayList) in.readObject();

            in.close();

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
            xstream.alias("Job", Record1.class);

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
            xstream.alias("Job", Record1.class);

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

    public static void writeCSV(ArrayList<Record1> al, String filename) {
        try {

            FileWriter fr = new FileWriter("csv/" + filename + ".csv");
            fr.write("Name,Price,Q,Age\n");
            for (int i = 0; i < al.size(); i++) {
                fr.write(al.get(i).name + "," + al.get(i).priceflag + "," + al.get(i).getQflag() + "," + al.get(i).ageflag + "\n");
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
            Record1.random(20, 1000, "agent" + i);
        }
    }

    /**
     * @return the qflag
     */
    public String getQflag() {
        return qflag;
    }
}
