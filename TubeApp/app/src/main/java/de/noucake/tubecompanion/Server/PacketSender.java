package de.noucake.tubecompanion.Server;

import java.util.LinkedList;
import java.util.List;

public class PacketSender {

    private final TubeServer server;
    private final List<Packet> packets;

    public PacketSender(TubeServer server){
        this.server = server;

        packets = new LinkedList<>();
    }

    private void sendNext(){
        if(!server.isConnected() || !server.isLoggedIn())
            return;

        if(packets.size() > 0){
            Packet packet = packets.get(0);
            server.sendPacketDirect(packet.tag, packet.packet);
            packets.remove(packet);
        }
    }

    public void queuePacket(String tag, String packet){
        Packet p = new Packet();
        p.tag = tag;
        p.packet = packet;
        packets.add(p);
    }

    private class Packet{
        String tag;
        String packet;
    }

}
