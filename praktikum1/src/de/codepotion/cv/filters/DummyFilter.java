package de.codepotion.cv.filters;

import de.codepotion.cv.ImageHelper;
import javafx.scene.image.Image;

public class DummyFilter extends ImageFilter {

	public DummyFilter() {
		this.filterName = "Dummy Filter";
	}

	@Override
	public Image doFilter(Image input) {
		return ImageHelper.mat2Image(ImageHelper.image2Mat(input));
	}

}
