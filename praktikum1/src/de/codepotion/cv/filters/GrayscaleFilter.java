package de.codepotion.cv.filters;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import de.codepotion.cv.ImageHelper;
import javafx.scene.image.Image;

public class GrayscaleFilter extends ImageFilter {

	public GrayscaleFilter() {
		this.filterName = "Grayscale Filter";
	}

	@Override
	public Image doFilter(Image input) {
		Mat inputMat = ImageHelper.image2Mat(input);
		Mat outputMat = new Mat(inputMat.rows(),inputMat.cols(),CvType.CV_8UC1);
		Imgproc.cvtColor(inputMat, outputMat, Imgproc.COLOR_BGR2GRAY);
		
		return ImageHelper.mat2Image(outputMat);
	}

}
