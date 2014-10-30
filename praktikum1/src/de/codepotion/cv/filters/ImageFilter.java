package de.codepotion.cv.filters;

import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;

public abstract class ImageFilter {

	protected String filterName = "default name";
	protected BorderPane configuration = new BorderPane();
	
	public ImageFilter()
	{
		
	}
	
	public abstract Image useFilter(Image input);
	
	public String getName()
	{
		return filterName;
	}
	
	public BorderPane getConfiguration()
	{
		return configuration;
	}
}
