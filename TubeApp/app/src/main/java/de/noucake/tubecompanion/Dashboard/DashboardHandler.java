package de.noucake.tubecompanion.Dashboard;

import android.os.Handler;
import android.os.Message;

import java.util.LinkedList;
import java.util.List;

import de.noucake.tubecompanion.Data.TubeData;

public class DashboardHandler extends Handler {

    private static final int ADD_DATA = 100;
    private static final int REMOVE_DATA = 110;
    private static final int REMOVE_ALL = 120;


    private DashboardActivity dashboard;
    private List<HandlerCommand> queue;


    public DashboardHandler(DashboardActivity dashboard){
        this.dashboard = dashboard;
        queue = new LinkedList<>();
    }

    void enqueueAdd(TubeData data){
        synchronized (queue){
            queue.add(new HandlerCommand(ADD_DATA, data));
        }
        sendEmptyMessage(0);
    }

    void enqueueRemove(TubeData data){
        synchronized (queue){
            queue.add(new HandlerCommand(REMOVE_DATA, data));
        }
        sendEmptyMessage(0);
    }

    void enqueueRemoveAll(){
        synchronized (queue){
            queue.add(new HandlerCommand(REMOVE_ALL));
        }
        sendEmptyMessage(0);

    }

    private void doAdd(HandlerCommand command){
        dashboard.getView().addTubeDataFromHandler(command.data);
    }

    private void doRemove(HandlerCommand command){
        dashboard.getView().removeTubeDataFromHandler(command.data);
    }

    private void doRemoveAll(HandlerCommand command){
        dashboard.getView().removeAllFromHandler();
    }

    private void doCommand(HandlerCommand command){
        switch (command.type){
            case ADD_DATA:
                doAdd(command);
                break;
            case REMOVE_DATA:
                doRemove(command);
                break;
            case REMOVE_ALL:
                doRemoveAll(command);
                break;
        }
    }

    @Override
    public void handleMessage(Message msg) {
        if(queue.size() > 0){
            HandlerCommand command;
            synchronized (queue){
                command = queue.get(0);
                queue.remove(command);
            }
            doCommand(command);
        }
    }

    private class HandlerCommand{
        private int type;
        private TubeData data;

        public HandlerCommand(int type, TubeData data){
            this(type);
            this.data = data;
        }

        public HandlerCommand(int type){
            this.type = type;
        }
    }
}