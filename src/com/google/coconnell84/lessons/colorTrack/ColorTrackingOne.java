package com.google.coconnell84.lessons.colorTrack;

import java.awt.GridLayout;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

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


    public ColorTrackingOne() {
	CameraCapturer aCapturer = new CameraCapturer();
	
	mFrame = new  JFrame();
	
	
	int numRows = (NUM_STEPS/STEPS_PER_ROW);
	
	JPanel contentPanel = new JPanel(new GridLayout(numRows, STEPS_PER_ROW));
	for(int i=0;i<NUM_STEPS;i++) {
	    ImageRenderer aRender = new ImageRenderer();
	    String title = (i ==0)?"ORIG IMAGE" : "STEP-"+i;
	    aRender.getView().setBorder(BorderFactory.createTitledBorder(title));
	    contentPanel.add(aRender.getView());
	    mRenderes.put(i, aRender);
	}
	
	
	mFrame.setContentPane(contentPanel);
	mFrame.pack();
	mFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	mFrame.setVisible(true);
	

	aCapturer.addFrameAvailListener(this);
	
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
	Scalar lower = new Scalar(20, 100, 100);
	Scalar upper = new Scalar(30, 255, 255);
	Core.inRange(hsvImage, lower, upper, threshImage);
	
	mRenderes.get(2).setImage(ComputerVisionTutorial.toBufferedImage(threshImage));
	
	mFrame.pack();
	
    }
}
