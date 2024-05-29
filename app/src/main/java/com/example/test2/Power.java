package com.example.test2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;
import android.widget.Switch;

public class Power extends Character{

    String type;
    Bitmap img;
    Context context;
    Game game;
    boolean show = true;

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
//        Log.d("power", img.getHeight()+","+img.getHeight());
//        System.out.println(img.getHeight());
//        System.out.println(img.getWidth());
    }


    public void update() {
        this.setCenterY(this.getCenterY() + game.getBackgroundSpeed());
    }

    public void draw(Canvas canvas){
        if(show){
            super.drawImage(canvas, img);
        }
    }

    public String getType() {
        return type;
    }

    public void setVisble(boolean visble){
        show = visble;
    }
}
