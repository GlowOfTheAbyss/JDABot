package org.glow.discordBot.entities.npc;

import org.glow.discordBot.entities.Inventory;
import org.glow.discordBot.entities.Person;
import org.glow.discordBot.entities.SkillBook;
import org.glow.discordBot.items.skill.Element;
import org.glow.discordBot.message.characteristic.Characteristic;
import org.glow.discordBot.message.characteristic.CharacteristicFactory;

public class NPC extends Person {

    private String name;
    private String image;
    private Element element;

    private NPC(NPCBuilder npcBuilder) {
        setId(npcBuilder.id);

        this.name = npcBuilder.name;
        this.image = npcBuilder.image;
        this.element = npcBuilder.element;

        setHealth(npcBuilder.health);
        setMana(npcBuilder.mana);
        setEnergy(npcBuilder.energy);
        setCoins(npcBuilder.coins);

        setStrength(npcBuilder.strength);
        setEndurance(npcBuilder.endurance);
        setAgility(npcBuilder.agility);
        setIntelligence(npcBuilder.intelligence);
        setPerception(npcBuilder.perception);
        setLuck(npcBuilder.luck);

        setInventory(npcBuilder.inventory);
        setSkillBook(npcBuilder.skillBook);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Element getElement() {
        return element;
    }

    public void setElement(Element element) {
        this.element = element;
    }

    public static class NPCBuilder {

        private final String id;

        private String name;
        private String image;
        private Element element;

        private int health;
        private int mana;
        private int energy;
        private int coins;

        private final Characteristic strength;
        private final Characteristic endurance;
        private final Characteristic agility;
        private final Characteristic intelligence;
        private final Characteristic perception;
        private final Characteristic luck;

        private Inventory inventory;
        private SkillBook skillBook;

        public NPCBuilder(String id) {
            this.id = id;
            strength = CharacteristicFactory.INSTANCE.createStrength();
            endurance = CharacteristicFactory.INSTANCE.createEndurance();
            agility = CharacteristicFactory.INSTANCE.createAgility();
            intelligence = CharacteristicFactory.INSTANCE.createIntelligence();
            perception = CharacteristicFactory.INSTANCE.createPerception();
            luck = CharacteristicFactory.INSTANCE.createLuck();
        }

        public NPCBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public NPCBuilder setImage(String image) {
            this.image = image;
            return this;
        }

        public NPCBuilder setElement(Element element) {
            this.element = element;
            return this;
        }

        public NPCBuilder setHealth(int health) {
            this.health = health;
            return this;
        }

        public NPCBuilder setMana(int mana) {
            this.mana = mana;
            return this;
        }

        public NPCBuilder setEnergy(int energy) {
            this.energy = energy;
            return this;
        }

        public NPCBuilder setCoins(int coins) {
            this.coins = coins;
            return this;
        }

        public NPCBuilder setStrengthValue(int value) {
            strength.setValue(value);
            return this;
        }

        public NPCBuilder setEnduranceValue(int value) {
            endurance.setValue(value);
            return this;
        }

        public NPCBuilder setAgilityValue(int value) {
            agility.setValue(value);
            return this;
        }

        public NPCBuilder setIntelligenceValue(int value) {
            intelligence.setValue(value);
            return this;
        }

        public NPCBuilder setPerceptionValue(int value) {
            perception.setValue(value);
            return this;
        }

        public NPCBuilder setLuckValue(int value) {
            luck.setValue(value);
            return this;
        }

        public NPCBuilder setInventory(Inventory inventory) {
            this.inventory = inventory;
            return this;
        }

        public NPCBuilder setSkillBook(SkillBook skillBook) {
            this.skillBook = skillBook;
            return this;
        }

        public NPC build() {
            return new NPC(this);
        }

    }

}
