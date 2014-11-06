package de.codepotion.cv.filters;

import java.io.IOException;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;

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
	public Mat useFilter(Mat input) {
		Mat outputMat = new Mat(input.rows(), input.cols(), input.type());
		Imgproc.Canny(input, outputMat, minThreshold, maxThreshold);
		return outputMat;
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
