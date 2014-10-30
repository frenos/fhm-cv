package de.codepotion.cv.filters;

import java.io.IOException;
import java.text.DecimalFormat;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import de.codepotion.cv.ImageHelper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;

public class GaussianBlurFilter extends ImageFilter{
	
	private double sigmaX=1.0;
	private double sigmaY=1.0;
	
	public GaussianBlurFilter() {
		this.filterName = "Gaussian Blur Filter";
		try {
			this.configuration = FXMLLoader.load(getClass().getResource("GaussianBlurConfiguration.fxml"));
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
		
		Imgproc.GaussianBlur(inputMat, outputMat, new Size(0,0), sigmaX, sigmaY);
		
		return ImageHelper.mat2Image(outputMat);
	}
	
	public void buildGui()
	{
		Slider sigmaxSlider = (Slider) configuration.lookup("#sigmaxSlider");
		sigmaxSlider.setValue(sigmaX);
		Slider sigmaySlider = (Slider) configuration.lookup("#sigmaySlider");
		sigmaySlider.setValue(sigmaY);
		
		Label minLabel = (Label) configuration.lookup("#sigmaxLabel");
		minLabel.setText(String.valueOf(sigmaX));
		Label maxLabel = (Label) configuration.lookup("#sigmayLabel");
		maxLabel.setText(String.valueOf(sigmaY));
		
		DecimalFormat runden = new DecimalFormat("#0.00");
		
		sigmaxSlider.valueProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				sigmaX = newValue.doubleValue();
				minLabel.setText(runden.format(sigmaX));
			}
		});
		
		sigmaySlider.valueProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				sigmaY = newValue.doubleValue();
				maxLabel.setText(runden.format(sigmaY));
				
			}
		});
	}
}
