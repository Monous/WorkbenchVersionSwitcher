package mo.workbench.switcher.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import mo.workbench.switcher.model.MainApp;
import javafx.event.ActionEvent;
import mo.workbench.switcher.model.NiagaraModel;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by Moheem Ilyas on 12/29/2015.
 *
 * Last Modified: 1/2/2016 (Moheem Ilyas)
 *
 * Description: This controller class serves to connect the presentation layer with the logic for file manipulation
 *              found in the NiagaraModel class.
 */
public class SwitcherHomeController {

    // By default, the Niagara installation will default in the "C:\" drive.
    private String niagaraHome = "C:\\Niagara";
    private MainApp mainApp;
    private DirectoryChooser directoryChooser = new DirectoryChooser();
    private NiagaraModel niagaraModel;
    private ToggleGroup versionNumsGroup = new ToggleGroup();


    // Holds the version numbers that are going to be displayed;
    private ArrayList<String> versions = new ArrayList<>();
    @FXML
    private Button chooseLocation = new Button();
    @FXML
    private Button switchVersions = new Button();
    @FXML
    private TextField filePreview = new TextField();
    @FXML
    private VBox versionsVBox = new VBox();
    @FXML
    private CheckBox launchCheckBox = new CheckBox();


    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }


    /**
     * Checks to make sure the default location for the Niagara home folder exists. If it exists, the NiagaraModel
     * will be used to create a temporary file and create the necessary batch files. If it does not exist, then the
     * user will be allowed to select where the Niagara home is.
     */
    @FXML
    private void initialize() {
        File file = new File(niagaraHome);
        if (file.exists()) {
            this.niagaraModel = new NiagaraModel(file);
            filePreview.setText(niagaraHome);


            switchVersions.setOnAction(this::handleSwitchVersions);
            directoryChooser.setTitle("Locate Niagara Base Folder");
            chooseLocation.setVisible(false);
            showVersionOptions();

        }
        else {
            filePreview.setText("Where did you install Niagara?");
            chooseLocation.setVisible(true);
            chooseLocation.setOnAction(this::handleSetLocation);
            switchVersions.setVisible(false);

        }
    }

    /**
     * Shows the versions of Niagara detected in the specified Niagara home directory.
     */
    private void showVersionOptions(){
        for (String versionNum : this.niagaraModel.getVersionNumsAsStrings()) {
            RadioButton radioButton = new RadioButton(versionNum);
            // If a RadioButton is not assigned to a ToggleGroup, the user would be able to select multiple RadioButton's
            radioButton.setToggleGroup(this.versionNumsGroup);
            this.versionsVBox.getChildren().add(radioButton);
        }

    }

    /**
     * When installing Niagara for the first time, the user may have specified a location other than
     * C:\Niagara (the default directory). The application will check in the initialize() method of this class whether
     * or not the folder exists. If not, this method allows the user to specify the location.
     * @param e
     */
    private void handleSetLocation(ActionEvent e) {
        /**
         * (Don't throw an exception, just a warning (Alert).
         */
        niagaraHome = new File(directoryChooser.showDialog(this.mainApp.getPrimaryStage()).toString()).toString();

        filePreview.setText(niagaraHome);
    }

    /**
     * Finds the version of Niagara that the user wants to work in, installs it, and, if they want to launch the console,
     * the console is launched.
     * @param e
     */
    private void handleSwitchVersions(ActionEvent e) {
        // Polymorphism <3
        for (Object obj: this.versionsVBox.getChildren()){
            RadioButton radioButton = (RadioButton) obj;
            if (radioButton.isSelected()) {
                niagaraModel.runInstall(radioButton.getText());
                if (this.launchCheckBox.isSelected()) niagaraModel.launchConsole(radioButton.getText());
            }

        }

    }

    public NiagaraModel getNiagaraModel(){
        return this.niagaraModel;
    }
}