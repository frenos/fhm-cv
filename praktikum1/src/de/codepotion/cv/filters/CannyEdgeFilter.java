package de.codepotion.cv.filters;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import de.codepotion.cv.ImageHelper;
import javafx.scene.image.Image;

public class CannyEdgeFilter extends ImageFilter{
	
	private int minThreshold = 35;
	private int maxThreshold = 75;
	
	public CannyEdgeFilter() {
		this.filterName = "Canny Edge Filter";
		
	}

	@Override
	public Image useFilter(Image input) {
		Mat inputMat = ImageHelper.image2Mat(input);
		Mat outputMat = new Mat(inputMat.rows(), inputMat.cols(), inputMat.type());
		Imgproc.Canny(inputMat, outputMat, minThreshold, maxThreshold);
		return ImageHelper.mat2Image(outputMat);
	}

}
