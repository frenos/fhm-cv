package de.codepotion.cv.filters;

import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;

public abstract class ImageFilter {

	protected String filterName = "default name";
	private AnchorPane configuration = new AnchorPane();
	
	public ImageFilter()
	{
		
	}
	
	public abstract Image doFilter(Image input);
	
	public String getName()
	{
		return filterName;
	}
	
	public AnchorPane getConfiguration()
	{
		return configuration;
	}
}
