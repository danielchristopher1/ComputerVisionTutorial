package com.google.coconnell84;

import java.awt.Dimension;
import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class ImageWindow {
    
    private JPanel mView = new JPanel() {
	protected void paintComponent(java.awt.Graphics g) {
	    super.paintComponent(g);
	    g.drawImage(mImage,0,0,mImage.getWidth(null),mImage.getHeight(null), null);
	};
    };

    private Image mImage;    
    
    private ImageWindow(Image pImage) {
	mImage = pImage;
	mView.setPreferredSize(new Dimension(mImage.getWidth(null), mImage.getHeight(null)));
    }
    
    public static void createImageFrame(Image anImage) {
	JFrame aFrame = new JFrame();
	ImageWindow aWindow = new ImageWindow(anImage);
	aFrame.setContentPane(aWindow.mView);
	aFrame.pack();
	aFrame.setVisible(true);
    }

}
