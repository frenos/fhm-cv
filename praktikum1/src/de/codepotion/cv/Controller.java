package de.codepotion.cv;

import java.util.Timer;
import java.util.TimerTask;

import de.codepotion.cv.filters.DummyFilter;
import de.codepotion.cv.filters.GrayscaleFilter;
import de.codepotion.cv.filters.ImageFilter;
import de.codepotion.cv.sources.WebcamSource;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

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
	
	WebcamSource mySource;
	Timer backgroundServ;
	boolean isCapturing = false;
	ImageFilter activeFilter;
	
	public Controller() {
		mySource = new WebcamSource();
		activeFilter = new GrayscaleFilter();
		availableFilters.add(new DummyFilter());
		availableFilters.add(new GrayscaleFilter());
	}

	public void initialize() {
		webcamImageView.fitHeightProperty().bind(webcamPane.heightProperty());
		webcamImageView.fitWidthProperty().bind(webcamPane.widthProperty());

		filteredImageView.fitHeightProperty().bind(
				filteredPane.heightProperty());
		filteredImageView.fitWidthProperty().bind(filteredPane.widthProperty());

		filterBox.setItems(availableFilters);
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
					Image temp = mySource.getFrame();
					Image filtered = activeFilter.doFilter(temp);
					Platform.runLater(new Runnable() {

						@Override
						public void run() {
							setWebcamImage(temp);
							setFilterImage(filtered);
						}
					});

				}
			}, 1, 1);
		}
	}
}
