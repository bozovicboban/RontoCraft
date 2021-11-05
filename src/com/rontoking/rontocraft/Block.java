package com.rontoking.rontocraft;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Array;

/**
 -z 0
 +z 1
 -y 2
 +y 3
 -x 4
 +x 5
 */
public class Block {

    public static byte firstID = -128;
    public static byte lastID;

    public enum Type {
        DIRT("dirt", (byte)-128),
        GRASS("grass", (byte)-127, true),
        STONE("stone", (byte)-126),
        PLANKS("planks", (byte)-123),
        SAND("sand", (byte)-125),
        GRAVEL("gravel", (byte)-124),

        BLACK_WOOL("black wool", (byte)-107),
        BLUE_WOOl("blue wool", (byte)-122),
        BROWN_WOOl("brown wool", (byte)-121),
        CYAN_WOOl("cyan wool", (byte)-120),
        GRAY_WOOl("gray wool", (byte)-119),
        GREEN_WOOl("green wool", (byte)-118),
        LIGHT_BLUE_WOOl("light blue wool", (byte)-117),
        LIME_WOOl("lime wool", (byte)-116),
        MAGENTA_WOOl("magenta wool", (byte)-115),
        ORANGE_WOOl("orange wool", (byte)-114),
        PINK_WOOl("pink wool", (byte)-113),
        PURPLE_WOOl("purple wool", (byte)-112),
        RED_WOOl("red wool", (byte)-111),
        SILVER_WOOl("silver wool", (byte)-110),
        WHITE_WOOl("white wool", (byte)-109),
        YELLOW_WOOl("yellow wool", (byte)-108);

        public final String name;
        public final byte type;
        public final boolean hasDifferentTop;

        Type(String name, byte type)
        {
            this.name = name;
            this.type = type;
            this.hasDifferentTop = false;
        }

        Type(String name, byte type, boolean hasDifferentTop)
        {
            this.name = name;
            this.type = type;
            this.hasDifferentTop = hasDifferentTop;
        }
    }

    public static void setUp(){
        lastID = (byte)(firstID + Type.values().length - 1);
    }

    public static BoundingBox getBoundingBox(Vector3 v){
        return new BoundingBox(v, new Vector3(v.x + 1, v.y + 1, v.z + 1));
    }

    public static boolean isCovered(Point3 p){
        int coveredSides = 0;
        if(isSideCovered(neighbourNegY(p), p))
            coveredSides++;
        if(isSideCovered(neighbourPosY(p), p))
            coveredSides++;
        if(isSideCovered(neighbourNegX(p), p))
            coveredSides++;
        if(isSideCovered(neighbourPosX(p), p))
            coveredSides++;
        if(isSideCovered(neighbourNegZ(p), p))
            coveredSides++;
        if(isSideCovered(neighbourPosZ(p), p))
            coveredSides++;
        if(coveredSides == 6)
            return true;
        return false;
    }

    public static Point3 neighbourNegY(Point3 p){
        return new Point3(p.x, p.y - 1, p.z);
    }

    public static Point3 neighbourPosY(Point3 p){
        return new Point3(p.x, p.y + 1, p.z);
    }

    public static Point3 neighbourNegX(Point3 p){
        return new Point3(p.x - 1, p.y, p.z);
    }

    public static Point3 neighbourPosX(Point3 p){
        return new Point3(p.x + 1, p.y, p.z);
    }

    public static Point3 neighbourNegZ(Point3 p){
        return new Point3(p.x, p.y, p.z - 1);
    }

    public static Point3 neighbourPosZ(Point3 p){
        return new Point3(p.x, p.y, p.z + 1);
    }

    public static boolean isSideCovered(Point3 neighbour, Point3 p){
        if(World.getBlockAtPoint(neighbour) != 0)
            return true;
        if(neighbour.y < p.y && p.y == 0)
            return true;
        return false;
    }

    public static Array<Point3> neighbours(Point3 p){
        Array<Point3> list = new Array<Point3>();
        for(int x = -1; x <= 1; x++){
            for(int y = -1; y <= 1; y++){
                for(int z = -1; z <= 1; z++){
                    if(World.getBlockAtPoint(new Point3(p.x + x, p.y + y, p.z + z)) != 0){
                        list.add(new Point3(p.x + x, p.y + y, p.z + z));
                    }
                }
            }
        }
        return list;
    }

    public static ModelInstance getModelInstance(Point3 p, boolean enabled0, boolean enabled1, boolean enabled2, boolean enabled3, boolean enabled4, boolean enabled5){
        ModelInstance instance = new ModelInstance(ModelManager.blockModels.get(World.getBlockAtPoint(p) + 128), p.x + 0.5f, p.y + 0.5f, p.z + 0.5f);
        instance.nodes.get(0).parts.get(0).enabled = enabled0;
        instance.nodes.get(0).parts.get(1).enabled = enabled1;
        instance.nodes.get(0).parts.get(2).enabled = enabled2;
        instance.nodes.get(0).parts.get(3).enabled = enabled3;
        instance.nodes.get(0).parts.get(4).enabled = enabled4;
        instance.nodes.get(0).parts.get(5).enabled = enabled5;
        return instance;
    }

    public static String nameOf(byte type){
        for(Type t : Type.values()){
            if(t.type == type){
                return t.name.toUpperCase();
            }
        }
        return "AIR";
    }
}
