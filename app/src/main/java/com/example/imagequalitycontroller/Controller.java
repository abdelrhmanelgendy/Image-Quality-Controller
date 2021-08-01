package com.example.imagequalitycontroller;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Controller {

    private static Context mContext;

    public static void setContext(Context context) {
        mContext = context;
    }

    private static File createFileDirec() {
        File file = new File(Environment.getExternalStorageDirectory().getAbsoluteFile() +"/"+ mContext.getResources().getString(R.string.app_name));
        boolean mkdir = file.mkdir();
        return file;
    }


    private static String saveBitMab(Bitmap bitmap,int quality) {
        File fileDirec = createFileDirec();
        long timeInMillis = System.currentTimeMillis();
        String filePath = fileDirec + "/" + timeInMillis + ".JPEG";
        try (FileOutputStream out = new FileOutputStream(filePath)) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, out);
        } catch (IOException e) {
            e.printStackTrace();

        }
        return filePath;
    }

    public static String reduceBitmab(Uri uri,int quality) {
        Bitmap bitmapByUri = getBitmapByUri(uri);

        String outPutPath = "";
        AssetFileDescriptor file = null;
        try {
            file = mContext.getContentResolver().openAssetFileDescriptor(uri, "r");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (file != null) {

            long sizeInKB = file.getLength() / 1024;
            if (sizeInKB <= 250) {
                outPutPath = saveBitMab(bitmapByUri, quality);
            } else if (sizeInKB > 250 && sizeInKB <= 500) {
                outPutPath = saveBitMab(bitmapByUri, quality);
            } else if (sizeInKB > 500 && sizeInKB < 700) {
                outPutPath = saveBitMab(bitmapByUri, quality);
            } else if (sizeInKB >= 700 && sizeInKB <= 1000) {
                outPutPath = saveBitMab(bitmapByUri, quality);
            } else if (sizeInKB > 1000 && sizeInKB <= 3000) {
                outPutPath = saveBitMab(bitmapByUri, quality);
            } else if (sizeInKB > 3000 && sizeInKB <= 5000) {
                outPutPath = saveBitMab(bitmapByUri, quality);
            } else if (sizeInKB > 5000 && sizeInKB <= 7000) {
                outPutPath = saveBitMab(bitmapByUri, quality);
            } else if (sizeInKB > 7000 && sizeInKB <= 10000) {
                outPutPath = saveBitMab(bitmapByUri, quality);
            } else if (sizeInKB > 10000) {
                outPutPath = saveBitMab(bitmapByUri, quality);
            }
        }
        return outPutPath;
    }



    private static Bitmap getBitmapByUri(Uri uri) {
        Bitmap mBitmap = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            try {
                mBitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(mContext.getContentResolver(), uri));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                mBitmap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return mBitmap;
    }
}
