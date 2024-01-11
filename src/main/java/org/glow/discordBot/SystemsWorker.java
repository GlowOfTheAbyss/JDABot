package org.glow.discordBot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.glow.discordBot.fileworkers.SaveAndLoad;
import org.glow.discordBot.fileworkers.TokenFinder;
import org.glow.discordBot.timeactions.TimeAction;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SystemsWorker {

    private final JDA jda;

    public SystemsWorker() {

        // создаем менеджер потоков
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1, r -> {
            Thread thread = new Thread(r);
            thread.setDaemon(true);
            return thread;
        });

        // запуск отдельного потока для смены состояния по времени
        executorService.scheduleAtFixedRate(TimeAction.getInstance(), 0, 30, TimeUnit.MINUTES);

        // задаем активность для бота
        Activity activity = Activity.watching("за тобой");

        /*
        * создаем JDA исользуя токен
        * включает боту кеширование пользователей
        * бот проверяет входящих и ливнувших пользователей
        * добовляем слушателя команд
        * устанавливаем активность
        * */
        jda = JDABuilder.createDefault(new TokenFinder().findToken())
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .enableIntents(GatewayIntent.GUILD_MEMBERS)
                .addEventListeners(new Listener())
                .setActivity(activity)
                .build();

        // загрузка сохраненных игроков
        SaveAndLoad.INSTANCE.load();

    }

    public JDA getJda() {
        return jda;
    }
}
