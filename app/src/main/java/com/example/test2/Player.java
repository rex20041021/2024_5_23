package com.example.test2;

import android.app.Service;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Vibrator;
import android.util.Log;

// 玩家class
class Player extends Character{

    private Paint paint;
    private Context context;
    private int HP = 2;

    private Bitmap[] images = new Bitmap[3];

    private double speed = 15;
    private double sensorDx;

    private boolean hidden = false;
    private long hide_time;
    Vibrator vibrator;


    // constructor
    public Player(Context context, double centerX, double centerY){
        super(centerX, centerY);
        this.context = context;
        paint = new Paint();
        loadImages();
        this.setImageHeightAndWidth(images[0].getHeight(), images[0].getWidth());
        vibrator = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);

    }

    // 移動
    public void move(){
        this.sensorDx = -Game.getSensorDx();
        this.setCenterX(this.getCenterX() + speed*sensorDx);
    }

    // player受傷函式
    public void getHurt(){
        vibrator.vibrate(100);
        HP--;
        if(HP < 0) HP=0;
        this.setImageHeightAndWidth(images[HP].getHeight(), images[HP].getWidth());
        if(this.HP > 0)
        this.hide();
    }

    // 把player藏起來
    public void hide(){
        hidden = true;
        hide_time = System.currentTimeMillis();
        this.setCenterY(-20000);
    }

    // 每一幀要做的事
    public void update() {
        this.move();
        if(this.getLeftX() <= 0){
            this.setLeftX(0);
        }

        if(this.getRightX() >= Game.getWIDTH()){
            this.setRightX(Game.getWIDTH());
        }

        if(this.hidden && Game.now - this.hide_time > 1000){
            this.hidden = false;
            this.setCenterX(Game.getWIDTH()/2);
            this.setBottomY(Game.getHEIGHT() - 100);
        }
    }

    // 畫到畫布上
    public void draw(Canvas canvas) {
        super.drawImage(canvas, images[HP]);
    }

    // 載入圖片
    private void loadImages(){
        Bitmap image;
        int ID;
        String[] colorByHp = {"red", "yellow", "green"};
        for(int i=0; i<colorByHp.length; i++){
            ID = context.getResources().getIdentifier(String.format("motorcycle_%s", colorByHp[i]),"drawable",context.getPackageName());
            image = BitmapFactory.decodeResource(context.getResources(), ID);
            image = Bitmap.createScaledBitmap( image, (int)(image.getWidth()*0.8), (int)(image.getHeight()*0.8), true);
            images[i] = image;
        }
    }
}
