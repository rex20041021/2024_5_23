package com.example.test2;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class MyData {
    Context context;
    SharedPreferences shp;
    SharedPreferences.Editor editor;

    public MyData(Context context){
        this.context = context;
        shp = context.getSharedPreferences("MY_DATA", context.MODE_PRIVATE);
        editor = shp.edit();
    }

    public int getData(String key){
        return shp.getInt(key, 0);
    }

    public void setData(String key, int value){
        editor.putInt(key, value);
        editor.apply();
    }

}
