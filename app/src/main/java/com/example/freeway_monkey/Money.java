package com.example.freeway_monkey;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

public class Money extends Character{
    Context context;
    Game game;
    boolean counted = false;
    Bitmap img;

    public Money(Context context, double centerX, double centerY, Game game){
        super(centerX, centerY);
        this.context = context;
        this.game = game;
        this.img = BitmapFactory.decodeResource(context.getResources(), R.drawable.money);
        this.img = Bitmap.createScaledBitmap( img, 99, 99, true);
        this.setImageHeightAndWidth(img.getHeight(), img.getWidth());
    }

    // 每一幀要做的事
    public void update() {
        this.setCenterY(this.getCenterY() + game.getBackgroundSpeed());
    }

    // 畫到畫布上
    public void draw(Canvas canvas){
        if(!counted){
            super.drawImage(canvas, img);
        }
    }

    public void setCounted(boolean counted){
        this.counted = counted;
    }

    public boolean getCounted(){
        return counted;
    }
}
