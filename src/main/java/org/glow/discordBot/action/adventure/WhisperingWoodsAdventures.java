package org.glow.discordBot.action.adventure;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.glow.discordBot.action.Action;
import org.glow.discordBot.action.chest.CommonChest;
import org.glow.discordBot.action.chest.ExquisiteChest;
import org.glow.discordBot.action.fight.FightWorker;
import org.glow.discordBot.entities.PersonWorker;
import org.glow.discordBot.entities.Player;
import org.glow.discordBot.entities.npc.NPC;
import org.glow.discordBot.entities.npc.NPCHilichurlFactory;
import org.glow.discordBot.entities.npc.NPCSlimeFactory;
import org.glow.discordBot.fileworkers.SaveAndLoad;
import org.glow.discordBot.message.MessageSender;
import org.glow.discordBot.message.TextCreator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WhisperingWoodsAdventures implements Action {

    private final Logger logger = LoggerFactory.getLogger(this.getName());
    private SlashCommandInteractionEvent event;
    private Player player;

    public WhisperingWoodsAdventures() {}

    @Override
    public String getName() {
        return "Шепчущий лес";
    }

    @Override
    public String getDescription() {
        return "Лес вблизи города Мондштадт, населен в основном слабыми противниками, шанс найти что то ценное доаольно мал";
    }

    @Override
    public void start(SlashCommandInteractionEvent event, Player player) {

        logger.info("{}: СТАРТ", getName().toUpperCase());

        this.event = event;
        this.player = player;

        player.setEnergy(player.getEnergy() - 1);
        SaveAndLoad.INSTANCE.save(player);
        logger.info("АКТИВНОСТЬ {}: ИГРОК С ID {} ТЕРЯЕТ 1 ЭНЕРГИЮ", getName().toUpperCase(), player.getId());

        int random = new Random().nextInt(1, 101);
        logger.debug("АКТИВНОСТЬ {}: СЛУЧАЙНОЕ ЧИСЛО {}", getName().toUpperCase(), random);
        int chestChance = 6 + player.getLuck().getValue();
        chestChance -= (int) (0.1 * PersonWorker.INSTANCE.getPersonLevel(player));
        logger.debug("АКТИВНОСТЬ {}: ШАНС СУНДУКА {}", getName().toUpperCase(), chestChance);

        if (chestChance > random) {
            chest();
        } else {
            adventure();
        }

    }

    private void adventure() {

        logger.info("АКТИВНОСТЬ {}: ИГРОК С ID {} ПОПОДАЕТ В ПРИКЛЮЧЕНИЕ", getName().toUpperCase(), player.getId());

        int random = new Random().nextInt(1, 101);
        logger.debug("АКТИВНОСТЬ {}: СЛУЧАЙНОЕ ЧИСЛО {}", getName().toUpperCase(), random);
        int nothingChance = 10;

        if (nothingChance >= random) {

            String title = String.format("""
                    %s спокойно прогуливается по лесу, но ничего не находит
                    """, PersonWorker.INSTANCE.getPersonName(player));
            new MessageSender.MessageBuilder(event.getHook(), title)
                    .setDescription(TextCreator.INSTANCE.getPlayerParameters(player))
                    .build().replyMessage();
            logger.debug("АКТИВНОСТЬ {}: ИГРОК НИЧЕГО НЕ НАХОДИТ, КОНЕЦ", getName().toUpperCase());

        } else {
            enemyChoose();
        }

    }

    private void enemyChoose() {

        List<NPC> slimes = new ArrayList<>();
        slimes.add(NPCSlimeFactory.INSTANCE.getHydroSlime());
        slimes.add(NPCSlimeFactory.INSTANCE.getPyroSlime());

        List<NPC> hilichurl = new ArrayList<>();
        hilichurl.add(NPCHilichurlFactory.INSTANCE.getHilichurl());
        hilichurl.add(NPCHilichurlFactory.INSTANCE.getHilichurlFighter());
        hilichurl.add(NPCHilichurlFactory.INSTANCE.getHilichurlBerserker());

        int random = new Random().nextInt(1, 101);
        logger.debug("АКТИВНОСТЬ {}: СЛУЧАЙНОЕ ЧИСЛО {}", getName().toUpperCase(), random);
        int hilichurlChance = 20;
        hilichurlChance += 2 * PersonWorker.INSTANCE.getPersonCombatLevel(player);
        logger.debug("АКТИВНОСТЬ {}: ШАНС ХИЛИЧУРЛА {}", getName().toUpperCase(), hilichurlChance);

        NPC npc;
        if (hilichurlChance > random) {
            npc = hilichurl.get(new Random().nextInt(hilichurl.size()));
        } else {
            npc = slimes.get(new Random().nextInt(slimes.size()));
        }
        logger.debug("АКТИВНОСТЬ {}: ПРОТИВНИК ВЫБРАН {}", getName().toUpperCase(), npc.getName());

        String title = String.format("""
                %s встречает %s
                """, PersonWorker.INSTANCE.getPersonName(player), PersonWorker.INSTANCE.getPersonName(npc));
        String description = String.format("""
                %s уровееь: %s
                %s боевой уровень: %s
                %s HP: %s
                """, PersonWorker.INSTANCE.getPersonName(npc), PersonWorker.INSTANCE.getPersonLevel(npc),
                PersonWorker.INSTANCE.getPersonName(npc), PersonWorker.INSTANCE.getPersonCombatLevel(npc),
                PersonWorker.INSTANCE.getPersonName(npc), npc.getHealth());

        new MessageSender.MessageBuilder(event.getHook(), title)
                .setDescription(description)
                .setImage(npc.getImage())
                .build().replyMessage();

        FightWorker.INSTANCE.createFight(player, npc, event);

    }

    private void chest() {

        int random = new Random().nextInt(1, 101);
        int exquisiteChestChance = 15;
        exquisiteChestChance += (int) (0.5 * player.getLuck().getValue());
        exquisiteChestChance -= (int) (0.05 * PersonWorker.INSTANCE.getPersonLevel(player));

        if (exquisiteChestChance >= random) {
            new ExquisiteChest(player).open(event);
        } else {
            new CommonChest(player).open(event);
        }

    }

}
