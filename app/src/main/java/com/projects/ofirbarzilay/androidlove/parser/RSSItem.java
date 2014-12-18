package com.projects.ofirbarzilay.androidlove.parser;

import android.graphics.Bitmap;

/**
 * Created by Ofir.Barzilay on 17/12/2014.
 */
public class RSSItem {
    private String title = null;
    private String description = null;
    private String date = null;
    private String link;
    private Bitmap image;

    public RSSItem() {

    }

    public Bitmap getImage() {
        return image;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    public String getLink() {
        return link;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public boolean isReady() {
        return (image != null && date !=null && title !=null && description != null);
    }
}
