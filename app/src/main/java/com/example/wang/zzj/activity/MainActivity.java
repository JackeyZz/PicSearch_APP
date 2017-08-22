package com.example.wang.zzj.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.wang.zzj.R;
import com.example.wang.zzj.viewmodel.MainActivityViewModel;
import com.example.wang.zzj.widget.SelectMethodDialog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class MainActivity extends AppCompatActivity implements MainActivityViewModel.IView {

    private MainActivityViewModel viewModel;
    private String fileName;
    private static final int CAMERA = 1;
    private static final int ALBUM = 2;
    private static final int PHOTO_REQUEST_CUT = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);//设置界面
        viewModel = new MainActivityViewModel(this, this);

        File file = new File("sdcard/zzjIMG");//检测界面存在与否
        if (!file.exists()) {
            file.mkdirs(); //创建文件夹
        }
    }


    public void selectClick(View view) {



        showSelectDialog();//选择对话框
    }

    public void connClick(View view){

    }

    private void showSelectDialog() {

        SelectMethodDialog dialog = new SelectMethodDialog();

        dialog.setSelectListener(new SelectMethodDialog.OnSelectListener() {
            @Override
            public void cameraClick() {
                takePhoto();
            }

            @Override
            public void albumClick() {
                getBitmapFromAlbum();
            }
        });

        dialog.show(getSupportFragmentManager(), null);

    }

    private void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        startActivityForResult(intent, CAMERA);


    }

    private void getBitmapFromAlbum() {
//        Intent getAlbum = new Intent(Intent.ACTION_GET_CONTENT);
//        String IMAGE_TYPE = "image/*";
//        getAlbum.setType(IMAGE_TYPE);
//        startActivityForResult(getAlbum, ALBUM);


        Intent intent;

        if (Build.VERSION.SDK_INT < 19) {

            intent = new Intent(Intent.ACTION_GET_CONTENT);

            intent.setType("image/*");

        } else {

            intent = new Intent(

                    Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        }

        startActivityForResult(Intent.createChooser(intent, "选择图片"), ALBUM);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ALBUM && data != null) {
//            Uri uri = data.getData();
//            Log.d("test",uri.getPath());
//            Intent intent = new Intent();
//            intent.setClass(this, SearchActivity.class);
//            intent.putExtra(Constant.Key.ALBUM_IMG, uri);
//            startActivity(intent);


            startPhotoZoom(data.getData());//系统返回图片并剪切


        } else if (requestCode == CAMERA && resultCode == RESULT_OK) {
//            MediaScannerConnection.scanFile(this, new String[]{fileName}, null, null);  //刷新图片，使能在相册中能立即可见
//            Intent intent = new Intent();
//            intent.setClass(this, SearchActivity.class);
//            intent.putExtra(Constant.Key.CAMERA_IMG, fileName);
//            startActivity(intent);


            Bundle bundle = data.getExtras();
            Bitmap bm = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式
            saveMyBitmap(bm);
//            Intent intent=new Intent(getActivity(),PreShow.class);
//            intent.putExtra("bitmap", bitmap);
//            startActivity(intent);
        } else {
//            Uri selectedImage = data.getData(); // 获得图片的uri
//
//            if (selectedImage != null) {
//                sendPicByUri(selectedImage);
//            }
            if(data!=null){
            Bitmap bm = data.getParcelableExtra("data");
            saveMyBitmap(bm);}

        }

    }

    /**
     * 根据图库图片uri发送图片
     *
     * @param selectedImage
     */
    private void sendPicByUri(Uri selectedImage) {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(selectedImage,
                filePathColumn, null, null, null);
        String st8 = "未能找到任何图片";
        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            cursor = null;

            if (picturePath == null || picturePath.equals("null")) {
//                showSToast("图片不存在");
                return;
            }
            setPic(picturePath);
        } else {
            File file = new File(selectedImage.getPath());
            if (!file.exists()) {
//                showSToast("图片不存在");
                return;

            }
            setPic(file.getAbsolutePath());
        }

    }

    private void setPic(String path) {
        // iv.setImageBitmap(BitmapFactory.decodeFile(path));

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_4444;    // Bitmap.Config.RGB_565默认是Bitmap.Config.ARGB_8888
        options.inPurgeable = true;
        options.inInputShareable = true;


        Bitmap bm = BitmapFactory.decodeFile(path, options);
        saveMyBitmap(bm);
//        imgBtn.setImageBitmap(scaleBitmap(bm, 100,
//                100));
//        files[fileposition] = path;
//		if (!bm.isRecycled()) {
//			bm.recycle(); // 回收图片所占的内存
//			System.gc(); // 提醒系统及时回收
//		}
//		bm.recycle();
    }


    public void saveMyBitmap(Bitmap mBitmap) {
        if (mBitmap != null) {
            String dir_path = Environment.getExternalStorageDirectory() + "/youqianIMG";

//        if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
//            NotificationHelper.toastDanger(MainActivity.this, "手机存储卡读取失败");
//            return;
//        }
            dir_path = "sdcard/zzjIMG";
            File file = new File(dir_path);
            if (!file.exists()) {
                file.mkdirs(); //创建文件夹
            }
            File f = new File(dir_path + "/tem" + ".t");
            FileOutputStream fOut = null;
            try {
                fOut = new FileOutputStream(f);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            try {
                fOut.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fOut.close();
            } catch (IOException e) {
                e.printStackTrace();
            }


            Intent intent = new Intent(this, SearchActivity.class);
//            intent.putExtra("bitmap", bitmap);
            startActivity(intent);
        }
    }


    //    public void getid(){
//        SIFT sift = new SIFT(0, 3, 0.04, 10, 1.6);
//        FeatureDetector detector = sift.getFeatureDetector();
////	     ORB orb_descriptor = new ORB(500, 1.2f, 8, 31, 0, 2, 0, 31);
////	     DescriptorExtractor descriptor = orb_descriptor.getDescriptorExtractor();
//        CvMat image = cvLoadImageM("sdcard/youqianIMG/tem.jpg");
//        KeyPoint keypoints = new KeyPoint();
//        CvMat descriptors = new CvMat(null);
//        detector.detect(image, keypoints, null);
//
//
//        BFMatcher b = new BFMatcher(keypoints);
//
//        System.out.println("Keypoints found: "+ keypoints.capacity());
////	     descriptor.compute(image, keypoints, descriptors);
////        System.out.println("Descriptors calculated: "+descriptors.rows());
//    }
//
//    public static CvMat featureDetect(IplImage image) {
//
//        SIFT sift = new SIFT();
//        FeatureDetector featureDetector  = sift.getFeatureDetector();
//        KeyPoint keypoints = new KeyPoint();
//        featureDetector.detect(image, keypoints, null);
//
//        DescriptorExtractor extractor  = sift.getDescriptorExtractor();
//        CvMat descriptors = cvCreateMat(keypoints.capacity(), extractor.descriptorSize(), CV_32F);
//        extractor.compute(image, keypoints, descriptors);
//
//        if(keypoints.isNull() || descriptors.isNull()) {
//            //System.out.println("feature is Null");
//            return null;
//        }
//
//        //System.out.println("Keypoints found: "+ keypoints.capacity());
//        // System.out.println("Descriptors calculated: "+descriptors.rows());
//        return descriptors;
//
//    }
    private void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");

        // aspectX aspectY 是宽高的比例
//    intent.putExtra("aspectX", 1);
//    intent.putExtra("aspectY", 1);

        // outputX,outputY 是剪裁图片的宽高
//    intent.putExtra("outputX", 300);
//    intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }

    //Grabcut算法
}
