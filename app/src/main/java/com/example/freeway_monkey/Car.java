package com.example.freeway_monkey;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;

// 車車class
public class Car extends Character{

    Context context;
    Bitmap image;
    double speed;
    boolean counted = false;
    Game game;

    // constructor
    public Car(Context context, Bitmap image, double centerX, double centerY, Game game){
        super(centerX ,centerY);
        this.context = context;
        this.game = game;
        this.image = image;
        this.setImageHeightAndWidth(image.getHeight(), image.getWidth());

    }

    // 每一幀要做的事(一直往下跑)
    public void update(){
        speed = game.getBackgroundSpeed();
        this.setCenterY(this.getCenterY()+speed);
    }

    // 畫到畫布上
    public void draw(Canvas canvas){
        super.drawImage(canvas, image);
    }

}
