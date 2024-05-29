package com.example.test2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Button extends Character{

    private Paint paint;
    private Context context;
    private Bitmap image;
    private double WIDTH;
    private double HEIGHT;
    Game game;

    public Button(Context context, Bitmap image, double centerX, double centerY, Game game) {
        super(centerX, centerY);
        this.context = context;
        this.game = game;
        paint = new Paint();
        this.image = image;
        this.setImageHeightAndWidth(image.getHeight(), image.getWidth());
    }

    public void draw(Canvas canvas) {
        super.drawImage(canvas, image);
    }

    public double getWIDTH() { return image.getWidth(); }
    public double getHEIGHT() { return image.getHeight(); }
}
