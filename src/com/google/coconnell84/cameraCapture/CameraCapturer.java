package com.google.coconnell84.cameraCapture;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import org.opencv.core.Mat;
import org.opencv.highgui.VideoCapture;

/**
 * Class responsible for registering with the web camera and grabbing frames from
 * the camera at a regular rate
 *
 */
public class CameraCapturer {


    /**
     * Interface to the webacamera
     */
    private final VideoCapture mCamera;
    /**
     * Set of listeners for new frames
     */
    private final Set<IFrameAvailListener>mListeners = new CopyOnWriteArraySet<>();
    
    /**
     * Timer task that will grab a frame and then notify listeners
     */
    private final Runnable mCameraRunnable = new Runnable() {

	@Override
	public void run() {
	    Mat currentFrame = new Mat();
	    mCamera.read(currentFrame);
	    notifyListeners(currentFrame);
	}
    };
    /**
     * Executor service to start a timer to start pulling frames of the camera 
     * and then notify listeners
     */
    private static final ScheduledExecutorService CAMERA_EXEC = Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {

	@Override
	public Thread newThread(Runnable pR) {
	    return new Thread(pR, "Camera Thread");
	}
    });
    /**
     * The rate at which to pull frames from the camera, in milliseconds
     */
    private static final long CAM_RATE_MILLIS = 100;

    /**
     * This will connect to the camera and start the camera frame pulling thread.
     */
    public CameraCapturer() {

	int DEVICE = 0;
	mCamera = new VideoCapture(DEVICE);
	try {
	    Thread.sleep(1000l);
	} catch (InterruptedException e1) {
	    e1.printStackTrace();
	}


	mCamera.open(DEVICE);

	if(!mCamera.isOpened()) {
	    throw new IllegalStateException("Unable to start the camera");
	}
	CAMERA_EXEC.scheduleAtFixedRate(mCameraRunnable, CAM_RATE_MILLIS, CAM_RATE_MILLIS, TimeUnit.MILLISECONDS);
    }


    /**
     * Method to register a listener for frame updates
     * @param pListener
     * 	The listener to register. This listener will be notified when a frame 
     * is available
     */
    public void addFrameAvailListener(IFrameAvailListener pListener) {
	mListeners.add(pListener);
    }

    /**
     * Method to remove a listener. Once this method is called, the listener
     * will no longer receive updates of new frames
     * @param pListener
     * 	The listener that will stop receiving updates
     */
    public void removeFrameAvailListener(IFrameAvailListener pListener) {
	mListeners.remove(pListener);
    }

    private void notifyListeners(Mat pMat) {

	for(IFrameAvailListener aListener : mListeners) {
	    try {
		aListener.newFramAvail(pMat);


	    } catch(Exception e) {
		e.printStackTrace();
	    }
	}
    }

}
