package com.rontoking.rontocraft;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;

public class TerrainGenerator {
    public static final int MAX_Y = 32;
    public static float SMOOTHNESS = 0.01f;

    public static void generate(){
        SimplexNoise.seed();

        Save.generatedChunks.clear();
        Gdx.files.local(Save.savePath()).emptyDirectory();
        CameraManager.updatingVisibility = false;
        World.chunkArray.clear();
        World.chunkMap.clear();
        ChunkLoader.chunksToLoad.clear();

        //for (int x = -worldX / 2; x < worldX / 2; x ++) {
        //    for (int z = -worldZ / 2; z < worldZ / 2; z ++) {
        //        for (int y = 0; y < worldY; y ++) {
        //            generateAtPoint(x, y, z);
        //        }
        //    }
        //}
        CameraManager.updateVisibleChunks();
        CameraManager.updatingVisibility = true;
    }

    public static byte generateAtPoint(int x, int y, int z){
        int height = getHeight(x, z, 0.01f);
        if(height < y)
            return 0;
        if(height == y)
            return Block.Type.GRASS.type;
        if(height - y <= 4)
            return Block.Type.DIRT.type;
        return Block.Type.STONE.type;
    }


    // The smaller the smoothness the smoother and less laggy it is.
    public static int getHeight(int x, int z, float smoothness){
        return (int)((SimplexNoise.sample(x * smoothness, z * SMOOTHNESS) + 1) * MAX_Y);
    }
}
