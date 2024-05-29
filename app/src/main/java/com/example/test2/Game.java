package com.example.test2;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Switch;
import android.media.MediaPlayer;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Random;

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

    private static ArrayList<Power> allPower = new ArrayList<Power>();


    public static long now;
    private boolean isGameover = false;

    private String status = "start";
    private int endMode = 1;

    private int score = 0;

    private Paint freezePaint;
    private boolean isFreeze = false;


    private Button pausebutton1;
    private Button pausebutton2;
    private Button continuebutton_1;
    private Button continuebutton_2;
    private Button instructionbutton;
    private Button homebutton_1;
    private Button homebutton_2;

    MediaPlayer bgmPlayer;
    MediaPlayer bgm2Player;
    MediaPlayer start2Player;
    MediaPlayer deadPlayer;
    MediaPlayer pausePlayer;
    MediaPlayer accPlayer;
    MediaPlayer brakePlayer;

    Typeface tf;

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
        player = new Player(this.context, WIDTH/2, HEIGHT -500, this);
        player.setCenterX(WIDTH/2);
        player.setBottomY(HEIGHT-100);
        // 初始化背景
        allBackground = new AllBackground(context, this);
        // 初始化車車
        allCar = new AllCar(context, this);
        freezePaint = new Paint();
        freezePaint.setColor(ContextCompat.getColor(context, R.color.freezeBlue));
        freezePaint.setAlpha(100);


        String surfaceState = "";

        this.createPower(100, 100, "shield");

        //        暫停鍵、繼續建、介紹建、主頁建
        Bitmap buttonimage1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.pause_1);
        buttonimage1 = Bitmap.createScaledBitmap( buttonimage1, (int)(buttonimage1.getWidth()*0.15), (int)(buttonimage1.getHeight()*0.12), true);
        pausebutton1 = new Button(this.context, buttonimage1, WIDTH * 0.87, HEIGHT * 0.05, this);
        Bitmap buttonimage2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.pause_1);
        buttonimage2 = Bitmap.createScaledBitmap( buttonimage2, (int)(buttonimage2.getWidth()*0.15), (int)(buttonimage2.getHeight()*0.12), true);
        pausebutton2 = new Button(this.context, buttonimage2, WIDTH * 0.93, HEIGHT * 0.05, this);
        Bitmap buttonimage3_1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.continue_1);
        buttonimage3_1 = Bitmap.createScaledBitmap( buttonimage3_1, (int)(buttonimage2.getWidth()*8), (int)(buttonimage2.getHeight()*2), true);
        continuebutton_1 = new Button(this.context, buttonimage3_1, WIDTH * 0.3, HEIGHT * 0.8, this);
        Bitmap buttonimage3_2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.continue_2);
        buttonimage3_2 = Bitmap.createScaledBitmap( buttonimage3_2, (int)(buttonimage2.getWidth()*8), (int)(buttonimage2.getHeight()*2), true);
        continuebutton_2= new Button(this.context, buttonimage3_2, WIDTH * 0.3, HEIGHT * 0.8, this);
        Bitmap buttonimage4 = BitmapFactory.decodeResource(context.getResources(), R.drawable.instruction_1);
        buttonimage4 = Bitmap.createScaledBitmap( buttonimage4, (int)(buttonimage2.getWidth()*8), (int)(buttonimage2.getHeight()*2), true);
        instructionbutton = new Button(this.context, buttonimage4, WIDTH * 0.7, HEIGHT * 0.8, this);
        Bitmap buttonimage5_1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.home_1);
        buttonimage5_1 = Bitmap.createScaledBitmap( buttonimage5_1, (int)(buttonimage2.getWidth()*8), (int)(buttonimage2.getHeight()*2), true);
        homebutton_1 = new Button(this.context, buttonimage5_1, WIDTH * 0.7, HEIGHT * 0.8, this);
        Bitmap buttonimage5_2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.home_2);
        buttonimage5_2 = Bitmap.createScaledBitmap( buttonimage5_2, (int)(buttonimage2.getWidth()*8), (int)(buttonimage2.getHeight()*2), true);
        homebutton_2 = new Button(this.context, buttonimage5_2, WIDTH * 0.7, HEIGHT * 0.8, this);

        bgm2Player = MediaPlayer.create(this.context, R.raw.bgm2);
        start2Player = MediaPlayer.create(this.context, R.raw.start2);
        deadPlayer = MediaPlayer.create(this.context, R.raw.dead);
        pausePlayer = MediaPlayer.create(this.context, R.raw.pause);
        accPlayer = MediaPlayer.create(this.context, R.raw.accelerating);
        brakePlayer = MediaPlayer.create(this.context, R.raw.braking_cut);
        bgm2Player.setLooping(true);
        start2Player.setLooping(true);
        pausePlayer.setLooping(true);
        accPlayer.setLooping(true);
        brakePlayer.setLooping(true);

        tf =Typeface.createFromAsset(context.getAssets(), "fonts/font.ttf");
//        tf = Typeface.createFromFile(getResources()+"font/font.ttf");




        setFocusable(true);
    }

    // 偵測觸控事件
    // 偵測觸控事件
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()){
            // 有手指落下或放著的話
            case MotionEvent.ACTION_DOWN:
                Log.d("teest", 1+"");
                if (status.equals("end")) {
                    collideNum = 0;
                }
                for(int i=0; i<event.getPointerCount(); i++){
                    //暫停
                    if(WIDTH * 0.87 - pausebutton1.getWIDTH() / 2 <= event.getX(i)
                            && event.getX(i) <= WIDTH * 0.93 + pausebutton2.getWIDTH() / 2
                            && HEIGHT * 0.05 - pausebutton1.getHEIGHT() / 2 <= event.getY(i)
                            && event.getY(i) <= HEIGHT * 0.05 + pausebutton1.getHEIGHT() / 2
                            && status.equals("run")) {
                        status = "pause";
                        bgm2Player.pause();
                        pausePlayer.start();
                        Log.d("status", status);
                    }
                    //開始鍵
                    else if (WIDTH * 0.3 - continuebutton_2.getWIDTH() / 2 <= event.getX(i)
                            && event.getX(i) <= WIDTH * 0.3 + continuebutton_2.getWIDTH() / 2
                            && HEIGHT * 0.8 - continuebutton_2.getHEIGHT() / 2 <= event.getY(i)
                            && event.getY(i) <= HEIGHT * 0.8 + continuebutton_2.getHEIGHT() / 2) {
                        if (status.equals("start") || status.equals("pause") || status.equals("end")) {
                            if(!status.equals("pause")){
                                this.initialize();
                            }
                            else {
                                pausePlayer.pause();
                            }
                            status = "run";
                            start2Player.pause();
                            bgm2Player.start();
                        }
                        Log.d("status", status);
                    }
                    //說明鍵
                    else if (WIDTH * 0.7 - instructionbutton.getWIDTH() / 2 <= event.getX(i)
                            && event.getX(i) <= WIDTH * 0.7 + instructionbutton.getWIDTH() / 2
                            && HEIGHT * 0.8 - instructionbutton.getHEIGHT() / 2 <= event.getY(i)
                            && event.getY(i) <= HEIGHT * 0.8 + instructionbutton.getHEIGHT() / 2
                            && status.equals("start")) {
                        status = "instruction";
                        Log.d("status", status);
                    }

                }
                return true;
                // 手指放著
            case MotionEvent.ACTION_MOVE:
                Log.d("teest", 2+"");
                // 看所有手指的位置偵測要break還是accelerate (break最大)
                for(int i=0; i<event.getPointerCount(); i++){
                    if(event.getX(i) < WIDTH/2) {
                        speedState = "break";
                        if(status.equals("run")){
                            brakePlayer.setVolume(1, 1);
                            brakePlayer.start();
                            accPlayer.pause();
                        }
                        break;
                    }
                    else{
                        speedState = "accelerate";
                        if(status.equals("run")){
                            accPlayer.setVolume(1, 1);
                            accPlayer.start();
                            brakePlayer.pause();
                        }
                    }
                }
                return true;
            // 手指離開回到normal
            case MotionEvent.ACTION_UP:
                Log.d("teest", 3+"");
                if (status.equals("run")) {
                    accPlayer.pause();
                    brakePlayer.pause();
                    speedState = "normal";
                }
                if (status.equals("instruction")) {
                    status = "start";
                    Log.d("status", status);
                }
                if(status.equals("start")){
                    accPlayer.setVolume(0, 0);
                    brakePlayer.setVolume(0, 0);
                }
                for(int i=0; i<event.getPointerCount(); i++){
                    //主頁建
                    if (WIDTH * 0.7 - homebutton_1.getWIDTH() / 2 <= event.getX(i)
                            && event.getX(i) <= WIDTH * 0.7 + homebutton_1.getWIDTH() / 2
                            && HEIGHT * 0.8 - homebutton_1.getHEIGHT() / 2 <= event.getY(i)
                            && event.getY(i) <= HEIGHT * 0.8 + homebutton_1.getHEIGHT() / 2) {
                        if (status.equals("pause") || status.equals("end")) {
                            status = "start";
                            pausePlayer.pause();
                            deadPlayer.pause();
                            bgm2Player.pause();
                            start2Player.start();
                        }
                    }

                }
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
        int canvasWidth = canvas.getWidth();
        int canvasHeight = canvas.getHeight();

        if (status.equals("start")) {
            Paint paint = new Paint();
            Bitmap image = BitmapFactory.decodeResource(context.getResources(), R.drawable.open_1);
            double ratio = WIDTH / image.getWidth();
            image = Bitmap.createScaledBitmap( image, (int)(image.getWidth()*ratio), (int)(image.getHeight()*ratio), true);
            image = Bitmap.createScaledBitmap( image, (int)WIDTH, (int)HEIGHT, true);

            // 獲取位圖的寬度和高度
            int imageWidth = image.getWidth();
            int imageHeight = image.getHeight();

            // 計算圖片的左上角位置，使其中心與畫布中心對齊
            float left = (canvasWidth - imageWidth) / 2.0f;
            float top = (canvasHeight - imageHeight) / 2.0f;

            // 繪製位圖
            canvas.drawBitmap(image, left, top, paint);
            continuebutton_2.draw(canvas);
            instructionbutton.draw(canvas);
        }

        else if (status.equals("run")) {
            Log.d("ooo", "run");
            allBackground.draw(canvas);
            player.draw(canvas);
            allCar.draw(canvas);
            for(Power power :allPower){
                power.draw(canvas);
            }
//            drawText(canvas, "UPS: "+gameLoop.getAverageUPS(), 100, 100, 50);
//            drawText(canvas, "FPS: "+gameLoop.getAverageFPS(), 100, 200, 50);
//            drawText(canvas, "SPEED: "+backgroundSpeed, 100, 300, 50);
            drawText(canvas, "SCORE: "+score, 100, 60, 50);
            drawBar(canvas, 300*(backgroundSpeed/backgroundMaxSpeed), 100, 100, "red", 300);
            if(isFreeze){
                canvas.drawRect(0, 0, (float) WIDTH+1000, (float) HEIGHT+1000, freezePaint);
            }
            pausebutton1.draw(canvas);
            pausebutton2.draw(canvas);
        }

        else if (status.equals("pause")) {
            Paint paint = new Paint();
            Bitmap image = BitmapFactory.decodeResource(context.getResources(), R.drawable.pause_screen);
            double ratio = HEIGHT / image.getWidth();
            image = Bitmap.createScaledBitmap(image, (int) (image.getWidth() * ratio), (int) (image.getHeight() * ratio), true);
            // 獲取位圖的寬度和高度
            int imageWidth = image.getWidth();
            int imageHeight = image.getHeight();

            // 計算圖片的左上角位置，使其中心與畫布中心對齊
            float left = (canvasWidth - imageWidth) / 2.0f;
            float top = (canvasHeight - imageHeight) / 2.0f;

            // 繪製位圖
            canvas.drawBitmap(image, left, top, paint);
            continuebutton_2.draw(canvas);
            homebutton_1.draw(canvas);
        }

        else if (status.equals("instruction")) {
            Paint paint = new Paint();
            Bitmap image = BitmapFactory.decodeResource(context.getResources(), R.drawable.instructions_1);
            double ratio = WIDTH / image.getWidth();
            image = Bitmap.createScaledBitmap( image, (int)(image.getWidth()*ratio), (int)(image.getHeight()*ratio), true);

            // 獲取位圖的寬度和高度
            int imageWidth = image.getWidth();
            int imageHeight = image.getHeight();

            // 計算圖片的左上角位置，使其中心與畫布中心對齊
            float left = (canvasWidth - imageWidth) / 2.0f;
            float top = (canvasHeight - imageHeight) / 2.0f;

            // 繪製位圖
            canvas.drawBitmap(image, left, top, paint);
        }

        else if (status.equals("end")) {
            Paint paint = new Paint();
            Bitmap image = BitmapFactory.decodeResource(context.getResources(), R.drawable.end_1);
            double ratio = WIDTH / image.getWidth();
            image = Bitmap.createScaledBitmap( image, (int)(image.getWidth()*ratio), (int)(image.getHeight()*ratio), true);

            // 獲取位圖的寬度和高度
            int imageWidth = image.getWidth();
            int imageHeight = image.getHeight();

            // 計算圖片的左上角位置，使其中心與畫布中心對齊
            float left = (canvasWidth - imageWidth) / 2.0f;
            float top = (canvasHeight - imageHeight) / 2.0f;


            canvas.drawBitmap(image, left, top, paint);
            Paint textPaint = new Paint();
            textPaint.setTextAlign(Paint.Align.CENTER);
            textPaint.setTextSize(70);
            textPaint.setColor(ContextCompat.getColor(context, R.color.white));
            textPaint.setTypeface(tf);
            int xPos = (int)(WIDTH / 2);
            int yPos = (int) ((HEIGHT / 2) - ((textPaint.descent() + textPaint.ascent()) / 2)) ;
            //((textPaint.descent() + textPaint.ascent()) / 2) is the distance from the baseline to the center.




            // 繪製位圖
            canvas.drawBitmap(image, left, top, paint);
            canvas.drawText("SCORE:"+score, xPos, yPos, textPaint);

            continuebutton_1.draw(canvas);
            homebutton_2.draw(canvas);


        }
    }


    // 畫文字的函數，x跟y是左上角的座標
    public void drawText(Canvas canvas, String value, double x, double y, double size) {
        Paint paint = new Paint();
        int color = ContextCompat.getColor(context, R.color.magenta);
        paint.setColor(color);
        paint.setTextSize((float) size);
        paint.setTypeface(tf);
        canvas.drawText(value ,(float) x, (float) y, paint);
    }

    public void drawBar(Canvas canvas, double value, double x, double y, String color, double length){
        Paint hollowPaint = new Paint();
        hollowPaint.setStyle(Paint.Style.STROKE);
        hollowPaint.setStrokeWidth(5);
        Paint solidPaint = new Paint();
        solidPaint.setStyle(Paint.Style.FILL);
        int ID = context.getResources().getIdentifier(color,"color",context.getPackageName());
        int showColor = ContextCompat.getColor(context, ID);
        solidPaint.setColor(showColor);
        canvas.drawRect((float)x, (float) y, (float)( x+value ), (float)( y+50 ), solidPaint);
        canvas.drawRect((float)x, (float) y, (float)( x+length), (float)( y+50 ), hollowPaint);

    }

    // 每一幀要做的事
    public void update() {
        Log.d("teest", "update");

        if (status.equals("run")) {
            score += backgroundSpeed/10;
            this.updateCollideNum();
            this.speedUpdate();
            allBackground.update();
            player.update();
            allCar.update();
            for(Power power :allPower){
                power.update();
                if (Character.isCollide(power, player)){
                    player.getPower(power.getType());
                    power.setVisble(false);
                    Log.d("power", power.getType());
                }
            }
        }
        if (status.equals("end")) {
            endMode = (endMode + 1) % 3;
        }
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

    public void initialize(){
        allBackground.initialize();
        player.initialize();
        allCar.initialize();
        allPower.clear();
        isGameover = false;
        score = 0;
        backgroundSpeed = backgroundNormalSpeed;
    }

    public void createPower(double centerX, double centerY, String type){
        if(type.equals("random")){
            Random random = new Random();
            if(random.nextInt(10)<3){
                type = "freeze";
            }
            else {
                type = "shield";
            }
        }
        Power power = new Power(this.context, centerX, centerY, type, this);
        allPower.add(power);

    }

    public void pauseBGM(){
        if(status.equals("start")){
            start2Player.pause();
        }
        else if(status.equals("run")){
            bgm2Player.pause();
        }
        deadPlayer.pause();
        accPlayer.pause();
        brakePlayer.pause();
    }

    int pauseTimes = 0;
    public void resumeBGM(){
        if(status.equals("start")){

            start2Player.start();
        }
        else if(status.equals("run")){
            bgm2Player.start();
        }
        else if(pauseTimes != 0){
            deadPlayer.start();
        }
        accPlayer.setVolume(0, 0);
        accPlayer.start();
        brakePlayer.setVolume(0, 0);
        brakePlayer.start();
        pauseTimes++;
    }


    public void setGameover(){
        isGameover = true;
        bgm2Player.pause();
        accPlayer.setVolume(0, 0);
        brakePlayer.setVolume(0, 0);
        deadPlayer.setVolume(1, 1);
        deadPlayer.start();
        status = "end";
    }

    public void setFreeze(boolean isFreeze){
        this.isFreeze = isFreeze;
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
    public double getHEIGHT(){
        return HEIGHT;
    }

    // 回傳視窗寬度
    public double getWIDTH() {
        return WIDTH;
    }

    // 回傳背景捲動速度
    public double getBackgroundSpeed() {
        return backgroundSpeed;
    }

    public long getNow(){
        return now;
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
