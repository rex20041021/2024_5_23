package com.example.test2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.widget.Switch;

public class Power extends Character{

    String type;
    Bitmap img;
    Context context;
    Game game;

    public Power(Context context, double centerX, double centerY, String type, Game game){
        super(centerX, centerY);
        this.context = context;
        this.game = game;
        this.type = type;
        switch (type){
            case "shield":
                img = BitmapFactory.decodeResource(context.getResources(), R.drawable.power_red_shield);
                this.setImageHeightAndWidth(img.getHeight(), img.getWidth());
                break;
            case "inverse":
                img = BitmapFactory.decodeResource(context.getResources(), R.drawable.power_red_inverse);
                this.setImageHeightAndWidth(img.getHeight(), img.getWidth());
                break;
        }
    }


    public void update() {
        this.setCenterY(this.getCenterY() + game.getBackgroundSpeed());
    }

    public void draw(Canvas canvas){
        super.drawImage(canvas, img);
    }

    public String getType() {
        return type;
    }
}
