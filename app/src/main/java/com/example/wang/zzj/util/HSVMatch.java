package com.example.wang.zzj.util;

import android.content.Context;

import com.googlecode.javacv.cpp.opencv_core.IplImage;

import java.io.InputStream;

import static com.googlecode.javacv.cpp.opencv_core.cvCreateImage;
import static com.googlecode.javacv.cpp.opencv_core.cvGetSize;
import static com.googlecode.javacv.cpp.opencv_core.cvReleaseImage;
import static com.googlecode.javacv.cpp.opencv_core.cvSplit;
import static com.googlecode.javacv.cpp.opencv_highgui.cvLoadImage;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_BGR2HSV;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_COMP_BHATTACHARYYA;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_HIST_ARRAY;
import static com.googlecode.javacv.cpp.opencv_imgproc.CvHistogram;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvCalcHist;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvCompareHist;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvCreateHist;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvCvtColor;



public class
        HSVMatch {
	static Context context;
	
public static double doit(String str){

    str = "/"+str;

        CvHistogram hist1 = histtohsv(str);//直方图,CvHistogram结构体（int type;CvArr* bins;float thresh[CV_MAX_DIM][2];floa[][] thresh2;CvMatND mat; ）
        CvHistogram hist2 = histtohsv(Constant.ORG_PATH+"/tem.t");
        double hsv = cvCompareHist(hist1,hist2,CV_COMP_BHATTACHARYYA);//比较两个统计直方图的分布,常态分布比对的Bhattacharyya距离
      
        return hsv;
        }

    private static byte[] InputStreamToByte(InputStream abpath) {
	// TODO Auto-generated method stub
	return null;
}

	public static CvHistogram histtohsv(String path){
    	IplImage img_source = cvLoadImage(path);
    	if(img_source==null)
    	{
    		
    		System.out.println("pic fail");
            return null;
    	}
    	//IplImage img_sourc=context.getResources().openRawResource(R.id.pr1);
    	IplImage hsv = cvCreateImage(cvGetSize(img_source),8,3);
        //rgb色彩空间转换到hsv色彩空间
        cvCvtColor(img_source,hsv,CV_BGR2HSV);
        IplImage h_plane = cvCreateImage(cvGetSize(img_source),8,1);
        IplImage s_plane = cvCreateImage(cvGetSize(img_source),8,1);
        IplImage v_plane = cvCreateImage(cvGetSize(img_source),8,1);

        IplImage planes[] ={h_plane,s_plane};
        //将三通道的转化为单通道

        cvSplit( hsv,h_plane, s_plane, v_plane,null);
        //为创建直方图做准备
        int h_bins =30, s_bins = 32;
        CvHistogram hist;
        int hist_size[] = {h_bins,s_bins};
        float h_ranges[] = {0,180}; /* hue varies from 0 (~0°red) to 180 (~360°red again) */
        float s_ranges[] = {0,255};  /* saturation varies from 0 (black-gray-white) to 255 (pure spectrum color) */
        float ranges[][] = {h_ranges,s_ranges};


        hist = cvCreateHist(
        		2, // int dims 
        		hist_size, // int* sizes 
        		CV_HIST_ARRAY, // int type
        		ranges, // float** ranges
        		1       //uniform 
        		);

        cvCalcHist(planes, hist, 0, null);//计算图像image(s) 的直方图 

        cvReleaseImage(img_source);//cvReleaseImage函数只是将IplImage*型的变量值赋为NULL，而这个变量本身还是存在的并且在内存中的存储位置不变。
        cvReleaseImage(hsv);
   	 	cvReleaseImage(h_plane);
   	 	cvReleaseImage(s_plane);
   	 	cvReleaseImage(v_plane);
   	 	return hist;
		
    }

}
	
