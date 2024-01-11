package org.glow.discordBot.commands;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.glow.discordBot.commands.rpgcommands.*;
import org.glow.discordBot.commands.systemscommands.AddPlayerCommand;

import java.util.HashSet;
import java.util.Set;

public class CommandLogger {

    private final Set<SlashCommand> slashCommands;
    private final Set<AutoCompleteCommand> autoCompleteCommands;

    private final Guild guild;

    public CommandLogger(Guild guild) {

        this.guild = guild;
        slashCommands = new HashSet<>();
        autoCompleteCommands = new HashSet<>();

        initialization();

    }

    private void initialization() {

        Set<CommandData> commandData = new HashSet<>();
        // иниацилизируем команды и добавляем их в сеты

        /* rpg commands */

        EquipItemCommand equipItemCommand = new EquipItemCommand();
        slashCommands.add(equipItemCommand);
        autoCompleteCommands.add(equipItemCommand);
        commandData.add(equipItemCommand.getSlashCommandData());


        GiveCoinsCommand giveCoinsCommand = new GiveCoinsCommand();
        slashCommands.add(giveCoinsCommand);
        commandData.add(giveCoinsCommand.getSlashCommandData());


        GoOnAdventureCommand goOnAdventureCommand = new GoOnAdventureCommand();
        slashCommands.add(goOnAdventureCommand);
        commandData.add(goOnAdventureCommand.getSlashCommandData());


        GoOnMineCommand goOnMineCommand = new GoOnMineCommand();
        slashCommands.add(goOnMineCommand);
        commandData.add(goOnMineCommand.getSlashCommandData());


        PlayerBuyCommand playerBuyCommand = new PlayerBuyCommand();
        slashCommands.add(playerBuyCommand);
        commandData.add(playerBuyCommand.getSlashCommandData());


        PlayerLevelUpCommand playerLevelUpCommand = new PlayerLevelUpCommand();
        slashCommands.add(playerLevelUpCommand);
        commandData.add(playerLevelUpCommand.getSlashCommandData());


        PlayerStealCommand playerStealCommand = new PlayerStealCommand();
        slashCommands.add(playerStealCommand);
        commandData.add(playerStealCommand.getSlashCommandData());


        PlayInCasinoCommand playInCasinoCommand = new PlayInCasinoCommand();
        slashCommands.add(playInCasinoCommand);
        commandData.add(playInCasinoCommand.getSlashCommandData());


        SellItemCommand sellItemCommand = new SellItemCommand();
        slashCommands.add(sellItemCommand);
        autoCompleteCommands.add(sellItemCommand);
        commandData.add(sellItemCommand.getSlashCommandData());


        ShowPlayerInventoryCommand showPlayerInventoryCommand = new ShowPlayerInventoryCommand();
        slashCommands.add(showPlayerInventoryCommand);
        commandData.add(showPlayerInventoryCommand.getSlashCommandData());


        ShowPlayerCharacteristicCommand showPlayerCharacteristicCommand = new ShowPlayerCharacteristicCommand();
        slashCommands.add(showPlayerCharacteristicCommand);
        commandData.add(showPlayerCharacteristicCommand.getSlashCommandData());


        ShowPlayerSkillBookCommand showPlayerSkillBookCommand = new ShowPlayerSkillBookCommand();
        slashCommands.add(showPlayerSkillBookCommand);
        commandData.add(showPlayerSkillBookCommand.getSlashCommandData());


        UnequipItemCommand unequipItemCommand = new UnequipItemCommand();
        slashCommands.add(unequipItemCommand);
        autoCompleteCommands.add(unequipItemCommand);
        commandData.add(unequipItemCommand.getSlashCommandData());


        UseSkillCommand useSkillCommand = new UseSkillCommand();
        slashCommands.add(useSkillCommand);
        autoCompleteCommands.add(useSkillCommand);
        commandData.add(useSkillCommand.getSlashCommandData());

        /* systems commands */

        AddPlayerCommand addPlayerCommand = new AddPlayerCommand();
        slashCommands.add(addPlayerCommand);
        commandData.add(addPlayerCommand.getSlashCommandData());

        // добавляем все команды в память сервера
        guild.updateCommands().addCommands(commandData).queue();

    }

    public Set<SlashCommand> getSlashCommands() {
        return slashCommands;
    }

    public Set<AutoCompleteCommand> getAutoCompleteCommands() {
        return autoCompleteCommands;
    }
}
