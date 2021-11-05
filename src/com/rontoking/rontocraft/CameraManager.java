package com.rontoking.rontocraft;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Core 2 Duo on 16.8.2016.
 */
public class CameraManager {

    public static final int defaultFOV = 70;
    public static final float defaultNear = 0.01f;
    public static final float defaultFar = 512f;
    public static final float rotSpeed = 0.1f;
    public static final float yDirectionLimit = 0.996f;
    public static final PerspectiveCamera camera = new PerspectiveCamera(defaultFOV, Main.width, Main.height);;

    public static boolean isFPS = true;
    public static int chunkLoadX, chunkLoadY, chunkLoadZ;
    public static boolean updatingVisibility = false;
    public static boolean shouldUpdateVisibility = false;
    public static final Array<Chunk> frustumChunks = new Array<Chunk>();
    public static final Array<Point3> loadedChunks = new Array<Point3>();

    public static final Point2 prevMousePos = new Point2(Gdx.input.getX(), Gdx.input.getY());
    public static final Point2 deltaMousePos = Point2.zero;

    public static void createCamera(){
        followPlayer();
        camera.lookAt(Player.spawnDir);
        camera.near = defaultNear;
        camera.far = defaultFar;
    }

    public static void updateModels(){
        ModelManager.skyInstance.transform.setToTranslation(camera.position.x, 0, camera.position.z);
        ModelManager.skyInstance.transform.scale(500, 500, 500);
    }

    public static void followPlayer(){ // If first person just sets it to Player.position. If third person adds an offset.
        if(isFPS){
            camera.position.set(Player.position);
            shouldUpdateVisibility = true;
        }
        else{
            // TODO: add third person view.
        }
        camera.update();
    }

    public static void updateVisibleChunks() {
        updateLoadedChunks();
        updateFrustumChunks();
    }

    public static void updateLoadedChunks() {
        chunkLoadX = MathUtils.ceil(camera.far / Chunk.chunkX);
        chunkLoadY = MathUtils.ceil(camera.far / Chunk.chunkY);
        chunkLoadZ = MathUtils.ceil(camera.far / Chunk.chunkZ);

        loadedChunks.clear();
        for (int x = -ChunkLoader.CHUNK_LOADING_RANGE; x <= ChunkLoader.CHUNK_LOADING_RANGE; x++) {
            //for (int y = -ChunkLoader.CHUNK_LOADING_RANGE; y <= ChunkLoader.CHUNK_LOADING_RANGE; y++) {
                for (int z = -ChunkLoader.CHUNK_LOADING_RANGE; z <= ChunkLoader.CHUNK_LOADING_RANGE; z++) {
                    Chunk playerChunk = Player.getChunk();
                    if (playerChunk != null) {
                        Point3 loadChunkPosition = new Point3(playerChunk.position.x + x * Chunk.chunkX, /*playerChunk.position.y + y * Chunk.chunkY*/0, playerChunk.position.z + z * Chunk.chunkZ);
                        if (loadChunkPosition.y >= 0 && World.getChunkAtPoint(loadChunkPosition) == null) {
                            ChunkLoader.addChunkToQueue(loadChunkPosition);
                            loadedChunks.add(loadChunkPosition);
                        }
                    }
                }
            //}
        }
    }

    public static void updateFrustumChunks(){
        frustumChunks.clear();
        for (Chunk c : World.chunkArray) {
            if (camera.frustum.boundsInFrustum(c.boundingBox()) || !updatingVisibility) {
                frustumChunks.add(c);
            }
        }
    }

    public static void updateCamera(Point2 mousePos){
        mousePos.set(Gdx.input.getX(), Gdx.input.getY());

        deltaMousePos.x = Math.abs(prevMousePos.x - mousePos.x);
        deltaMousePos.y = Math.abs(prevMousePos.y - mousePos.y);
        if(deltaMousePos.x > 100)
            deltaMousePos.x = 100;
        if(deltaMousePos.y > 50)
            deltaMousePos.y = 50;

        if (prevMousePos.x > mousePos.x) {
            camera.rotate(Vector3.Y, 1 * deltaMousePos.x * rotSpeed);
            camera.update();
        }

        if (prevMousePos.x < mousePos.x) {
            camera.rotate(Vector3.Y, -1 * deltaMousePos.x * rotSpeed);
            camera.update();
        }

        if (prevMousePos.y < mousePos.y) {
            if (camera.direction.y > -yDirectionLimit) {
                camera.rotate(camera.direction.cpy().crs(Vector3.Y), -1 * deltaMousePos.y * rotSpeed);
                if(camera.direction.y <= -1)
                    camera.direction.y = -yDirectionLimit;
            }
            camera.update();
        }

        if (prevMousePos.y > mousePos.y) {

            if (camera.direction.y < yDirectionLimit) {
                camera.rotate(camera.direction.cpy().crs(Vector3.Y), 1 * deltaMousePos.y * rotSpeed);
                if(camera.direction.y >= 1)
                    camera.direction.y = yDirectionLimit;
            }
            camera.update();
        }

        prevMousePos.set(mousePos);
        if(deltaMousePos.x != 0 || deltaMousePos.y != 0)
            shouldUpdateVisibility = true;
    }
}
