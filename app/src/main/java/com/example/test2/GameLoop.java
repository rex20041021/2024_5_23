package com.example.test2;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

import java.util.Observer;

/*
!!!!!
這個class我們應該不會改到
!!!!!
 */

// 遊戲迴圈
class GameLoop extends Thread
{
    private static final double MAX_UPS = 60.0;
    private static final double UPS_PERIOD = 1E+3/MAX_UPS;
    private boolean isRunning = false;
    private SurfaceHolder surfaceHolder;
    private Game game;
    private double averageUPS;
    private double averageFPS;


    public GameLoop(Game game, SurfaceHolder surfaceHolder)
    {
        this.surfaceHolder = surfaceHolder;
        this.game = game;
    }

    public double getAverageUPS() {
        return averageUPS;
    }

    public double getAverageFPS() {
        return averageFPS;
    }

    public void startLoop() {
        isRunning = true;
        start();
    }

    @Override
    public void run() {
        super.run();

        int updateCount = 0;
        int frameCount = 0;

        long startTime;
        long elapsedTime;
        long sleepTime;



        // Game loop
        Canvas canvas = null;
        startTime = System.currentTimeMillis();
        while (isRunning)
        {
            // try to update and render game
            try {
                canvas = surfaceHolder.lockCanvas();
                synchronized (surfaceHolder) {
                    game.update();
                    updateCount++;
                    game.draw(canvas);
                }

            }catch (IllegalAccessError e)
            {
                e.printStackTrace();
            }
            finally
            {
                if (canvas != null){
                    try{
                        surfaceHolder.unlockCanvasAndPost(canvas);
                        frameCount++;

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

            }



            elapsedTime = System.currentTimeMillis() - startTime;
            sleepTime = (long)(updateCount*UPS_PERIOD - elapsedTime);
            if(sleepTime > 0){
                try {
                    sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            while (sleepTime<0 && updateCount < MAX_UPS-1){
                game.update();
                updateCount++;
                elapsedTime = System.currentTimeMillis() - startTime;
                sleepTime = (long)(updateCount*UPS_PERIOD - elapsedTime);

            }

            elapsedTime = System.currentTimeMillis() - startTime;
            if(elapsedTime >= 1000){
                averageUPS = updateCount/(1E-3 * elapsedTime);
                averageFPS = frameCount/(1E-3 * elapsedTime);
                updateCount = 0;
                frameCount = 0;
                startTime = System.currentTimeMillis();
            }

        }
    }


    public void stopLoop() {
        isRunning = false;
        try {
            join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

