package com.rontoking.rontocraft;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Array;

import static com.rontoking.rontocraft.Main.deltaTime;

public class Player {
    public static final float height = 1.7f;
    public static final float walkSpeed = 4.317f;
    public static final float sprintSpeed = 5.612f;
    public static final Vector3 spawnPos = new Vector3(0, height + 100, 0);
    public static final Vector3 spawnDir = new Vector3(1, spawnPos.y, 1);
    public static Vector3 position = new Vector3(spawnPos);
    public static float jumpForce = 8.5f; // 8.5f
    public static float moveSpeed = walkSpeed;
    public static Vector3 velocity = new Vector3(0, 0, 0);
    public static boolean inAir = true;
    public static boolean alreadyCollided = false;

    public static float commandSpeed = 1f;
    public static float commandJump = 1f;

    public static final float eyesHeightDelta = 0.1f;
    public static final float eyesBottomDelta = height - eyesHeightDelta;
    public static final float radius = 0.3f;

    public static BoundingBox boundingBox(){
        return new BoundingBox(new Vector3(position.x - radius, getBottom(), position.z - radius),
                new Vector3(position.x + radius, getTop(), position.z + radius));
    }

    public static BoundingBox placeBlockBoundingBox(){
        return new BoundingBox(new Vector3(position.x - radius, getBottom() + 0.01f, position.z - radius),
                new Vector3(position.x + radius, getTop(), position.z + radius));
    }

    public static void jump(){
        if(!inAir){
            velocity.y = jumpForce * commandJump;
            inAir = true;
        }
    }

    public static void updatePosition(){
        position.x += velocity.x * deltaTime();
        if (Collision.isPlayerIntersecting()) {
            position.x -= velocity.x * deltaTime();
            alreadyCollided = true;
        }

        position.z += velocity.z * deltaTime();
        if (Collision.isPlayerIntersecting()) {
            position.z -= velocity.z * deltaTime();
            alreadyCollided = true;
        }

        position.y += velocity.y * deltaTime();
    }

    public static Array<Point3> boundingBlocks(){
        Array<Point3> b = new Array<Point3>();
        b.add(Point3.floorVec(new Vector3(position.x, getTop() - Collision.minIntersection, position.z)));
        b.add(Point3.floorVec(new Vector3(position.x, getBottom() + Collision.minIntersection, position.z)));
        return b;
    }

    public static Chunk getChunk(){
        if(World.getChunkAtPoint(new Point3(position)) != null)
            return World.getChunkAtPoint(new Point3(position));
        //ChunkLoader.immediatelyLoadChunk(new Point3(position));
        //return World.getChunkAtPoint(new Point3(position));
        ChunkLoader.addChunkToQueue(new Point3(position));
        return null;
    }

    public static float getTop(){
        return position.y + eyesHeightDelta;
    }

    public static float getBottom(){
        return position.y - eyesBottomDelta;
    }
}
