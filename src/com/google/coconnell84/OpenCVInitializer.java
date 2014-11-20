package com.google.coconnell84;

import org.opencv.core.Core;

/**
 * Class to initialize the OpenCV libaray. Ensure that the following property
 * java.library.path
 * includes the opencv path. For example:
 * $OPEN_CV_HOME/build/java/x64
 * @author Christopher
 *
 */
public class OpenCVInitializer {
    
    
    static {
	//Load the opencv library, exactly once
	System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }
    
    public static void init() {
	
    }
    
    private OpenCVInitializer() {
	
    }

}
