package com.example.wang.zzj.util;

import com.googlecode.javacv.cpp.opencv_core;
import com.googlecode.javacv.cpp.opencv_core.CvMat;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import com.googlecode.javacv.cpp.opencv_imgproc;

import static com.googlecode.javacv.cpp.opencv_core.CV_32FC1;
import static com.googlecode.javacv.cpp.opencv_core.CV_8UC1;
import static com.googlecode.javacv.cpp.opencv_core.CV_C;
import static com.googlecode.javacv.cpp.opencv_core.CV_MINMAX;
import static com.googlecode.javacv.cpp.opencv_core.cvConvert;
import static com.googlecode.javacv.cpp.opencv_core.cvCreateImage;
import static com.googlecode.javacv.cpp.opencv_core.cvCreateMat;
import static com.googlecode.javacv.cpp.opencv_core.cvGet2D;
import static com.googlecode.javacv.cpp.opencv_core.cvGetSize;
import static com.googlecode.javacv.cpp.opencv_core.cvNormalize;
import static com.googlecode.javacv.cpp.opencv_highgui.cvLoadImage;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_BGR2GRAY;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvCvtColor;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvGetHuMoments;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvMoments;
/**
 * Created by acer on 2017/4/26.
 */
public class HuMatch {

        /************************************************************************/
        /*
        *@作用：计算Hu不变矩
        *@ 输入：filename---源图像路径，可为灰度图或彩图
        *@输出：Double数组，Hu不变矩
        *@ 返回值：Hu不变矩
        */
        /************************************************************************/
//        public static double[] getHu(String filename){
//    	double M00,M01,M10;
//    	Double MU11,MU02,MU20,MU12,MU21,MU03,MU30;
//    	Double X_,Y_;
//    	Double N02,N20,N11,N03,N30,N12,N21;
//    	Double H1,H2,H3,H4,H5,H6,H7;
//    	IplImage img;
//
//    	img=cvLoadImage(filename,0);
//
//    	//零阶矩，一阶矩
//    	M00=MOMENTS_M(img,0,0);
//    	M01=MOMENTS_M(img,0,1);
//    	M10= MOMENTS_M(img,1,0);
//
//    	X_=M10/M00;
//    	Y_=M01/M00;
//         	//中心距
//
//         	MU02=MOMENTS_U(img,0,2,X_,Y_);
//         	MU20=MOMENTS_U(img,2,0,X_,Y_);
//    	MU11=MOMENTS_U(img,1,1,X_,Y_);
//    	MU30=MOMENTS_U(img,3,0,X_,Y_);
//    	MU03=MOMENTS_U(img,0,3,X_,Y_);
//    	MU12=MOMENTS_U(img,1,2,X_,Y_);
//    	MU21=MOMENTS_U(img,2,1,X_,Y_);
//    	 //归一化的中心距
//    	N02=MOMENTS_NORM(MU02,0,2,M00);
//    	N20=MOMENTS_NORM(MU20,2,0,M00);
//    	N11=MOMENTS_NORM(MU11,1,1,M00);
//    	N30=MOMENTS_NORM(MU30,3,0,M00);
//    	N03=MOMENTS_NORM(MU03,0,3,M00);
//    	N12=MOMENTS_NORM(MU12,1,2,M00);
//    	N21=MOMENTS_NORM(MU21,2,1,M00);
//
//    	//hu7个不变距
//
//    	H1=N20+N02;
//    	H2=(N20-N02)* (N20-N02)+4*N11*N11;
//    	H3=(N30-3*N12)* (N30-3*N12)+(3*N21-N03)* (3*N21-N03);
//    	H4=(N30+N12)* (N30+N12)+(N21+N03)* (N21+N03);
//    	H5=((N30-3*N12)*(N30+N12)*((N30+N12)*(N30+N12)-3*(N21+N03)*(N21+N03))
//                +(3*N21-N03)*(N21+N03)*(3*(N30+N12)* (N30+N12)-(N21+N03)* (N21+N03)));
//    	H6=(N20-N02)*((N30+N12)* (N30+N12)-(N21+N03)* (N21+N03))
//                +4*N11*(N30+N12)*(N21+N03);
//    	H7=((3*N21-N03)*(N30+N12)*((N30+N12)*(N30+N12)-3*(N21+N03)*(N21+N03))
//                +(3*N12-N30)*(N21+N03)*(3*(N30+N12)* (N30+N12)- (N21+N03)* (N21+N03)));
//    	return new double[]{H1,H2,H3,H4,H5,H6,H7};
//
//        }

        //计算空间距
        public static Double MOMENTS_M(IplImage img,int p,int q){
            opencv_core.CvScalar scl;
            double M_sum_y=0;
            double M_sum_xy=0;
            int H,W;
            H=img.height();
            W=img.width();
            for(int i=0;i<H;i++){
                for(int j=0;j<W;j++){
                    scl=cvGet2D(img,i,j);
                    M_sum_y+=Math.pow(j,q)*scl.val(0);
                }
                M_sum_xy+=M_sum_y*Math.pow(i,p);
            }
            return M_sum_xy;
        }
        //计算中心距
        public static Double MOMENTS_U(IplImage img,int p,int q,Double X_,Double Y_){
            opencv_core.CvScalar scl;
            double N_sum_y=0;
            double N_sum_xy=0;
            int H,W;
            H=img.height();
            W=img.width();
            for(int i=0;i<H;i++){
                N_sum_y=0;
                for(int j=0;j<W;j++){
                    scl=cvGet2D(img,i,j);
                    N_sum_y+=Math.pow(j-Y_,q)*scl.val(0);
                }
                N_sum_xy+=N_sum_y*Math.pow(i-X_,p);
            }
            return N_sum_xy;
        }
        //计算归一化中心距
        public static Double MOMENTS_NORM(Double QW,int p,int q,Double MU){
            double Norm_val=0;
            double sqrt_inv=0;
            int t;
            t=(p+q+2);
            sqrt_inv=1./Math.sqrt(Math.abs(MU));
            Norm_val=QW*Math.pow(sqrt_inv,t);
            return  Norm_val;
        }
        //  计算相似度2
        public static Double calShapeDiff(String str){
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
            double[] Sa=calHu(str);
            double[] Ta=calHu(Constant.ORG_PATH + "/tem.t");
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
        public static double[] calHu(String filename){

            IplImage src = cvLoadImage(filename);//灰度
            int width = src.width();
            int height = src.height();
            IplImage img=cvCreateImage(cvGetSize(src), 8, 1);
            cvCvtColor(src, img, CV_BGR2GRAY);

            CvMat imgmat = cvCreateMat(height, width, CV_8UC1 );
            cvConvert(img, imgmat);
            opencv_imgproc.CvMoments moments = new opencv_imgproc.CvMoments();
            opencv_imgproc.CvHuMoments humoments =new opencv_imgproc.CvHuMoments();
            cvMoments(img, moments, 0);
            cvGetHuMoments(moments, humoments);
            opencv_core.CvMat result=cvCreateMat(1, 7, CV_32FC1);
            result.put(0, 0, Math.abs(Math.log(Math.abs(humoments.hu1()))));
            result.put(0, 1, Math.abs(Math.log(Math.abs(humoments.hu2()))));
            result.put(0, 2, Math.abs(Math.log(Math.abs(humoments.hu3()))));
            result.put(0, 3, Math.abs(Math.log(Math.abs(humoments.hu4()))));
            result.put(0, 4, Math.abs(Math.log(Math.abs(humoments.hu5()))));
            result.put(0, 5, Math.abs(Math.log(Math.abs(humoments.hu6()))));
            result.put(0, 6, Math.abs(Math.log(Math.abs(humoments.hu7()))));
            opencv_core.CvMat dst=cvCreateMat(1, 7,CV_32FC1);
            cvNormalize(result,dst, 1, 0, CV_MINMAX, null );
            double[] hu=new double[7];
            for (int i=0;i<7;i++){
                hu[i]=dst.get(0, i);
            }
            CvMat hsvMat=cvCreateMat(1, hu.length,CV_32FC1);
            CvMat dstMat=cvCreateMat(1, hu.length,CV_32FC1);
            for (int i=0;i<hu.length;i++){
                hsvMat.put(0, i, hu[i]);
            }
            cvNormalize(hsvMat,dstMat, 1, 0, CV_C, null );
            for (int i=0;i<hu.length;i++){
                hu[i]=dstMat.get(0, i);
            }
            return hu;

    //        return new Double[]{Math.abs(Math.log(Math.abs(humoments.hu1()))),Math.abs(Math.log(Math.abs(humoments.hu2()))),Math.abs(Math.log(Math.abs(humoments.hu3()))),Math.abs(Math.log(Math.abs(humoments.hu4()))),Math.abs(Math.log(Math.abs(humoments.hu5()))),Math.abs(Math.log(Math.abs(humoments.hu6()))),Math.abs(Math.log(Math.abs(humoments.hu7())))};
        }
}
