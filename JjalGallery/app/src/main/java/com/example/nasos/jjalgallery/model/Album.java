package com.example.nasos.jjalgallery.model;

/**
 * Created by nasos on 2017-08-08.
 */

public class Album {
    private String imgPath;
    private String albumID;
    private String albumName;


    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getAlbumID() {
        return albumID;
    }

    public void setAlbumID(String albumID) {
        this.albumID = albumID;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public Album(String imgPath, String albumID, String albumName) {

        this.imgPath = imgPath;
        this.albumID = albumID;
        this.albumName = albumName;
    }
}
