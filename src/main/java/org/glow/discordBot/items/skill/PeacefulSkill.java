package org.glow.discordBot.items.skill;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.glow.discordBot.entities.Player;

public abstract class PeacefulSkill extends Skill implements Peaceful {

    @JsonIgnore
    private Player targetPlayer;

    public Player getTargetPlayer() {
        return targetPlayer;
    }

    @Override
    public void setTargetPlayer(Player targetPlayer) {
        this.targetPlayer = targetPlayer;
    }
}
