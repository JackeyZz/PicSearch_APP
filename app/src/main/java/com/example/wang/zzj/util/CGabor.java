package com.example.wang.zzj.util;

import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_highgui.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;

public class CGabor {
    public  int REAL= 1;
    public  int IMAG= 2;
    public  int MAG=  3;
    private  double m_fSigma;
    private  int	m_nScale;				//尺度
    private  double m_fRealScale;
    private  double m_fAngle;				//相位角
    private  int m_lWidth;				//核宽度
    private  CvMat m_matImag;				//实部核矩阵
    private  CvMat m_matReal;				//虚部核矩阵
    private  boolean m_bInitialised;
    private  boolean m_bKernel;
    private  boolean m_bConvImage;
    private  double PI=3.14159265;
    CGabor(double fSigma, int nScale, double fAngle){
        m_fSigma = fSigma;
        m_nScale = nScale;
        m_fAngle = fAngle;
        Init();
    }
    CGabor(){
        m_bInitialised	= false;
        m_bKernel		= false;
        m_bConvImage	= false;
    }
    public  void Init(){
        m_bInitialised = false;
        m_fRealScale = 6 * m_fSigma / m_nScale;

        //计算核窗宽度
        double k = (PI/2)/Math.pow(Math.sqrt(2.0), m_nScale);
        double w  = ((m_fSigma/k) * 6) + 1;
        m_lWidth = (int)w;
        if (m_lWidth%2.0 == 0.0) m_lWidth++;

        m_matReal = cvCreateMat( m_lWidth, m_lWidth, CV_32FC1);
        m_matImag = cvCreateMat( m_lWidth, m_lWidth, CV_32FC1);

        m_bInitialised = true;
        CreatKernel();
    }

    public  boolean IsInit(){

        return m_bInitialised;
    }


    public  void  CreatKernel(){
        int Width = m_lWidth;
        if (IsInit() == false) {System.out.println("Error: The Object has not been initilised in CreatKernel()!\n");}
        else {
            CvMat mReal, mImag;
            mReal = cvCreateMat( Width, Width, CV_32FC1);
            mImag = cvCreateMat( Width, Width, CV_32FC1);

            /**************************** Gabor Function ****************************/
            int x, y;
            double dReal;
            double dImag;
            double dTemp1, dTemp2, dTemp3;

            for (int i = 0; i < Width; i++){
                for (int j = 0; j < Width; j++){
                    x = i-(Width-1)/2;
                    y = j-(Width-1)/2;
                    dTemp1 = (1/(2*PI*Math.pow(m_fSigma, 2))) *Math.exp(-((Math.pow((double)x,2)+Math.pow((double)y,2))/(2*Math.pow(m_fSigma,2))));
                    dTemp2 = Math.cos((2*PI*(x*Math.cos(m_fAngle)+y*Math.sin(m_fAngle)))/m_fRealScale);
                    dTemp3 = Math.sin((2*PI*(x*Math.cos(m_fAngle)+y*Math.sin(m_fAngle)))/m_fRealScale);
                    dReal = dTemp1 * dTemp2;
                    dImag = dTemp1 * dTemp3;
					/*dTemp1 = (pow(K,2)/pow(Sigma,2))*exp(-(pow((double)x,2)+pow((double)y,2))*pow(K,2)/(2*pow(Sigma,2)));
					dTemp2 = cos(K*cos(Phi)*x + K*sin(Phi)*y) - exp(-(pow(Sigma,2)/2));
					dTemp3 = sin(K*cos(Phi)*x + K*sin(Phi)*y);
					dReal = dTemp1*dTemp2;
					dImag = dTemp1*dTemp3; */
                    mReal.put(i, j, dReal);
                    mImag.put(i, j, dImag);
//					cvmSet( (CvMat)mReal, i, j, dReal );
//					cvmSet( (CvMat)mImag, i, j, dImag );
                }
            }
            /**************************** Gabor Function ****************************/
            m_bKernel = true;
            cvCopy(mReal, m_matReal, null);
            cvCopy(mImag, m_matImag, null);
        }
    }

    public  boolean IsKernelCreate(){
        return m_bKernel;
    }

    public  int GetMaskWidth(){
        return m_lWidth;
    }

    public CvMat GetMatrix(int Type){
        if (!IsKernelCreate()) {System.out.println("Error: the gabor kernel has not been created!\n"); return null;}
        switch (Type){
            case 1:
                return m_matReal;
            case 2:
                return m_matImag;
            default:
                return null;
        }
    }

    public  void ConvImage(IplImage src, IplImage dst, int Type, CvMat realmat, CvMat imagmat){
        double ve, re,im;

        CvMat mat = cvCreateMat(src.width(), src.height(), CV_32FC1);
        for (int i = 0; i < src.width(); i++){
            for (int j = 0; j < src.height(); j++){
                ve = cvGetReal2D((IplImage)src, j, i);
                cvSetReal2D( (CvMat)mat, i, j, ve );
            }
        }

        CvMat rmat = cvCreateMat(src.width(), src.height(), CV_32FC1);
        CvMat imat = cvCreateMat(src.width(), src.height(), CV_32FC1);

        int Width = realmat.cols();
        CvMat kernel = cvCreateMat( Width, Width, CV_32FC1 );

        switch (Type){
            case 1:
                cvCopy( (CvMat)realmat, (CvMat)kernel, null );
                cvFilter2D( (CvMat)mat, (CvMat)mat, (CvMat)kernel, cvPoint( (Width-1)/2, (Width-1)/2));
                break;
            case 2:
                cvCopy( (CvMat)imagmat, (CvMat)kernel, null );
                cvFilter2D( (CvMat)mat, (CvMat)mat, (CvMat)kernel, cvPoint( (Width-1)/2, (Width-1)/2));
                break;
            case 3:
			/* Real Response */
                cvCopy( (CvMat)realmat, (CvMat)kernel, null );
                cvFilter2D( (CvMat)mat, (CvMat)rmat, (CvMat)kernel, cvPoint( (Width-1)/2, (Width-1)/2));
			/* Imag Response */
                cvCopy( (CvMat)imagmat, (CvMat)kernel, null );
                cvFilter2D( (CvMat)mat, (CvMat)imat, (CvMat)kernel, cvPoint( (Width-1)/2, (Width-1)/2));
			/* Magnitude response is the square root of the sum of the square of real response and imaginary response */
                for (int i = 0; i < mat.rows(); i++)
                {
                    for (int j = 0; j < mat.cols(); j++)
                    {
                        re = cvGetReal2D((CvMat)rmat, i, j);
                        im = cvGetReal2D((CvMat)imat, i, j);
                        ve = Math.sqrt(re*re + im*im);
                        cvSetReal2D( (CvMat)mat, i, j, ve );
                    }
                }
                break;
            default:
                break;
        }

        if (dst.depth() == IPL_DEPTH_8U){
            cvNormalize((CvMat)mat, (CvMat)mat, 0, 255, CV_MINMAX,null);
            for (int i = 0; i < mat.rows(); i++){
                for (int j = 0; j < mat.cols(); j++){
                    ve = cvGetReal2D((CvMat)mat, i, j);
                    ve = (int)(ve+0.5);
                    cvSetReal2D( (IplImage)dst, j, i, ve );
                }
            }
        }

        if (dst.depth() == IPL_DEPTH_32F){
            for (int i = 0; i < mat.rows(); i++){
                for (int j = 0; j < mat.cols(); j++){
                    ve = cvGetReal2D((CvMat)mat, i, j);
                    cvSetReal2D( (IplImage)dst, j, i, ve );
                }
            }
        }

    }

    public  boolean IsConvImage(){
        return m_bConvImage;
    }

    public  void ShowKernel(int Type){
        if(!IsInit()) {
            System.out.println("Error: the gabor kernel has not been created!\n");
        }
        else {
            IplImage pImage;
            pImage = GetKernelImage(Type);
            cvNamedWindow("Testing",1);
            cvShowImage("Testing",pImage);
            cvWaitKey(0);
        }

    }

    public  IplImage GetKernelImage(int Type){
        int Width = m_lWidth;
        if(IsKernelCreate() == false){
            System.out.println("Error: the Gabor kernel has not been created in get_image()!\n");
            return null;
        }
        else{
            IplImage pImage;
            IplImage newimage;
            newimage = cvCreateImage(cvSize(Width,Width), IPL_DEPTH_8U, 1 );
            //printf("Width is %d.\n",(int)Width);
            //printf("Sigma is %f.\n", Sigma);
            //printf("F is %f.\n", F);
            //printf("Phi is %f.\n", Phi);

            //pImage = gan_image_alloc_gl_d(Width, Width);
            pImage = cvCreateImage( cvSize(Width,Width), IPL_DEPTH_32F, 1 );


            CvMat kernel = cvCreateMat(Width, Width, CV_32FC1);
            double ve;
            switch(Type){
                case 1:  //Real

                    cvCopy( (CvMat)m_matReal, (CvMat)kernel, null );
                    //pImage = cvGetImage( (CvMat)kernel, pImageGL );
                    for (int i = 0; i < kernel.rows(); i++){
                        for (int j = 0; j < kernel.cols(); j++){
                            ve = cvGetReal2D((CvMat)kernel, i, j);
                            cvSetReal2D( (IplImage)pImage, j, i, ve );
                        }
                    }
                    break;
                case 2:  //Imag
                    cvCopy( (CvMat)m_matImag, (CvMat)kernel, null );
                    //pImage = cvGetImage( (CvMat)kernel, pImageGL );
                    for (int i = 0; i < kernel.rows(); i++){
                        for (int j = 0; j < kernel.cols(); j++){
                            ve = cvGetReal2D((CvMat)kernel, i, j);
                            cvSetReal2D( (IplImage)pImage, j, i, ve );
                        }
                    }
                    break;
                default:
                    break;
            }

            cvNormalize((IplImage)pImage, (IplImage)pImage, 0, 255, CV_MINMAX, null );


            cvConvertScaleAbs( (IplImage)pImage, (IplImage)newimage, 1, 0 );

            return newimage;
        }
    }
}
