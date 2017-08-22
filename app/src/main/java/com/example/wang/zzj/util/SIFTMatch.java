package com.example.wang.zzj.util;

import com.googlecode.javacv.cpp.opencv_core.CvMat;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import com.googlecode.javacv.cpp.opencv_features2d.BFMatcher;
import com.googlecode.javacv.cpp.opencv_features2d.DMatchVectorVector;
import com.googlecode.javacv.cpp.opencv_features2d.DescriptorExtractor;
import com.googlecode.javacv.cpp.opencv_features2d.FeatureDetector;
import com.googlecode.javacv.cpp.opencv_features2d.KeyPoint;
import com.googlecode.javacv.cpp.opencv_nonfree.SIFT;

import static com.googlecode.javacv.cpp.opencv_core.CV_32F;
import static com.googlecode.javacv.cpp.opencv_core.cvCreateMat;
import static com.googlecode.javacv.cpp.opencv_highgui.cvLoadImage;

public class SIFTMatch {
	public static CvMat featureDetect(String path) {
		IplImage image = cvLoadImage(path);
//		opencv_nonfree.SURF surf = new opencv_nonfree.SURF();
		SIFT sift = new SIFT(0,3,0.04,10,1.6);
//		SIFT sift = new SIFT();
		FeatureDetector featureDetector  = sift.getFeatureDetector();
		KeyPoint keypoints = new KeyPoint(); 
		featureDetector.detect(image, keypoints, null); 
		 
		DescriptorExtractor extractor  = sift.getDescriptorExtractor(); 		
        CvMat descriptors = cvCreateMat(keypoints.capacity(), extractor.descriptorSize(), CV_32F);         
        extractor.compute(image, keypoints, descriptors); 
        
        if(keypoints.isNull() || descriptors.isNull()) {
        	//System.out.println("feature is Null");
        	return null;
        }
        
        //System.out.println("Keypoints found: "+ keypoints.capacity()); 
       // System.out.println("Descriptors calculated: "+descriptors.rows()); 
        return descriptors;
		
	}
	public static double match(CvMat d1,CvMat d2,String path){
		double i1 = Ratio_match(d1,d2,path);
//		double i2 = Ratio_match(d2,d1,path);
//		return i1>i2?i1:i2;
		return i1;
	}
	public static double Ratio_match(CvMat d1,CvMat d2,String path){
		BFMatcher matcher = new BFMatcher(4, false);

		DMatchVectorVector matches = new DMatchVectorVector();
		if(d2 != null && d1 != null) {
			matcher.knnMatch(d1, d2, matches, 2, null, true);	
			//matches.size()
			int count = 0;
			for(int i=0; i<matches.size()-1; i++) {
				if(matches.size(i) > 1) {
					if(matches.get(i, 0).distance()/matches.get(i, 1).distance() < 0.90)
						count++;
				}
			}
			return (double)count/(double)matches.size();
		}
		
		return 1.0;
	}







	
}