package com.google.coconnell84.camStab;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;
import org.opencv.video.Video;

import com.google.coconnell84.ComputerVisionTutorial;
import com.google.coconnell84.ImageWindow;
import com.google.coconnell84.OpenCVInitializer;
import com.google.coconnell84.cameraCapture.CameraCapturer;
import com.google.coconnell84.cameraCapture.IFrameAvailListener;


/**
 * An example of a Camera Stabilization. Derived from C++ code located at:
 * http://nghiaho.com/?p=2093
 * @author Christopher
 *
 */
public class CameraStabilizationExample implements IFrameAvailListener {

    private Mat currentFrame = new Mat();
    private Mat prevGray = null;

    ImageWindow imageWindow = ImageWindow.createImageFrame(null, "Image Window");
    ImageWindow finalWindow = ImageWindow.createImageFrame(null, "Final Window");
    private CameraCapturer mCapture;
    private CameraStabilizationControlView mCalibControlView;

    public CameraStabilizationExample() {
	mCapture = new CameraCapturer();
	mCalibControlView = CameraStabilizationControlView.createAndShowGUI(new CameraStabilizationModel());
    }

    public void start() {

	mCapture.addFrameAvailListener(this);
    }

    @Override
    public void newFramAvail(Mat pFrame) {
	currentFrame = pFrame;
	 Mat currentFrame2 = new Mat();
	    currentFrame.copyTo(currentFrame2);
	    Mat currentGray = new Mat();
	    Imgproc.cvtColor(currentFrame, currentGray,Imgproc.COLOR_BGR2GRAY);
	    if(prevGray != null) {


		imageWindow.updateImage(ComputerVisionTutorial.toBufferedImage(currentFrame));

		MatOfPoint prevCorner = new MatOfPoint();
		MatOfPoint cur_corner = new MatOfPoint();
		CameraStabilizationModel aModel = mCalibControlView.getModel();

		Imgproc.GaussianBlur(prevGray, prevGray, new Size(3, 3) , 0);
		Imgproc.goodFeaturesToTrack(prevGray, prevCorner,aModel.getMaxCorners(), aModel.getQualityLevel(), aModel.getMinDistance());
		List<Point2D>overlays = new ArrayList<>();
		for(Point aPoint : prevCorner.toArray()) {
		    overlays.add(new Point2D.Double(aPoint.x, aPoint.y));
		}

		imageWindow.setOverlays(overlays);
		MatOfByte status = new MatOfByte();
		MatOfFloat err = new MatOfFloat();

		MatOfPoint2f prevCorner2F = new MatOfPoint2f(prevCorner.toArray());
		MatOfPoint2f cur_corner2F = new MatOfPoint2f(cur_corner.toArray());
		Video.calcOpticalFlowPyrLK(prevGray, currentGray, prevCorner2F, cur_corner2F, status, err);


		List<Byte> statusList = status.toList();
		Point[] prevCornerArray = prevCorner2F.toArray();
		Point[] currentCornerArray = cur_corner2F.toArray();

		List<Point>prevCornerFinal = new ArrayList<>();
		List<Point>currentCornerFinal = new ArrayList<Point>();
		for(int i=0; i<statusList.size();i++) {
		    Byte currentStatus = statusList.get(i);
		    if(currentStatus.intValue() == 1) {
			prevCornerFinal.add(prevCornerArray[i]);
			currentCornerFinal.add(currentCornerArray[i]);
		    }
		}

		if(!prevCornerFinal.isEmpty() && !currentCornerFinal.isEmpty()) {
		    MatOfPoint2f prevCornerFinalMat = new MatOfPoint2f(prevCornerFinal.toArray(new Point[prevCornerFinal.size()]));
		    MatOfPoint2f currCornerFinalMat = new MatOfPoint2f(currentCornerFinal.toArray(new Point[currentCornerFinal.size()]));
		    Mat finalTransform = Video.estimateRigidTransform(prevCornerFinalMat, currCornerFinalMat, false);


		    Size aSize = finalTransform.size();

		    if(aSize.width ==3 && aSize.height == 2) {
			System.out.println("Transform size= " + aSize.width +" x " + aSize.height);
			Imgproc.warpAffine(currentFrame, currentFrame2, finalTransform, currentFrame2.size());

			BufferedImage finalImage = ComputerVisionTutorial.toBufferedImage(currentFrame2);

			finalWindow.updateImage(finalImage);
		    } else {

			System.out.println("Transform size= " + aSize.width +" x " + aSize.height);
			finalWindow.updateImage(ComputerVisionTutorial.toBufferedImage(currentFrame));
			System.out.println("FAIL SIZE!");
		    }
		} else {
		    finalWindow.updateImage(ComputerVisionTutorial.toBufferedImage(currentFrame));
		    System.out.println("FAIL!");
		}
	    }

	    prevGray = currentGray;

    }

    public static void main(String[] args) {
	OpenCVInitializer.init();
	CameraStabilizationExample anExample = new CameraStabilizationExample();
	anExample.start();
    }

}
