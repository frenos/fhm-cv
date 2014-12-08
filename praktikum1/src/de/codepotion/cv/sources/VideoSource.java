package de.codepotion.cv.sources;

import java.io.File;

import javafx.stage.FileChooser;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.highgui.VideoCapture;

public class VideoSource implements InputSource {

	private VideoCapture myVideoFile;
	private String name = "VideoSource";
	private String inputFile;

	public VideoSource() {
	}

	@Override
	public Mat getFrame() {
		Mat newFrame = new Mat();
		myVideoFile.read(newFrame);
		if (newFrame.cols() == 0) {
			myVideoFile.open(inputFile);
			myVideoFile.read(newFrame);
		}
		return newFrame;
	}

	@Override
	public void reload() {
		FileChooser fc = new FileChooser();
		File file = fc.showOpenDialog(null);
		if (file != null) {
			myVideoFile = new VideoCapture(file.getPath());
			inputFile = file.getPath();
		}
	}

	public String toString() {
		return name;
	}
}
