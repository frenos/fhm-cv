package de.codepotion.cv.filters;

import org.opencv.core.Mat;

import javafx.scene.layout.BorderPane;

public abstract class ImageFilter {

	protected String filterName = "default name";
	protected BorderPane configuration = new BorderPane();
	
	public ImageFilter()
	{
		
	}
	
	public abstract Mat useFilter(Mat input);
	
	public String getName()
	{
		return filterName;
	}
	
	public BorderPane getConfiguration()
	{
		return configuration;
	}
	
	@Override
	public String toString()
	{
		return filterName;
	}
}
