package com.rontoking.rontocraft;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Core 2 Duo on 16.8.2016.
 */
public class GameInput{

    public static final Vector3 moveVector = new Vector3();
    public static final Ray ray = new Ray();
    public static final int range = 6;
    //public static final float increment = 0.1f;
    public static Point3 lookedAtBlock = new Point3(Point3.none);
    public static Point3 sideHitByRay = new Point3(Point3.none);
    public static final Array<Point3> blocksInRange = new Array<Point3>();

    public static byte selectedBlock = Block.firstID;

    public static boolean rightMousePressed = false;
    public static boolean leftMousePressed = false;

    public static void update(){
        if(!Chat.IS_CHAT_OPEN) {
            updateGeneral();
            updateMovement();
            updateMouse();
        }
    }

    public static void updateGeneral(){
        updateBlockSelection();
        //checkForReset();
        toggleVSync();
        updateGUI();
    }

    public static void updateMovement() {
        Player.alreadyCollided = false;
        updateHorizontalMovement();
        updateVerticalMovement();
        updateSprint();
    }

    public static void updateMouse() {
        if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT) && !rightMousePressed) { // Adding blocks.
            rightMousePressed = true;
            placeBlock();
        }
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT) && !leftMousePressed) { // Removing blocks.
            leftMousePressed = true;
            removeBlock();
        }
        if (!Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
            rightMousePressed = false;
        }
        if (!Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            leftMousePressed = false;
        }
    }

    public static void updateHorizontalMovement(){
        Player.velocity.x = 0;
        Player.velocity.z = 0;
        moveVector.set(0, 0, 0);
        if(InputManager.keys.containsKey(InputManager.forwardKey) && InputManager.keys.containsKey(InputManager.rightKey)){
            moveVector.set(CameraManager.camera.direction.x, 0, CameraManager.camera.direction.z);
            moveVector.rotate(Vector3.Y, -45);
        }else if(InputManager.keys.containsKey(InputManager.forwardKey) && InputManager.keys.containsKey(InputManager.leftKey)){
            moveVector.set(CameraManager.camera.direction.x, 0, CameraManager.camera.direction.z);
            moveVector.rotate(Vector3.Y, 45);
        }else if(InputManager.keys.containsKey(InputManager.backwardKey) && InputManager.keys.containsKey(InputManager.rightKey)){
            moveVector.set(-CameraManager.camera.direction.x, 0, -CameraManager.camera.direction.z);
            moveVector.rotate(Vector3.Y, 45);
        }else if(InputManager.keys.containsKey(InputManager.backwardKey) && InputManager.keys.containsKey(InputManager.leftKey)){
            moveVector.set(-CameraManager.camera.direction.x, 0, -CameraManager.camera.direction.z);
            moveVector.rotate(Vector3.Y, -45);
        }else if(InputManager.keys.containsKey(InputManager.forwardKey)){
            moveVector.set(CameraManager.camera.direction.x, 0, CameraManager.camera.direction.z);
        }else if(InputManager.keys.containsKey(InputManager.backwardKey)){
            moveVector.set(-CameraManager.camera.direction.x, 0, -CameraManager.camera.direction.z);
        }else if(InputManager.keys.containsKey(InputManager.rightKey)){
            moveVector.set(CameraManager.camera.direction.x, 0, CameraManager.camera.direction.z);
            moveVector.rotate(Vector3.Y, -90);
        }else if(InputManager.keys.containsKey(InputManager.leftKey)){
            moveVector.set(CameraManager.camera.direction.x, 0, CameraManager.camera.direction.z);
            moveVector.rotate(Vector3.Y, 90);
        }
        moveVector.nor();
        moveVector.set(moveVector.x * Player.moveSpeed * Player.commandSpeed, 0, moveVector.z * Player.moveSpeed * Player.commandSpeed);
        Player.velocity.x = moveVector.x;
        Player.velocity.z = moveVector.z;
    }

    public static void updateVerticalMovement(){
        if(InputManager.keys.containsKey(InputManager.jumpKey)){
            Player.jump();
        }
    }

    public static void updateSprint(){
        if(InputManager.keys.containsKey(InputManager.sprintKey)){
            Player.moveSpeed = Player.sprintSpeed;
        }
        else{
            Player.moveSpeed = Player.walkSpeed;
        }
    }

    public static void placeBlock() {
        Vector3 v = new Vector3();
        if (!lookedAtBlock.equals(Point3.none) && !Player.placeBlockBoundingBox().intersects(Block.getBoundingBox(sideHitByRay.toVector()))) // Placing on a block.
            if (World.addBlock(selectedBlock, new Point3(sideHitByRay)))
                AudioManager.blockSound.play(1, MathUtils.random(0.75f, 1.75f), 0);
            else if (lookedAtBlock.equals(Point3.none) && Intersector.intersectRayPlane(ray, World.floorPlane, v) && v.dst(Player.position) <= range) // Placing on the floor.
                if (World.addBlock(selectedBlock, Point3.floorVec(v)))
                    AudioManager.blockSound.play(1, MathUtils.random(0.75f, 1.75f), 0);
    }

    public static void removeBlock() {
        if (!lookedAtBlock.equals(Point3.none)) // Placing on a block.
            if (World.removeBlock(lookedAtBlock))
                AudioManager.blockSound.play(1, MathUtils.random(0.75f, 1.75f), 0);
    }

    public static void updateBlockSelection(){
        if(InputManager.keys.containsKey(InputManager.selectionRightKey)){
            InputManager.keys.remove(InputManager.selectionRightKey, InputManager.selectionRightKey);
            if (selectedBlock == Block.lastID)
                selectedBlock = Block.firstID;
            else
                selectedBlock++;
        }
        if(InputManager.keys.containsKey(InputManager.selectionLeftKey)){
            InputManager.keys.remove(InputManager.selectionLeftKey, InputManager.selectionLeftKey);
            if(selectedBlock == Block.firstID)
                selectedBlock = Block.lastID;
            else
                selectedBlock--;
        }
        for(int i = 1; i <= 9; i++){
            if(InputManager.keys.containsKey(7 + i)){
                InputManager.keys.remove(7 + i, InputManager.selectionLeftKey);
                if(Block.firstID + i <= Block.lastID)
                    selectedBlock = (byte)(Block.firstID + i - 1);
            }
        }
    }

    /*
    public static void checkForReset(){
        if(InputManager.keys.containsKey(InputManager.resetKey)){
            InputManager.keys.remove(InputManager.resetKey, 0);
            reset();
        }
        Renderer.r = !Gdx.input.isKeyPressed(Input.Keys.R);
    }
    */

    public static void reset(){
        Player.position = new Vector3(Player.spawnPos);
        Player.inAir = true;
        lookedAtBlock = new Point3(Point3.none);
        sideHitByRay = new Point3(Point3.none);
        World.totalBlockNum = 0;
        World.loadedBlockNum = 0;
        TerrainGenerator.generate();
    }

    public static void toggleVSync(){
        if(InputManager.keys.containsKey(InputManager.vsyncKey)){
            InputManager.keys.remove(InputManager.vsyncKey, 0);
            Main.vSyncOn = !Main.vSyncOn;
            Gdx.graphics.setVSync(Main.vSyncOn);
        }
    }

    public static void updateGUI(){
        if(InputManager.keys.containsKey(InputManager.toggleTextKey)){
            InputManager.keys.remove(InputManager.toggleTextKey, 0);
            if(Renderer.showGUI == 4)
                Renderer.showGUI = 0;
            else
                Renderer.showGUI++;
        }
    }

    public static void setRay(){
        ray.set(Player.position, CameraManager.camera.direction);
    }

    public static void setLookedAtBlock() {
        Vector3 v = new Vector3();
        float distance = -1;
        int index = -1;

        for (int i = 0; i < blocksInRange.size; i++) {
            if (Intersector.intersectRayBoundsFast(ray, Block.getBoundingBox(blocksInRange.get(i).toVector())) && (Player.position.dst(blocksInRange.get(i).toVector()) < distance || distance == -1)) {
                index = i;
                distance = Player.position.dst(blocksInRange.get(i).toVector());
            }
        }
        if (index != -1) {
            lookedAtBlock = new Point3(blocksInRange.get(index));
            Intersector.intersectRayBounds(ray, Block.getBoundingBox(lookedAtBlock.toVector()), v);
            sideHitByRay = new Point3(setSideHitByRay(lookedAtBlock, v));
            ModelManager.selectedBlockInstance.transform.setToTranslation(new Vector3(lookedAtBlock.x + 0.5f, lookedAtBlock.y + 0.5f, lookedAtBlock.z + 0.5f));
        } else {
            lookedAtBlock = new Point3(Point3.none);
            sideHitByRay = new Point3(Point3.none);
        }
    }

    public static Point3 setSideHitByRay(Point3 blockPos, Vector3 rayColl){
        if(rayColl.x == blockPos.x)
            return new Point3(blockPos.x - 1, blockPos.y, blockPos.z);
        else if(rayColl.x == blockPos.x + 1)
            return new Point3(blockPos.x + 1, blockPos.y, blockPos.z);
        if(rayColl.y == blockPos.y)
            return new Point3(blockPos.x, blockPos.y - 1, blockPos.z);
        else if(rayColl.y == blockPos.y + 1)
            return new Point3(blockPos.x, blockPos.y + 1, blockPos.z);
        if(rayColl.z == blockPos.z)
            return new Point3(blockPos.x, blockPos.y, blockPos.z - 1);
        else if(rayColl.z == blockPos.z + 1)
            return new Point3(blockPos.x, blockPos.y, blockPos.z + 1);
        return  Point3.none;
    }

    public static void setBlocksInRange(){
        blocksInRange.clear();
        for(int x = -range; x <= range; x++){
            for(int y = -range; y <= range; y++){
                for(int z = -range; z <= range; z++){
                    Point3 p = Point3.floorVec(new Vector3(Player.position.x + x, Player.position.y + y, Player.position.z + z));
                    if(World.getBlockAtPoint(p) != 0){
                        Vector3 v = new Vector3();
                        Intersector.intersectRayBounds(ray, Block.getBoundingBox(p.toVector()), v);
                        if(Player.position.dst(v) <= range)
                            blocksInRange.add(p);
                    }
                }
            }
        }
    }
}
