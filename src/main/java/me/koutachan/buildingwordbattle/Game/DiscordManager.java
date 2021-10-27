package me.koutachan.buildingwordbattle.Game;

import me.koutachan.buildingwordbattle.Game.Discord.DiscordMessageEvent;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class DiscordManager extends ListenerAdapter {

    private JDA jda = null;

    public DiscordManager(String token) throws Exception{
        JDABuilder builder = JDABuilder.createDefault(token);
        builder.addEventListeners(this);
        jda = builder.build();
    }

    public void sendMessage(String TextChannelId, String Message) throws Exception{

        TextChannel channel = jda.getTextChannelById(TextChannelId);

        if (channel != null){
            channel.sendMessage(Message).queue();
        }
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {

        new Thread(()->{
            Plugin plugin = Bukkit.getPluginManager().getPlugin("BuildingWordBattle");

            Bukkit.getScheduler().runTask(plugin, ()->{
                DiscordMessageEvent e = new DiscordMessageEvent();

                e.setTextChannelId(event.getTextChannel().getId());
                e.setTextMessageId(event.getMessageId());
                e.setMessage(event.getMessage().getContentDisplay());
                e.setMemberId(event.getMember().getId());

                plugin.getServer().getPluginManager().callEvent(e);
            });
        }).start();

    }
}
