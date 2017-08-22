package org.horaapps.leafpic.data;

import java.io.Serializable;

/**
 * Created by nasos on 2017-08-18.
 */

public class Jjal implements Serializable {
    public String firebaseKey;
    public String url;
    public int click;

    public String getFirebaseKey() {
        return firebaseKey;
    }

    public void setFirebaseKey(String firebaseKey) {
        this.firebaseKey = firebaseKey;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getClick() {
        return click;
    }

    public void setClick(int click) {
        this.click = click;
    }
}
