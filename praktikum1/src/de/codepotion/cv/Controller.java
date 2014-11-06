package de.codepotion.cv;

import java.util.Timer;
import java.util.TimerTask;

import org.opencv.core.Mat;

import de.codepotion.cv.filters.ImageFilter;
import de.codepotion.cv.sources.WebcamSource;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

public class Controller {

	// GUI VARIABLES
	@FXML
	ImageView webcamImageView;
	@FXML
	AnchorPane webcamPane;
	@FXML
	ImageView filteredImageView;
	@FXML
	AnchorPane filteredPane;

	@FXML
	ChoiceBox<ImageFilter> filterBox;
	ObservableList<ImageFilter> availableFilters = FXCollections.observableArrayList();
	
	@FXML
	BorderPane configurationPane;
	
	WebcamSource mySource;
	Timer backgroundServ;
	boolean isCapturing = false;
	ImageFilter activeFilter;
	
	public Controller() {
		mySource = new WebcamSource();
		availableFilters.addAll(FilterManager.getInstance().getFilters());
		activeFilter = availableFilters.get(0);
	}

	public void initialize() {
		webcamImageView.fitHeightProperty().bind(webcamPane.heightProperty());
		webcamImageView.fitWidthProperty().bind(webcamPane.widthProperty());

		filteredImageView.fitHeightProperty().bind(
				filteredPane.heightProperty());
		filteredImageView.fitWidthProperty().bind(filteredPane.widthProperty());

		filterBox.setItems(availableFilters);
		filterBox.getSelectionModel().selectFirst();
		configurationPane.setCenter(activeFilter.getConfiguration());
		bindFilterBox();
	}

	public void bindFilterBox()
	{
		filterBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ImageFilter>() {

			@Override
			public void changed(ObservableValue<? extends ImageFilter> observable, ImageFilter oldValue, ImageFilter newValue) {
				activeFilter = newValue;
				configurationPane.setCenter(activeFilter.getConfiguration());
			}
		});
	}
	
	public void setWebcamImage(Image input) {
		webcamImageView.setImage(input);
	}

	public void setFilterImage(Image input) {
		filteredImageView.setImage(input);
	}

	@FXML
	public void toggleCapture() {
		if (isCapturing) {
			backgroundServ.cancel();
			isCapturing = false;
		} else {
			isCapturing = true;
			// create new timer because canceled timers cant be restarted
			backgroundServ = new Timer(true);
			
			backgroundServ.schedule(new TimerTask() {

				@Override
				public void run() {
					// get Image before
					Mat temp = mySource.getFrame();
					Image filtered = ImageHelper.mat2Image(activeFilter.useFilter(temp));
					Image webcam = ImageHelper.mat2Image(temp);
					Platform.runLater(new Runnable() {

						@Override
						public void run() {
							setWebcamImage(webcam);
							setFilterImage(filtered);
						}
					});

				}
			}, 1, 1);
		}
	}
}
