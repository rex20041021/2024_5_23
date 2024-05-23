package com.example.test2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

// 背景class
class Background extends Character{

    private double speed = Game.getBackgroundSpeed();
    Bitmap image;

    // constructor
    public Background(Context context, double centerX, double centerY){
        super(centerX, centerY);
        image = BitmapFactory.decodeResource(context.getResources(), R.drawable.road);
        image = Bitmap.createScaledBitmap( image, (int)Game.getWIDTH(), (int)Game.getHEIGHT()+100, true);
        this.setImageHeightAndWidth(image.getHeight(), image.getWidth());
    }

    // 每一幀要做的事(一直往下跑)
    public void update() {
        speed = Game.getBackgroundSpeed();
        this.setCenterY(this.getCenterY() + speed);
    }

    // 畫到畫布上
    public void draw(Canvas canvas) {
        super.drawImage(canvas, image);
    }

}
