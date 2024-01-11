package org.glow.discordBot.items;

import org.glow.discordBot.entities.Player;
import org.glow.discordBot.items.body.BodyFactory;
import org.glow.discordBot.items.handleft.HandLeftFactory;
import org.glow.discordBot.items.handright.HandRightFactory;
import org.glow.discordBot.items.head.HeadFactory;
import org.glow.discordBot.items.legs.LegsFactory;
import org.glow.discordBot.items.skill.skills.Dawn;
import org.glow.discordBot.items.skill.skills.ShiningMiracle;
import org.glow.discordBot.items.skill.Skill;
import org.glow.discordBot.items.usableitem.TeyvatFriedEgg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public enum ItemWorker {

    INSTANCE;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final List<Armor> ironArmor = new ArrayList<>();
    private final List<Armor> whiteIronArmor = new ArrayList<>();
    private final List<Weapon> weapons = new ArrayList<>();
    private final List<Shield> shields = new ArrayList<>();
    private final List<Skill> skills = new ArrayList<>();
    private final List<Item> items = new ArrayList<>();

    ItemWorker() {
        ironArmor.add(HeadFactory.INSTANCE.getIronHeadArmor());
        ironArmor.add(BodyFactory.INSTANCE.getIronBodyArmor());
        ironArmor.add(LegsFactory.INSTANCE.getIronLegsArmor());

        whiteIronArmor.add(HeadFactory.INSTANCE.getWhiteIronHeadArmor());
        whiteIronArmor.add(BodyFactory.INSTANCE.getWhiteIronBodyArmor());
        whiteIronArmor.add(LegsFactory.INSTANCE.getWhiteIronLegsArmor());

        weapons.add(HandRightFactory.INSTANCE.getDullBlade());
        weapons.add(HandRightFactory.INSTANCE.getSilverSword());

        shields.add(HandLeftFactory.INSTANCE.getIronShield());
        shields.add(HandLeftFactory.INSTANCE.getWhiteIronShield());

        skills.add(new Dawn());
        skills.add(new ShiningMiracle());

        items.add(new TeyvatFriedEgg());
    }

    public Item findItemByName(String itemName) {

        logger.debug("{}: ИЩЕМ ПРЕДМЕТ ПО ИМЕНИ: {}",
                this.getClass().getSimpleName().toUpperCase(), itemName.toUpperCase());

        List<Item> items = new ArrayList<>();
        items.addAll(ironArmor);
        items.addAll(whiteIronArmor);
        items.addAll(weapons);
        items.addAll(shields);
        items.addAll(skills);

        Optional<Item> resultItem = items.stream().filter(item -> item.getName().equals(itemName)).findFirst();

        if (resultItem.isPresent()) {
            logger.debug("{}: ПРЕДМЕТ НАЙДЕН",
                    this.getClass().getSimpleName().toUpperCase());
            return resultItem.get();
        } else {
            logger.warn("{}: ПРЕДМЕТ НЕ НАЙДЕН",
                    this.getClass().getSimpleName().toUpperCase());
            throw new IllegalArgumentException("Предмет не найден");
        }

    }

    public List<Item> getPlayerEquippedItems(Player player) {

        logger.debug("{}: ИГРОК ID: {} ЗАПРОС ОДЕТЫХ ПРЕДМЕТОВ",
                this.getClass().getSimpleName().toUpperCase(), player.getId());

        return List.of(
                player.getInventory().getHead(),
                player.getInventory().getBody(),
                player.getInventory().getLegs(),
                player.getInventory().getHandRight(),
                player.getInventory().getHandLeft()
        );

    }

    public List<Armor> getIronArmor() {
        return ironArmor;
    }

    public List<Armor> getWhiteIronArmor() {
        return whiteIronArmor;
    }

    public List<Weapon> getWeapons() {
        return weapons;
    }

    public List<Shield> getShields() {
        return shields;
    }

    public List<Skill> getSkills() {
        return skills;
    }

    public List<Item> getItems() {
        return items;
    }
}
