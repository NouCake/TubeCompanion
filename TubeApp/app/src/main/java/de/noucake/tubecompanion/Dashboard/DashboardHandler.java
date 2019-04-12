package de.noucake.tubecompanion.Dashboard;

import android.os.Handler;
import android.os.Message;

import java.util.LinkedList;
import java.util.List;

import de.noucake.tubecompanion.Data.TubeData;

public class DashboardHandler extends Handler {

    private static final int ADD_DATA = 100;
    private static final int REMOVE_DATA = 110;
    private static final int REMOVE_ITEM = 111;
    private static final int REMOVE_ALL = 120;
    private static final int REORDER = 130;
    private static final int UPDATE = 140;


    private final DashboardActivity dashboard;
    private final List<HandlerCommand> queue;


    DashboardHandler(DashboardActivity dashboard){
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
    void enqueueRemove(DashboardItem item){
        synchronized (queue){
            queue.add(new HandlerCommand(REMOVE_ITEM, item));
        }
        sendEmptyMessage(0);
    }
    void enqueueRemoveAll(){
        synchronized (queue){
            queue.add(new HandlerCommand(REMOVE_ALL));
        }
        sendEmptyMessage(0);

    }
    void enqueueReorder() {
        synchronized (queue){
            queue.add(new HandlerCommand(REORDER));
        }
        sendEmptyMessage(0);
    }
    void enqueueUpdate(){
        synchronized (queue){
            queue.add(new HandlerCommand(UPDATE));
        }
        sendEmptyMessage(0);
    }

    private void doCommand(HandlerCommand command){
        switch (command.type){
            case ADD_DATA:
                doAdd(command);
                break;
            case REMOVE_DATA:
                doRemoveData(command);
                break;
            case REMOVE_ITEM:
                doRemoveItem(command);
                break;
            case REMOVE_ALL:
                doRemoveAll(command);
                break;
            case REORDER:
                doReorder(command);
                break;
            case UPDATE:
                doUpdate(command);
                break;
        }
    }
    private void doAdd(HandlerCommand command){
        dashboard.getView().addTubeDataAsHandler(command.data);
    }
    private void doRemoveData(HandlerCommand command){
        dashboard.getView().removeTubeDataAsHandler(command.data);
    }
    private void doRemoveItem(HandlerCommand command){
        dashboard.getView().removeItemAsHandler(command.item);
    }
    private void doRemoveAll(HandlerCommand command){
        dashboard.getView().removeAllAsHandler();
    }
    private void doReorder(HandlerCommand command){
        dashboard.getView().reorderItemsAsHandler();
    }
    private void doUpdate(HandlerCommand command){
        dashboard.getView().updateAsHandler();
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
        private DashboardItem item;

        public HandlerCommand(int type, TubeData data){
            this(type);
            this.data = data;
        }

        public HandlerCommand(int type, DashboardItem item){
            this(type);
            this.item = item;
        }

        public HandlerCommand(int type){
            this.type = type;
        }


    }
}
