package com.google.coconnell84.cameraCapture;

import org.opencv.core.Mat;

/**
 * Interface for a listener for a new frame from the camera
 *
 */
public interface IFrameAvailListener {
    
    /**
     * Method that will be called when a new frame is avaiable
     * @param pFrame
     * 	The passed in frame
     */
    public void newFramAvail(Mat pFrame);

}
