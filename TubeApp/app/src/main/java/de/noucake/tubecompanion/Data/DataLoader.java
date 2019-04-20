package de.noucake.tubecompanion.Data;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class DataLoader {

    private static final String PREF = "TUBECOMPANION_DATA";
    private static SharedPreferences SP;
    private static void getSP(Context context){
        if(SP == null){
            SP = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        }
    }

    public static boolean hasLoginData(Context context){
        getSP(context);

        return SP.getBoolean("LOGIN_REMEMBER", false);
    }
    /**
     * Loads previously saved login data
     * if no data is saved returns null
     * returns Array with [username, password]
     * @param context
     * @return
     */ public static String[] loadLoginData(Context context){
        getSP(context);

        if(SP.getBoolean("LOGIN_REMEMBER", false)){
            String[] data = new String[2];
            data[0] = SP.getString("LOGIN_USER", null);
            data[1] = SP.getString("LOGIN_PASSWORD", null);
            return data;
        }
        return null;
    }
    /**
     * Saves login data in a SharesPreferences Object
     * @param context context for creating SharesPreferences Object
     * @param username
     * @param password
     * @param remember
     */ public static void saveLoginData(Context context, String username, String password, boolean remember){
        getSP(context);

        SharedPreferences.Editor editor = SP.edit();
        editor.putBoolean("LOGIN_REMEMBER", remember);
        editor.putString("LOGIN_USER", username);
        editor.putString("LOGIN_PASSWORD", password);
        editor.apply();
    }

    public static void saveHostAdress(Context context, String host){
        getSP(context);

        SharedPreferences.Editor editor = SP.edit();
        editor.putString("HOST", host);
        editor.apply();
    }
    public static String loadHostAdress(Context context){
        getSP(context);
        return SP.getString("HOST", null);
    }

    private static final String DATA_COUNT = "DATA_COUNT";
    private static final String DATA_POS = "DATA_";
    public static void saveTubeData(Context context, List<TubeData> data){
        getSP(context);

        SharedPreferences.Editor editor = SP.edit();
        editor.putInt(DATA_COUNT, data.size());

        Log.d("TubeCompanion-D", "Störing " + data.size() + " Data");

        int index = 0;
        for(TubeData d : data){
            editor.putString(DATA_POS + index, d.getId());
            if(d.isDirty()){
                saveSingleData(context, editor, d);
            }
            index++;
        }
        editor.apply();


        Log.d("TubeCompanion-D", "Stored " + SP.getInt(DATA_COUNT, -1) + " Data");
    }

    public static List<TubeData> loadTubeData(Context context){
        getSP(context);

        List<TubeData> data = new LinkedList<>();
        int count = SP.getInt(DATA_COUNT, -1);
        Log.d("TubeCompanion-D", "Loading " + count + " Data");
        if(count <= 0){
            return data;
        }

        for(int i = 0; i < count; i++){
            String id = SP.getString(DATA_POS + i, "");

            if(id.length() == 0){
                Log.d("TubeCompanion-D", "ID has length 0");
                continue;
            }

            if(!SP.getBoolean(DATA_POS + id, false)){
                Log.d("TubeCompanion-D", DATA_POS + id + "    IS FALSE");
                continue; //does this continue with the next itteration?, I could google that but that would be too easy
            }

            TubeData d = loadSingleData(context, id);
            data.add(d);
        }
        Log.d("TubeCompanion-D", "Löaded " + count + " Data");

        return data;
    }

    private static void saveSingleData(Context context, SharedPreferences.Editor e, TubeData data){
        String key = DATA_POS + data.getId();
        Log.d("TubeCompanion-D", "Saving on " + key);

        e.putBoolean(key, true);
        if(data.hasMeta()){
            Log.d("TubeCompanion-D", "Saving on " + key + "_META");
            e.putBoolean(key + "_META", true);
            e.putString(key + "_META_TITLE", data.getTitle());
            e.putString(key + "_META_AUTHOR", data.getAuthor());
        }
        if(data.hasImage()){
            if(saveBitmap(context, data.getImage(), key+"_IMAGE_FILE")){
                e.putBoolean(key + "_IMAGE", true);
                e.putInt(key + "_IMAGE_SIZE", data.getImagesize());
            }
        }
        if(data.isHasAudio()){
            e.putBoolean(key + "_AUDIO", true);
            e.putInt(key + "_AUDIO_AUDIOSIZE", data.getAudiosize());
            //TODO save an audio file??
        }
    }

    private static TubeData loadSingleData(Context context, String id){

        String key = DATA_POS + id;

        TubeData data = new TubeData(id);

        if(SP.getBoolean(key + "_META", false)){
            Log.d("TubeCompanion-D", "Loading on " + key + "_META");
            String title = SP.getString(key + "_META_TITLE", "ERROR");
            String author = SP.getString(key + "_META_AUTHOR", "ERROR");
            data.setMeta(title, author);
        }

        if(SP.getBoolean(key + "_IMAGE", false)){
            int imagesize = SP.getInt(key + "_IMAGE_SIZE", -1);
            Bitmap bmp = loadBitmap(context, key + "_IMAGE_FILE");
            data.setImage(bmp, imagesize);
        }


        if(SP.getBoolean(key + "_AUDIO", false)){
            int audiosize = SP.getInt(key + "_AUDIO_SIZE", -1);
            //TODO load an audio file?? - i think not, but maybe check if exist
            data.setAudio(audiosize);
        }

        data.clearDirty();
        return data;
    }

    private static boolean saveBitmap(Context context, Bitmap bmp, String filename){
        try {
            FileOutputStream fs = context.openFileOutput(filename, Context.MODE_PRIVATE);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, fs);
            fs.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return  true;
    }

    private static Bitmap loadBitmap(Context context, String filename){
        try {
            FileInputStream in = context.openFileInput(filename);
            Bitmap bmp = BitmapFactory.decodeStream(in);
            in.close();
            return bmp;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /*
    *
    *
    //has to be called private so SP has be exist!!
    private static void saveSingleData(TubeData data, int index){
         assert SP != null;

        String key = DATA_POS + index;
        SharedPreferences.Editor e = SP.edit();

        e.putString(key + "_ID", data.getId());
        if(data.hasMeta()){
            e.putBoolean(key + "_META", true);
            e.putString(key + "_TITLE", data.getTitle());
            e.putString(key + "_AUTHOR", data.getAuthor());
        }

        if(data.hasImage()){
            e.putBoolean(key + "_IMAGE", true);
            e.putInt(key + "_IMAGESIZE", data.getImagesize());
            FileOutputStream out =
        }

        if(data.isHasAudio()){
            e.putBoolean(key + "_AUDIO", true);
            e.putInt(key + "_AUDIOSIZE", data.getAudiosize());
        }

    }
    *
    * */

}
