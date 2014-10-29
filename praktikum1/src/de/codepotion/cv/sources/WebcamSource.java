package de.codepotion.cv.sources;

import javafx.scene.image.Image;

import org.opencv.core.Mat;
import org.opencv.highgui.VideoCapture;

import de.codepotion.cv.ImageHelper;

public class WebcamSource {
	
	private VideoCapture myCamera;
	
	public WebcamSource()
	{
		myCamera = new VideoCapture(0);
	}
	
	public Image getFrame()
	{
		Mat newFrame = new Mat();
		myCamera.read(newFrame);
		return ImageHelper.mat2Image(newFrame);
	}
}
