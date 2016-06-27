package bit.beelin.browser;

import bit.beelin.browser.ui.MacMenuBar;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main extends Application {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    private static final String appName = "beeLīn";
    private static final String startPage = "http://msgilligan.bit";

    private MacMenuBar macMenuBar;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() {
        log.info("init()");

        // Configure Java to first search our Namecoin NameService provider, then fall back to the default
        System.setProperty("sun.net.spi.nameservice.provider.1", "beelin-switchable,beelin");
//        System.setProperty("sun.net.spi.nameservice.provider.1", "namecoin-dnschain,beelin");
//        System.setProperty("sun.net.spi.nameservice.provider.1", "namecoin-rpc,beelin");
        System.setProperty("sun.net.spi.nameservice.provider.2", "default");

        // TODO: Add a preference setting and connect to BeelinSwitchableNameService class
        // to switch between DNSChain and Namecoin RPC or others for lookup

    }

    @Override
    public void start(Stage stage) {
        log.info("start()");
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
        engine.setOnError(e -> {
            log.error("Error: {}", e.getMessage());
        });
        engine.load(startPage);
        Worker loadWorker = engine.getLoadWorker();
        loadWorker.stateProperty().addListener(e -> {
            Worker.State state = loadWorker.getState();
            if (state == Worker.State.RUNNING) {
                launch.setDisable(true);
            } else {
                launch.setDisable(false);
            }
          log.info("new state for worker = {}", loadWorker.getState());
        });
        launch.setOnAction(e -> {
            String resolved = resolveLocation(location.getText());
            engine.load(resolved);
            
        });
        
        Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
        Scene scene = new Scene(bp, visualBounds.getWidth(), visualBounds.getHeight());
        
        stage.setScene(scene);
        stage.setTitle(appName);
        stage.show();

        macMenuBar = new MacMenuBar(appName);
    }

    /**
     * Resolve input location into something standard that the WebEngine
     * can handle.
     *
     * @param input The input location from the URL TextField
     * @return A URL string that WebEngine (standard Java libs) can resolve.
     */
    String resolveLocation(String input) {
        log.info("resolveLocation() -> " + input);
        return input;   // No-op since we now have a Java NameService implementation
    }
    
}
