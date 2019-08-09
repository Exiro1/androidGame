package com.exiro.fortgame.base;

import com.exiro.fortgame.parts.Parts;
import com.exiro.fortgame.utils.PartType;

public class Case {

    int x, y;
    Parts part;
    PartType type;


    public Case(int x, int y, Parts part, PartType type) {
        this.x = x;
        this.y = y;
        this.part = part;
        this.type = type;
    }
}
