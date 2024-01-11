package org.glow.discordBot.items.skill;

import org.glow.discordBot.items.Item;

import java.util.Objects;

public abstract class Skill extends Item implements Castable {

    private Element spellElement;
    private String spellInfo;
    private int coastInMana;

    public Skill() {
    }

    public Skill(String name, int price, Element spellElement, String spellInfo, int coastInMana) {
        super(name, price);
        this.spellElement = spellElement;
        this.spellInfo = spellInfo;
        this.coastInMana = coastInMana;
    }

    public Element getSpellElement() {
        return spellElement;
    }

    public void setSpellElement(Element spellElement) {
        this.spellElement = spellElement;
    }

    public String getSpellInfo() {
        return spellInfo;
    }

    public void setSpellInfo(String spellInfo) {
        this.spellInfo = spellInfo;
    }

    public int getCoastInMana() {
        return coastInMana;
    }

    public void setCoastInMana(int coastInMana) {
        this.coastInMana = coastInMana;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Skill skill = (Skill) o;

        if (coastInMana != skill.coastInMana) return false;
        if (spellElement != skill.spellElement) return false;
        return Objects.equals(spellInfo, skill.spellInfo);
    }

    @Override
    public int hashCode() {
        int result = spellElement != null ? spellElement.hashCode() : 0;
        result = 31 * result + (spellInfo != null ? spellInfo.hashCode() : 0);
        result = 31 * result + coastInMana;
        return result;
    }
}
