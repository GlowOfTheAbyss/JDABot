package org.glow.discordBot.entities.npc;

import org.glow.discordBot.entities.Inventory;
import org.glow.discordBot.entities.PersonWorker;
import org.glow.discordBot.entities.SkillBook;
import org.glow.discordBot.items.skill.Element;

import java.util.Random;

public enum NPCSlimeFactory {

    INSTANCE;

    public NPC getHydroSlime() {

        NPC hydroSlime = new NPC.NPCBuilder("0")
                .setName("Гидро слайм")
                .setImage("https://cdn.discordapp.com/attachments/1066672288897978439/1087251608884228126/HydroSlime.png")
                .setElement(Element.HYDRO)

                .setStrengthValue(0)
                .setEnduranceValue(new Random().nextInt(2, 5))
                .setAgilityValue(1)
                .setIntelligenceValue(0)
                .setPerceptionValue(0)
                .setLuckValue(0)

                .setEnergy(0)

                .setInventory(new Inventory())
                .setSkillBook(new SkillBook())

                .build();

        hydroSlime.setHealth((4 + hydroSlime.getEndurance().getValue()) * 10);
        hydroSlime.setMana(0);
        hydroSlime.setCoins(PersonWorker.INSTANCE.getPersonLevel(hydroSlime) * 3 * 10);

        return hydroSlime;

    }

    public NPC getPyroSlime() {

        NPC pyroSlime = new NPC.NPCBuilder("0")
                .setName("Пиро слайм")
                .setImage("https://cdn.discordapp.com/attachments/1066672288897978439/1087251608389308498/PyroSlime.png")
                .setElement(Element.PYRO)

                .setStrengthValue(new Random().nextInt(1, 4))
                .setEnduranceValue(1)
                .setAgilityValue(1)
                .setIntelligenceValue(0)
                .setPerceptionValue(0)
                .setLuckValue(0)

                .setEnergy(0)

                .setInventory(new Inventory())
                .setSkillBook(new SkillBook())

                .build();

        pyroSlime.setHealth((4 + pyroSlime.getEndurance().getValue()) * 10);
        pyroSlime.setMana(0);
        pyroSlime.setCoins(PersonWorker.INSTANCE.getPersonLevel(pyroSlime) * 3 * 10);

        return pyroSlime;

    }

}
