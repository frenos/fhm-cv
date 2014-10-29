package de.codepotion.cv;

import de.codepotion.cv.sources.WebcamSource;
import javafx.fxml.FXML;
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
	
	@FXML
	public void setImage()
	{
		webcamImageView.setImage(mySource.getFrame());
	}
}
