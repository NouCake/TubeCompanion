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

    private boolean hasMeta = false;
    private boolean hasImage = false;
    private boolean hasAudio = false;
    private boolean dirty = false;

    public TubeData(String id, String title, String author){
        this.id = id;
        this.title = title;
        this.author = author;
    }
    public TubeData(String id){
        this.id = id;

        dirty = true;
    }

    public int getDownloadProgress(){
        if(!downloading){
            int prog = 0;
            if(hasMeta) prog += 33;
            if(hasImage) prog += 33;
            if(hasAudio) prog += 33;
            return prog;
        }
        return 100;
    }

    public void setTitle(String title) {
        this.title = title;
        dirty = true;
    }
    public void setAuthor(String author) {
        this.author = author;
        dirty = true;
    }
    public void setAudiosize(int audiosize) {
        this.audiosize = audiosize;
        dirty = true;
    }

    public void setDownloading(boolean downloading) {
        this.downloading = downloading;
    }

    public void setImage(Bitmap image, int imagesize) {
        this.image = image;
        this.imagesize = imagesize;
        hasImage = true;
        dirty = true;
        checkComplete();
    }
    public void setMeta(String title, String author){
        this.title = title;
        this.author = author;
        this.hasMeta = true;
        dirty = true;
        checkComplete();
    }
    public void setAudio(int audiosize){
        this.audiosize = audiosize;
        this.hasAudio = true;
        dirty = true;
        checkComplete();
    }

    private void checkComplete(){
        if(hasMeta && hasImage && hasAudio){
            complete = true;
            downloading = false;
        }
    }

    public boolean hasImage(){
        return hasImage;
    }
    public boolean hasMeta(){
        return hasMeta;
    }
    public boolean isHasAudio(){
        return hasAudio;
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
    public boolean isComplete(){
        return complete;
    }

    public boolean isDirty() {
        return dirty;
    }
    public void clearDirty() {
        dirty = true;
    }

    public int getImagesize() {
        return imagesize;
    }
    public int getAudiosize() {
        return audiosize;
    }
}


