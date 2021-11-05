package com.rontoking.rontocraft;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;

public class Save {
    private static final String savePath = "saves";
    private static String worldName = "default";

    public static Array<Point3> generatedChunks = new Array<Point3>();

    public static void saveChunk(Chunk chunk){
        Gdx.files.local(chunkPath(chunk)).writeBytes(chunkToBytes(chunk), false);
        chunk.hasBeenSaved = true;
    }

    public static void loadChunk(Chunk chunk){
        byte[] bytes = Gdx.files.local(chunkPath(chunk)).readBytes();
        for(int x = 0; x < Chunk.chunkX; x++){
            for(int y = 0; y < Chunk.chunkY; y++){
                for(int z = 0; z < Chunk.chunkZ; z++){
                    //World.setBlockAtPoint(new Point3(chunk.position.x + x, chunk.position.y + y, chunk.position.z + z), bytes[x * Chunk.chunkY * Chunk.chunkZ + y * Chunk.chunkZ + z]);
                    chunk.blocks[x][y][z] = bytes[x * Chunk.chunkY * Chunk.chunkZ + y * Chunk.chunkZ + z];
                }
            }
        }
        chunk.hasBeenSaved = true;
    }

    private static String chunkPath(Chunk chunk){
        return savePath + "/" + worldName + "/" + chunk.position.x + ", " + chunk.position.y + ", " + chunk.position.z;
    }

    public static String savePath(){
        return savePath + "/" + worldName;
    }

    private static byte[] chunkToBytes(Chunk chunk){
        byte[] ret = new byte[Chunk.chunkX * Chunk.chunkY * Chunk.chunkZ];
        for(int x = 0; x < Chunk.chunkX; x++){
            for(int y = 0; y < Chunk.chunkY; y++){
                for(int z = 0; z < Chunk.chunkZ; z++){
                    ret[x * Chunk.chunkY * Chunk.chunkZ + y * Chunk.chunkZ + z] = chunk.blocks[x][y][z];
                }
            }
        }
        return ret;
    }

    public static boolean isChunkGenerated(Chunk chunk){
        return generatedChunks.contains(chunk.position, false);
    }
}
