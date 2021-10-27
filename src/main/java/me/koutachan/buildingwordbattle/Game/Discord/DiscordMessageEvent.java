package me.koutachan.buildingwordbattle.Game.Discord;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class DiscordMessageEvent extends Event {

    private static HandlerList handlerList = new HandlerList();

    private String TextChannelId;
    private String TextMessageId;
    private String Message;
    private String MemberId;

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList(){
        return handlerList;
    }


    public static void setHandlerList(HandlerList handlerList) {
        DiscordMessageEvent.handlerList = handlerList;
    }

    public String getTextChannelId() {
        return TextChannelId;
    }

    public void setTextChannelId(String textChannelId) {
        TextChannelId = textChannelId;
    }

    public String getTextMessageId() {
        return TextMessageId;
    }

    public void setTextMessageId(String textMessageId) {
        TextMessageId = textMessageId;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getMemberId() {
        return MemberId;
    }

    public void setMemberId(String memberId) {
        MemberId = memberId;
    }
}
