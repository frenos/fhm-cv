package de.codepotion.cv.sources;

import org.opencv.core.Mat;

public interface InputSource {

	public Mat getFrame();
	public void reload();
}
