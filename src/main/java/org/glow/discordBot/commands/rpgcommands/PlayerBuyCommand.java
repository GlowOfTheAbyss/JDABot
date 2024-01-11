package org.glow.discordBot.commands.rpgcommands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import org.glow.discordBot.action.fight.FightWorker;
import org.glow.discordBot.commands.SlashCommand;
import org.glow.discordBot.entities.PersonWorker;
import org.glow.discordBot.entities.Player;
import org.glow.discordBot.fileworkers.SaveAndLoad;
import org.glow.discordBot.items.Armor;
import org.glow.discordBot.items.Item;
import org.glow.discordBot.items.ItemWorker;
import org.glow.discordBot.items.Weapon;
import org.glow.discordBot.items.Shield;
import org.glow.discordBot.items.skill.Skill;
import org.glow.discordBot.items.usableitem.Usable;
import org.glow.discordBot.message.Equipment;
import org.glow.discordBot.message.MessageSender;
import org.glow.discordBot.message.Parameters;
import org.glow.discordBot.message.TextCreator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static java.util.Objects.isNull;

public class PlayerBuyCommand implements SlashCommand {

    private final Logger logger = LoggerFactory.getLogger(this.getName());

    private final List<Armor> ironArmor = ItemWorker.INSTANCE.getIronArmor();
    private final List<Armor> whiteIronArmor = ItemWorker.INSTANCE.getWhiteIronArmor();
    private final List<Weapon> weapons = ItemWorker.INSTANCE.getWeapons();
    private final List<Shield> shields = ItemWorker.INSTANCE.getShields();
    private final List<Skill> skills = ItemWorker.INSTANCE.getSkills();
    private final List<Item> items = ItemWorker.INSTANCE.getItems();

    private final String armorOptionName = "armor";
    private final String weaponOptionName = "weapon";
    private final String skillOptionName = "skill";
    private final String itemOptionName = "item";

    @Override
    public String getName() {
        return "buy";
    }

    @Override
    public String getDescription() {
        return "купить броню";
    }

    @Override
    public void start(SlashCommandInteractionEvent event) {

        logger.info("{}: СТАРТ",
                getName().toUpperCase());

        event.deferReply().queue();

        Player player = PersonWorker.INSTANCE.getPlayerById(event.getUser().getId());
        logger.debug("{}: ИГРОК ID: {} НАЙДЕН",
                getName().toUpperCase(), player.getId());

        if (FightWorker.INSTANCE.personInFight(player)) {
            logger.info("{}: ИГРОК ID: {}, В БОЮ",
                    getName().toUpperCase(), player.getId());

            new MessageSender.MessageBuilder(event.getHook(), "Нельзя это использовать в бою")
                    .build().replyMessage();
            return;
        }

        String itemName = null;

        if (Objects.equals(event.getSubcommandName(), armorOptionName)) {
            if (isNull(event.getOption(armorOptionName))) {
                showArmor(event);
                return;
            } else {
                itemName = Objects.requireNonNull(event.getOption(armorOptionName)).getAsString();
            }
        } else if (Objects.equals(event.getSubcommandName(), weaponOptionName)) {
            if (isNull(event.getOption(weaponOptionName))) {
                showWeapon(event);
            } else {
                itemName = Objects.requireNonNull(event.getOption(weaponOptionName)).getAsString();
            }
        } else if (Objects.equals(event.getSubcommandName(), skillOptionName)) {
            if (isNull(event.getOption(skillOptionName))) {
                showSkill(event);
            } else {
                itemName = Objects.requireNonNull(event.getOption(skillOptionName)).getAsString();
            }
        } else if (Objects.equals(event.getSubcommandName(), itemOptionName)) {
            if (isNull(event.getOption(itemOptionName))) {
                showItems(event);
            } else {
                itemName = Objects.requireNonNull(event.getOption(itemOptionName)).getAsString();
            }
        } else {
            throw new IllegalArgumentException("Предмет не найден");
        }

        List<Item> items = new ArrayList<>();
        items.addAll(ironArmor);
        items.addAll(whiteIronArmor);
        items.addAll(weapons);
        items.addAll(shields);
        items.addAll(skills);

        String finalItemName = itemName;
        Optional<Item> findItem = items.stream().filter(item -> item.getName().equals(finalItemName)).findFirst();

        if (findItem.isPresent()) {

            Item item = findItem.get();

            if (item.getPrice() > player.getCoins()) {
                new MessageSender.MessageBuilder(event.getHook(),
                        "Недостаточно " + Parameters.COINS.getName())
                        .build().replyMessage();
                return;
            }

            if (item instanceof Skill skill) {

                if (player.getSkillBook().getSkills().size() == player.getSkillBook().getBookSize()) {
                    new MessageSender.MessageBuilder(event.getHook(),
                            "Книга навыков" + PersonWorker.INSTANCE.getPersonName(player) + " заполнена")
                            .build().replyMessage();
                    return;
                }
                for (Skill playerSkill : player.getSkillBook().getSkills()) {
                    if (playerSkill.equals(skill)) {
                        new MessageSender.MessageBuilder(event.getHook(),
                                PersonWorker.INSTANCE.getPersonName(player) + " уже знает этот навык")
                                .build().replyMessage();
                        return;
                    }
                }

                player.setCoins(player.getCoins() - skill.getPrice());
                player.getSkillBook().addSkill(skill);

            } else {

                if (player.getInventory().getBag().getItems().size() == player.getInventory().getBag().getSize()) {
                    new MessageSender.MessageBuilder(event.getHook(),
                            "Инвентарь " + PersonWorker.INSTANCE.getPersonName(player) + " заполнен")
                            .build().replyMessage();
                    return;
                }

                player.setCoins(player.getCoins() - item.getPrice());
                player.getInventory().getBag().addItem(item);

            }

            SaveAndLoad.INSTANCE.save(player);

            String title = String.format("""
                    %s приобрел %s
                    """, PersonWorker.INSTANCE.getPersonName(player), item.getName());
            String description = String.format("""
                            %s
                            """,
                    TextCreator.INSTANCE.getPlayerParameters(player));

            new MessageSender.MessageBuilder(event.getHook(), title)
                    .setDescription(description)
                    .build().replyMessage();

        }

    }

    private void showArmor(SlashCommandInteractionEvent event) {

        StringBuilder stringBuilder = new StringBuilder();
        List<Armor> allArmor = new ArrayList<>();
        allArmor.addAll(ironArmor);
        allArmor.addAll(whiteIronArmor);

        allArmor.forEach(armor -> stringBuilder.append(String.format("""
                        **%s** | %s %s | %s %s
                        """,
                armor.getName(),
                armor.getArmor(), Equipment.DEFEND.getName(),
                armor.getPrice(), Parameters.COINS.getName())));

        new MessageSender.MessageBuilder(event.getHook(), "Броня")
                .setDescription(stringBuilder.toString())
                .build().replyMessage();

    }

    private void showWeapon(SlashCommandInteractionEvent event) {

        StringBuilder stringBuilder = new StringBuilder();

        weapons.forEach(weapon -> stringBuilder.append(String.format("""
                        **%s** | %s %s | %s %s
                        """,
                weapon.getName(),
                weapon.getAttack(), Equipment.ATTACK.getName(),
                weapon.getPrice(), Parameters.COINS.getName())));
        shields.forEach(shield -> stringBuilder.append(String.format("""
                        **%s** | %s %s | %s %s
                        """,
                shield.getName(),
                shield.getArmor(), Equipment.DEFEND.getName(),
                shield.getPrice(), Parameters.COINS.getName())));

        new MessageSender.MessageBuilder(event.getHook(), "Оружие и щиты")
                .setDescription(stringBuilder.toString())
                .build().replyMessage();

    }

    private void showSkill(SlashCommandInteractionEvent event) {

        StringBuilder stringBuilder = new StringBuilder();

        skills.forEach(skill -> stringBuilder.append(String.format("""
                        **%s** | %s %s | %s %s
                        %s
                        """,
                skill.getName(),
                skill.getCoastInMana(), Parameters.MANA.getName(),
                skill.getPrice(), Parameters.COINS.getName(),
                skill.getSpellInfo())));

        new MessageSender.MessageBuilder(event.getHook(), "Магазин скиллов")
                .setDescription(stringBuilder.toString())
                .build().replyMessage();

    }

    private void showItems(SlashCommandInteractionEvent event) {

        StringBuilder stringBuilder = new StringBuilder();

        items.forEach(item -> {
            if (item instanceof Usable) {
                stringBuilder.append(String.format("""
                                **%s** | %s %s |
                                %s
                                """,
                        item.getName(), item.getPrice(), Parameters.COINS.getName(),
                        ((Usable) item).getInfo()));
            } else {
                stringBuilder.append(String.format("""
                                **%s** | %s %s |
                                """,
                        item.getName(), item.getPrice(), Parameters.COINS.getName()));
            }
        });

        new MessageSender.MessageBuilder(event.getHook(), "Магазин предметов")
                .setDescription(stringBuilder.toString())
                .build().replyMessage();

    }

    @Override
    public SlashCommandData getSlashCommandData() {

        SlashCommandData data = Commands.slash(getName(), getDescription());

        SubcommandData buyArmorSubcommand = new SubcommandData(armorOptionName, "купить броню");
        OptionData buyArmorOption = new OptionData(OptionType.STRING, armorOptionName, "выбери броню");
        ironArmor.forEach(armor -> buyArmorOption.addChoice(armor.getName(), armor.getName()));
        whiteIronArmor.forEach(armor -> buyArmorOption.addChoice(armor.getName(), armor.getName()));
        buyArmorSubcommand.addOptions(buyArmorOption);

        SubcommandData buyWeaponSubcommand = new SubcommandData(weaponOptionName, "купить оружие или щиты");
        OptionData buyWeaponOption = new OptionData(OptionType.STRING, weaponOptionName, "выбери оружие или щит");
        weapons.forEach(weapon -> buyWeaponOption.addChoice(weapon.getName(), weapon.getName()));
        shields.forEach(shield -> buyWeaponOption.addChoice(shield.getName(), shield.getName()));
        buyWeaponSubcommand.addOptions(buyWeaponOption);

        SubcommandData buySkillSubcommand = new SubcommandData(skillOptionName, "купить навык");
        OptionData buySkillOption = new OptionData(OptionType.STRING, skillOptionName, "выбери навык");
        skills.forEach(skill -> buySkillOption.addChoice(skill.getName(), skill.getName()));
        buySkillSubcommand.addOptions(buySkillOption);

        SubcommandData buyItemSubcommand = new SubcommandData(itemOptionName, "купить предмет");
        OptionData buyItemOption = new OptionData(OptionType.STRING, itemOptionName, "выбрать предмет");
        items.forEach(item -> buyItemOption.addChoice(item.getName(), item.getName()));
        buyItemSubcommand.addOptions(buyItemOption);

        data.addSubcommands(buyArmorSubcommand, buyWeaponSubcommand, buySkillSubcommand, buyItemSubcommand);
        return data;

    }

}
