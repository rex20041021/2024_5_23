package com.example.freeway_monkey;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

public class Power extends Character{

    String type;
    Bitmap img;
    Context context;
    Game game;
    boolean show = true;

    // constructor
    public Power(Context context, double centerX, double centerY, String type, Game game){
        super(centerX, centerY);
        this.context = context;
        this.game = game;
        this.type = type;
        switch (type){
            case "shield":
                img = BitmapFactory.decodeResource(context.getResources(), R.drawable.power_red_shield);
                img = Bitmap.createScaledBitmap( img, 99, 99, true);
                this.setImageHeightAndWidth(img.getHeight(), img.getWidth());
                break;
            case "freeze":
                img = BitmapFactory.decodeResource(context.getResources(), R.drawable.power_blue_freeze);
                img = Bitmap.createScaledBitmap( img, 99, 99, true);
                this.setImageHeightAndWidth(img.getHeight(), img.getWidth());
                break;
        }

    }

    // 每一幀要做的事
    public void update() {
        this.setCenterY(this.getCenterY() + game.getBackgroundSpeed());
    }

    // 畫到畫布上
    public void draw(Canvas canvas){
        if(show){
            super.drawImage(canvas, img);
        }
    }

    // 回傳道具類型
    public String getType() {
        return type;
    }

    // 設定畫面上看不看的到道具
    public void setVisble(boolean visble){
        show = visble;
    }
}
