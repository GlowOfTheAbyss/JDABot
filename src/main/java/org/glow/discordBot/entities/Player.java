package org.glow.discordBot.entities;

import org.glow.discordBot.message.characteristic.CharacteristicFactory;

public class Player extends Person {

    public Player() {
    }

    public Player(String id) {

        setId(id);

        setStrength(CharacteristicFactory.INSTANCE.createStrength());
        setEndurance(CharacteristicFactory.INSTANCE.createEndurance());
        setAgility(CharacteristicFactory.INSTANCE.createAgility());
        setIntelligence(CharacteristicFactory.INSTANCE.createIntelligence());
        setPerception(CharacteristicFactory.INSTANCE.createPerception());
        setLuck(CharacteristicFactory.INSTANCE.createLuck());

        setHealth(100);
        setMana(100);
        setEnergy(10);
        setCoins(10);

        setInventory(new Inventory());
        setSkillBook(new SkillBook());

    }

}
