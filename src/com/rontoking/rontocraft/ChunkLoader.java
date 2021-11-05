package com.rontoking.rontocraft;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;

public class ChunkLoader {
    public static Array<Point3> chunksToLoad = new Array<Point3>();
    public static int CHUNK_LOADING_RANGE = 8;

    private static void addChunkToWorld(Chunk chunk){
        for(int i = 0; i < World.chunkArray.size; i++){
            if(!isChunkInRange(World.chunkArray.get(i))){
                replaceChunk(World.chunkArray.get(i).position.copy(), chunk);
                return;
            }
        }
        World.chunkArray.add(chunk);
        World.chunkMap.put(World.chunkArray.get(World.chunkArray.size - 1).position.copy(), World.chunkArray.size - 1);
    }

    private static void replaceChunk(Point3 oldChunk, Chunk newChunk){
        if(!World.chunkArray.get(World.chunkMap.get(oldChunk)).hasBeenSaved)
            Save.saveChunk(World.chunkArray.get(World.chunkMap.get(oldChunk)));
        emptyChunk(World.chunkArray.get(World.chunkMap.get(oldChunk)));
        World.chunkArray.set(World.chunkMap.get(oldChunk), newChunk);
        World.chunkArray.get(World.chunkMap.get(oldChunk)).shouldBuild = false;
        World.chunkMap.put(newChunk.position.copy(), World.chunkMap.get(oldChunk));
        World.chunkMap.remove(oldChunk);
    }

    private static void emptyChunk(Chunk chunk){
        for(int x = 0; x < Chunk.chunkX; x++){
            for(int y = 0; y < Chunk.chunkY; y++){
                for(int z = 0; z < Chunk.chunkZ; z++){
                    if(chunk.blocks[x][y][z] != 0) {
                        chunk.blocks[x][y][z] = 0;
                        World.loadedBlockNum--;
                    }
                }
            }
        }
        chunk.numOfBlocks = 0;
        chunk.shouldBuild = false;
    }

    private static boolean isChunkInRange(Chunk chunk){
        return Math.abs(chunk.position.x - Player.position.x) <= (CHUNK_LOADING_RANGE + 1) * Chunk.chunkX && Math.abs(chunk.position.z - Player.position.z) <= (CHUNK_LOADING_RANGE + 1) * Chunk.chunkZ;
    }

    public static void addChunkToQueue(Point3 position){
        if(World.getChunkAtPoint(position) == null && !chunksToLoad.contains(position, false))
            chunksToLoad.add(position.copy());
    }

    public static void loadQueuedChunks(){
        if (chunksToLoad.size > 0){
            loadChunk(chunksToLoad.get(0));
        }
    }

    private static void loadChunk(final Point3 position){
        Chunk chunk = new Chunk(position);
        boolean shouldAdd = generateChunk(chunk);
        chunk.shouldBuild = true;
        addChunkToWorld(chunk);
        World.getChunkAtPoint(chunk.position).shouldBuild = true;
        Array<Chunk> nbs = chunk.neighbours();
        for (int i = 0; i < nbs.size; i++) {
            if (nbs.get(i) != null) {
                nbs.get(i).shouldBuild = true;
            }
        }
        int blockNum = World.getChunkAtPoint(chunk.position).updateBlockNum();
        World.totalBlockNum += blockNum;
        World.loadedBlockNum += blockNum;
        chunksToLoad.removeIndex(0);
        if(shouldAdd)
            Save.generatedChunks.add(chunk.position.copy());
    }

    /*public static void immediatelyLoadChunk(Point3 position){
        position.set(Utility.roundFloor(position.x, Chunk.chunkX), Utility.roundFloor(position.y, Chunk.chunkY), Utility.roundFloor(position.z, Chunk.chunkZ));
        for(int i = chunksToLoad.size - 1; i >= 0; i--)
            if(chunksToLoad.get(i).equals(position))
                chunksToLoad.removeIndex(i);
        loadChunk(position);
    }*/


    public static boolean generateChunk(Chunk chunk){
        if (Save.isChunkGenerated(chunk)) {
            Save.loadChunk(chunk);
            return false;
        }
        else {
            for (int x = 0; x < Chunk.chunkX; x++)
                for (int y = 0; y < Chunk.chunkY; y++)
                    for (int z = 0; z < Chunk.chunkZ; z++)
                        chunk.blocks[x][y][z] = TerrainGenerator.generateAtPoint(chunk.position.x + x, chunk.position.y + y, chunk.position.z + z);
            return true;
        }
    }
}
