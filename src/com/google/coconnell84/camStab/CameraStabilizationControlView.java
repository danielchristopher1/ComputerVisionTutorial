package com.google.coconnell84.camStab;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class CameraStabilizationControlView {
    
    private CameraStabilizationModel mModel;
    private Object SYNC = new Object();
    
    JPanel mView = new JPanel();

    private JSlider mMaxCorners;

    public CameraStabilizationControlView(CameraStabilizationModel pModel) {
	mModel = pModel;
	
	mView.setLayout(new BoxLayout(mView, BoxLayout.Y_AXIS));
	JPanel maxCornersPanel = new JPanel();
	maxCornersPanel.add(new JLabel("MAX CORNERS: "));
	mMaxCorners = new JSlider(1, 500, mModel.getMaxCorners());
	maxCornersPanel.add(mMaxCorners);
	final JLabel maxLabels = new JLabel(Integer.toString(mModel.getMaxCorners()));
	maxCornersPanel.add(maxLabels);
	
	mMaxCorners.addChangeListener(new ChangeListener() {
	    
	    @Override
	    public void stateChanged(ChangeEvent pE) {
		maxLabels.setText(Integer.toString(mMaxCorners.getValue()));
		if(!mMaxCorners.getValueIsAdjusting()) {
		    synchronized(SYNC) {
		    mModel.setMaxCorners(mMaxCorners.getValue());
		    }
		}
	    }
	});
	
	JPanel qualityPanel = new JPanel();
	int qualityPercentage = (int)(mModel.getQualityLevel() * 100);
	final JSlider qualitySlider = new JSlider(1, 100, qualityPercentage);
	qualityPanel.add(new JLabel("Quality: "));
	qualityPanel.add(qualitySlider);
	final JLabel qualityPercentageLabel = new JLabel("000%");
	qualityPanel.add(qualityPercentageLabel);
	
	qualitySlider.addChangeListener(new ChangeListener() {
	    
	    @Override
	    public void stateChanged(ChangeEvent pE) {
		int currentPercentage = qualitySlider.getValue();
		qualityPercentageLabel.setText(Integer.toString(currentPercentage)+"%");
		
		if(!qualitySlider.getValueIsAdjusting()) {
		    mModel.setQualityLevel(currentPercentage/100d);
		}
	    }
	});
	
	JPanel minDistancePanel = new JPanel();
	double minDistance = (int)(mModel.getMinDistance());
	final JSlider distanceSlider = new JSlider(1, 300, (int)minDistance);
	minDistancePanel.add(new JLabel("MinDistance: "));
	minDistancePanel.add(distanceSlider);
	final JLabel distanceLabel = new JLabel("000 Pixels");
	distanceLabel.setText(Double.toString(minDistance) +" Pixels");
	minDistancePanel.add(distanceLabel);
	
	distanceSlider.addChangeListener(new ChangeListener() {
	    
	    @Override
	    public void stateChanged(ChangeEvent pE) {
		int minDistance = distanceSlider.getValue();
		distanceLabel.setText(Integer.toString(minDistance) +" Pixels");
		
		if(!distanceSlider.getValueIsAdjusting()) {
		    mModel.setMinDistance(minDistance);
		}
	    }
	});
	
	
	mView.add(maxCornersPanel);
	mView.add(qualityPanel);
	mView.add(minDistancePanel);
	
    }
    
    public JPanel getView() {
	return mView;
    }
    
    public CameraStabilizationModel getModel() {
	synchronized(SYNC) {
	    return new CameraStabilizationModel(mModel);
	}
    }
    
    public static CameraStabilizationControlView createAndShowGUI(CameraStabilizationModel pModel) {
	final CameraStabilizationControlView returnVal = new CameraStabilizationControlView(pModel);
	
	SwingUtilities.invokeLater(new Runnable() {
	    
	    @Override
	    public void run() {
		JFrame aFrame = new JFrame("Cam Calib Control");
		aFrame.setContentPane(returnVal.getView());
		aFrame.pack();
		aFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		aFrame.setVisible(true);
		
	    }
	});
	
	return returnVal;
    }

}
