package com.rontoking.rontocraft;

import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.math.Plane;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

import java.util.Hashtable;

public class World {

    public static final Environment environment = new Environment();
    public static final Array<Chunk> chunkArray = new Array<Chunk>();
    public static final ObjectMap<Point3, Integer> chunkMap = new ObjectMap<Point3, Integer>(327); // Use Hashtable for multithreading and ObjectMap for single thread.
    public static final Plane floorPlane = new Plane(new Vector3(0, 0, 0), new Vector3(1, 0, 0), new Vector3(0, 0, 1));

    public static int totalBlockNum = 0;
    public static int loadedBlockNum = 0;

    public static void createEnvironment(){
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
    }

    public static void setBlockAtPoint(Point3 p, byte t) {
        Point3 point = new Point3(p);
        Chunk c = getChunkAtPoint(point);
        if (c != null) {
            point.set(point.x - c.position.x, point.y - c.position.y, point.z - c.position.z);
            c.blocks[point.x][point.y][point.z] = t;
            updateBlockCounters(c, t);
            c.shouldBuild = true;
            c.hasBeenSaved = false;
            Array<Point3> nbs = Block.neighbours(p);
            for(int i = 0; i < nbs.size; i++)
                getChunkAtPoint(nbs.get(i)).shouldBuild = true;
        }
    }

    private static void updateBlockCounters(Chunk chunk, byte t){
        if(t == 0){
            chunk.numOfBlocks--;
            loadedBlockNum--;
            totalBlockNum--;
        }else{
            chunk.numOfBlocks++;
            loadedBlockNum++;
            totalBlockNum++;
        }
    }

    public static byte getBlockAtPoint(Point3 v) {
        Chunk getChunk = getChunkAtPoint(v);
        if (getChunk != null) {
            Point3 getBlockPoint = new Point3(v.x - getChunk.position.x, v.y - getChunk.position.y, v.z - getChunk.position.z);
            return getChunk.blocks[getBlockPoint.x][getBlockPoint.y][getBlockPoint.z];
        }
        return 0;
    }

    public static Chunk getChunkAtPoint(Point3 v) {
        Point3 getChunkPoint = new Point3(Utility.roundFloor(v.x, Chunk.chunkX), Utility.roundFloor(v.y, Chunk.chunkY), Utility.roundFloor(v.z, Chunk.chunkZ));
        if(chunkMap.containsKey(getChunkPoint))
            return chunkArray.get(chunkMap.get(getChunkPoint));
        return null;
    }

    public static boolean addBlock(byte t, Point3 v) {
        if (v.y >= 0) {
            if (getChunkAtPoint(v) == null)
                return false;
            if (getBlockAtPoint(v) == 0) {
                setBlockAtPoint(v, t);
                return true;
            }
        }
        return false;
    }

    public static boolean removeBlock(Point3 v) {
        if (getChunkAtPoint(v) != null && getBlockAtPoint(v) != 0) {
            setBlockAtPoint(v, (byte)0);
            return true;
        }
        return false;
    }

    public static void dispose(){
        for(Chunk c : chunkArray){
            c.dispose();
        }
    }
}
