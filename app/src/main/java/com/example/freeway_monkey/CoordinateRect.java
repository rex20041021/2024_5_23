package com.example.freeway_monkey;

// 角色座標(長方形)
public class CoordinateRect {
    protected double imageWidth;
    protected double imageHeight;

    private double centerX;
    private double centerY;
    private double rightX;
    private double leftX;
    private double topY;
    private double bottomY;

    public CoordinateRect(double centerX, double centerY){
        this.centerX = centerX;
        this.centerY = centerY;
        setCoordinate();
    }

    public void setImageHeight(double imageHeight) {
        this.imageHeight = imageHeight;
    }

    public void setImageWidth(double imageWidth) {
        this.imageWidth = imageWidth;
    }

    public void setCoordinate(){
        rightX = centerX + imageWidth/2;
        leftX = centerX - imageWidth/2;
        topY = centerY - imageHeight/2;
        bottomY = centerY + imageHeight/2;
    }

    public void setRightX(double rightX) {
        this.rightX = rightX;
        centerX = rightX - imageWidth/2;
        setCoordinate();
    }

    public void setCenterX(double centerX) {
        this.centerX = centerX;
        setCoordinate();
    }

    public void setLeftX(double leftX) {
        this.leftX = leftX;
        centerX = leftX + imageWidth/2;
        setCoordinate();
    }

    public void setTopY(double topY) {
        this.topY = topY;
        centerY = topY + imageHeight/2;
        setCoordinate();
    }

    public void setCenterY(double centerY) {
        this.centerY = centerY;
        setCoordinate();
    }

    public void setBottomY(double bottomY) {
        this.bottomY = bottomY;
        centerY = bottomY - imageHeight/2;
        setCoordinate();
    }

    public double getRightX() {
        return rightX;
    }

    public double getCenterX() {
        return centerX;
    }

    public double getLeftX() {
        return leftX;
    }

    public double getTopY() {
        return topY;
    }

    public double getCenterY() {
        return centerY;
    }

    public double getBottomY() {
        return bottomY;
    }
}
