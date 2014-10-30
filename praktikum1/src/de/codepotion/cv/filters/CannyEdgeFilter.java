package de.codepotion.cv.filters;

import java.io.IOException;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import de.codepotion.cv.ImageHelper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;

public class CannyEdgeFilter extends ImageFilter {

	private int minThreshold = 35;
	private int maxThreshold = 75;

	public CannyEdgeFilter() {
		this.filterName = "Canny Edge Filter";
		try {
			this.configuration = FXMLLoader.load(getClass().getResource("CannyEdgeConfiguration.fxml"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		buildGui();
	}

	@Override
	public Image useFilter(Image input) {
		Mat inputMat = ImageHelper.image2Mat(input);
		Mat outputMat = new Mat(inputMat.rows(), inputMat.cols(), inputMat.type());
		Imgproc.Canny(inputMat, outputMat, minThreshold, maxThreshold);
		return ImageHelper.mat2Image(outputMat);
	}

	public void buildGui()
	{
		Slider minSlider = (Slider) configuration.lookup("#minSlider");
		minSlider.setValue(minThreshold);
		Slider maxSlider = (Slider) configuration.lookup("#maxSlider");
		maxSlider.setValue(maxThreshold);
		
		Label minLabel = (Label) configuration.lookup("#minLabel");
		minLabel.setText(String.valueOf(minThreshold));
		Label maxLabel = (Label) configuration.lookup("#maxLabel");
		maxLabel.setText(String.valueOf(maxThreshold));
		
		
		minSlider.valueProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				minThreshold = newValue.intValue();
				minLabel.setText(String.valueOf(minThreshold));
			}
		});
		
		maxSlider.valueProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				maxThreshold = newValue.intValue();
				maxLabel.setText(String.valueOf(maxThreshold));
				
			}
		});
	}
}
