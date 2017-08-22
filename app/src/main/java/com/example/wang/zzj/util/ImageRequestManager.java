package com.example.wang.zzj.util;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;

import com.example.wang.zzj.interfaces.CallBack;

import java.io.InputStream;
import java.lang.ref.WeakReference;

/**
 * Created by wang on 2016/1/22.
 */
public class ImageRequestManager {


    private static ImageRequestManager sInstance;
    //    图片加载类
    private LruBitmapCache mLruBitmapCache;

    public static ImageRequestManager getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new ImageRequestManager(context.getApplicationContext());
        }
        return sInstance;
    }

    private ImageRequestManager(Context context) {
        mLruBitmapCache = new LruBitmapCache(context);
    }

    public <T> void loadBitmap(T url, ContentResolver cr, ImageView imageView, CallBack<Bitmap> callBack){
        if (url != null){
            Bitmap bitmap = mLruBitmapCache.getBitmap(cr == null ? (String)url : ((Uri)url).getPath());
            if (bitmap == null){
                BitmapWorkerTask<T> task = new BitmapWorkerTask<>(imageView, cr, callBack);
                task.execute(url);
            }
            else {
                imageView.setImageBitmap(bitmap);
                if (callBack != null){
                    callBack.success(bitmap);
                }
            }
        }
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static Bitmap decodeSampledBitmapFromLocal(String url, int reqWidth, int reqHeight){
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(url, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;


        return BitmapFactory.decodeFile(url, options);
    }

    public static Bitmap decodeSampledBitmapFromLocal(Uri uri, ContentResolver cr, int reqWidth, int reqHeight){

        Bitmap bitmap = null;
        try {
            InputStream input = cr.openInputStream(uri);
//            ParcelFileDescriptor fileDescriptor = cr.openFileDescriptor(uri, "r");
            Log.d("test", uri.getPath());
            if (input != null){
                final BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeStream(input, null, options);
                input.close();

                // Calculate inSampleSize
                options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
                input = cr.openInputStream(uri);
                if (input != null){
                    // Decode bitmap with inSampleSize set
                    options.inJustDecodeBounds = false;
                    bitmap = BitmapFactory.decodeStream(input, null, options);
                    input.close();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        return bitmap;
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    //    普通图片lru缓存图片加载技术
    public static class LruBitmapCache extends LruCache<String, Bitmap>{
        /**
         * @param maxSize for caches that do not override {@link #sizeOf}, this is
         *                the maximum number of entries in the cache. For all other caches,
         *                this is the maximum sum of the sizes of the entries in this cache.
         */

        // Get max available VM memory, exceeding this amount will throw an
        // OutOfMemory exception. Stored in kilobytes as LruCache takes an
        // int in its constructor.
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        // Use 1/8th of the available memory for this memory cache.
        final int cacheSize = maxMemory / 8;

        public LruBitmapCache(int maxSize) {
            super(maxSize);
        }

        public LruBitmapCache(Context ctx) {
            this(getCacheSize(ctx));
        }

        @Override
        protected int sizeOf(String key, Bitmap value) {
            return value.getRowBytes() * value.getHeight();
        }


        public Bitmap getBitmap(String url) {
            return get(url);
        }

        public void putBitmap(String url, Bitmap bitmap) {
            if (get(url) == null){
                put(url, bitmap);
            }
        }

        public static int getCacheSize(Context ctx) {
            final DisplayMetrics displayMetrics = ctx.getResources().getDisplayMetrics();
            final int screenWidth = displayMetrics.widthPixels;
            final int screenHeight = displayMetrics.heightPixels;
            final int screenBytes = screenHeight * screenWidth;

            return screenBytes * 3;
        }

    }

    class BitmapWorkerTask<T> extends AsyncTask<T, Void, Bitmap> {

        WeakReference<ImageView> imageViewReference;

        CallBack<Bitmap> callBack;

        ContentResolver cr;

        public BitmapWorkerTask(ImageView imageView, ContentResolver cr, CallBack<Bitmap> callBack) {
            // Use a WeakReference to ensure the ImageView can be garbage collected
            imageViewReference = new WeakReference<>(imageView);
            this.callBack = callBack;
            this.cr = cr;
        }

        // Decode image in background.

        @SafeVarargs
        @Override
        protected final Bitmap doInBackground(T... params) {
            Bitmap bitmap;
            if (cr != null){
                bitmap = decodeSampledBitmapFromLocal((Uri)params[0], cr, 640, 1084);
                Log.d("test", "album");
                mLruBitmapCache.putBitmap(((Uri)params[0]).getPath(), bitmap);
            }
            else {
                bitmap = decodeSampledBitmapFromLocal((String) params[0], 640, 1084);
                mLruBitmapCache.putBitmap((String) params[0], bitmap);
            }


            return bitmap;
        }

        // Once complete, see if ImageView is still around and set bitmap.
        @Override
        protected void onPostExecute(Bitmap bitmap) {

            if (imageViewReference != null && bitmap != null) {
                ImageView imageView = imageViewReference.get();
                if (imageView != null) {
                    imageView.setImageBitmap(bitmap);
                    callBack.success(bitmap);
                }
                else {
                    callBack.success(bitmap);
                }
            }
            else {
                callBack.error();
            }
        }

    }
}
