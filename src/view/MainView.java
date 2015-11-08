package view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
 
public class MainView extends Application {
	
	private static final double STAGE_WIDTH = 1200;
	private static final double STAGE_HEIGHT = 900;
    
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
    	
    	MainViewController mvc = new MainViewController(STAGE_WIDTH, STAGE_HEIGHT);
    	Scene scene = new Scene(mvc, STAGE_WIDTH, STAGE_HEIGHT);
    	
    	scene.getStylesheets().add(getClass().getResource("MainView.css").toExternalForm());
    	
    	stage.setTitle("Curriculum Vitae Analyzer");
    	stage.centerOnScreen();
    	
    	stage.setMaxHeight(STAGE_HEIGHT);
    	stage.setMinHeight(STAGE_HEIGHT);
    	stage.setMaxWidth(STAGE_WIDTH);
    	stage.setMinWidth(STAGE_WIDTH);
    	
    	stage.setScene(scene);
    	stage.show();
    	
    }
}