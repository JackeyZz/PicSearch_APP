package com.example.wang.zzj.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.example.wang.zzj.R;
import com.example.wang.zzj.model.Search;
import com.example.wang.zzj.util.GaborMatchThread;
import com.example.wang.zzj.util.HSVMatchThread;
import com.example.wang.zzj.util.HuMatchThread;
import com.example.wang.zzj.util.LBPMatchThread;
import com.example.wang.zzj.util.MatchImageThread;
import com.example.wang.zzj.viewmodel.SearchActivityViewModel;
import com.example.wang.zzj.widget.CommonToolBar;

/**
 * Created by wang on 2016/1/22.
 */
public class SearchActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {

    //    private View loading;
    private SearchActivityViewModel viewModel;
    private RadioGroup searchGroup;
    private Search search;
    private int flag = 0;

//    private Uri uri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

//        loading = findViewById(R.id.loading);
        ImageView selectImg = (ImageView) findViewById(R.id.select_img);
        selectImg.setImageBitmap(getPic("sdcard/zzjIMG" + "/tem" + ".t"));
//        viewModel = new SearchActivityViewModel(this, this);
        search = new Search();
        search.setMethod(Search.SearchMethod.Color);

//        uri = getIntent().getParcelableExtra(Constant.Key.ALBUM_IMG);
//        String fileName = getIntent().getStringExtra(Constant.Key.CAMERA_IMG);
//标题栏初始化
        ((CommonToolBar) findViewById(R.id.tool_bar)).setOnToolBarClickListener(new CommonToolBar.OnToolBarClickListener() {
            @Override
            public void onLeftBtnClick() {
                finish();
            }

            @Override
            public void onRightBtnClick() {

            }
        });
//RadioGroup
        searchGroup = (RadioGroup) findViewById(R.id.search_methods);
        searchGroup.setOnCheckedChangeListener(this);
//        onCheckedChanged(searchGroup, R.id.color_btn);

//        loading.setVisibility(View.VISIBLE);
////        NotificationHelper.toast(this, "正在加载图片");
//        if (uri == null){
//            viewModel.loadBitmap(fileName, null, selectImg);
//            search.setFileName(fileName);
//        }
//        else {
//            viewModel.loadBitmap(uri, getContentResolver(), selectImg);
//        }

    }

    public void startClick(View view) {
//        Intent intent = new Intent();
//        intent.setClass(this, ResultActivity.class);
//        intent.putExtra(Constant.Key.SEARCH, search);
////        intent.putExtra(Constant.Key.Uri, uri);
//        startActivity(intent);

        switch (flag) {
            case 0:
                new HSVMatchThread(SearchActivity.this).execute();
                break;
            case 1:
                new GaborMatchThread(SearchActivity.this).execute();
                break;
            case 2:
                new HuMatchThread(SearchActivity.this).execute();
                break;
            case 3:
                new LBPMatchThread(SearchActivity.this).execute();
                break;
            default:
                new MatchImageThread(SearchActivity.this).execute();
                break;
        }


    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.color_btn: {
//                search.setMethod(Search.SearchMethod.Color);
                flag = 0;
                break;
            }

            case R.id.texture_btn: {
//                search.setMethod(Search.SearchMethod.Texture);
                flag = 1;
                break;
            }

            case R.id.shape_btn: {
//                search.setMethod(Search.SearchMethod.All);
                flag = 2;
                break;
            }

            case R.id.shape1_btn: {
//                search.setMethod(Search.SearchMethod.All);
                flag = 3;
                break;
            }

            case R.id.color_texture_btn: {
//                search.setMethod(Search.SearchMethod.All);
                flag = 4;
                break;
            }
        }
    }


    private Bitmap getPic(String path) {
        // iv.setImageBitmap(BitmapFactory.decodeFile(path));

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_4444;    // Bitmap.Config.RGB_565默认是Bitmap.Config.ARGB_8888
        options.inPurgeable = true;
        options.inInputShareable = true;


        Bitmap bm = BitmapFactory.decodeFile(path, options);
        return bm;

    }




}
