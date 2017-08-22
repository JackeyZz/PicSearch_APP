package com.example.wang.zzj.util;

import com.googlecode.javacv.cpp.opencv_core.IplImage;

import static com.googlecode.javacv.cpp.opencv_core.CV_32FC1;
import static com.googlecode.javacv.cpp.opencv_core.CV_MINMAX;
import static com.googlecode.javacv.cpp.opencv_core.CvMat;
import static com.googlecode.javacv.cpp.opencv_core.CvRect;
import static com.googlecode.javacv.cpp.opencv_core.IPL_DEPTH_8U;
import static com.googlecode.javacv.cpp.opencv_core.cvCopy;
import static com.googlecode.javacv.cpp.opencv_core.cvCreateImage;
import static com.googlecode.javacv.cpp.opencv_core.cvCreateMat;
import static com.googlecode.javacv.cpp.opencv_core.cvNormalize;
import static com.googlecode.javacv.cpp.opencv_core.cvRect;
import static com.googlecode.javacv.cpp.opencv_core.cvResetImageROI;
import static com.googlecode.javacv.cpp.opencv_core.cvSetImageROI;
import static com.googlecode.javacv.cpp.opencv_core.cvSize;
import static com.googlecode.javacv.cpp.opencv_highgui.cvLoadImage;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_BGR2GRAY;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvCvtColor;

/**
 * Created by acer on 2017/4/29.
 */
public class LBPMatch {
    private static double  PI= 3.1415926;

    public static class  MyPoint{
        private double x;
        public double getX() {
            return x;
        }
        public void setX(double x) {
            this.x = x;
        }
        public double getY() {
            return y;
        }
        public void setY(double y) {
            this.y = y;
        }
        private double y;
    };
    public static  MyPoint[] calc_position(int radius,int num_sp){
        double theta;
        MyPoint[] spoint=new MyPoint[num_sp];
        for (int i = 0; i < num_sp; i++) {
            spoint[i]=new MyPoint();
        }
        theta = 2*PI/num_sp;

        for (int i = 0; i < num_sp; i++){
            double y=-radius * Math.sin(i * theta);
            double x=radius * Math.cos(i * theta);
            spoint[i].setY (y);
            spoint[i].setX ( x);
        }
        return spoint;
    }
    public static  int calc_sum(int r){
        int res_sum;

        res_sum = 0;
        while (r>1){
            res_sum = res_sum + r % 2;
            r /= 2;
        }
        return res_sum;
    }
    public static void rotation_uniform_invariant_mapping(int range,int num_sp,int[] Mapping){
        int numt,i,j,tem_xor;

        numt = 0;
        tem_xor = 0;
        for (i = 0; i< range; i++){
            j = i << 1;
            if (j > range -1){
                j = j - (range -1);
            }

            tem_xor = i ^ j;	// 异或
            numt = calc_sum(tem_xor);//计算异或结果中1的个数，即跳变个数

            if (numt <= 2){
                Mapping[i] = calc_sum(i);
            }else{
                Mapping[i] = num_sp;
            }
        }

    }

    @SuppressWarnings("unused")
    public static double[] rotation_uniform_invariant_lbp(IplImage src,int height,int width,int num_sp,MyPoint[] spoint,int[] Mapping){
        IplImage target,hist;
        int i,j,k,box_x,box_y,orign_x,orign_y,dx,dy,tx,ty,fy,fx,cy,cx,v;
        double min_x,max_x,min_y,max_y,w1,w2,w3,w4,N,x,y;
        int[] result;
        float dishu;

        dishu = 2;
        max_x=0;max_y=0;min_x=0;min_y=0;
        for (k=0;k<num_sp;k++){
            if (max_x<spoint[k].x){
                max_x=spoint[k].x;
            }
            if (max_y<spoint[k].y){
                max_y=spoint[k].y;
            }
            if (min_x>spoint[k].x){
                min_x=spoint[k].x;
            }
            if (min_y>spoint[k].y){
                min_y=spoint[k].y;
            }
        }

        //计算模版大小

        box_x = (int) (Math.ceil(Math.max(max_x,0)) - Math.floor(Math.min(min_x,0)) + 1);
        box_y = (int) (Math.ceil(Math.max(max_y,0)) - Math.floor(Math.min(min_y,0)) + 1);

        if (width<box_x||height<box_y){
            System.out.println("Too small input image. Should be at least (2*radius+1) x (2*radius+1)");
            return null;
        }

        //计算可滤波图像大小,opencv图像数组下标从0开始
        orign_x = (int) (0 - Math.floor(Math.min(min_x,0)));//起点
        orign_y = (int) (0 - Math.floor(Math.min(min_x,0)));

        dx = width - box_x+1;//终点
        dy = height - box_y+1;

        target = cvCreateImage(cvSize(dx,dy),IPL_DEPTH_8U,1);
        result = new int[dx*dy];

        CvRect roi =cvRect(orign_x, orign_y, dx, dy);
        cvSetImageROI(src, roi);
        cvCopy(src, target);
        cvResetImageROI(src);

        for ( k = 0; k<num_sp;k++){
            x = spoint[k].x+orign_x;
            y = spoint[k].y+orign_y;

            //二线性插值图像
            fy = (int) Math.floor(y);	//向下取整
            fx = (int) Math.floor(x);
            cy = (int) Math.ceil(y);	//向上取整
            cx = (int) Math.ceil(x);
            ty = (int) (y - fy);
            tx = (int) (x - fx);
            w1 = (1 - tx) * (1 - ty);
            w2 = tx  * (1 - ty);
            w3 = (1 - tx) * ty ;
            w4 = tx * ty ;
            v = (int) Math.pow(dishu,(float)k);

            for (i = 0;i<dy;i++){
                for (j = 0;j<dx;j++){
                    //灰度插值图像像素
                    N = w1 * (double)src.imageData().get((i+fy)*src.width()+j+fx)+
                            w2 * (double)src.imageData().get((i+fy)*src.width()+j+cx)+
                            w3 * (double)src.imageData().get((i+cy)*src.width()+j+fx)+
                            w4 * (double)src.imageData().get((i+cy)*src.width()+j+cx);

                    if( N >= (double)target.imageData().get(i*dx+j))
                    {
                        result[i*dx+j] += v * 1;
                    }else{
                        result[i*dx+j] += v * 0;
                    }
                }
            }
        }

        //将result的值映射为mapping的值
        for(i = 0; i < dy ;i++){
            for (j = 0; j < dx ;j ++){
                result[i*dx+j] = Mapping[result[i*dx+j]];
            }
        }

        //显示图像
        int cols = 0;//直方图的横坐标，也是result数组的元素种类
        int mapping_size = (int) Math.pow(dishu,(float)num_sp);
        for (i = 0;i < mapping_size; i++ ){
            if (cols < Mapping[i]){
                cols = Mapping[i]+1;
            }
        }

        if (cols < 255){
            //只有采样数小于8，则编码范围0-255，才能显示图像
            for (i = 0;i<dy;i++){
                for (j = 0;j<dx;j++){
                    target.imageData().put(i*dx+j, (byte)result[i*dx+j]);;
                    //printf("%d\n",(unsigned char)target.imageData[i*width+j]);
                }
            }
        }

        //计算直方图
        hist = cvCreateImage(cvSize(300,200),IPL_DEPTH_8U,3);//直方图图像
        double[] val_hist =new double[cols];	//直方图数组
        for (i=0;i<cols;i++){
            val_hist[i]=0.0;
        }
        for (i=0; i<dy*dx;i++){
            val_hist[result[i]]++;
        }
        CvMat lbp=cvCreateMat(1, cols,CV_32FC1);
        CvMat dst=cvCreateMat(1, cols,CV_32FC1);
        for (i=0;i<cols;i++){
            lbp.put(0, i, val_hist[i]);
        }
        cvNormalize(lbp,dst, 1, 0, CV_MINMAX, null );
        for (i=0;i<cols;i++){
            val_hist[i]=dst.get(0, i);
        }
        return  val_hist;
    }
    //  计算相似度2
    public static Double calLBPDiff(String str){
//            double[] Sa=calHu(str);
//            double[] Ta=calHu(Constant.ORG_PATH + "/tem.t");
//            double temp2 =0;
//            double temp3 =0;
//            for(int i=0;i<7;i++)
//            {
//                temp2 += Math.abs(Sa[i]-Ta[i]);
//                temp3 += Math.abs(Sa[i]+Ta[i]);
//            }
//            return (1- (temp2*1.0)/(temp3));
        double dbR =0; //相似度
        double dSigmaST =0;
        double dSigmaS =0;
        double dSigmaT =0;
        double[] Sa=calLBP(str);
        double[] Ta=calLBP(Constant.ORG_PATH + "/tem.t");
        double temp =0;
        {for(int i=0;i<7;i++)
        {
            temp = Math.abs(Sa[i]*Ta[i]);
            dSigmaST+=temp;
            dSigmaS+=Math.pow(Sa[i],2);
            dSigmaT+=Math.pow(Ta[i],2);
        }}
        dbR = dSigmaST/(Math.sqrt(dSigmaS)*Math.sqrt(dSigmaT));
        return dbR;
    }
    public static double[] calLBP(String filename) {
        IplImage src,grey;
        int samples,radius,range;
        int[] mapping;
        MyPoint[] spoint;
        float Mi;

        samples = 8;
        radius = 10;
        Mi = 2;
        range = (int) Math.pow(Mi,samples);

        src = cvLoadImage(filename);
        grey = cvCreateImage(cvSize(src.width(),src.height()),IPL_DEPTH_8U,1);
        cvCvtColor(src,grey,CV_BGR2GRAY);
        mapping =new int[range];

        //计算采样点相对坐标
        spoint =calc_position(radius,samples);

        //计算旋转不变等价LBP特征
        rotation_uniform_invariant_mapping(range,samples,mapping);
        return rotation_uniform_invariant_lbp(grey,src.height(),src.width(),samples,spoint,mapping);
    }
}
