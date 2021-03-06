package de.codepotion.cv;

import org.opencv.core.Core;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("Praktikum1 - ComputerVision");
		try {
			Parent root = FXMLLoader.load(getClass()
					.getResource("maingui.fxml"));
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		
		//init opencv
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		launch(args);
	}
}
