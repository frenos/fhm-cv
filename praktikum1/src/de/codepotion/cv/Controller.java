package de.codepotion.cv;

import java.util.Timer;
import java.util.TimerTask;

import de.codepotion.cv.sources.WebcamSource;
import javafx.application.Platform;
import javafx.fxml.FXML;
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

	WebcamSource mySource;
	Timer backgroundServ;
	boolean isCapturing = false;

	public Controller() {
		mySource = new WebcamSource();
	}

	public void initialize() {
		webcamImageView.fitHeightProperty().bind(webcamPane.heightProperty());
		webcamImageView.fitWidthProperty().bind(webcamPane.widthProperty());

		filteredImageView.fitHeightProperty().bind(
				filteredPane.heightProperty());
		filteredImageView.fitWidthProperty().bind(filteredPane.widthProperty());
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
					Platform.runLater(new Runnable() {

						@Override
						public void run() {
							setWebcamImage(temp);
							setFilterImage(temp);
						}
					});

				}
			}, 1, 1);
		}
	}
}
