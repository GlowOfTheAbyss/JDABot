package org.glow.discordBot.items.skill.skills;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.glow.discordBot.action.fight.Fight;
import org.glow.discordBot.action.fight.FightWorker;
import org.glow.discordBot.entities.Person;
import org.glow.discordBot.entities.PersonWorker;
import org.glow.discordBot.entities.Player;
import org.glow.discordBot.entities.npc.NPC;
import org.glow.discordBot.fileworkers.SaveAndLoad;
import org.glow.discordBot.items.skill.BattleSkill;
import org.glow.discordBot.items.skill.Element;
import org.glow.discordBot.message.MessageSender;
import org.glow.discordBot.message.TextCreator;

import java.util.Random;

public class Dawn extends BattleSkill {


    public Dawn() {
        setName("Рассвет");
        setSpellElement(Element.PYRO);
        setSpellInfo("""
                Собравшись на оружии пламя превращается в стремящегося вперёд феникса, который наносит большой Пиро урон всем врагам на своём пути.
                """);
        setCoastInMana(50);
        setPrice(10_000);
    }

    @Override
    public void cast(SlashCommandInteractionEvent event, Player player) {

        Fight fight;
        Person enemy;
        if (FightWorker.INSTANCE.personInFight(player)) {
            fight = FightWorker.INSTANCE.getFightByPerson(player);
            if (fight.getAttacker().equals(player)) {
                enemy = fight.getDefender();
            } else {
                enemy = fight.getAttacker();
            }
        } else {
            new MessageSender.MessageBuilder(event.getHook(), "Нальзя это использовать вне боя")
                    .build().replyMessage();
            return;
        }

        int damage = new Random().nextInt(1, 4);
        damage += player.getIntelligence().getValue();
        damage *= 10;

        if (enemy instanceof NPC) {
            if (((NPC) enemy).getElement().equals(getSpellElement())) {
                damage = 0;
            }
        }

        player.setMana(player.getMana() - getCoastInMana());
        SaveAndLoad.INSTANCE.save(player);
        enemy.setHealth(enemy.getHealth() - damage);
        if (enemy instanceof Player) {
            SaveAndLoad.INSTANCE.save((Player) enemy);
        }

        String title = String.format("""
                %s использует %s на %s
                """, PersonWorker.INSTANCE.getPersonName(player), getName(),
                PersonWorker.INSTANCE.getPersonName(enemy));
        String description = String.format("""
                %s получает %s урона
                
                %s
                """, PersonWorker.INSTANCE.getPersonName(enemy), damage,
                TextCreator.INSTANCE.getPlayerParameters(player));

        new MessageSender.MessageBuilder(event.getHook(), title)
                .setDescription(description).build().replyMessage();

    }

}
