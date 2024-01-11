package org.glow.discordBot.message;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.interactions.InteractionHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.util.Objects.nonNull;

public class MessageSender {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final EmbedBuilder embedBuilder = new EmbedBuilder();
    private final InteractionHook hook;
    private final MessageChannel channel;

    private final String title;

    private final String description;
    private final String image;

    private MessageSender(MessageBuilder builder) {
        this.hook = builder.hook;
        this.channel = builder.channel;
        this.title = builder.title;
        this.description = builder.description;
        this.image = builder.image;
    }

    public static class MessageBuilder {
        private InteractionHook hook;
        private MessageChannel channel;

        private final String title;

        private String description;
        private String image;

        public MessageBuilder(MessageChannel channel, String title) {
            this.channel = channel;
            this.title = title;
        }

        public MessageBuilder(InteractionHook hook, String title) {
            this.hook = hook;
            this.title = title;
        }

        public MessageBuilder setDescription(String description) {
            this.description = description;
            return this;
        }

        public MessageBuilder setImage(String image) {
            this.image = image;
            return this;
        }

        public MessageSender build() {
            return new MessageSender(this);
        }

    }

    public void replyMessage() {

        embedBuilder.setTitle(title);
        embedBuilder.setDescription(description);
        embedBuilder.setImage(image);

        if (nonNull(hook)) {

            logger.debug("{}: ОТПРАВКА СООБЩЕНИЯ С ПОМОЩЬЮ HOOK",
                    this.getClass().getSimpleName().toUpperCase());

            hook.sendMessageEmbeds(embedBuilder.build()).queue();
        } else if (nonNull(channel)) {

            logger.debug("{}: ОТПРАВКА СООБЩЕНИЯ С ПОМОЩЬЮ CHANNEL",
                    this.getClass().getSimpleName().toUpperCase());

            channel.sendMessageEmbeds(embedBuilder.build()).queue();
        } else {

            logger.warn("{}: ОШИБКА ОТПРАВКИ СООБЩЕНИЯ",
                    this.getClass().getSimpleName().toUpperCase());

            throw new IllegalArgumentException("Ошибка отправки сообщения");
        }

    }

}
