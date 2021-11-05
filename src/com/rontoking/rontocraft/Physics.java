package com.rontoking.rontocraft;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.collision.BoundingBox;

public class Physics {

    public static final float gravity =  30f; // 30f
    public static final float terminalVelocity = 30f; // 30f

    public static void updatePlayer(){
        Player.updatePosition();
        checkPlayerYCollision();
    }

    public static void checkPlayerYCollision(){
        if(Player.getBottom() < 0){
            Player.inAir = false;
            Player.velocity.y = 0;
            Player.position.y = Player.eyesBottomDelta;
        }
        else if(Collision.isPlayerIntersecting()){
            for(BoundingBox b : Collision.blocksIntersectingPlayer()){
                if (b.max.y > Player.getBottom() && b.max.y < Player.getTop()){ // If block is below player.
                    Player.velocity.y = 0;
                    Player.position.y = (int)Math.ceil(Player.getBottom()) + Player.eyesBottomDelta;
                    Player.inAir = false;
                }
                else if (b.min.y > Player.getBottom() && b.min.y < Player.getTop()){ // If block is above player.
                    Player.velocity.y = 0;
                    Player.position.y = (int)Math.floor(Player.getTop()) - Player.eyesHeightDelta;
                }
                else{
                    System.out.println("Max: " + b.max.y + " Min: " + b.min.y + " Top: " + Player.getTop());
                }
                break;
            }
        }
        else{
            Player.velocity.y -= gravity * Gdx.graphics.getDeltaTime();
            Player.inAir = true;
            if(Player.velocity.y > terminalVelocity)
                Player.velocity.y = terminalVelocity;
            else if(Player.velocity.y < -terminalVelocity)
                Player.velocity.y = -terminalVelocity;
        }
    }
}
