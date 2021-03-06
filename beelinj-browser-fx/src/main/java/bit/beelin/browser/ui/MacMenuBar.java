package bit.beelin.browser.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;

import de.codecentric.centerdevice.MenuToolkit;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

/**
 *
 */
public class MacMenuBar {

    String appName;

    public MacMenuBar(String appName) {
        this.appName = appName;

        MenuToolkit tk = MenuToolkit.toolkit();

        MenuBar bar = new MenuBar();

        // Application Menu
        // TBD: services menu
        Menu appMenu = new Menu(appName); // Name for appMenu can't be set at
        // Runtime
        MenuItem aboutItem = new MenuItem("About " + appName);
        aboutItem.setOnAction(this::aboutHandler);
        MenuItem prefsItem = new MenuItem("Preferences...");
        appMenu.getItems().addAll(aboutItem, new SeparatorMenuItem(), prefsItem, new SeparatorMenuItem(),
                tk.createHideMenuItem(appName), tk.createHideOthersMenuItem(), tk.createUnhideAllMenuItem(),
                new SeparatorMenuItem(), tk.createQuitMenuItem(appName));

        // File Menu (items TBD)
        Menu fileMenu = new Menu("File");
        MenuItem newItem = new MenuItem("New...");
        fileMenu.getItems().addAll(newItem, new SeparatorMenuItem(), tk.createCloseWindowMenuItem(),
                new SeparatorMenuItem(), new MenuItem("TBD"));

        // Edit (items TBD)
        Menu editMenu = new Menu("Edit");
        editMenu.getItems().addAll(new MenuItem("TBD"));

        // Format (items TBD)
        Menu formatMenu = new Menu("Format");
        formatMenu.getItems().addAll(new MenuItem("TBD"));

        // View Menu (items TBD)
        Menu viewMenu = new Menu("View");
        viewMenu.getItems().addAll(new MenuItem("TBD"));

        // Window Menu
        // TBD standard window menu items
        Menu windowMenu = new Menu("Window");
        windowMenu.getItems().addAll(tk.createMinimizeMenuItem(), tk.createZoomMenuItem(), tk.createCycleWindowsItem(),
                new SeparatorMenuItem(), tk.createBringAllToFrontItem());

        // Help Menu (items TBD)
        Menu helpMenu = new Menu("Help");
        helpMenu.getItems().addAll(new MenuItem("TBD"));

        bar.getMenus().addAll(appMenu, fileMenu, editMenu, formatMenu, viewMenu, windowMenu, helpMenu);

        tk.autoAddWindowMenuItems(windowMenu);
        tk.setGlobalMenuBar(bar);
    }

    public void aboutHandler(ActionEvent event) {
        Parent root;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("about.fxml"));
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        Stage stage = new Stage(StageStyle.UNDECORATED);
        stage.setWidth(600);
        stage.setHeight(400);
        stage.setResizable(false);

        Scene scene = new Scene(root, 600, 400);
        scene.setOnMousePressed(mouseEvent -> {
            stage.close();
        });

        stage.setScene(scene);
        stage.show();
    }

}
