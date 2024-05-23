package com.example.test2;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

// 主要遊戲迴圈
class Game extends SurfaceView implements SurfaceHolder.Callback {

    private GameLoop gameLoop;
    private Context context;

    // 視窗高度and寬度
    private static double HEIGHT;
    private static double WIDTH;
    // 偵測陀螺儀的數字
    private static double sensorDx;
    // 現在的背景滾動速度
    private static double backgroundSpeed = 5;
    // 最大速度能到多少
    private final static double backgroundMaxSpeed = 40;
    // 不加速不減速的速度
    private final static double backgroundNormalSpeed = 10;
    // 撞到幾台車
    private static double collideNum;

    // 速度狀態(會有"normal","accelerate","break"的情況)
    private String speedState = "normal";

    // 玩家(那台車)
    private Player player;
    // 背景管理員(會有兩個背景交錯出現)
    private AllBackground allBackground;
    // 所有車的管理員
    private AllCar allCar;

    public static long now;



    // constructor
    // 在這邊放要在遊戲迴圈前做的事
    public Game(Context context) {
        super(context);
        // 我也不知道這具體是什麼，好像可以想像成是螢幕
        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        // MainActivity
        this.context = context;
        // 遊戲迴圈具體實施的地方
        gameLoop = new GameLoop(this, surfaceHolder);

        // 拿到Height and Width
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        HEIGHT = displayMetrics.heightPixels + getNavigationBarHeight();
        WIDTH = displayMetrics.widthPixels;

        // 初始化player
        player = new Player(this.context, WIDTH/2, HEIGHT -500);
        player.setCenterX(WIDTH/2);
        player.setBottomY(HEIGHT-100);
        // 初始化背景
        allBackground = new AllBackground(context);
        // 初始化車車
        allCar = new AllCar(context);

        String surfaceState = "";

        setFocusable(true);
    }

    // 偵測觸控事件
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()){
            // 有手指落下或放著的話
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                // 看所有手指的位置偵測要break還是accelerate (break最大)
                for(int i=0; i<event.getPointerCount(); i++){
                    if(event.getX(i) < WIDTH/2) {
                        speedState = "break";
                        break;
                    }
                    else{
                        speedState = "accelerate";
                    }
                }
                return true;
            // 手指離開回到normal
            case MotionEvent.ACTION_UP:
                speedState = "normal";
                return true;

        }
        return super.onTouchEvent(event);

    }

    // 創建遊戲迴圈(應該不會動到)
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (gameLoop.getState().equals(Thread.State.TERMINATED)) {
            SurfaceHolder surfaceHolder = getHolder();
            surfaceHolder.addCallback(this);
            gameLoop = new GameLoop(this, surfaceHolder);
        }
        gameLoop.startLoop();
    }

    // 視窗大小改變的話(應該不會動到)
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    // 視窗被摧毀的話(應該不會動到)
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }


    // 每一幀要畫的東西
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
//        Bitmap img = BitmapFactory.decodeResource(context.getResources(), R.drawable.car_black_4);
//        canvas.drawBitmap(img, 0, 0, new Paint());

        allBackground.draw(canvas);
        player.draw(canvas);
        allCar.draw(canvas);
        drawText(canvas, "UPS: "+gameLoop.getAverageUPS(), 100, 100, 50);
        drawText(canvas, "FPS: "+gameLoop.getAverageFPS(), 100, 200, 50);
        drawText(canvas, "SPEED: "+backgroundSpeed, 100, 300, 50);
        drawText(canvas, "COLLIDE: "+collideNum, 100, 400, 50);


    }

    // 畫文字的函數，x跟y是左上角的座標
    public void drawText(Canvas canvas, String value, double x, double y, double size) {
        Paint paint = new Paint();
        int color = ContextCompat.getColor(context, R.color.magenta);
        paint.setColor(color);
        paint.setTextSize((float) size);
        canvas.drawText(value ,(float) x, (float) y, paint);
    }

    // 每一幀要做的事
    public void update() {
        Log.d("speed", backgroundSpeed+"");
        this.updateCollideNum();
        this.speedUpdate();
        allBackground.update();
        player.update();
        allCar.update();
        now=System.currentTimeMillis();
    }

    // 更新撞到車子的數量
    private void updateCollideNum() {
        allCar.updateCollideNum(player);
    }

    // 讓別人call的，讓撞到車的數量++
    public static void CollideNumAddOne(){
        collideNum++;
    }

    // 更新背景速度
    public void speedUpdate(){
        switch (speedState){
            case "normal":
                if (backgroundSpeed > backgroundNormalSpeed) {
                    backgroundSpeed--;
                }
                else if (backgroundSpeed < backgroundNormalSpeed) {
                    backgroundSpeed++;
                }
                return;
            case "accelerate":
                backgroundSpeed ++;
                if (backgroundSpeed >= backgroundMaxSpeed) {
                    backgroundSpeed=backgroundMaxSpeed;
                }
                return;
            case "break":
                backgroundSpeed -= 3;
                if(backgroundSpeed <= 0){
                    backgroundSpeed = 0;
                }
        }
    }

    // 加速規感應的數字
    public void setSensorDx(double dx){
        sensorDx = dx;
    }

    // 回傳加速規感應數字
    public static double getSensorDx() {
        return sensorDx;
    }

    // 回傳視窗高度
    public static double getHEIGHT(){
        return HEIGHT;
    }

    // 回傳視窗寬度
    public static double getWIDTH() {
        return WIDTH;
    }

    // 回傳背景捲動速度
    public static double getBackgroundSpeed() {
        return backgroundSpeed;
    }

    // onPause會call這個函數
    public void pause() {
        gameLoop.stopLoop();
    }

    // 拿到手機底下狀態欄高度的數字
    public int getNavigationBarHeight(){
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }
}
