package com.exiro.fortgame.base;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.exiro.fortgame.Player;
import com.exiro.fortgame.fortGame;
import com.exiro.fortgame.parts.Parts;
import com.exiro.fortgame.parts.guns.Gun;
import com.exiro.fortgame.utils.PartType;
import com.exiro.fortgame.utils.Team;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Base {

    private int ammoBaseSupply, energyBaseSupply, ammoCurrentSupply, energyCurrentSupply;
    private List<Case> Cases; //case used
    int xCoord;
    private double life;
    private Map<Integer, Parts> parts;
    private Team team;
    private Player owner;
    private Stage baseStage;

    public Base(Viewport vp) {
        Cases = new ArrayList<>();
        parts = new HashMap<>();
        baseStage = new Stage(vp, fortGame.getInstance().batch);
        baseStage.setDebugAll(true);

    }

    public static Base getBaseFromData(String data, Team team, Viewport vp) {
        Base b = new Base(vp);
        b.setTeam(team);
        Class<?> clazz = null;

        JSONParser parser = new JSONParser();
        try {
            JSONObject json = (JSONObject) parser.parse(data);
            JSONArray baseArray = (JSONArray) json.get("base");
            for (int i = 0; i < baseArray.size(); i++) {
                JSONObject baseE = (JSONObject) baseArray.get(i);
                try {
                    Parts object = null;
                    if (!((String) baseE.get("class")).contains("null")) {
                        clazz = Class.forName("com.exiro.fortgame.parts.guns." + baseE.get("class"));
                        Constructor<?> ctor = clazz.getConstructor(Base.class, List.class, int.class);
                        object = (Parts) ctor.newInstance(b, new ArrayList<>(), Integer.parseInt(String.valueOf(baseE.get("level"))));
                        object.spritePos(Integer.parseInt(String.valueOf(baseE.get("x"))), Integer.parseInt(String.valueOf(baseE.get("y"))));
                        if (baseE.containsKey("ID")) {// BattleBase
                            b.parts.put(Integer.parseInt(String.valueOf(baseE.get("ID"))), object);
                            object.setID(Integer.parseInt(String.valueOf(baseE.get("ID"))));
                        }
                        b.baseStage.addActor(object);
                    }
                    PartType type = null;
                    switch ((String) baseE.get("type")) {
                        case "gun":
                            type = PartType.GUN;
                            break;
                        case "util":
                            type = PartType.UTILS;
                            break;
                        case "defense":
                            type = PartType.DEFENSE;
                            break;
                        default:
                            type = PartType.UTILS;

                    }
                    b.getCases().add(new Case(Integer.parseInt(String.valueOf(baseE.get("x"))), Integer.parseInt(String.valueOf(baseE.get("y"))), object, type));




                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }


            }

        } catch (ParseException e) {
            e.printStackTrace();
        }


        return b;
    }


    public void draw(SpriteBatch batch, ShapeRenderer shapeRenderer) {

        //TODO make containerTex an actor to reduce batch begin/end
        batch.begin();
        if (getTeam() == Team.ALLY) {
            for (Case c : getCases()) {


                if (c.part instanceof Gun) {
                    Gun g = (Gun) c.part;
                    Texture t = g.getContainerTex();
                    Sprite s = new Sprite(t);
                    s.setPosition(c.x * 20 + 20, c.y * 20 + 20);
                    s.setSize(20, 20);
                    s.draw(batch);


                }
            }
        } else {
            for (Case c : getCases()) {
                if (c.part instanceof Gun) {
                    Gun g = (Gun) c.part;
                    Texture t = g.getContainerTex();
                    Sprite s = new Sprite(t);
                    s.flip(true, false); // flip the sprite
                    s.setPosition(xCoord + 140 - c.x * 20 - 20, c.y * 20 + 20);
                    s.setSize(20, 20);

                    s.draw(batch);
                }
            }
        }
        batch.end();

        baseStage.act(Gdx.graphics.getDeltaTime());
        baseStage.draw();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setAutoShapeType(true);
        int factor = 0;
        int add = 1;
        if (team == Team.ENEMY) {
            factor = xCoord + 140;
            add = -1;
        }
        for (Case c : getCases()) {
            if (c.part instanceof Parts) {
                Parts parts = c.part;

                shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
                shapeRenderer.setColor(Color.BLUE);
                double lifeFactor = (parts.getCurrentLife() / parts.getMaxLife());
                lifeFactor *= 18;
                shapeRenderer.rect(factor + add * (c.x * 20 + 21), c.y * 20 + 21, (int) lifeFactor, 1);
                shapeRenderer.set(ShapeRenderer.ShapeType.Line);
                shapeRenderer.setColor(Color.BLACK);
                shapeRenderer.rect(factor + add * (c.x * 20 + 21), c.y * 20 + 21, 18, 1);
            }
        }
        shapeRenderer.end();


    }

    public int getxCoord() {
        return xCoord;
    }

    public void setxCoord(int xCoord) {
        this.xCoord = xCoord;
    }

    public Stage getBaseStage() {
        return baseStage;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public int getAmmoBaseSupply() {
        return ammoBaseSupply;
    }

    public void setAmmoBaseSupply(int ammoBaseSupply) {
        this.ammoBaseSupply = ammoBaseSupply;
    }

    public int getEnergyBaseSupply() {
        return energyBaseSupply;
    }

    public void setEnergyBaseSupply(int energyBaseSupply) {
        this.energyBaseSupply = energyBaseSupply;
    }

    public Map<Integer, Parts> getParts() {
        return parts;
    }

    public void setParts(Map<Integer, Parts> parts) {
        this.parts = parts;
    }

    public List<Case> getCases() {
        return Cases;
    }

    public void setCases(List<Case> cases) {
        Cases = cases;
    }

    public double getLife() {
        return life;
    }

    public void setLife(double life) {
        this.life = life;
    }

    public int getAmmoCurrentSupply() {
        return ammoCurrentSupply;
    }

    public void setAmmoCurrentSupply(int ammoCurrentSupply) {
        this.ammoCurrentSupply = ammoCurrentSupply;
    }

    public void addAmmoCurrentSupply(int amount) {
        this.ammoCurrentSupply += amount;
    }

    public int getEnergyCurrentSupply() {
        return energyCurrentSupply;
    }

    public void setEnergyCurrentSupply(int energyCurrentSupply) {
        this.energyCurrentSupply = energyCurrentSupply;
    }

    public void addEnergyCurrentSupply(int amount) {
        this.energyCurrentSupply += amount;
    }
}
