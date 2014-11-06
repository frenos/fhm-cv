package de.codepotion.cv.filters;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class GrayscaleFilter extends ImageFilter {

	public GrayscaleFilter() {
		this.filterName = "Grayscale Filter";
	}

	@Override
	public Mat useFilter(Mat input) {
		Mat outputMat = new Mat(input.rows(),input.cols(),CvType.CV_8UC1);
		Imgproc.cvtColor(input, outputMat, Imgproc.COLOR_BGR2GRAY);
		
		return outputMat;
	}

}
