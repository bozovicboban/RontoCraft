package com.rontoking.rontocraft;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.ModelCache;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Array;

public class Chunk {

    public static final int chunkX = 16;
    public static final int chunkY = 128;
    public static final int chunkZ = 16;

    //public static MeshBuilder modelBuilder = new MeshBuilder();

    public byte[][][] blocks;
    public Point3 position;

    public ModelCache modelCache;
    public boolean shouldBuild, hasBeenSaved, isVisible;
    public int numOfBlocks;
    public boolean isBuilding;


    public Chunk(Point3 position, boolean shouldBuild){
        this.position = new Point3(Utility.roundFloor(position.x, chunkX), Utility.roundFloor(position.y, chunkY), Utility.roundFloor(position.z, chunkZ));
        this.blocks = new byte[chunkX][chunkY][chunkZ];

        this.modelCache = new ModelCache();
        this.shouldBuild = shouldBuild;
        this.hasBeenSaved = false;
        this.isVisible = true;
        this.numOfBlocks = 0;
        this.isBuilding = false;
    }

    public Chunk(Point3 position){
        this.position = new Point3(Utility.roundFloor(position.x, chunkX), Utility.roundFloor(position.y, chunkY), Utility.roundFloor(position.z, chunkZ));
        this.blocks = new byte[chunkX][chunkY][chunkZ];

        this.modelCache = new ModelCache();
        this.shouldBuild = true;
        this.hasBeenSaved = false;
        this.isVisible = true;
        this.numOfBlocks = 0;
        this.isBuilding = false;
    }

    public Chunk(Vector3 v){
        this.position = new Point3(Utility.roundFloor(v.x, chunkX), Utility.roundFloor(v.y, chunkY), Utility.roundFloor(v.z, chunkZ));
        this.blocks = new byte[chunkX][chunkY][chunkZ];

        this.modelCache = new ModelCache();
        this.shouldBuild = true;
        this.hasBeenSaved = false;
        this.isVisible = true;
        this.numOfBlocks = 0;
        this.isBuilding = false;
    }

    public Chunk(int x, int y, int z){
        this.position =  new Point3(Utility.roundFloor(x, chunkX), Utility.roundFloor(y, chunkY), Utility.roundFloor(z, chunkZ));
        this.blocks = new byte[chunkX][chunkY][chunkZ];

        this.modelCache = new ModelCache();
        this.shouldBuild = true;
        this.hasBeenSaved = false;
        this.isVisible = true;
        this.numOfBlocks = 0;
        this.isBuilding = false;
    }

    public void buildModelCache(){
        isBuilding = true;
        modelCache.begin();
        Array<Point3> ncb = notCoveredBlocks();
        for(int i = 0; i < ncb.size; i++){
            modelCache.add(Block.getModelInstance(convertToWorldPos(ncb.get(i).x, ncb.get(i).y, ncb.get(i).z)
                    , !Block.isSideCovered(Block.neighbourNegZ(convertToWorldPos(ncb.get(i).x, ncb.get(i).y, ncb.get(i).z)), convertToWorldPos(ncb.get(i).x, ncb.get(i).y, ncb.get(i).z))
                    , !Block.isSideCovered(Block.neighbourPosZ(convertToWorldPos(ncb.get(i).x, ncb.get(i).y, ncb.get(i).z)), convertToWorldPos(ncb.get(i).x, ncb.get(i).y, ncb.get(i).z))
                    , !Block.isSideCovered(Block.neighbourNegY(convertToWorldPos(ncb.get(i).x, ncb.get(i).y, ncb.get(i).z)), convertToWorldPos(ncb.get(i).x, ncb.get(i).y, ncb.get(i).z))
                    , !Block.isSideCovered(Block.neighbourPosY(convertToWorldPos(ncb.get(i).x, ncb.get(i).y, ncb.get(i).z)), convertToWorldPos(ncb.get(i).x, ncb.get(i).y, ncb.get(i).z))
                    , !Block.isSideCovered(Block.neighbourNegX(convertToWorldPos(ncb.get(i).x, ncb.get(i).y, ncb.get(i).z)), convertToWorldPos(ncb.get(i).x, ncb.get(i).y, ncb.get(i).z))
                    , !Block.isSideCovered(Block.neighbourPosX(convertToWorldPos(ncb.get(i).x, ncb.get(i).y, ncb.get(i).z)), convertToWorldPos(ncb.get(i).x, ncb.get(i).y, ncb.get(i).z))));
        }
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                modelCache.end();
                shouldBuild = false;
                isBuilding = false;
                Renderer.isChunkBeingBuilt = false;
            }
        });
    }

    public int updateBlockNum(){
        numOfBlocks = 0;
        for(int x = 0; x < chunkX; x++){
            for(int y = 0; y < chunkY; y++){
                for(int z = 0; z < chunkZ; z++){
                    if(blocks[x][y][z] != 0) {
                        numOfBlocks++;
                    }
                }
            }
        }
        return numOfBlocks;
    }

    public Array<Point3> notCoveredBlocks(){
        Array<Point3> list = new Array<Point3>();
        for(int x = 0; x < chunkX; x++){
            for(int y = 0; y < chunkY; y++){
                for(int z = 0; z < chunkZ; z++){
                    if(blocks[x][y][z] != 0 && !Block.isCovered(convertToWorldPos(x, y ,z))) {
                        list.add(new Point3(x, y, z));
                    }
                }
            }
        }
        return list;
    }

    public BoundingBox boundingBox(){
        return new BoundingBox(position.toVector(), new Vector3(position.x + chunkX, position.y + chunkY, position.z + chunkZ));
    }

    public Point3 convertToWorldPos(int x, int y, int z){
        return new Point3(x + position.x, y + position.y, z + position.z);
    }

    public Array<Chunk> neighbours(){
        Array<Chunk> ret = new Array<Chunk>();
        for(int x = -chunkX; x <= chunkX; x += chunkX)
            for(int y = -chunkY; y <= chunkY; y += chunkY)
                for(int z = -chunkZ; z <= chunkZ; z += chunkZ)
                    ret.add(World.getChunkAtPoint(new Point3(position.x + x, position.y + y, position.z + z)));
        return ret;
    }

    public void dispose(){
        modelCache.dispose();
    }
}
