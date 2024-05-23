package com.example.test2;

import androidx.annotation.RequiresApi;
import android.app.Service;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.os.Vibrator;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

// 車群class
class CarGroup {

    // 這個車群所有的車
    ArrayList<Car> cars = new ArrayList<Car>();
    Context context;
    // 車道中心
    //  0:      130
    //  1:      420
    //  2:      680
    //  3:      980
    int[] laneCenterx = {130, 420, 680, 980};
    // 所有圖片
    HashMap<String, Bitmap[]> allImages;
    String[] carColor = {"black", "blue", "green", "red", "yellow"};
    // 車子的高度
    final int carImageHeight = (int)(393*0.8);
    // 這個車群的排法
    boolean[][] alignment;
    // 亂數
    Random randomNumbers = new Random();


    // consturctor
    public CarGroup(Context context, HashMap<String, Bitmap[]> allImages , boolean[][] alignment){
        this.allImages = allImages;
        this.alignment = alignment;
        this.createCar(context);
    }

    // 創造車車
    public void createCar(Context context){
        for(int i=0; i<alignment.length; i++) {
            for(int j=0; j<alignment[i].length; j++){
                if(alignment[i][j]){
                    String color = carColor[randomNumbers.nextInt(5)];
                    int carType = randomNumbers.nextInt(5);
                    cars.add(new Car(context, allImages.get(color)[carType], laneCenterx[j], -10000+carImageHeight*i));
                }
            }
        }
    }

    // 每一幀要做的事
    public void update(){
        for(Car car: cars){
            car.update();
        }
    }

    // 畫到畫布上
    public void draw(Canvas canvas){
        for(Car car: cars){
            car.draw(canvas);
        }
    }

    // 回傳車群目前的top位置
    public double getTopCarY(){
        return cars.get(0).getTopY();
    }

    // 回傳車群目前的bottom位置
    public double getBottomCarY(){
        return cars.get(cars.size()-1).getBottomY();
    }

    // 更新碰撞數字
    public void updateCollideNum(Player player) {
        for(Car car: cars){
            if(Character.isCollide(car, player) && !car.counted){
                Game.CollideNumAddOne();
                car.counted = true;
                player.getHurt();
            }
        }
    }
}
