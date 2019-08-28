package com.exiro.fortgame.parts.guns;

import com.exiro.fortgame.base.Base;
import com.exiro.fortgame.utils.AmmoType;
import com.exiro.fortgame.utils.FileManager;
import com.exiro.fortgame.utils.Worker;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.List;

public class testGun extends Gun<testGun> {


    public testGun(Base base, List<Worker> workers, int level) {
        super("mediumCanonCase.png", null, null, null, null, 4, 3, 1, level, 1, 0, 0, null, null, base, workers, false, true, false, false, AmmoType.NORMAL, "testGun.png");
        setLevel(level);
    }


    @Override
    public testGun getValueFromFile(String path) {
        return null;
    }



    /**
     * recover data from dataFile
     *
     * @param level
     */
    @Override
    public void setLevel(int level) {

        Document document = FileManager.getXMLDocuement("test.a");
        final Element GunElem = (Element) document.getElementsByTagName("Gun").item(0);

        final Element info = (Element) GunElem.getElementsByTagName("testinfo").item(0);

        final Element correctGun = (Element) info.getElementsByTagName("test").item(level);

        final Element damageE = (Element) correctGun.getElementsByTagName("damage").item(0);
        final Element lifeE = (Element) correctGun.getElementsByTagName("life").item(0);
        final Element peopleE = (Element) correctGun.getElementsByTagName("people").item(0);
        final Element reloadE = (Element) correctGun.getElementsByTagName("reload").item(0);

        this.setDamage(Integer.parseInt(damageE.getTextContent()));
        this.setMaxLife(Float.parseFloat(lifeE.getTextContent()));
        this.setMaxPeople(Integer.parseInt(peopleE.getTextContent()));
        this.setReloadingTime(Integer.parseInt(reloadE.getTextContent()));
    }

    @Override
    public void desactivate() {

    }

    @Override
    public void activate() {

    }
}
