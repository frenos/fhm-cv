package de.codepotion.cv.filters;

import org.opencv.core.Mat;

public class DummyFilter extends ImageFilter {

	public DummyFilter() {
		this.filterName = "Dummy Filter";
	}

	@Override
	public Mat useFilter(Mat input) {
		return input;
	}

}
