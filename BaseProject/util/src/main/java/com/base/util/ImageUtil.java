package com.base.util;


import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 * Created by LZ on 2017/10/12.
 * 保存图片到相册
 */
public class ImageUtil {
    
    /**
     * 保存bitmap到指定路径 并刷新相册
     *
     * @param context
     * @param bitmap
     * @param dir
     *
     * @return
     */
    public static Uri saveBitmapToAlbum(Context context, Bitmap bitmap, String dir) {
        String fullPath = saveBitmapToSD(bitmap, dir);
        Uri uri = exportToGallery(context, fullPath);
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
        return uri;
    }

//    public static Uri saveImageToAlbum(Context context, Bitmap bitmap) {
//        String newUrl = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "lianni_invite.jpg", "lianni_invite");
//        File fileNew = new File(newUrl);
//        Uri uri = exportToGallery(context, fileNew.getAbsolutePath());
//        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
//        return uri;
//    }
//
    
    /**
     * 字符图片转成图片
     *
     * @param data
     *
     * @return
     */
    public static Bitmap stringToBitmap(String data) {
        Bitmap bitmap = null;
        
        try {
            byte[] bitmapArray = Base64.decode(data, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }
    
    private static Uri exportToGallery(Context context, String filename) {
        // Save the name and description of a video in a ContentValues map.
        final ContentValues values = new ContentValues(2);
        values.put(MediaStore.Video.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.Video.Media.DATA, filename);
        // Add a new record (identified by uri)
        final Uri uri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                                            values);
        return uri;
    }
    
    /**
     * 保存bitmap到指定路径
     *
     * @param bitmap
     * @param dir
     *
     * @return
     */
    public static String saveBitmapToSD(Bitmap bitmap, String dir) {
        File file = new File(dir);
        if (!file.exists()) {
            file.mkdirs();
        }
        String fullPath = dir + "/" + String.valueOf(System.currentTimeMillis()) + ".jpg";
        FileOutputStream out = null;
        
        File imageFile = new File(fullPath);//
        try {
            out = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            System.out.println("___________保存的__sd___下_______________________");
        } catch (FileNotFoundException e) {
        }
        try {
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return fullPath;
    }
    
    public static boolean hasImage(String logo) {
        return StringUtils.isNotEmpty(logo) && (logo.contains(".jpg") || logo.contains(".png") || logo.contains(".gif") || logo.contains(".jpeg") || logo.contains("wx"));
    }
    
    /**
     * 质量压缩方法
     *
     * @param image 压缩图片到1MB以下
     *
     * @return
     */
    public static Bitmap compressImage(Bitmap image, int baseQuality, int quality) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, baseQuality, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 70;
        do { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset(); // 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;// 每次都减少10
        } while (baos.toByteArray().length / 1024 > quality);
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        return bitmap;
    }
    
}
