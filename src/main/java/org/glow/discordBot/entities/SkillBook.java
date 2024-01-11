package org.glow.discordBot.entities;

import org.glow.discordBot.items.skill.Skill;

import java.util.ArrayList;
import java.util.List;

public class SkillBook {

    private List<Skill> skills = new ArrayList<>();

    private int bookSize = 10;

    public List<Skill> getSkills() {
        return skills;
    }

    public void setSkills(List<Skill> skills) {
        this.skills = skills;
    }

    public void addSkill(Skill skill) {
        skills.add(skill);
    }

    public void removeSkill(Skill skill) {
        skills.remove(skill);
    }

    public int getBookSize() {
        return bookSize;
    }

    public void setBookSize(int bookSize) {
        this.bookSize = bookSize;
    }

}
