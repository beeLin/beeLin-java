package bit.beelin.browser;

import bit.beelin.PreResolver;
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

    private final String startPage = "http://okturtles.bit";
    private final PreResolver resolver = new PreResolver();
    
    @Override
    public void start(Stage stage) {
        BorderPane bp = new BorderPane();
        // Load text field with default text (the start page URL)
        TextField location = new TextField(startPage);
        
        Button launch = new Button("go");
        HBox toolbar = new HBox(20, location, launch);
        toolbar.setPrefHeight(40);
        toolbar.setAlignment(Pos.BOTTOM_CENTER);
        bp.setTop(toolbar);
        
        WebView webView = new WebView();
        bp.setCenter(webView);
        WebEngine engine = webView.getEngine();
        engine.load(resolveLocation(startPage));
        Worker loadWorker = engine.getLoadWorker();
        loadWorker.stateProperty().addListener(e -> {
            Worker.State state = loadWorker.getState();
            if (state == Worker.State.RUNNING) {
                launch.setDisable(true);
            } else {
                launch.setDisable(false);
            }
//            System.out.println("new state for worker = " + loadWorker.getState());
        });
        launch.setOnAction(e -> {
            String resolved = resolveLocation(location.getText());
            engine.load(resolved);
            
        });
        
        Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
        Scene scene = new Scene(bp, visualBounds.getWidth(), visualBounds.getHeight());
        
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Resolve input location into something standard that the WebEngine
     * can handle.
     *
     * @param input The input location from the URL TextField
     * @return A URL string that WebEngine (standard Java libs) can resolve.
     */
    String resolveLocation(String input) {
        return resolver.resolve(input);
    }
    
}
