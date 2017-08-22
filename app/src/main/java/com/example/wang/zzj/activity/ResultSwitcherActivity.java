package com.example.wang.zzj.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Gallery.LayoutParams;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.ViewSwitcher.ViewFactory;

import com.example.wang.zzj.R;
import com.example.wang.zzj.util.Constant;
import com.example.wang.zzj.util.ImageItem;
import com.example.wang.zzj.widget.CommonToolBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResultSwitcherActivity extends Activity implements
        OnItemSelectedListener, ViewFactory {
   // private ImageSwitcher is;
   // private Gallery gallery;
    List<ImageItem> list;
    TextView tx1, tx2;
    private double[] names;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_show);

        Intent intent = getIntent();
        list = (List<ImageItem>) intent.getSerializableExtra(Constant.Key.LIST_RESULT);
        String[] imageId=new String[]{list.get(0).getPath(),list.get(1).getPath(),
                list.get(2).getPath(),list.get(3).getPath(),list.get(4).getPath(),list.get(5).getPath(),list.get(6).getPath(),
                list.get(7).getPath(),list.get(8).getPath(),list.get(9).getPath()};
        names=new double[]{list.get(0).getResult(),list.get(1).getResult(),
                list.get(2).getResult(),list.get(3).getResult(),list.get(4).getResult(),list.get(5).getResult(),list.get(6).getResult(),
                list.get(7).getResult(),list.get(8).getResult(),list.get(9).getResult()};
        listView= (ListView) findViewById(R.id.listView);
        List<Map<String,Object>> listItems = new ArrayList<Map<String, Object>>();
        for (int i=0;i<names.length;i++){
            Map<String,Object> listItem = new HashMap<String,Object>();
            listItem.put("img",imageId[i]);
            listItem.put("text",names[i]);
            listItems.add(listItem);
            }
        SimpleAdapter mSimpleAdapter = new SimpleAdapter(this,listItems,R.layout.listview_temp,
                new String[]{"img","text"},new int[]{R.id.header,R.id.name});
        listView.setAdapter(mSimpleAdapter);

        //tx1 = (TextView) findViewById(R.id.textView);
       // tx2 = (TextView) findViewById(R.id.textView2);

        String type = intent.getStringExtra("type");
        //tx2.setText(type);


        ((CommonToolBar) findViewById(R.id.tool_bar)).setOnToolBarClickListener(new CommonToolBar.OnToolBarClickListener() {
            @Override
            public void onLeftBtnClick() {
                finish();
            }

            @Override
            public void onRightBtnClick() {

            }
        });


       // is = (ImageSwitcher) findViewById(R.id.switcher);
       // is.setFactory(this);

       // is.setInAnimation(AnimationUtils.loadAnimation(this,
               //  android.R.anim.fade_in));
               //  is.setOutAnimation(AnimationUtils.loadAnimation(this,
             //   android.R.anim.fade_out));

                //gallery = (Gallery) findViewById(R.id.gallery);

       // gallery.setAdapter(new ImageAdapter(this));
        //gallery.setOnItemSelectedListener(this);
    }

    @Override
    public View makeView() {
        ImageView i = new ImageView(this);
        i.setBackgroundColor(0xFF000000);
        i.setScaleType(ImageView.ScaleType.FIT_CENTER);
        i.setLayoutParams(new ImageSwitcher.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        return i;
    }

    public class ImageAdapter extends BaseAdapter {
        public ImageAdapter(Context c) {
            mContext = c;
        }

        public int getCount() {
            if (list.size() < 10)
                return list.size();
            else
                return 10;
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView i = new ImageView(mContext);

//			i.setImageResource();

            i.setImageBitmap(getPic(list.get(position).getPath()));
            i.setAdjustViewBounds(true);
            i.setLayoutParams(new LayoutParams(
                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            //i.setBackgroundResource(R.drawable.e);
            return i;
        }

        private Context mContext;

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position,
                               long id) {
//		is.setImageResource(mImageIds[position]);
        Drawable drawable = new BitmapDrawable(getPic(list.get(position).getPath()));
       // is.setImageDrawable(drawable);
        //tx1.setText(position + 1 + "");
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // TODO Auto-generated method stub

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

