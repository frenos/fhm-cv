package de.codepotion.cv.filters;

import java.io.IOException;

import org.opencv.core.Mat;

import de.codepotion.cv.FilterManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class BuildAFilter extends ImageFilter {

	private ObservableList<ImageFilter> availableFilters = FXCollections.observableArrayList();
	private ObservableList<ImageFilter> activeFilters = FXCollections.observableArrayList();

	private Stage configStage = new Stage();
	private Scene configScene = new Scene(new BorderPane());
	
	public BuildAFilter() {
		this.filterName = "Build a Filter";
		configStage.setScene(configScene);
		buildGui();
	}

	@SuppressWarnings("unchecked")
	public void buildGui() {
		FXMLLoader myLoader = new FXMLLoader(getClass().getResource("BuildAFilterConfiguration.fxml"));
		try {
			configuration = myLoader.load();

			ListView<ImageFilter> availableListview = (ListView<ImageFilter>) myLoader.getNamespace().get("availablelist");
			ListView<ImageFilter> activeListview = (ListView<ImageFilter>) myLoader.getNamespace().get("activelist");

			availableListview.setItems(availableFilters);
			activeListview.setItems(activeFilters);

			Button addButton = (Button) myLoader.getNamespace().get("addButton");
			Button removeButton = (Button) myLoader.getNamespace().get("removeButton");
			Button configButton = (Button) myLoader.getNamespace().get("configButton");

			addButton.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					activeFilters.add(availableListview.getSelectionModel().getSelectedItem());
				}
			});

			removeButton.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					if (!activeFilters.isEmpty() && activeListview.getSelectionModel().getSelectedItem() != null) {
						activeFilters.remove(activeListview.getSelectionModel().getSelectedIndex());
					}
				}

			});

			configButton.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					if(activeListview.getSelectionModel().getSelectedItem() == null)
					{
						activeListview.getSelectionModel().selectFirst();
					}
					configScene.setRoot(activeListview.getSelectionModel().getSelectedItem().getConfiguration());
					configStage.show();
				}
			});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void updateAvailableFilters() {
		availableFilters.clear();
		availableFilters.addAll(FilterManager.getInstance().getFilters());
	}

	@Override
	public Mat useFilter(Mat input) {
		ObservableList<ImageFilter> tempList= FXCollections.observableArrayList();
		tempList.addAll(activeFilters);
		Mat temp = input;
		for (ImageFilter currentFilter : tempList) {
			temp = currentFilter.useFilter(temp);
		}

		return temp;
	}

	@Override
	public BorderPane getConfiguration() {
		updateAvailableFilters();
		return configuration;
	}

}
