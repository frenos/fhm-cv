package de.codepotion.cv.filters;

import java.io.IOException;
import java.util.Vector;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

public class HistogramFilter extends ImageFilter{

	private Mat b_histogram = new Mat();
	private Mat g_histogram = new Mat();
	private Mat r_histogram = new Mat();
	private int number_bins = 8;
	
	public HistogramFilter() {
		this.filterName = "Histogram";
		try {
			this.configuration = FXMLLoader.load(getClass().getResource("HistogramConfiguration.fxml"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		buildGui();
		
	}
	
	public void buildGui()
	{
		TextField binNumberText = (TextField) configuration.lookup("#binNumberText");
		binNumberText.setText("8");
		Slider binNumberSlider = (Slider) configuration.lookup("#binNumberSlider");
		binNumberSlider.setValue(8);
		binNumberSlider.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				binNumberText.setText(String.valueOf(newValue.intValue()));
				number_bins = newValue.intValue();
			}
		});

		binNumberText.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				number_bins = Integer.parseInt(newValue);
				binNumberSlider.setValue(number_bins);
			}
		});
	}
	
	@Override
	public Mat useFilter(Mat input) {
		
		Vector<Mat> splittedChannels = new Vector<Mat>();
		
		MatOfInt hist_size = new MatOfInt(number_bins);
		final MatOfFloat hist_range = new MatOfFloat(0f,256f);
		
		Core.split(input, splittedChannels);
		
		Imgproc.calcHist(splittedChannels, new MatOfInt(0), new Mat(), b_histogram, hist_size, hist_range, false);
		Imgproc.calcHist(splittedChannels, new MatOfInt(1), new Mat(), g_histogram, hist_size, hist_range, false);
		Imgproc.calcHist(splittedChannels, new MatOfInt(2), new Mat(), r_histogram, hist_size, hist_range, false);
		
		int hist_w = 800;
		int hist_h = 600;
		//offset zum rand damit max und min nicht direkt auf dem bildrand liegen
		int offset = 20;
		//Weite einer gesammten Kategorie (B+G+R)
		int bin_w =(int) Math.round((double)hist_w/number_bins);
		//Weite einer einzelnen Farbe
		int bin_column = (int) Math.round((double)bin_w/3);
		
		Mat outputImage = new Mat(hist_h+2*offset, hist_w, CvType.CV_8UC3, new Scalar(0,0,0));
		
		Core.normalize(b_histogram, b_histogram, 0,hist_h, Core.NORM_MINMAX);
		Core.normalize(g_histogram, g_histogram, 0,hist_h, Core.NORM_MINMAX);
		Core.normalize(r_histogram, r_histogram, 0,hist_h, Core.NORM_MINMAX);
		
		for (int i = 0; i < number_bins; i++)
		{
			Core.rectangle(outputImage, new Point(bin_w*i, offset+hist_h), new Point(bin_w*i+bin_column   , hist_h - b_histogram.get(i,0)[0]+offset), new Scalar(255,0,0), Core.FILLED);
			Core.rectangle(outputImage, new Point(bin_w*i+bin_column, offset+hist_h), new Point(bin_w*i+bin_column*2   , hist_h - g_histogram.get(i,0)[0]+offset), new Scalar(0,255,0), Core.FILLED);
			Core.rectangle(outputImage, new Point(bin_w*i+bin_column*2, offset+hist_h), new Point(bin_w*i+bin_column*3   , hist_h - r_histogram.get(i,0)[0]+offset), new Scalar(0,0,255), Core.FILLED);
		}
		
		return outputImage;
	}

}
