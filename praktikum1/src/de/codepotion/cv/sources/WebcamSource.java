package de.codepotion.cv.sources;

import org.opencv.core.Mat;
import org.opencv.highgui.VideoCapture;

public class WebcamSource implements InputSource{
	
	private VideoCapture myCamera;
	private String name = "WebcamSource";
	
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

	@Override
	public void reload() {
		myCamera = new VideoCapture(0);
	}
	
	public String toString()
	{
		return name;
	}
}
