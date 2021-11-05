package com.rontoking.rontocraft;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class Point2 {

    public static final Point2 zero = new Point2(0, 0);
    public int x, y;

    public Point2(){
        this.x = 0;
        this.y = 0;
    }

    public Point2(int x, int y){
        this.x = x;
        this.y = y;
    }

    public Point2(Vector2 v){
        this.x = (int)v.x;
        this.y = (int)v.y;
    }

    public Point2(Point2 p){
        this.x = p.x;
        this.y = p.y;
    }

    public void set(int x, int y){
        this.x = x;
        this.y = y;
    }

    public void set(Vector2 v){
        this.x = (int)v.x;
        this.y = (int)v.y;
    }

    public void set(Point2 p){
        this.x = p.x;
        this.y = p.y;
    }

    public Vector2 toVector(){
        return new Vector2(this.x, this.y);
    }
}
