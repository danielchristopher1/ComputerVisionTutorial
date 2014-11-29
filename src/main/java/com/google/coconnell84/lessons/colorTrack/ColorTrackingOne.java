package com.google.coconnell84.lessons.colorTrack;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import com.google.coconnell84.ComputerVisionTutorial;
import com.google.coconnell84.OpenCVInitializer;
import com.google.coconnell84.cameraCapture.CameraCapturer;
import com.google.coconnell84.cameraCapture.IFrameAvailListener;
import com.google.coconnell84.utils.ImageRenderer;

/**
 * This class is the first part of the lesson on color tracking. This is based 
 * off of the tutorial located at: 
 * http://www.aishack.in/tutorials/tracking-colored-objects-in-opencv/ Each
 * commit on this branch is a single tutorial step.
 *
 */
public class ColorTrackingOne implements IFrameAvailListener {

    private Map<Integer, ImageRenderer>mRenderes = new TreeMap();
    private JFrame mFrame;
    private static final int NUM_STEPS = 3;
    private static final int STEPS_PER_ROW=3;
    protected static final Scalar lower = new Scalar(20, 100, 100);
    protected static final Scalar upper = new Scalar(30, 255, 255);

    private  DrawGraph mHueGraph = null;

    private Color mLowerColor = getFromScalar(lower);
    private Color mUpperColor = getFromScalar(upper);

    public static Color getFromScalar(Scalar pScalar) {
	double hValue = (pScalar.val[0]/180f);
	double sValue = (pScalar.val[1]/255f);
	double vValue = (pScalar.val[2]/255f);

	return Color.getHSBColor((float)hValue, (float)sValue, (float)vValue);
    }

    public static Scalar getFromColor(Color pColor) {
	float[] hsv = new float[3];
	Color.RGBtoHSB(pColor.getRed(),pColor.getGreen(),pColor.getBlue(),hsv);

	return new Scalar(Math.ceil(hsv[0]*179f), Math.ceil(hsv[1]*255f),Math.ceil(hsv[2]*255f));
    }

    public ColorTrackingOne() {
	mFrame = new  JFrame();


	int numRows = (NUM_STEPS/STEPS_PER_ROW);
	JPanel contentPanel = new JPanel(new BorderLayout());
	JPanel imagePanel = new JPanel(new GridLayout(numRows, STEPS_PER_ROW));
	for(int i=0;i<NUM_STEPS;i++) {
	    ImageRenderer aRender = new ImageRenderer();
	    String title = (i ==0)?"ORIG IMAGE" : "STEP-"+i;
	    aRender.getView().setBorder(BorderFactory.createTitledBorder(title));
	    imagePanel.add(aRender.getView());
	    mRenderes.put(i, aRender);
	}

	JMenuBar aMenuBar = new JMenuBar();
	JMenu mainMenu = new JMenu("MAIN");


	aMenuBar.add(mainMenu);

	JPanel topPanel = new JPanel();
	topPanel.add(aMenuBar);

	final Color lowerColor = Color.getHSBColor((float)lower.val[0],(float) lower.val[1],(float) lower.val[2]);
	final JLabel lowerColorLabel= new JLabel("LOWER");
	lowerColorLabel.setForeground(lowerColor);
	topPanel.add(lowerColorLabel);

	Color upperColor= Color.getHSBColor((float)upper.val[0],(float) upper.val[1],(float) upper.val[2]);
	final JLabel upperColorLabel = new JLabel("UPPER");
	upperColorLabel.setForeground(upperColor);
	topPanel.add(upperColorLabel);


	JMenuItem setLowerColor = new JMenuItem("Set Lower Color");
	setLowerColor.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent pE) {
		Color newColor = JColorChooser.showDialog(null, "Pick Lower Color", mLowerColor);
		if(newColor != null)  {
		    mLowerColor = newColor;

		    lowerColorLabel.setForeground(mLowerColor);
		}
	    }
	});
	mainMenu.add(setLowerColor);

	JMenuItem setUpperColor = new JMenuItem("Set Upper Color");
	setUpperColor.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent pE) {
		Color newColor = JColorChooser.showDialog(null, "Pick Upper Color", mUpperColor);

		if(newColor != null) {
		    mUpperColor = newColor;
		    upperColorLabel.setForeground(mUpperColor);
		}
	    }
	});

	mainMenu.add(setUpperColor);

	contentPanel.add(topPanel, BorderLayout.NORTH);
	contentPanel.add(imagePanel, BorderLayout.CENTER);
	mFrame.setContentPane(contentPanel);
	mFrame.pack();
	mFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	mFrame.setVisible(true);

    }


    public static void main(String[] args) {
	OpenCVInitializer.init();
	ColorTrackingOne aTracker = new ColorTrackingOne();
    }


    @Override
    public void newFramAvail(Mat pFrame) {

	mRenderes.get(0).setImage(ComputerVisionTutorial.toBufferedImage(pFrame));

	Mat hsvImage = new Mat();
	//Step 1: Convert the image to HUV
	Imgproc.cvtColor(pFrame, hsvImage, Imgproc.COLOR_BGR2HSV);

	mRenderes.get(1).setImage(ComputerVisionTutorial.toBufferedImage(hsvImage));

	Mat threshImage = new Mat(pFrame.size(),CvType.CV_8UC1);

	Scalar lowerColor = getFromColor(mLowerColor);
	Scalar upperColor = getFromColor(mUpperColor);

	String lowerString = printScalar("lower= ", lowerColor);
	String upperString = printScalar("upper= ", upperColor);
	System.out.println(lowerString +", "+ upperString);

	Core.inRange(hsvImage, lowerColor, upperColor, threshImage);

	mRenderes.get(2).setImage(ComputerVisionTutorial.toBufferedImage(threshImage));

	mFrame.pack();

    }

    private String printScalar(String pString, Scalar pLowerColor) {
	StringBuilder returnString = new StringBuilder();
	double[] aVal = pLowerColor.val;
	returnString.append(pString);
	returnString.append("[H= ").append(aVal[0]);
	returnString.append(", S= ").append(aVal[1]);
	returnString.append(", V= ").append(aVal[2]);
	returnString.append("]");
	return returnString.toString();
    }
}
