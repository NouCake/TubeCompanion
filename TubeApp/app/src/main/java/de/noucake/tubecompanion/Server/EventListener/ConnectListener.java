package de.noucake.tubecompanion.Server.EventListener;

import com.github.nkzawa.emitter.Emitter;

import de.noucake.tubecompanion.Server.TubeServer;

public class ConnectListener implements Emitter.Listener {

    TubeServer server;

    public ConnectListener(TubeServer server){
        this.server = server;
    }

    @Override
    public void call(Object... args) {
        server.onConnected();
    }

}
