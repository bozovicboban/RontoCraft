package com.rontoking.rontocraft;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.IntAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.graphics.g3d.model.Animation;
import com.badlogic.gdx.graphics.g3d.model.MeshPart;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.BaseJsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.UBJsonReader;

import java.io.InputStream;

/**
 * Created by Core 2 Duo on 16.8.2016.
 */
public class ModelManager {

    public static final Array<Model> blockModels = new Array<Model>();
    public static final ModelBuilder modelBuilder = new ModelBuilder();
    public static final Model floorModel = modelBuilder.createBox(1000f, 0.1f, 1000f, new Material(ColorAttribute.createDiffuse(new Color(0, 0.5f, 0.1f, 1))), VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
    public static final ModelInstance floorInstance = new ModelInstance(floorModel, 0, 0, 0);
    public static final Model selectedBlockModel = modelBuilder.createBox(1f, 1f, 1f, new Material(TextureAttribute.createDiffuse(TextureManager.selectedBlockTexture)), VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates);
    public static final ModelInstance selectedBlockInstance = new ModelInstance(selectedBlockModel, Point3.zero.toVector());
    public static void loadModels(){
        loadBlockModels();
    }
    public static int attr = VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates;

    public static Model skyModel, donutModel;
    public static ModelInstance skyInstance;

    public static void loadBlockModels(){
        selectedBlockInstance.materials.get(0).set(new BlendingAttribute(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA));
        for(int i = 0; i < TextureManager.regions.size; i++){
            //blockModels.add(modelBuilder.createBox(0.5f, 0.5f, 0.5f, new Material(TextureAttribute.createDiffuse(t)), VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates));
            modelBuilder.begin();
            modelBuilder.part("front", GL20.GL_TRIANGLES, attr, blockMaterial(i, 0))
                    .rect(    0.5f,-0.5f,-0.5f, -0.5f,-0.5f,-0.5f,-0.5f,0.5f,-0.5f,0.5f,0.5f,-0.5f,0,0,-0.5f);
            modelBuilder.part("back", GL20.GL_TRIANGLES, attr, blockMaterial(i, 1))
                    .rect( -0.5f,-0.5f,0.5f,  0.5f,-0.5f,0.5f, 0.5f,0.5f,0.5f,-0.5f,0.5f,0.5f, 0,0,0.5f);
            modelBuilder.part("bottom", GL20.GL_TRIANGLES, attr,blockMaterial(i, 2))
                    .rect(0.5f,-0.5f,0.5f, -0.5f,-0.5f,0.5f,-0.5f,-0.5f,-0.5f,0.5f,-0.5f,-0.5f, 0,-0.5f,0);
            modelBuilder.part("top", GL20.GL_TRIANGLES, attr,blockMaterial(i, 3))
                    .rect(0.5f,0.5f,-0.5f,-0.5f,0.5f,-0.5f, -0.5f,0.5f,0.5f,0.5f,0.5f,0.5f,    0,0.5f,0);
            modelBuilder.part("left", GL20.GL_TRIANGLES, attr, blockMaterial(i, 4))
                    .rect(-0.5f,-0.5f,-0.5f, -0.5f,-0.5f,0.5f,-0.5f,0.5f,0.5f, -0.5f,0.5f,-0.5f,-0.5f,0,0);
            modelBuilder.part("right", GL20.GL_TRIANGLES, attr, blockMaterial(i, 5))
                    .rect( 0.5f,-0.5f,0.5f, 0.5f,-0.5f,-0.5f,0.5f,0.5f,-0.5f,0.5f,0.5f,0.5f,  0.5f,0,0);
            blockModels.add(modelBuilder.end());
        }

        Texture front = new Texture(Gdx.files.internal("sky/front.png"));
        Texture back = new Texture(Gdx.files.internal("sky/back.png"));
        Texture top = new Texture(Gdx.files.internal("sky/top.png"));
        Texture bottom = new Texture(Gdx.files.internal("sky/bottom.png"));
        Texture right = new Texture(Gdx.files.internal("sky/right.png"));
        Texture left = new Texture(Gdx.files.internal("sky/left.png"));

        front.setWrap(Texture.TextureWrap.ClampToEdge, Texture.TextureWrap.ClampToEdge);
        back.setWrap(Texture.TextureWrap.ClampToEdge, Texture.TextureWrap.ClampToEdge);
        top.setWrap(Texture.TextureWrap.ClampToEdge, Texture.TextureWrap.ClampToEdge);
        bottom.setWrap(Texture.TextureWrap.ClampToEdge, Texture.TextureWrap.ClampToEdge);
        right.setWrap(Texture.TextureWrap.ClampToEdge, Texture.TextureWrap.ClampToEdge);
        left.setWrap(Texture.TextureWrap.ClampToEdge, Texture.TextureWrap.ClampToEdge);

        modelBuilder.begin();
        modelBuilder.part("front", GL20.GL_TRIANGLES, attr, new Material(new IntAttribute(IntAttribute.CullFace, 0), TextureAttribute.createDiffuse(back)))
                .rect(-0.5f,-0.5f,-0.5f, -0.5f,0.5f,-0.5f,  0.5f,0.5f,-0.5f, 0.5f,-0.5f,-0.5f, 0,0,-0.5f);
        modelBuilder.part("back", GL20.GL_TRIANGLES, attr, new Material(new IntAttribute(IntAttribute.CullFace, 0), TextureAttribute.createDiffuse(front)))
                .rect(-0.5f,0.5f,0.5f, -0.5f,-0.5f,0.5f,  0.5f,-0.5f,0.5f, 0.5f,0.5f,0.5f, 0,0,0.5f);
        modelBuilder.part("bottom", GL20.GL_TRIANGLES, attr, new Material(new IntAttribute(IntAttribute.CullFace, 0), TextureAttribute.createDiffuse(bottom)))
                .rect(-0.5f,-0.5f,0.5f, -0.5f,-0.5f,-0.5f,  0.5f,-0.5f,-0.5f, 0.5f,-0.5f,0.5f, 0,-0.5f,0);
        modelBuilder.part("top", GL20.GL_TRIANGLES, attr, new Material(new IntAttribute(IntAttribute.CullFace, 0), TextureAttribute.createDiffuse(top)))
                .rect(-0.5f,0.5f,-0.5f, -0.5f,0.5f,0.5f,  0.5f,0.5f,0.5f, 0.5f,0.5f,-0.5f, 0,0.5f,0);
        modelBuilder.part("left", GL20.GL_TRIANGLES, attr, new Material(new IntAttribute(IntAttribute.CullFace, 0), TextureAttribute.createDiffuse(right)))
                .rect(-0.5f,-0.5f,0.5f, -0.5f,0.5f,0.5f,  -0.5f,0.5f,-0.5f, -0.5f,-0.5f,-0.5f, -0.5f,0,0);
        modelBuilder.part("right", GL20.GL_TRIANGLES, attr, new Material(new IntAttribute(IntAttribute.CullFace, 0), TextureAttribute.createDiffuse(left)))
                .rect(0.5f,-0.5f,-0.5f, 0.5f,0.5f,-0.5f,  0.5f,0.5f,0.5f, 0.5f,-0.5f,0.5f, 0.5f,0,0);

        skyModel = modelBuilder.end();
        skyInstance = new ModelInstance(skyModel);

        //UBJsonReader jsonReader = new UBJsonReader();
        //G3dModelLoader modelLoader = new G3dModelLoader(jsonReader);
    }

    private static Material blockMaterial(int blockIndex, int blockSide){
        if(Block.Type.values()[blockIndex].hasDifferentTop){
            if(blockSide == 2)
                return new Material(TextureAttribute.createDiffuse(TextureManager.atlas.findRegion(Block.Type.values()[blockIndex].name + " bottom")));
            else if(blockSide == 3)
                return new Material(TextureAttribute.createDiffuse(TextureManager.atlas.findRegion(Block.Type.values()[blockIndex].name + " top")));
            else
                return new Material(TextureAttribute.createDiffuse(TextureManager.atlas.findRegion(Block.Type.values()[blockIndex].name + " side")));
        }
        else return new Material(TextureAttribute.createDiffuse(TextureManager.regions.get(blockIndex)));
    }

    public static void dispose(){
        for(Model m : blockModels)
            m.dispose();
        floorModel.dispose();
        selectedBlockModel.dispose();

        skyModel.dispose();
    }
}
