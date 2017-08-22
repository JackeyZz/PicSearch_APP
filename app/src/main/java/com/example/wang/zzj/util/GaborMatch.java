package com.example.wang.zzj.util;
import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_highgui.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;


public class GaborMatch {
    public static class  GABOR_FILTER{
        private CvMat realmat;
        private CvMat imagmat;
        public CvMat getRealmat() {
            return realmat;
        }
        public void setRealmat(CvMat realmat) {
            this.realmat = realmat;
        }
        public CvMat getImagmat() {
            return imagmat;
        }
        public void setImagmat(CvMat imagmat) {
            this.imagmat = imagmat;
        }
    }
    public static int REAL= 1;
    public static int IMAG= 2;
    public static int MAG=  3;
    private static double PI=3.14159265;
    private static double	m_fSigma;
    private static int[]	m_lScale=new int[3];		//尺度数组
    private static int	m_nAngleNum;        //相位个数
    //int			m_nFilterNum;       //Gabor 滤波器个数
    private static GABOR_FILTER[] m_FilterArray=new GABOR_FILTER[48];   //滤波器组
    private static boolean	m_bCreateFilter;
    GaborMatch(){
        m_fSigma= PI ;
        m_lScale[0] = 2;
        m_lScale[1] = 4;
        m_lScale[2] = 8;
        m_nAngleNum = 8;        //相位个数
        m_bCreateFilter =false;
    }
    public  static boolean CreateFilterArray(){
        m_bCreateFilter = false;
//		for (int i=0; i<3; i++)
        for (int j=0; j<m_nAngleNum; j++){
            CGabor gabor=new CGabor(m_fSigma,1 /*m_lScale[i]*/, j*PI/8);
            gabor.Init();
            m_FilterArray[j]=new GABOR_FILTER();
            m_FilterArray[j].setRealmat(gabor.GetMatrix(REAL));
            m_FilterArray[j].setImagmat(gabor.GetMatrix(IMAG));

        }
        m_bCreateFilter = true;
        return true;
    }

    public static double[] CreateImageFeatureVec(IplImage src){
        //cvNamedWindow("test", 1);
        if (!m_bCreateFilter){
            CreateFilterArray();
        }

        double[] Vector=new double[16];
        IplImage dst = cvCreateImage(cvSize(src.width(), src.height()), 8, 1);
//		for (int i=0; i<3; i++)
        for (int j=0; j<m_nAngleNum; j++){
            CGabor gabor=new CGabor(m_fSigma, 1,j*PI/8);
            gabor.Init();
            //CGabor gabor(m_fSigma, m_lScale[i], j*CV_PI/8);
            gabor.ConvImage(src, dst, 3, m_FilterArray[j].realmat, m_FilterArray[j].imagmat);
            Vector[(j)*2] = GetImgEx(dst);
            Vector[(j)*2+1] = GetImgVa(dst);
        }
        //cvDestroyAllWindows();
        return Vector;
    }

    public static double  GetImgEx(IplImage src){
        double ex = 0;
        for (int i = 0; i < src.width(); i++){
            for (int j = 0; j < src.height(); j++){
                ex += cvGetReal2D((IplImage)src, j, i);
            }
        }
        return ex/(src.width() * src.height());
    }

    public static double GetImgVa(IplImage src){
        double ex = GetImgEx(src);
        double va = 0;
        for (int i = 0; i < src.width(); i++){
            for (int j = 0; j < src.height(); j++)
            {
                va += Math.pow((cvGetReal2D((IplImage) src, j, i) - ex), 2);
            }
        }
        return Math.sqrt(va/(src.width() * src.height()));
    }

    public static Double calGaborDiff(String str){

        double dbR =0; //相似度
        double dSigmaST =0;
        double dSigmaS =0;
        double dSigmaT =0;
        double[] Sa=calGabor(str);
        double[] Ta=calGabor(Constant.ORG_PATH + "/tem.t");
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
    public static double[] calGabor(String filename){
        m_fSigma= PI ;
        IplImage image = cvLoadImage(filename);
        IplImage grayimage = cvCreateImage(cvSize(image.width(), image.height()), 8, 1);
        cvCvtColor(image, grayimage, CV_BGRA2GRAY);
        IplImage normalsizedst;			//目标图像指针
        CvSize dst_cvsize=cvSize(64, 64);			//目标图像尺寸
        normalsizedst = cvCreateImage( dst_cvsize, grayimage.depth(), grayimage.nChannels());	//构造目标图象
        cvResize(grayimage, normalsizedst, CV_INTER_LINEAR);
        m_nAngleNum = 8;        //相位个数
        m_bCreateFilter =false;

        CreateFilterArray();
        double[] gabor= CreateImageFeatureVec(normalsizedst);
        CvMat src=cvCreateMat(1, gabor.length,CV_32FC1);
        CvMat dst=cvCreateMat(1, gabor.length,CV_32FC1);
        for (int i=0;i<gabor.length;i++){
            src.put(0, i, gabor[i]);
        }
        cvNormalize(src,dst, 1, 0, CV_C, null );
        for (int i=0;i<gabor.length;i++){
            gabor[i]=dst.get(0, i);
        }
        return  sorting(gabor);
    }
    public static double[] sorting(double[] x) {
        for (int i = 0; i < x.length; i++) {
            for (int j = i + 1; j < x.length; j++) {
                if (x[i] > x[j]) {
                    double temp = x[i];
                    x[i] = x[j];
                    x[j] = temp;
                }
            }
        }
        return  x;
    }
}
