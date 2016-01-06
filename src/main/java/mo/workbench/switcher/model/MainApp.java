package mo.workbench.switcher.model;

/**
 * Created by Moheem Ilyas on 12/29/2015.
 *
 * Last Modified: 1/2/2016 (Moheem Ilyas)
 *
 * Description: This application's intention is to provide a GUI for automatically switching Niagara versions.
 *
 */

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import mo.workbench.switcher.controller.RootLayoutController;
import mo.workbench.switcher.controller.SwitcherHomeController;

import java.io.IOException;

public class MainApp extends Application {

    Stage primaryStage;
    BorderPane rootLayout;

    public Stage getPrimaryStage() {
        return this.primaryStage;
    }

    public BorderPane getRootLayout(){ return this.rootLayout; }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Setting up the primary stage;
        this.primaryStage = primaryStage;

        primaryStage.setTitle("Welcome to Workbench Switcher.");

        // Getting the BorderPane (RootLayout) container ready to take center stage,
        // and then putting the AnchorPane (SwitcherHome) in that shit.

        initRootLayout();
        showSwitcherHome();
    }


    /**
    Initializing the BorderPane root layout, which will serve as the "home" scene for this application.
    God I love this new keyboard.
     */
    public void initRootLayout(){
        try {
            // Since Maven can be a real douche, getClassLoader as opposed to "MainApp.class.getClassLoader().getResource..."
            // If getClassLoader is not used, the relative path that the compiler is using will not be the right
            // path to the fxml.
            FXMLLoader loader = new FXMLLoader(MainApp.class.getClassLoader().getResource("fxmls/RootLayout.fxml"));

            RootLayoutController controller = new RootLayoutController();

            // There is not "fx:controller" attribute in RootLayout.fxml. According to the documentation,
            // setController has to be called prior to loading.
            loader.setController(controller);

            this.rootLayout = (BorderPane) loader.load();

            controller.setMainApp(this);

            Scene scene = new Scene(this.rootLayout);
            this.primaryStage.setScene(scene);

            primaryStage.show();



        } catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
    Putting the "switcher" view in place. The switcher view is basically where the application will search
    for the batch files given the home Niagara directory from the user, and present the user with the options to select
    the versions
     */
    public void showSwitcherHome() {
        try {
            FXMLLoader loader = new FXMLLoader(MainApp.class.getClassLoader().getResource("fxmls/SwitcherHomeLayout.fxml"));
            SwitcherHomeController switcherHomeController = new SwitcherHomeController();

            loader.setController(switcherHomeController);

            AnchorPane switcherOverview = (AnchorPane) loader.load();
            this.rootLayout.setCenter(switcherOverview);

            switcherHomeController.setMainApp(this);

            this.primaryStage.setOnCloseRequest(event -> {
                switcherHomeController.getNiagaraModel().deleteTempDir();
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
