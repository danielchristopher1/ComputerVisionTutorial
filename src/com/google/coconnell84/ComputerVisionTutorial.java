package com.google.coconnell84;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

/**
 * A class that contains utility methods for computer vision 
 */
public class ComputerVisionTutorial {

    /**
     * Helper method to convert an OPENCV {@link Mat} to an {@link Image}
     * If the passed in image is a gray scale, the returned image will be gray. 
     * If the passed in image is multi-channel, the return image is RGB
     * @param pMatrix
     * 	The matrix to convert
     * @return
     * 	The Image
     */
    public static BufferedImage toBufferedImage(Mat pMatrix){
	
	int type = BufferedImage.TYPE_BYTE_GRAY;
	if ( pMatrix.channels() > 1 ) {
	    Mat m2 = new Mat();
	    Imgproc.cvtColor(pMatrix,m2,Imgproc.COLOR_BGR2RGB);
	    type = BufferedImage.TYPE_3BYTE_BGR;
	    pMatrix = m2;
	}
	byte [] b = new byte[pMatrix.channels()*pMatrix.cols()*pMatrix.rows()];
	pMatrix.get(0,0,b); // get all the pixels
	BufferedImage image = new BufferedImage(pMatrix.cols(),pMatrix.rows(), type);
	image.getRaster().setDataElements(0, 0, pMatrix.cols(),pMatrix.rows(), b);
	return image;
    }

    /**
     * Method to convert an image 
     * @param pImage
     * 	The image to convert
     * @return
     * 	A {@link Mat} implemetation of the image. The type will be 8-bit, unsigned, 
     * 3 channels (RGB) 
     */
    public static Mat toMatrix(BufferedImage pImage)
    {
	byte[] pixels = ((DataBufferByte) pImage.getRaster().getDataBuffer()).getData();
	Mat tmp = new Mat(pImage.getHeight(), pImage.getWidth(), CvType.CV_8UC3);
	tmp.put(0, 0, pixels);
	return tmp;
    }
    
    /**
     * This will load an image from the file. If
     * @param pFile
     * 	The file that represents a location of the image. If the {@link File} isn't
     * an image, this method will return null
     * @return
     * 	An {@link Image} or null if the file doesn't exist or isn't an image
     */
    public static BufferedImage getImageFromFile(File pFile) {
	BufferedImage returnImage = null;
	try {
	    returnImage = ImageIO.read(pFile);
	} catch (IOException e) {
	    e.printStackTrace();
	}

	return returnImage;
    }

    public static void saveImageToFile(BufferedImage pImage, File pFile) throws IOException {
	String anExtension = getFileExtension(pFile);
	if(anExtension != null) {

	    ImageIO.write(pImage, anExtension, pFile);
	}
	throw new IOException("File: " + pFile +" is not an image type");
    }

    private static String getFileExtension(File file) {
	String name = file.getName();
	int lastIndexOf = name.lastIndexOf(".");
	if (lastIndexOf == -1) {
	    return null;
	}
	String aSubstring = name.substring(lastIndexOf);
	aSubstring = aSubstring.toLowerCase();

	if(!aSubstring.equals("png") && !aSubstring.equals("jpg") && !aSubstring.endsWith("gif")) {
	    aSubstring = null;
	}

	return aSubstring;
    }
    
    public static Mat convertToGrayScale(Mat pMat) {
	Imgproc.cvtColor(pMat, pMat, Imgproc.COLOR_BGR2GRAY);
	return pMat;
    }

    public static void main(String[] args) {
	OpenCVInitializer.init();
	URL aResource = ComputerVisionTutorial.class.getResource("Test.jpg");
	File aFile = new File(aResource.getFile());
	
	if(aFile.exists()) {
	    Mat aMatrix = toMatrix(getImageFromFile(aFile));
	    final BufferedImage orignalImage = toBufferedImage(aMatrix);
	    aMatrix = convertToGrayScale(aMatrix);
	    final BufferedImage grayImage = toBufferedImage(aMatrix);
	    
	    SwingUtilities.invokeLater(new Runnable() {
	        
	        @Override
	        public void run() {
	    		ImageWindow.createImageFrame(orignalImage, "Original Image");
	    		ImageWindow.createImageFrame(grayImage, "Gray Image");
	        }
	    });
	}
    }
}
