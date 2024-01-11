package org.glow.discordBot.commands.rpgcommands;

import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import org.glow.discordBot.commands.AutoCompleteCommand;
import org.glow.discordBot.commands.SlashCommand;
import org.glow.discordBot.entities.PersonWorker;
import org.glow.discordBot.entities.Player;
import org.glow.discordBot.items.skill.BattleSkill;
import org.glow.discordBot.items.skill.PeacefulSkill;
import org.glow.discordBot.items.skill.Skill;
import org.glow.discordBot.message.MessageSender;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.util.Objects.isNull;

public class UseSkillCommand implements SlashCommand, AutoCompleteCommand {

    private final String peacefulOptionName = "peaceful";

    private final String playerOptionName = "player";
    private final String battleOptionName = "battle";

    @Override
    public void autoComplete(CommandAutoCompleteInteractionEvent event) {

        Player player = PersonWorker.INSTANCE.getPlayerById(event.getUser().getId());
        List<Skill> skills = player.getSkillBook().getSkills();

        if (event.getFocusedOption().getName().equals(peacefulOptionName)) {
            List<Command.Choice> option = skills.stream()
                    .filter(skill -> skill instanceof PeacefulSkill)
                    .map(skill -> new Command.Choice(skill.getName(), skill.getName()))
                    .toList();
            event.replyChoices(option).queue();
        }
        if (event.getFocusedOption().getName().equals(battleOptionName)) {
            List<Command.Choice> option = player.getSkillBook().getSkills().stream()
                    .filter(skill -> skill instanceof BattleSkill)
                    .map(skill -> new Command.Choice(skill.getName(), skill.getName()))
                    .toList();
            event.replyChoices(option).queue();
        }

    }

    @Override
    public String getName() {
        return "cast";
    }

    @Override
    public String getDescription() {
        return "использовать скилл";
    }

    @Override
    public void start(SlashCommandInteractionEvent event) {

        event.deferReply().queue();
        Player player = PersonWorker.INSTANCE.getPlayerById(event.getUser().getId());
        String skillName;
        Player targetPlayer = null;

        if (Objects.equals(event.getSubcommandName(), peacefulOptionName)) {

            skillName = Objects.requireNonNull(event.getOption(peacefulOptionName)).getAsString();
            targetPlayer = PersonWorker.INSTANCE.getPlayerById(
                    Objects.requireNonNull(event.getOption(playerOptionName)).getAsUser().getId());

        } else if (Objects.equals(event.getSubcommandName(), battleOptionName)) {
            skillName = Objects.requireNonNull(event.getOption(battleOptionName)).getAsString();
        } else {
            throw new IllegalArgumentException("Скилл не найден");
        }

        Optional<Skill> findSkill = player.getSkillBook().getSkills().stream()
                .filter(skill -> skill.getName().equals(skillName)).findFirst();

        if (findSkill.isPresent()) {

            Skill skill = findSkill.get();

            if (player.getMana() < skill.getCoastInMana()) {
                new MessageSender.MessageBuilder(event.getHook(), "Недостаточно маны")
                        .build().replyMessage();
                return;
            }

            if (skill instanceof PeacefulSkill) {

                if (isNull(targetPlayer)) {
                    new MessageSender.MessageBuilder(event.getHook(), "Цель заклинания не найдена")
                            .build().replyMessage();
                    return;
                } else {
                    ((PeacefulSkill) skill).setTargetPlayer(targetPlayer);
                }

            }

            skill.cast(event, player);

        }

    }

    @Override
    public SlashCommandData getSlashCommandData() {

        SlashCommandData data = Commands.slash(getName(), getDescription());

        SubcommandData castPeacefulData = new SubcommandData(peacefulOptionName,
                "выберете мирное заклинание");
        OptionData castPeacefulOption = new OptionData(OptionType.STRING, peacefulOptionName,
                "выбрать заклинание", true, true);
        OptionData targetPlayerOption = new OptionData(OptionType.USER, playerOptionName,
                "выберете цель заклинания", true);
        castPeacefulData.addOptions(castPeacefulOption, targetPlayerOption);

        SubcommandData castBattleData = new SubcommandData(battleOptionName,
                "выберете боевое заклинание");
        OptionData castBattleOption = new OptionData(OptionType.STRING, battleOptionName,
                "выбрать заклинание", true, true);
        castBattleData.addOptions(castBattleOption);

        data.addSubcommands(castPeacefulData, castBattleData);

        return data;
    }
}
