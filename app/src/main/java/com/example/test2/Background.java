package com.example.test2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

// 背景class
class Background extends Character{

    private double speed;
    Bitmap image;
    Game game;

    // constructor
    public Background(Context context, double centerX, double centerY, Game game){
        super(centerX, centerY);
        image = BitmapFactory.decodeResource(context.getResources(), R.drawable.road);
        image = Bitmap.createScaledBitmap( image, (int)game.getWIDTH(), (int)game.getHEIGHT()+100, true);
        this.setImageHeightAndWidth(image.getHeight(), image.getWidth());
        this.game = game;
        speed = game.getBackgroundSpeed();
    }

    // 每一幀要做的事(一直往下跑)
    public void update() {
        speed = game.getBackgroundSpeed();
        this.setCenterY(this.getCenterY() + speed);
    }

    // 畫到畫布上
    public void draw(Canvas canvas) {
        super.drawImage(canvas, image);
    }

}
