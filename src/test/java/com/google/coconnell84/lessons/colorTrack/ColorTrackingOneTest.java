package com.google.coconnell84.lessons.colorTrack;

import java.awt.Color;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.opencv.core.Scalar;

import com.google.coconnell84.OpenCVInitializer;

public class ColorTrackingOneTest {
    @Before
    public void setup() {
	OpenCVInitializer.init();
    }

    @Test
    public void testGetFromScalar() {
	Color aFromScalar = ColorTrackingOne.getFromScalar(ColorTrackingOne.lower);
	;
	
	Assert.assertEquals(ColorTrackingOne.lower, ColorTrackingOne.getFromColor(aFromScalar));
	
	Color upper = ColorTrackingOne.getFromScalar(ColorTrackingOne.upper);
	Assert.assertEquals(ColorTrackingOne.upper, ColorTrackingOne.getFromColor(upper));
    }

    @Test
    public void testGetFromColor() {
	Color origColor = Color.RED;
	
	Scalar redScalar = ColorTrackingOne.getFromColor(origColor);
	Color aFromScalar = ColorTrackingOne.getFromScalar(redScalar);
	Assert.assertEquals(origColor, aFromScalar);
    }

}
