package com.example.test2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

// 所有車群的管理員
class AllCar {

    Context context;
    // 最多幾個車群(回收利用)
    int maxCarGroupNum = 20;
    // 現在數到第幾個車群(0~19無限輪迴)
    int nowNum = -1;
    int type;
    // 判斷是不是正在生成
    boolean creating = false;
    // 所有車群
    ArrayList<CarGroup> allCarGroup = new ArrayList<CarGroup>();
    // 所有圖片
    HashMap<String, Bitmap[]> allImages = new HashMap<String, Bitmap[]>();
    String[] carColor = {"black", "blue", "green", "red", "yellow"};
    final int carImageHeight = (int)(400*0.8); //其實是393
    // 亂數
    Random randomNumbers = new Random();

    Game game;
    // 所有車群的種類
    ArrayList<boolean[][]> allGroupType = new ArrayList<boolean[][]>(Arrays.asList(
            new boolean[][] {
                    {true, false, true, true},
                    {true, false, true, true}},
            new boolean[][] {
                    {true, true, true, false}},
            new boolean[][]{
                    {true, true, true, false},
                    {true, true, true, false},
                    {true, false, true, false},
                    {true, false, true, false},
            },
            new boolean[][]{
                    {true, false, true, false},
                    {true, false, true, false},
                    {true, false, true, false}
            },
            new boolean[][]{
                    {false, true, true, false},
                    {false, true, true, false},
                    {false, true, true, false}
            },
            new boolean[][]{
                    {false, true, false, true},
                    {false, true, false, true},
                    {false, true, false, true}
            },
            new boolean[][]{
                    {true, false, false, true},
                    {false, true, false, true},
                    {true, false, false, true},
                    {false, false, true, false}
            },
            new boolean[][]{
                    {false, false, true, true},
                    {false, false, true, true},
                    {false, false, true, true}
            },
            new boolean[][]{
                    {false, false, true, false},
                    {false, true, false, true}
            },
            new boolean[][]{
                    {true, false, true, true},
                    {true, false, true, true},
                    {true, false, false, true},
                    {true, false, false, true},
                    {true, true, false, true},
                    {true, true, false, true},
            },
            new boolean[][]{
                    {true, false, true, true},
                    {true, false, true, true},
                    {true, false, false, true},
                    {true, false, false, true},
                    {true, false, false, true},
                    {true, false, false, true},
            },
            new boolean[][]{
                    {true, false, false, true},
                    {true, false, false, true},
                    {true, false, false, true},

            }




    ));

    // constructor
    public AllCar(Context context, Game game){
        this.context = context;
        this.game = game;
        this.load_image(context);
        this.createCarGroup(0);
    }

    // 載入圖片
    private void load_image(Context context){
        int ID;
        Matrix matrix = new Matrix();
        matrix.postRotate(180);
        Bitmap image;
        for(String color : carColor){
            allImages.put(color, new Bitmap[5]);
            for(int i=0; i<5; i++){
                ID = context.getResources().getIdentifier(String.format("car_%s_%d", color, i+1),"drawable",context.getPackageName());
                image = BitmapFactory.decodeResource(context.getResources(), ID);
                image = Bitmap.createScaledBitmap( image, (int)(image.getWidth()*0.8), (int)(image.getHeight()*0.8), true);
                allImages.get(color)[i] = Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight(), matrix, true);
            }
        }

    }

    // 創建車群
    public void createCarGroup(int type){
        nowNum++;
        Log.d("teest", "999"+allCarGroup.size());
        if(nowNum==maxCarGroupNum) nowNum = 0;
        // 還沒創滿
        if (allCarGroup.size()<maxCarGroupNum) {
            allCarGroup.add(new CarGroup(context, allImages, allGroupType.get(type), game));
        }
        else {
            allCarGroup.set(nowNum, new CarGroup(context, allImages, allGroupType.get(type), game));
        }
        Log.d("teest", "size:"+allCarGroup.size()+",8000");
    }

    // 每一幀要做的事
    public void update(){
        if(allCarGroup.size() == 0) return;
        Log.d("teest", nowNum+","+allCarGroup.size()+",900000");
        if(!creating){
            type = randomNumbers.nextInt(allGroupType.size());
            creating = true;
            Log.d("abc", nowNum+":"+allCarGroup.get(nowNum).getTopCarY()+"");
        }
        else {
            // 判斷能生的時候就生車群
            if(allCarGroup.get(nowNum).getTopCarY() - (-10000) >= (allGroupType.get(type).length+3) * carImageHeight){
                createCarGroup(type);
                creating = false;
            }
        }
        Log.d("tag", game.getBackgroundSpeed()+"");
        for(CarGroup carGroup: allCarGroup){
            carGroup.update();
        }

    }

    // 畫到畫布上
    public void draw(Canvas canvas){
        for (CarGroup carGroup: allCarGroup){
            if(carGroup.getBottomCarY() >= -carImageHeight && carGroup.getTopCarY() <= game.getHEIGHT()){
                carGroup.draw(canvas);
            }
        }


    }

    // 更新撞到車的數量
    public void updateCollideNum(Player player) {
        for(CarGroup carGroup: allCarGroup){
            carGroup.updateCollideNum(player);
        }
    }

    public void initialize() {
        allCarGroup.clear();
        nowNum = -1;
        creating = false;
        this.createCarGroup(0);
    }
}
