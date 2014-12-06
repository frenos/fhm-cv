package de.codepotion.cv;

import java.util.Timer;
import java.util.TimerTask;

import org.opencv.core.Mat;
import org.opencv.core.Rect;

import de.codepotion.cv.filters.ImageFilter;
import de.codepotion.cv.sources.ImageSource;
import de.codepotion.cv.sources.InputSource;
import de.codepotion.cv.sources.WebcamSource;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

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
	ChoiceBox<InputSource> inputBox;
	ObservableList<InputSource> availableInputs = FXCollections.observableArrayList();

	Rectangle guiSelection = new Rectangle();
	double startX, startY;

	@FXML
	ChoiceBox<ImageFilter> filterBox;
	ObservableList<ImageFilter> availableFilters = FXCollections.observableArrayList();

	@FXML
	BorderPane configurationPane;

	InputSource mySource;
	Timer backgroundServ;
	boolean isCapturing = false;
	ImageFilter activeFilter;
	Rect selection;

	public Controller() {
		mySource = new ImageSource();
		availableFilters.addAll(FilterManager.getInstance().getFilters());
		activeFilter = availableFilters.get(0);
		selection = new Rect();
		guiSelection.setFill(Color.TRANSPARENT);
		guiSelection.setStroke(Color.GREENYELLOW);
		guiSelection.setStrokeWidth(2.0);
	}

	public void initialize() {
		webcamImageView.fitHeightProperty().bind(webcamPane.heightProperty());
		webcamImageView.fitWidthProperty().bind(webcamPane.widthProperty());

		webcamImageView.setOnMouseDragged(mouseDragHandler);
		webcamImageView.setOnMousePressed(mouseClickHandler);
		webcamImageView.setOnMouseReleased(mouseReleaseHandler);

		filteredImageView.fitHeightProperty().bind(filteredPane.heightProperty());
		filteredImageView.fitWidthProperty().bind(filteredPane.widthProperty());

		fillAndBindInputBox();
		inputBox.setItems(availableInputs);
		inputBox.getSelectionModel().selectFirst();

		filterBox.setItems(availableFilters);
		filterBox.getSelectionModel().selectFirst();
		configurationPane.setCenter(activeFilter.getConfiguration());
		webcamPane.getChildren().add(guiSelection);
		bindFilterBox();
	}

	EventHandler<MouseEvent> mouseClickHandler = new EventHandler<MouseEvent>() {

		@Override
		public void handle(MouseEvent event) {
			if (event.getButton() == MouseButton.PRIMARY) {
				startX = event.getX();
				startY = event.getY();

				guiSelection.xProperty().set(event.getX());
				guiSelection.yProperty().set(event.getY());
			} else if (event.getButton() == MouseButton.SECONDARY) {
				selection = new Rect();
				guiSelection.setWidth(0);
				guiSelection.setHeight(0);
			}
		}
	};

	EventHandler<MouseEvent> mouseDragHandler = new EventHandler<MouseEvent>() {
		@Override
		public void handle(MouseEvent event) {
			guiSelection.xProperty().set(Math.min(startX, event.getX()));
			guiSelection.yProperty().set(Math.min(startY, event.getY()));

			guiSelection.widthProperty().set(Math.abs(startX - event.getX()));
			guiSelection.heightProperty().set(Math.abs(startY - event.getY()));
		}
	};

	EventHandler<MouseEvent> mouseReleaseHandler = new EventHandler<MouseEvent>() {

		@Override
		public void handle(MouseEvent event) {
			if (event.getButton() == MouseButton.PRIMARY) {
				double scaleWidthFactor = webcamImageView.getImage().getWidth() / webcamImageView.getBoundsInLocal().getWidth();
				double scaleHeightFactor = webcamImageView.getImage().getHeight()
						/ webcamImageView.getBoundsInLocal().getHeight();

				double xValue = guiSelection.xProperty().intValue() * scaleWidthFactor;
				if (xValue == 0) {
					xValue = 1;
				} else if (xValue > webcamImageView.getImage().getWidth()) {
					xValue = webcamImageView.getImage().getWidth();
				}

				double yValue = guiSelection.yProperty().intValue() * scaleHeightFactor;
				if (yValue == 0) {
					yValue = 1;
				} else if (yValue > webcamImageView.getImage().getHeight()) {
					yValue = webcamImageView.getImage().getHeight();
				}

				double widthScaled = guiSelection.widthProperty().intValue() * scaleWidthFactor;
				if (xValue + widthScaled > webcamImageView.getImage().getWidth()) {
					widthScaled = webcamImageView.getImage().getWidth() - xValue;
				}

				double heightScaled = guiSelection.heightProperty().intValue() * scaleHeightFactor;
				if (yValue + heightScaled > webcamImageView.getImage().getHeight()) {
					heightScaled = webcamImageView.getImage().getHeight() - yValue;
				}
				selection = new Rect((int) xValue, (int) yValue, (int) widthScaled, (int) heightScaled);
			}
		}
	};

	public void fillAndBindInputBox() {
		availableInputs.add(new ImageSource());
		availableInputs.add(new WebcamSource());
		inputBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<InputSource>() {

			@Override
			public void changed(ObservableValue<? extends InputSource> observable, InputSource oldValue, InputSource newValue) {
				mySource = newValue;
			}
		});
	}

	public void bindFilterBox() {
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
			mySource.reload();
			isCapturing = true;
			// create new timer because canceled timers cant be restarted
			backgroundServ = new Timer(true);

			backgroundServ.schedule(new TimerTask() {

				@Override
				public void run() {
					// get Image before
					Mat temp = mySource.getFrame();
					Image filtered;
					Rect selectTemp = selection;
					if (selectTemp.height != 0 && selectTemp.width != 0)
						filtered = ImageHelper.mat2Image(activeFilter.useFilter(temp.submat(selectTemp)));
					else
						filtered = ImageHelper.mat2Image(activeFilter.useFilter(temp));

					Image webcam = ImageHelper.mat2Image(temp);
					Platform.runLater(new Runnable() {

						@Override
						public void run() {
							setWebcamImage(webcam);
							setFilterImage(filtered);
						}
					});

				}
			}, 1, 25);
		}
	}
}
