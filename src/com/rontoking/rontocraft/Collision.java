package com.rontoking.rontocraft;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Core 2 Duo on 17.8.2016.
 */
public class Collision {

    public static final float minIntersection = 0.000001f;

    public static boolean isPlayerIntersecting(){
        for(Point3 p : blocksAroundPlayer()){
            if(Block.getBoundingBox(p.toVector()).intersects(Player.boundingBox()) && playerIntersectsBlockY(p))
                return true;
        }
        return false;
    }

    public static Array<BoundingBox> blocksIntersectingPlayer(){
        Array<BoundingBox> list = new Array<BoundingBox>();

        for(Point3 p : blocksAroundPlayer()){
            if(Block.getBoundingBox(p.toVector()).intersects(Player.boundingBox()) && playerIntersectsBlockY(p))
                list.add(Block.getBoundingBox(p.toVector()));
        }
        return list;
    }

    public static Array<Point3> blocksAroundPlayer(){
        Array<Point3> list = new Array<Point3>();

        for(Point3 p : Player.boundingBlocks()) {
            for (int x = -1; x <= 1; x++) {
                for (int z = -1; z <= 1; z++) {
                    Point3 point = new Point3(p.x + x, p.y, p.z + z);
                    if(World.getBlockAtPoint(point) != 0)
                        list.add(point);
                }
            }
        }

        return list;
    }

    public static boolean playerIntersectsBlockY(Point3 p) {
        if (Block.getBoundingBox(p.toVector()).max.y >= Player.getBottom() && Block.getBoundingBox(p.toVector()).min.y <= Player.getBottom()) { // Block below player.
            if (Block.getBoundingBox(p.toVector()).max.y - Player.getBottom() > minIntersection)
                return true;
            return false;
        }
        if (Block.getBoundingBox(p.toVector()).max.y >= Player.getTop() && Block.getBoundingBox(p.toVector()).min.y <= Player.getTop()) { // Block above player.
            if (Player.getTop() - Block.getBoundingBox(p.toVector()).min.y > minIntersection)
                return true;
            return false;
        }
        return true;
    }
}
