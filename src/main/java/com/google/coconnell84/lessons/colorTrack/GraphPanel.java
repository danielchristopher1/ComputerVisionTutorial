package com.google.coconnell84.lessons.colorTrack;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import com.google.coconnell84.cameraCapture.IFrameAvailListener;

public class GraphPanel implements IFrameAvailListener{
    private enum GRAPH_TYPE {
	HUE(180),
	SAT(256),
	VAL(256);
	private GRAPH_TYPE(int pMaxVal) {
	    mMaxValue = pMaxVal;
	}

	private final int mMaxValue;
    }

    private Map<GRAPH_TYPE, DrawGraphModel>mModel = new EnumMap<>(GRAPH_TYPE.class);
    private Map<GRAPH_TYPE, DrawGraph>mView = new EnumMap<>(GRAPH_TYPE.class);

    private static final Dimension GRAPH_SIZE = new Dimension(300,100);
    public GraphPanel() {
	for(GRAPH_TYPE aGraphType : GRAPH_TYPE.values()) {
	    DrawGraphModel aModel = new DrawGraphModel(GRAPH_SIZE);
	    mModel.put(aGraphType, aModel);
	    mView.put(aGraphType, new DrawGraph(aModel));
	}
	
	JFrame aFrame = new JFrame();
	JPanel contentPanel = new JPanel(new GridLayout(3, 1));
	for(GRAPH_TYPE aType : GRAPH_TYPE.values()) {
	    contentPanel.add(mView.get(aType));
	}
	aFrame.setContentPane(contentPanel);
	aFrame.pack();
	aFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	aFrame.setVisible(true);
    }
    @Override
    public void newFramAvail(Mat pFrame) {
	Mat hsvImage = new Mat();
	//Step 1: Convert the image to HUV
	Imgproc.cvtColor(pFrame, hsvImage, Imgproc.COLOR_BGR2HSV);

	final Map<GRAPH_TYPE, int []>countsMap = new EnumMap<>(GRAPH_TYPE.class);
	for(GRAPH_TYPE aGraphType : GRAPH_TYPE.values()) {

	    int counts[] = new int[aGraphType.mMaxValue];


	    for(int i=0;i<counts.length;i++) {
		counts[i] = 0;
	    }
	    countsMap.put(aGraphType, counts);

	}

	for(int i=0;i<hsvImage.rows();i++) {
	    for(int j=0;j<hsvImage.cols();j++) {
		double[] singlePixel = hsvImage.get(i, j);

		for(GRAPH_TYPE aType : GRAPH_TYPE.values()) {
		    countsMap.get(aType)[(int)singlePixel[aType.ordinal()]]++;
		}
	    }
	}

	SwingUtilities.invokeLater(new Runnable() {

	    @Override
	    public void run() {


		for(GRAPH_TYPE aType : GRAPH_TYPE.values()) {
		    List<Integer>newCounts = new ArrayList<Integer>();
		    for(int aVal : countsMap.get(aType)) {
			newCounts.add(aVal);
		    }
		    
		    mModel.get(aType).setCounts(newCounts);
		    mView.get(aType).updateModel(mModel.get(aType));
		}
	    }
	});

    }

}
