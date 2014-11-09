package de.codepotion.cv.sources;

import java.io.File;

import javafx.stage.FileChooser;

import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;

public class ImageSource implements InputSource {
	
	private Mat image;
	private String name= "ImageSource";
	
	@Override
	public Mat getFrame() {
		return image;
	}

	@Override
	public void reload() {
		// TODO Auto-generated method stub
		FileChooser fc = new FileChooser();
		File file = fc.showOpenDialog(null);
		if ( file != null)
		{
			image = Highgui.imread(file.getPath());
		}
		
	}
	
	public String toString()
	{
		return name;
	}

}
