package com.google.coconnell84.lessons.colorTrack;

import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.opencv.core.Mat;

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
    
    private ImageRenderer mOrigRender;
    private JFrame mFrame;


    public ColorTrackingOne() {
	CameraCapturer aCapturer = new CameraCapturer();
	
	mFrame = new  JFrame();
	JPanel contentPanel = new JPanel(new GridLayout(1, 2));
	mOrigRender = new ImageRenderer();
	mOrigRender.getView().setBorder(BorderFactory.createTitledBorder("Orig Image"));
	
	ImageRenderer finishedImage = new ImageRenderer();
	finishedImage.getView().setBorder(BorderFactory.createTitledBorder("Final Image"));
	contentPanel.add(mOrigRender.getView());
	contentPanel.add(finishedImage.getView());
	
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
	
	mOrigRender.setImage(ComputerVisionTutorial.toBufferedImage(pFrame));

	mFrame.pack();
	
    }
}
