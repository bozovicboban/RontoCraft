package com.rontoking.rontocraft;

public class Commands {
    private static final String HELP_MSG = "/help /reset /inv /speed [number] /jump [number] /smooth [number]";
    private static final String FLOAT_MSG = "That is not a number.";

    public static boolean tryParse(String msg){
        if(msg.toLowerCase().equals("/help")){
            Chat.addSystemMsg(HELP_MSG);
            return true;
        }
        if(msg.toLowerCase().equals("/reset")){
            GameInput.reset();
            return true;
        }
        if(msg.toLowerCase().equals("/inv")){
            if(!GameInput.lookedAtBlock.equals(Point3.none))
                World.getChunkAtPoint(GameInput.lookedAtBlock).isVisible = !World.getChunkAtPoint(GameInput.lookedAtBlock).isVisible;
            return true;
        }
        if(isCommand(msg, "/speed ")){
            try{
                Player.commandSpeed = Float.parseFloat(msg.split(" ")[1].trim());
            }catch (Exception e){
                Chat.addSystemMsg(FLOAT_MSG);
            }
            return true;
        }
        if(isCommand(msg, "/jump ")){
            try{
                Player.commandJump = Float.parseFloat(msg.split(" ")[1].trim());
            }catch (Exception e){
                Chat.addSystemMsg(FLOAT_MSG);
            }
            return true;
        }
        if(isCommand(msg, "/smooth ")){
            try{
                TerrainGenerator.SMOOTHNESS = Float.parseFloat(msg.split(" ")[1].trim());
                GameInput.reset();
            }catch (Exception e){
                Chat.addSystemMsg(FLOAT_MSG);
            }
            return true;
        }
        return false;
    }

    private static boolean isCommand(String msg, String command){
        return msg.length() >= command.length() && msg.toLowerCase().substring(0, command.length()).equals(command);
    }
}
