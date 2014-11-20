package com.google.coconnell84.camStab;

public class CameraStabilizationModel {
    
    public CameraStabilizationModel() {
    }
    
    public CameraStabilizationModel(CameraStabilizationModel pModel) {
	maxCorners = pModel.getMaxCorners();
	minDistance = pModel.getMinDistance();
	qualityLevel = pModel.getQualityLevel();
    }

    public int getMaxCorners() {
        return maxCorners;
    }

    public void setMaxCorners(int pMaxCorners) {
        maxCorners = pMaxCorners;
    }

    public double getQualityLevel() {
        return qualityLevel;
    }

    public void setQualityLevel(double pQualityLevel) {
        qualityLevel = pQualityLevel;
    }

    public double getMinDistance() {
        return minDistance;
    }

    public void setMinDistance(double pMinDistance) {
        minDistance = pMinDistance;
    }

    /**The maximum number of points**/
    private int maxCorners = 200;
    /**Quality, in percentage decimal, i.e. 0.01 = 1%**/
    private double qualityLevel = 0.01;
    
    private double minDistance = 30;
}
