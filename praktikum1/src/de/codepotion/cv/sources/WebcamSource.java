package de.codepotion.cv.sources;

import org.opencv.core.Mat;
import org.opencv.highgui.VideoCapture;

public class WebcamSource {
	
	private VideoCapture myCamera;
	
	public WebcamSource()
	{
		myCamera = new VideoCapture(0);
	}
	
	public Mat getFrame()
	{
		Mat newFrame = new Mat();
		myCamera.read(newFrame);
		return newFrame;
	}
}
