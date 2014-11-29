package com.google.coconnell84.lessons.colorTrack;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DrawGraphModel {

    public DrawGraphModel(List<Integer>scores, Dimension pSize) {

	if(scores != null && !scores.isEmpty()) {

	    List<Integer>sorted = new ArrayList<Integer>(scores);
	    Collections.sort(sorted);
	    mMaxCount = sorted.get(sorted.size()-1);
	}
	counts = scores;
	mSize = pSize;
    }

    private int mMaxCount;
    private List<Integer>counts = new ArrayList<Integer>();
    private Dimension mSize;
    public int getMaxCount() {
	return mMaxCount;
    }

    public List<Integer> getCounts() {
	return counts;
    }
    public void setCounts(List<Integer> pCounts) {
	counts = pCounts;
	List<Integer>sorted = new ArrayList<Integer>(pCounts);
	Collections.sort(sorted);
	mMaxCount = sorted.get(sorted.size()-1);
    }
    public Dimension getSize() {
	return mSize;
    }
    public void setSize(Dimension pSize) {
	mSize = pSize;
    }


}
