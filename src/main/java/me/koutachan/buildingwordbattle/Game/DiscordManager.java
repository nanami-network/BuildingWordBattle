package me.koutachan.buildingwordbattle.Game;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

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


}
