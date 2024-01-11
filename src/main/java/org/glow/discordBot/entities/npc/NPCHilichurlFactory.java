package org.glow.discordBot.entities.npc;

import org.glow.discordBot.entities.Inventory;
import org.glow.discordBot.entities.PersonWorker;
import org.glow.discordBot.entities.SkillBook;
import org.glow.discordBot.items.skill.Element;

import java.util.Random;

public enum NPCHilichurlFactory {

    INSTANCE;

    public NPC getHilichurl() {

        NPC hilichurl = new NPC.NPCBuilder("0")
                .setName("Хиличурл")
                .setImage("https://cdn.discordapp.com/attachments/1066672288897978439/1087251591637241886/Hilichurl.png")
                .setElement(Element.PHYSICAL)

                .setStrengthValue(new Random().nextInt(1, 3))
                .setEnduranceValue(1)
                .setAgilityValue(new Random().nextInt(2, 4))
                .setIntelligenceValue(0)
                .setPerceptionValue(1)
                .setLuckValue(0)

                .setEnergy(0)

                .setInventory(new Inventory())
                .setSkillBook(new SkillBook())

                .build();

        hilichurl.setHealth((6 + hilichurl.getEndurance().getValue()) * 10);
        hilichurl.setMana(0);
        hilichurl.setCoins(PersonWorker.INSTANCE.getPersonLevel(hilichurl) * 3 * 10);

        return hilichurl;

    }

    public NPC getHilichurlFighter() {

        NPC hilichurlFighter = new NPC.NPCBuilder("0")
                .setName("Хиличурл-боец")
                .setImage("https://cdn.discordapp.com/attachments/1066672288897978439/1087251591188447363/HilichurlFighter.png")
                .setElement(Element.PHYSICAL)

                .setStrengthValue(new Random().nextInt(2, 4))
                .setEnduranceValue(1)
                .setAgilityValue(new Random().nextInt(2, 4))
                .setIntelligenceValue(0)
                .setPerceptionValue(1)
                .setLuckValue(0)

                .setEnergy(0)

                .setInventory(new Inventory())
                .setSkillBook(new SkillBook())

                .build();

        hilichurlFighter.setHealth((6 + hilichurlFighter.getEndurance().getValue()) * 10);
        hilichurlFighter.setMana(0);
        hilichurlFighter.setCoins(PersonWorker.INSTANCE.getPersonLevel(hilichurlFighter) * 3 * 10);

        return hilichurlFighter;

    }

    public NPC getHilichurlBerserker() {

        NPC hilichurlBerserker = new NPC.NPCBuilder("0")
                .setName("Хиличурл-берсерк")
                .setImage("https://cdn.discordapp.com/attachments/1066672288897978439/1087251592056684605/HilichurlBerserker.png")
                .setElement(Element.PHYSICAL)

                .setStrengthValue(new Random().nextInt(3, 5))
                .setEnduranceValue(1)
                .setAgilityValue(2)
                .setIntelligenceValue(0)
                .setPerceptionValue(1)
                .setLuckValue(0)

                .setEnergy(0)

                .setInventory(new Inventory())
                .setSkillBook(new SkillBook())

                .build();

        hilichurlBerserker.setHealth((8 + hilichurlBerserker.getEndurance().getValue()) * 10);
        hilichurlBerserker.setMana(0);
        hilichurlBerserker.setCoins(PersonWorker.INSTANCE.getPersonLevel(hilichurlBerserker) * 3 * 10);

        return hilichurlBerserker;

    }

}
