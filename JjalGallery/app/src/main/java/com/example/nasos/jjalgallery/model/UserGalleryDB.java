package com.example.nasos.jjalgallery.model;

import android.content.ContentResolver;
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

    public Cursor getAlbumList() {
        String[] PROJECTION_BUCKET = {
                MediaStore.Images.ImageColumns.BUCKET_ID,
                MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
                MediaStore.Images.ImageColumns.DATE_TAKEN,
                MediaStore.Images.ImageColumns.DATA};
        imageCursor = ctx.getContentResolver().query(
                images, // 이미지 컨텐트 테이블
                PROJECTION_BUCKET, // DATA, _ID를 출력
                BUCKET_GROUP_BY,   // 모든 개체 출력
                null,
                BUCKET_ORDER_BY);
        return imageCursor;
    }

    public Cursor getAlbum(String albumName) {
        String[] PROJECTION_BUCKET = {
                MediaStore.Images.ImageColumns.BUCKET_ID,
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media._ID,
                MediaStore.Images.ImageColumns.DATE_TAKEN  };

        String orderBy = MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC";

        imageCursor = ctx.getContentResolver().query(
                images,
                PROJECTION_BUCKET,
                MediaStore.Images.ImageColumns.BUCKET_ID + "=?",
                new String[]{albumName},
                orderBy );

        return imageCursor;
    }

    public String getThumbnailPath(String imageId) {
        String[] projection = {MediaStore.Images.Thumbnails.DATA};
        ContentResolver contentResolver = ctx.getContentResolver();
        // 원본 이미지의 _ID가 매개변수 imageId인 썸네일을 출력
        Cursor thumbnailCursor = contentResolver.query(
                MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, // 썸네일 컨텐트 테이블
                projection, // DATA를 출력
                MediaStore.Images.Thumbnails.IMAGE_ID + "=?", // IMAGE_ID는 원본 이미지의 _ID를 나타냅니다.
                new String[]{imageId},
                null);
        String filePath = "";
        int thumbnailPath = thumbnailCursor.getColumnIndex(projection[0]);
        if (thumbnailCursor == null) {
            // Error 발생
        } else if (thumbnailCursor.moveToFirst()) {
            do {
                filePath = thumbnailCursor.getString(thumbnailPath);
            } while (thumbnailCursor.moveToNext());
        } else {
        }
        thumbnailCursor.close();
        return filePath;
    }

    public void closeCursor(){
        imageCursor.close();
    }
}
