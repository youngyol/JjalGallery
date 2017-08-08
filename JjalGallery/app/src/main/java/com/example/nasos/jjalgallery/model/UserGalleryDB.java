package com.example.nasos.jjalgallery.model;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

/**
 * Created by nasos on 2017-08-07.
 */

public class UserGalleryDB  {
    private static final Uri images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    private static final String BUCKET_GROUP_BY = "1) GROUP BY 1,(2";
    private static final String BUCKET_ORDER_BY = "MAX(datetaken) DESC";

    private Cursor imageCursor;
    private Context ctx;

    public UserGalleryDB(Context ctx) {
        super();
        this.ctx = ctx;
    }

    private Cursor getAlbumList() {
        String[] PROJECTION_BUCKET = {
                MediaStore.Images.ImageColumns.BUCKET_ID,
                MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
                MediaStore.Images.ImageColumns.DATE_TAKEN,
                MediaStore.Images.ImageColumns.DATA};
        imageCursor = ctx.getContentResolver().query(
                images, // 이미지 컨텐트 테이블
                PROJECTION_BUCKET, // DATA, _ID를 출력
                BUCKET_GROUP_BY,       // 모든 개체 출력
                null,
                BUCKET_ORDER_BY);      // 정렬 안 함
        return imageCursor;
    }
}
