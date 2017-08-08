package com.example.nasos.jjalgallery.model;

import java.io.Serializable;

/**
 * Created by nasos on 2017-08-09.
 */

public class Images implements Serializable {
    private  static final long serialVersionUID = 1L;

    private String thumbnailImgPath;
    private String imgPath;
    private String imgID;

    public Images(String thumbnailImgPath, String imgPath, String imgID) {
        this.thumbnailImgPath = thumbnailImgPath;
        this.imgPath = imgPath;
        this.imgID = imgID;
    }

    public String getThumbnailImgPath() {
        return thumbnailImgPath;
    }

    public void setThumbnailImgPath(String thumbnailImgPath) {
        this.thumbnailImgPath = thumbnailImgPath;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getImgID() {
        return imgID;
    }

    public void setImgID(String imgID) {
        this.imgID = imgID;
    }
}
