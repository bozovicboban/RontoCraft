package com.rontoking.rontocraft;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class Point3 {
    public static final Point3 zero = new Point3(0, 0, 0);
    public static final Point3 none = new Point3(-1, -1, -1);

    public int x, y, z;

    public Point3(){
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

    public Point3(int x, int y, int z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Point3(Vector3 v){
        this.x = (int)v.x;
        this.y = (int)v.y;
        this.z = (int)v.z;
    }

    public Point3(Point3 p){
        this.x = p.x;
        this.y = p.y;
        this.z = p.z;
    }

    public void set(int x, int y, int z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void set(Vector3 v){
        this.x = (int)v.x;
        this.y = (int)v.y;
        this.z = (int)v.z;
    }

    public void set(Point3 p){
        this.x = p.x;
        this.y = p.y;
        this.z = p.z;
    }

    public String getString(){
        return x + ", " + y + ", " + z;
    }

    public Vector3 toVector(){
        return new Vector3(this.x, this.y, this.z);
    }

    public static Point3 floorVec(Vector3 v){
        return new Point3((int)Math.floor(v.x), (int)Math.floor(v.y), (int)Math.floor(v.z));
    }

    @Override
    public int hashCode(){
        return ((this.x*122949823)^(this.y*141650939)^(this.z*160481183))%1000;
    }

    @Override
    public boolean equals(Object o){
        if(o == null)
            return false;
        if(!(o instanceof Point3))
            return false;
        Point3 p = (Point3)o;
        return this.x == p.x && this.y == p.y && this.z == p.z;
    }

    public Point3 copy(){
        return new Point3(this);
    }
}
