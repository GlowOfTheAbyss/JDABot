package org.glow.discordBot.commands.rpgcommands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.glow.discordBot.action.fight.FightWorker;
import org.glow.discordBot.commands.SlashCommand;
import org.glow.discordBot.entities.PersonWorker;
import org.glow.discordBot.entities.Player;
import org.glow.discordBot.fileworkers.SaveAndLoad;
import org.glow.discordBot.message.MessageSender;
import org.glow.discordBot.message.Parameters;
import org.glow.discordBot.message.TextCreator;
import org.glow.discordBot.message.characteristic.Characteristic;
import org.glow.discordBot.message.characteristic.CharacteristicFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class PlayerLevelUpCommand implements SlashCommand {

    private final String optionName = "characteristic";
    private final List<Characteristic> characteristics = new ArrayList<>();

    public PlayerLevelUpCommand() {
        characteristics.add(CharacteristicFactory.INSTANCE.createStrength());
        characteristics.add(CharacteristicFactory.INSTANCE.createEndurance());
        characteristics.add(CharacteristicFactory.INSTANCE.createAgility());
        characteristics.add(CharacteristicFactory.INSTANCE.createIntelligence());
        characteristics.add(CharacteristicFactory.INSTANCE.createPerception());
        characteristics.add(CharacteristicFactory.INSTANCE.createLuck());
    }

    @Override
    public String getName() {
        return "levelup";
    }

    @Override
    public String getDescription() {
        return "повысить уровень характеристики";
    }

    @Override
    public void start(SlashCommandInteractionEvent event) {

        event.deferReply().queue();

        Player player = PersonWorker.INSTANCE.getPlayerById(event.getUser().getId());
        if (FightWorker.INSTANCE.personInFight(player)) {
            new MessageSender.MessageBuilder(event.getHook(), "Нельзя это использовать в бою")
                    .build().replyMessage();
            return;
        }
        List<Characteristic> playerCharacteristics = List.of(
                player.getStrength(),
                player.getEndurance(),
                player.getAgility(),
                player.getIntelligence(),
                player.getPerception(),
                player.getLuck()
        );

        String characteristicName = Objects.requireNonNull(event.getOption(optionName)).getAsString();
        Optional<Characteristic> optionalCharacteristic = playerCharacteristics.stream()
                .filter(characteristic -> characteristic.getName().equals(characteristicName))
                .findFirst();

        if (optionalCharacteristic.isPresent()) {
            Characteristic characteristic = optionalCharacteristic.get();

            int oneLevelCost = 600;
            int perLevelCost = 200;

            int price = oneLevelCost + (perLevelCost * characteristic.getValue());

            if (player.getCoins() < price) {
                String title = String.format("""
                        Недостаточно %s
                        """, Parameters.COINS.getName());
                String description = String.format("""
                        Цена: %s
                        Нехватает еще: %s
                        
                        %s
                        """, price,
                        price - player.getCoins(),
                        TextCreator.INSTANCE.getPlayerParameters(player));

                new MessageSender.MessageBuilder(event.getHook(), title)
                        .setDescription(description)
                        .build().replyMessage();
                return;
            }

            player.setCoins(player.getCoins() - price);
            characteristic.setValue(characteristic.getValue() + 1);
            SaveAndLoad.INSTANCE.save(player);

            String title = String.format("""
                            %s повысил %s
                            """, PersonWorker.INSTANCE.getPersonName(player), characteristic.getName());
            String description = String.format("""
                            %s
                            %s
                            """,
                    TextCreator.INSTANCE.getPlayerCharacteristic(player),
                    TextCreator.INSTANCE.getPlayerParameters(player));

            new MessageSender.MessageBuilder(event.getHook(), title)
                    .setDescription(description)
                    .build().replyMessage();

        }


    }

    @Override
    public SlashCommandData getSlashCommandData() {

        SlashCommandData data = Commands.slash(getName(), getDescription());
        OptionData optionData = new OptionData(OptionType.STRING, optionName, "выберите характеристику",
                true);
        characteristics.forEach(characteristic -> optionData
                .addChoice(characteristic.getName(), characteristic.getName()));

        data.addOptions(optionData);
        return data;
    }
}
