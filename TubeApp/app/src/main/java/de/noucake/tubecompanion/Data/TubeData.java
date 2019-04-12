package de.noucake.tubecompanion.Data;

import android.graphics.Bitmap;

public class TubeData {

    private String title;
    private String id;
    private String author;
    private Bitmap image;

    private int imagesize;
    private int audiosize;

    private boolean downloading = false;
    private boolean complete = false;

    public TubeData(String id, String title, String author){
        this.id = id;
        this.title = title;
        this.author = author;
    }

    public TubeData(String id){
        this.id = id;
    }

    public int getDownloadProgress(){
        if(!downloading){
            return 0;
        }
        return 100;
    }

    public boolean isComplete(){
        return complete;
    }
    public void setComplete(boolean complete) {
        this.complete = complete;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
    public void setAudiosize(int audiosize) {
        this.audiosize = audiosize;
    }
    public void setImagesize(int imagesize) {
        this.imagesize = imagesize;
    }
    public void setImage(Bitmap image) {
        this.image = image;
    }

    public int getAudiosize() {
        return audiosize;
    }
    public int getImagesize() {
        return imagesize;
    }
    public Bitmap getImage() {
        return image;
    }
    public String getAuthor() {
        return author;
    }
    public String getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
}
