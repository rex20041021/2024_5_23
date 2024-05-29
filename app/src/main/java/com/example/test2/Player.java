package com.example.test2;

import android.app.Service;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Vibrator;
import android.util.Log;

import androidx.core.content.ContextCompat;

// 玩家class
class Player extends Character{

    private Paint paint;
    private Context context;
    private int HP = 2;

    private Bitmap[] images = new Bitmap[3];

    private double speed = 15;
    private double sensorDx;

    private boolean canMove;

    private boolean hidden = false;
    private long hide_time;

    private boolean isShield = false;
    private long shieldTime;

    private boolean isFreeze = false;
    private long freezeTime;

    Paint shieldPaint = new Paint();

    Vibrator vibrator;
    Game game;

    // constructor
    public Player(Context context, double centerX, double centerY, Game game){
        super(centerX, centerY);
        this.context = context;
        this.game = game;
        paint = new Paint();
        loadImages();
        this.setImageHeightAndWidth(images[0].getHeight(), images[0].getWidth());
        shieldPaint.setColor(ContextCompat.getColor(context, R.color.white));
        shieldPaint.setAlpha(100);
        vibrator = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
    }

    // 移動
    public void move(double speed){
        if(canMove){
            this.sensorDx = -Game.getSensorDx();
            this.setCenterX(this.getCenterX() + speed*sensorDx);
        }

    }

    // player受傷函式
    public void getHurt(){
        if(!isShield){
            vibrator.vibrate(100);
            HP--;
            if(HP < 0){
                HP=0;
                game.setGameover();
            }
            this.setImageHeightAndWidth(images[HP].getHeight(), images[HP].getWidth());
            this.hide();

        }
    }

    public boolean hasShield(){
        if(isShield){
            return true;
        }
        else{
            return false;
        }
    }


    public void getPower(String type) {
        switch (type){
            case "shield":
                isShield = true;
                shieldTime = System.currentTimeMillis();
                break;
            case "freeze":
                isFreeze = true;
                freezeTime = System.currentTimeMillis();
                break;
        }
    }

    // 把player藏起來
    public void hide(){
        hidden = true;
        hide_time = System.currentTimeMillis();
        this.setCenterY(-20000);
    }

    // 每一幀要做的事
    public void update() {
        if(isFreeze && game.getNow() - freezeTime >3000){
            isFreeze = false;
        }

        if(isShield && game.getNow() - shieldTime >3000){
            isShield = false;
        }

        if(isFreeze){
            this.move(3);
        }
        else {
            this.move(speed);
        }
        if(this.getLeftX() <= 0){
            this.setLeftX(0);
        }

        if(this.getRightX() >= game.getWIDTH()){
            this.setRightX(game.getWIDTH());
        }

        if(this.hidden && game.getNow() - this.hide_time > 1000){
            this.hidden = false;
            this.setCenterX(game.getWIDTH()/2);
            this.setBottomY(game.getHEIGHT() - 100);
            isShield = true;
            shieldTime = game.getNow();
        }

        game.setFreeze(isFreeze);

        if(game.getBackgroundSpeed() == 0){
            canMove = false;
        }
        else {
            canMove = true;
        }
    }

    // 畫到畫布上
    public void draw(Canvas canvas) {
        super.drawImage(canvas, images[HP]);
        if(isShield){
            shieldPaint.setAlpha((int)((1 - ( (game.getNow() - shieldTime) /3000.0 ))*100) );
            Log.d("shield", ((game.getNow() - shieldTime)) /3000.0 +"");
            canvas.drawCircle((float) this.getCenterX(), (float) this.getCenterY(), 200, shieldPaint);
            game.drawBar(canvas, 300*(1 - (float)(game.getNow() - shieldTime)/3000), 100, 200, "green", 300);
        }

        if(isFreeze){
            game.drawBar(canvas, 300*(1 - (float)(game.getNow() - freezeTime)/3000), 100, 300, "blue", 300);
        }
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

    public void initialize() {
        this.setCenterX(game.getWIDTH()/2);
        this.setBottomY(game.getHEIGHT()-100);
        HP = 2;
        this.hidden = false;
        this.isFreeze = false;
        this.isShield = false;
    }
}
