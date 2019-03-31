package de.noucake.tubecompanion.Server.EventListener;

import com.github.nkzawa.emitter.Emitter;

import de.noucake.tubecompanion.Server.TubeServerHandler;

public class ConnectListener implements Emitter.Listener {

    TubeServerHandler server;

    public ConnectListener(TubeServerHandler server){
        this.server = server;
    }

    @Override
    public void call(Object... args) {
        server.onConnected();
    }

}
