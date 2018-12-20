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
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer3D;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

/**
 * A simple demonstration application showing how to create a vertical 3D bar
 * chart using data from a {@link CategoryDataset}.
 *
 */
public class BarChart3D extends JInternalFrame {

    String title = "";
    public String yaxislabel = "";
    public String xaxislable = "";

    /**
     * Creates a new demo.
     *
     * @param title the frame title.
     */
    public BarChart3D(String title, String yaxislabel,
            String xaxislable, ArrayList al, ArrayList xtable,
            ArrayList series) {

        super(title);
        this.title = title;
        final CategoryDataset dataset =  createDataset(al,  series,xtable);
        final JFreeChart chart = createChart(dataset);
        this.yaxislabel = yaxislabel;
        this.xaxislable = xaxislable;
 setClosable(true);
        setMaximizable(true);
        // add the chart to a panel...
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        setContentPane(chartPanel);

    }

    private CategoryDataset createDataset(ArrayList al, ArrayList series, ArrayList xlable) {

        // r
        // create the dataset...
        final DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (int i = 0; i < al.size(); i++) {
            ArrayList al1 = (ArrayList) al.get(i);
            for (int j = 0; j < al1.size(); j++) {
                if (Integer.class.isInstance(xlable.get(i))) {
                    dataset.addValue((Double) al1.get(j), (Integer) xlable.get(i), (String) series.get(j));
                } else if (Double.class.isInstance(xlable.get(i))) {
                    dataset.addValue((Double) al1.get(j), (Double) xlable.get(i), (String) series.get(j));
                } else {
                    dataset.addValue((Double) al1.get(j), (String) xlable.get(i), (String) series.get(j));
                }
            }
        }

        return dataset;

    }

    /**
     * Creates a sample dataset.
     *
     * @return a sample dataset.
     */
//    private CategoryDataset createDataset() {
//        ArrayList x = new ArrayList();
//        ArrayList<Double> y = new ArrayList();
//        try {
//            // Open the file that is the first 
//            // command line parameter
//            FileInputStream fstream = new FileInputStream("graph.txt");
//            // Get the object of DataInputStream
//            DataInputStream in = new DataInputStream(fstream);
//            BufferedReader br = new BufferedReader(new InputStreamReader(in));
//            String strLine;
//
//            //Read File Line By Line
//            while ((strLine = br.readLine()) != null) {
//                // Print the content on the console
//                String d[] = strLine.split(",");
//                if (d.length >= 2) {
//                    x.add(d[0]);
//                    y.add(Double.parseDouble(d[1]));
//                }
//                System.out.println(strLine);
//            }
//            //Close the input stream
//            in.close();
//        } catch (Exception e) {//Catch exception if any
//            System.err.println("Error: " + e.getMessage());
//        }
//
//        return createDataset(x, "No of Rule", y);
//
//    }
    /**
     * Creates a chart.
     *
     * @param dataset the dataset.
     *
     * @return The chart.
     */
    private JFreeChart createChart(final CategoryDataset dataset) {

        final JFreeChart chart = ChartFactory.createBarChart3D(
                title, // chart title
                xaxislable, // domain axis label
                yaxislabel, // range axis label
                dataset, // data
                PlotOrientation.VERTICAL, // orientation
                true, // include legend
                true, // tooltips
                false // urls
        );

        final CategoryPlot plot = chart.getCategoryPlot();
        final CategoryAxis axis = plot.getDomainAxis();
        axis.setCategoryLabelPositions(
                CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 8.0)
        );
        final BarRenderer3D renderer = (BarRenderer3D) plot.getRenderer();
        renderer.setDrawBarOutline(false);

        return chart;

    }

    // ****************************************************************************
    // * JFREECHART DEVELOPER GUIDE                                               *
    // * The JFreeChart Developer Guide, written by David Gilbert, is available   *
    // * to purchase from Object Refinery Limited:                                *
    // *                                                                          *
    // * http://www.object-refinery.com/jfreechart/guide.html                     *
    // *                                                                          *
    // * Sales are used to provide funding for the JFreeChart project - please    * 
    // * support us so that we can continue developing free software.             *
    // ****************************************************************************
    /**
     * Starting point for the demonstration application.
     *
     * @param args ignored.
     */
    public static void main(final String[] args) {

    }

}
