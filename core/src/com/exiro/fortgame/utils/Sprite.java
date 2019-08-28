package com.exiro.fortgame.utils;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;
import java.util.List;

public class Sprite {


    List<Texture> textures;     // all the texture of the sprite
    double timeSinceLastFrame,timePerFrames;
    int totalFrame,h,w,currentFrameID;
    Pixmap spriteSheet;
    String path;

    public Sprite(double timePerFrames, int totalFrame, int h, int w, Pixmap spriteSheet,int columns,String path) {
        this.path = path;
        this.textures = new ArrayList<>();
        this.timeSinceLastFrame = 0;
        this.timePerFrames = timePerFrames;
        this.totalFrame = totalFrame;
        this.h = h;
        this.w = w;
        this.currentFrameID = 0;
        this.spriteSheet = spriteSheet;
        textures = loadSheet(path,totalFrame,h,w,columns);
    }

    /**
     * Load a Sprite sheet
     * @param path : path to sprite sheet
     * @param totalFrame : number of frame of the sprite
     * @param h : height of the Texture
     * @param w : width of the texture
     * @param columns : number of columns in the sprite sheet
     * @return an Arraylist of all the texture
     */
    public static List<Texture> loadSheet(String path,int totalFrame,int h,int w,int columns){
        Pixmap bigTexture = new Pixmap(Gdx.files.getFileHandle(path, Files.FileType.Internal));
        List<Texture> tex = new ArrayList<>();
        for(int i=0;i<totalFrame;i++) {
            int srcX = (i%columns) * w;
            int srcY = ((i-(i%columns))/columns) * h;
            Pixmap sheetPart = new Pixmap(w, h, Pixmap.Format.RGBA8888);
            sheetPart.drawPixmap(bigTexture, 0, 0, srcX, srcY, w, h);
            tex.add(new Texture(sheetPart, Pixmap.Format.RGBA8888, false));
        }
        return tex;
    }

    /**
     * Change frame if needed and return current Texture (frame)
     * @param delta deltaTime since last iteration
     * @return Sprite's current texture
     */
    public Texture processSprite(double delta){
        this.timeSinceLastFrame += delta;
        if(this.timeSinceLastFrame>=this.timePerFrames){
            this.timeSinceLastFrame = 0d;
            this.currentFrameID++;
            if(this.currentFrameID>=this.totalFrame)
                this.currentFrameID = 0;
        }
        return this.textures.get(currentFrameID);
    }


}
