package me.zeo_x.management.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

public class ImageCompress {
    private static final String TAG = "ImageCompress";

    public void compress(final String path, final Context context, final ImageCompressListener listener) {
        Log.d(TAG, "ImageCompress");
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Bitmap result = getImage(path);
                    String name = Utils.getRandomString(10) + ".jpg";
                    File file = new File(context.getExternalCacheDir(), name);
                    if (file.createNewFile()) {
                        FileOutputStream fos = new FileOutputStream(file);
                        result.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                        fos.flush();
                        fos.close();
                        listener.onCompressSuccess(context.getExternalCacheDir() + File.separator + name);
                    } else {
                        listener.onCompressFailed("保存照片失败");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    listener.onCompressFailed(e.getMessage());
                }
            }
        });
        t.start();
    }

    private Bitmap getImage(String srcPath) {
        Log.d(TAG, "getImage");
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        newOpts.inJustDecodeBounds = false;
        int a = (int) Math.floor(((float) newOpts.outHeight) / 1024f);
        int b = (int) Math.floor(((float) newOpts.outWidth) / 1024f);
        if (a > b) {
            newOpts.inSampleSize = a;
        } else {
            newOpts.inSampleSize = b;
        }
        if (newOpts.inSampleSize <= 0) {
            newOpts.inSampleSize = 1;
        }
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return compressImage(bitmap);
    }

    private Bitmap compressImage(Bitmap image) {
        Log.d(TAG, "compressImage");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        int options = 90;
        int count = 0;
        while ((baos.toByteArray().length / 1024 > 300) && (count < 5)) {
            count++;
            baos.reset();
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        return BitmapFactory.decodeStream(isBm, null, null);
    }

    public interface ImageCompressListener {
        void onCompressSuccess(String path);

        void onCompressFailed(String error);
    }
}
