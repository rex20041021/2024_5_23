package com.example.test2;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;

// 所有背景的管理員(其實也就兩個)
class AllBackground {
    Background background1;
    Background background2;
    Game game;

    // constructor
    AllBackground(Context context, Game game){
        this.game = game;
        background1 = new Background(context, 0,0, game);
        background2 = new Background(context, 0, 0, game);
        this.initialize();
    }

    // 背景無限滾動
    public void update(){
        background1.update();
        background2.update();
        if(background1.getTopY() >= game.getHEIGHT()){
            background1.setBottomY(background2.getTopY());
        }

        if(background2.getTopY() >= game.getHEIGHT()){
            background2.setBottomY(background1.getTopY());
        }
    }

    public void draw(Canvas canvas){
        background1.draw(canvas);
        background2.draw(canvas);
    }

    public void initialize() {
        background1.setLeftX(0);
        background1.setTopY(0);
        background2.setLeftX(0);
        background2.setBottomY(0);
    }
}
