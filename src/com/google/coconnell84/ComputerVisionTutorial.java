package com.google.coconnell84;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class ComputerVisionTutorial {

    public static Image toBufferedImage(Mat m){
	int type = BufferedImage.TYPE_BYTE_GRAY;
	if ( m.channels() > 1 ) {
	    Mat m2 = new Mat();
	    Imgproc.cvtColor(m,m2,Imgproc.COLOR_BGR2RGB);
	    type = BufferedImage.TYPE_3BYTE_BGR;
	    m = m2;
	}
	byte [] b = new byte[m.channels()*m.cols()*m.rows()];
	m.get(0,0,b); // get all the pixels
	BufferedImage image = new BufferedImage(m.cols(),m.rows(), type);
	image.getRaster().setDataElements(0, 0, m.cols(),m.rows(), b);
	return image;
    }
    public static Mat toMatrix(BufferedImage img)
    {
	byte[] pixels = ((DataBufferByte) img.getRaster().getDataBuffer()).getData();
	Mat tmp = new Mat(img.getHeight(), img.getWidth(), CvType.CV_8UC3);
	tmp.put(0, 0, pixels);
	return tmp;
    }

    public static void main(String[] args) {
	System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }
}
