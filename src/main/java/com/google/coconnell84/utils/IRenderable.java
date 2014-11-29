package com.google.coconnell84.utils;

import java.awt.Graphics2D;

/**
 * Interface for a class that will render components. Rendering is donw in the 
 * render method
 */
public interface IRenderable {

    /**
     * Method to render, using the passed in graphics to draw on. 
     * @param pGraphics
     * 	The graphics that's used to draw onto
     */
    public void render(Graphics2D pGraphics);
}
