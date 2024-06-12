package com.example.freeway_monkey;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

// 每個角色的父類別
public abstract class Character extends CoordinateRect{
    protected Paint paint;

    private  double imageHeight;
    private  double imageWidth;

    // consturctor
    public Character(double centerX, double centerY){
        super(centerX, centerY);
        paint = new Paint();
    }

    // 設定高跟寬
    public void setImageHeightAndWidth(double imageHeight, double imageWidth){
        this.imageHeight = imageHeight;
        this.imageWidth = imageWidth;
        setImageHeight(imageHeight);
        setImageWidth(imageWidth);
        setCoordinate();
    }

    // 畫到畫布上
    public void drawImage(Canvas canvas, Bitmap image){
        canvas.drawBitmap(image, (float)(getLeftX()), (float) (getTopY()), paint);
//        canvas.drawBitmap();
    }

    // 判斷是否碰撞
    public static boolean isCollide(Character a, Character b){
        if(a.getRightX() <= b.getLeftX() + 10 || a.getLeftX() + 10 >= b.getRightX() || a.getBottomY() <= b.getTopY()+10|| a.getTopY()+10 >= b.getBottomY()){
            return false;
        }
        return  true;
    };

}
