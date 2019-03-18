package de.noucake.tubecompanion.Server;

public class TubeTypes {

    private TubeTypes(){

    }

    //Packet types
    public final static int LOGIN =                     0;
    public final static int LOGIN_RESPONSE =            1;
    public final static int META_DATA =                 2;
    public final static int PENDING_DOWNLOAD_REQUEST =  4;
    public final static int REQUEST =                   6;
    public final static int FILE =                      7;

    //Login types
    public static final int LOGIN_DEVICE =              100;
    public static final int LOGIN_CLIENT =              101;
    public static final int LOGIN_SUCCESS =             102;
    public static final int LOGIN_FAILED =              103;

    //FILE TYPES
    public static final int FILE_IMAGE =                300;
    public static final int FILE_AUDIO =                301;

    //Request Types
    //public static final int REQUEST_PENDING =           200;
    //public static final int REQUEST_META =              201;
    //public static final int REQUEST_COVER =             202;
    //public static final int REQUEST_AUDIO =             203;

    //MISC
    public static final int DEFAULT_VALUE =             -8555; // magical random number
    public static final int BYTE_SIZE =                 1024;
}
