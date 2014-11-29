import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.google.coconnell84.OpenCVInitializer;
import com.google.coconnell84.camStab.CameraStabilizationExample;
import com.google.coconnell84.lessons.colorTrack.ColorTrackingOne;


public class App {
    
    private static CameraStabilizationExample anExample;
    private static ColorTrackingOne aTracker;
    public static void createAndShowGUI() {
	JFrame aFrame = new JFrame();
	JPanel buttonPanel = new JPanel();
	JButton launchCameraStab = new JButton("Camera Stabilization");
	buttonPanel.add(launchCameraStab);
	launchCameraStab.addActionListener(new ActionListener() {
	    
	    @Override
	    public void actionPerformed(ActionEvent pE) {
		if(anExample == null) {
		    anExample = new CameraStabilizationExample();
		    anExample.start();
		}
	    }
	});
	
	JButton launchColorTrack = new JButton("Color Tracking");
	buttonPanel.add(launchColorTrack);
	launchColorTrack.addActionListener(new ActionListener() {
	    
	    @Override
	    public void actionPerformed(ActionEvent pE) {
		
		if(aTracker == null) {
		    aTracker = new ColorTrackingOne();
		}
	    }
	});
	
	aFrame.setContentPane(buttonPanel);
	aFrame.pack();
	aFrame.setVisible(true);
    }

    public static void main(String[] args) {

	OpenCVInitializer.init();

	SwingUtilities.invokeLater(new Runnable() {
	    
	    @Override
	    public void run() {
		createAndShowGUI();
	    }
	});
    }

}
