package com.exiro.fortgame.base;

import com.exiro.fortgame.parts.Parts;
import com.exiro.fortgame.utils.PartType;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class Base {

    private int ammoBaseSupply, energyBaseSupply, ammoCurrentSupply, energyCurrentSupply;
    private List<Case> Cases; //case used
    private double life;

    public Base() {
        Cases = new ArrayList<>();
    }

    public static Base getBaseFromData(String data) {
        Base b = new Base();
        Class<?> clazz = null;

        JSONParser parser = new JSONParser();
        try {
            JSONObject json = (JSONObject) parser.parse(data);
            JSONArray baseArray = (JSONArray) json.get("base");
            for (int i = 0; i < baseArray.size(); i++) {
                JSONObject baseE = (JSONObject) baseArray.get(i);
                try {
                    Object object = null;
                    if (!((String) baseE.get("class")).contains("null")) {
                        clazz = Class.forName("com.exiro.fortgame.parts.guns." + baseE.get("class"));
                        Constructor<?> ctor = clazz.getConstructor(Base.class, List.class, int.class);
                        object = ctor.newInstance(b, null, baseE.get("level"));
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
                    b.getCases().add(new Case((int) baseE.get("x"), (int) baseE.get("y"), (Parts) object, type));

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
