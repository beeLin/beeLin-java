package bit.beelin.browser;

import javafx.application.Application;
import javafx.concurrent.Worker;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Main extends Application {
    
    @Override
    public void start(Stage stage) {
        BorderPane bp = new BorderPane();
        TextField location = new TextField("https://okturtles.com");
        
        Button launch = new Button("go");
        HBox toolbar = new HBox(20, location, launch);
        toolbar.setPrefHeight(40);
        toolbar.setAlignment(Pos.BOTTOM_CENTER);
        bp.setTop(toolbar);
        
        WebView webView = new WebView();
        bp.setCenter(webView);
        WebEngine engine = webView.getEngine();
        engine.load("http://javafxports.org");
        Worker loadWorker = engine.getLoadWorker();
        loadWorker.stateProperty().addListener(e -> {
            Worker.State state = loadWorker.getState();
            if (state == Worker.State.RUNNING) {
                launch.setDisable(true);
            } else {
                launch.setDisable(false);
            }
            System.out.println("new state for worker = " + loadWorker.getState());
        });
        launch.setOnAction(e -> {
            engine.load(location.getText());
            
        });
        
        Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
        Scene scene = new Scene(bp, visualBounds.getWidth(), visualBounds.getHeight());
        
        stage.setScene(scene);
        stage.show();
    }
    
}
