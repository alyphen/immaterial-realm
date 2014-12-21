package io.github.alyphen.amethyst.common.packet.clientbound.chat;

import io.github.alyphen.amethyst.common.chat.ChatChannel;
import io.github.alyphen.amethyst.common.packet.Packet;
import io.github.alyphen.amethyst.common.player.Player;

public class PacketClientboundGlobalChatMessage extends Packet {

    private long playerId;
    private String channel;
    private String message;

    public PacketClientboundGlobalChatMessage(Player player, String channel, String message) {
        this.playerId = player.getId();
        this.channel = channel;
        this.message = message;
    }

    public long getPlayerId() {
        return playerId;
    }

    public String getChannel() {
        return channel;
    }

    public String getMessage() {
        return message;
    }

}
