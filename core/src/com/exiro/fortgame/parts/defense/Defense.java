package com.exiro.fortgame.parts.defense;

import com.exiro.fortgame.base.Base;
import com.exiro.fortgame.parts.Parts;
import com.exiro.fortgame.utils.PartInteractionState;
import com.exiro.fortgame.utils.PartType;
import com.exiro.fortgame.utils.Sprite;
import com.exiro.fortgame.utils.Worker;

import java.util.List;

public class Defense extends Parts {
    public Defense(Sprite defaultSprite, Sprite actionSprite, Sprite damagedSprite, Sprite damagedActionSprite, PartType type, float life, int ammoCost, int energyCost, int maxPeople, int level, int xlength, int ylength, int x, int y, int currentAmmoModifier, int currentEnergyModifier, Parts upperPart, Parts exposedSidePart, PartInteractionState interactionState, Base base, List<Worker> workers) {
        super(defaultSprite, actionSprite, damagedSprite, damagedActionSprite, type, life, ammoCost, energyCost, maxPeople, level, xlength, ylength, x, y, currentAmmoModifier, currentEnergyModifier, upperPart, exposedSidePart, interactionState, base, workers);
    }

    @Override
    public void desactivate() {

    }

    @Override
    public void activate() {

    }
}
