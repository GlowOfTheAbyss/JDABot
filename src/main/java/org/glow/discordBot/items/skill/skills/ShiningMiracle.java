package org.glow.discordBot.items.skill.skills;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.glow.discordBot.entities.PersonWorker;
import org.glow.discordBot.entities.Player;
import org.glow.discordBot.fileworkers.SaveAndLoad;
import org.glow.discordBot.items.skill.Element;
import org.glow.discordBot.items.skill.PeacefulSkill;
import org.glow.discordBot.message.MessageSender;
import org.glow.discordBot.message.Parameters;
import org.glow.discordBot.message.TextCreator;

import java.util.Random;

public class ShiningMiracle extends PeacefulSkill {

    public ShiningMiracle() {
        setName("Чудесное сияние");
        setSpellElement(Element.HYDRO);
        setSpellInfo("""
                Восстанавливает определённое количество HP любому игроку, включая самого кастующего.
                """);
        setCoastInMana(20);
        setPrice(6_000);
    }

    @Override
    public void cast(SlashCommandInteractionEvent event, Player player) {

        player.setMana(player.getMana() - getCoastInMana());
        SaveAndLoad.INSTANCE.save(player);

        int health = getTargetPlayer().getHealth();
        int maxHealth = PersonWorker.INSTANCE.getPlayerMaxHealth(getTargetPlayer());

        int heal = new Random().nextInt(1, 3);
        heal += player.getIntelligence().getValue();
        heal *= 10;

        getTargetPlayer().setHealth(getTargetPlayer().getHealth() + heal);
        if (getTargetPlayer().getHealth() > maxHealth) {
            getTargetPlayer().setHealth(maxHealth);
            heal = maxHealth - health;
        }
        SaveAndLoad.INSTANCE.save(getTargetPlayer());

        String title = String.format("""
                %s использует %s на %s
                """, PersonWorker.INSTANCE.getPersonName(player), getName(),
                PersonWorker.INSTANCE.getPersonName(getTargetPlayer()));
        String description = String.format("""
                %s восстановил %s %s
                
                %s
                """, PersonWorker.INSTANCE.getPersonName(getTargetPlayer()), heal, Parameters.HEALTH.getName(),
                TextCreator.INSTANCE.getPlayerParameters(getTargetPlayer()));

        new MessageSender.MessageBuilder(event.getHook(), title)
                .setDescription(description).build().replyMessage();

    }

}
