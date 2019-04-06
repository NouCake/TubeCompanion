package de.noucake.tubecompanion.Data;

import android.content.Context;
import android.content.SharedPreferences;

public class DataLoader {

    private static final String PREF = "TUBECOMPANION_DATA";
    private static SharedPreferences SP;

    /**
     * Saves login data in a SharesPreferences Object
     * @param context context for creating SharesPreferences Object
     * @param username
     * @param password
     * @param remember
     */
    public static void saveLoginData(Context context, String username, String password, boolean remember){
        getSP(context);

        SharedPreferences.Editor editor = SP.edit();
        editor.putBoolean("LOGIN_REMEMBER", remember);
        editor.putString("LOGIN_USER", username);
        editor.putString("LOGIN_PASSWORD", password);
        editor.apply();
    }

    /**
     * Loads previously saved login data
     * if no data is saved returns null
     * returns Array with [username, password]
     * @param context
     * @return
     */
    public static String[] loadLoginData(Context context){
        getSP(context);

        if(SP.getBoolean("LOGIN_REMEMBER", false)){
            String[] data = new String[2];
            data[0] = SP.getString("LOGIN_USER", null);
            data[1] = SP.getString("LOGIN_PASSWORD", null);
            return data;
        }
        return null;
    }

    public static boolean hasLoginData(Context context){
        getSP(context);

        return SP.getBoolean("LOGIN_REMEMBER", false);
    }

    public static String loadHostAdress(Context context){
        getSP(context);
        return SP.getString("HOST", null);
    }

    public static void saveHostAdress(Context context, String host){
        getSP(context);

        SharedPreferences.Editor editor = SP.edit();
        editor.putString("HOST", host);
        editor.apply();
    }

    private static void getSP(Context context){
        if(SP == null){
            SP = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        }
    }

}
