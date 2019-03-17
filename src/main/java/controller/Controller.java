package controller;

import domain.Map;
import domain.Properties;
import domain.Text;
import domain.Track;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import service.MapService;
import service.TrackService;

import java.io.*;
import java.time.LocalTime;

/**
 * Created by Jakub Filipiak on 06.03.2019.
 */
public class Controller {

    private Map map = Map.INSTANCE;
    private MapService mapService = MapService.INSTANCE;
    private Track track = Track.INSTANCE;
    private TrackService trackService = TrackService.INSTANCE;
    private Text text = Text.INSTANCE;
    private Properties properties = Properties.INSTANCE;

    @FXML private TextField mapFileNameTextField;
    @FXML private TextField bottomLeftCornerLatitudeTextField;
    @FXML private TextField bottomLeftCornerLongitudeTextField;
    @FXML private TextField upperRightCornerLatitudeTextField;
    @FXML private TextField upperRightCornerLongitudeTextField;
    @FXML private TextField trackFileNameTextField;

    @FXML private Label correctnessOfAllowedColors;
    @FXML private Label correctnessOfForbiddenColors;
    private Label correctnessOfCoordinates;

    @FXML private Button checkConfigurationCorrectnessButton;
    @FXML private Button chooseTrackFileButton;
    @FXML private Button generateForbiddenZonesButton;

    @FXML private TextArea logTextArea;

    @FXML private ComboBox<String> existingConfigurationsComboBox =
            new ComboBox<>();

    //

    @FXML private TextField dropStartTimeTextField;
    @FXML private TextField dropStopTimeTextField;
    @FXML private TextField minimumAngleTextField;
    @FXML private TextField ignoredTurnsMinValueTextField;
    @FXML private TextField ignoredTurnsMaxValueTextField;
    @FXML private TextField timeBufferTextField;
    @FXML private CheckBox dropOnTurnsCheckBox;


    @FXML
    public void updateConfigurationsComboBox() {
        existingConfigurationsComboBox.getItems().add("Domyślna");
    }

    @FXML
    public void loadChosenConfiguration() {
        String chosenConfiguration = existingConfigurationsComboBox.getValue();
        if (chosenConfiguration.equals("Domyślna")) {

            chooseTrackFileButton.setDisable(false);
            logTextArea.appendText('\n' + "Załadowano domyślną konfigurację! " +
                    "Wybierz plik trasy...");
        }
    }

    @FXML
    public void chooseMapFile() throws FileNotFoundException {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Wybierz plik mapy");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(
                "PNG", "*.png"));
        File mapFileTmp = fileChooser.showOpenDialog(null);

        if (mapFileTmp != null) {
            System.out.println("Map file: " + mapFileTmp.getAbsolutePath());

            InputStream inputStream = new FileInputStream(mapFileTmp);
            map.setMapFileInputStream(inputStream);
            mapFileNameTextField.setText(mapFileTmp.getName());

            checkConfigurationCorrectnessButton.setDisable(false);
            logTextArea.appendText('\n' + "Wybrano plik mapy: " + mapFileTmp.getName());
            logTextArea.appendText('\n' + "Wprowadź współrzędne narożników mapy w formie dziesiętnej, np. 52.503133...");
        }
    }

    @FXML
    public void checkConfigurationCorrectness() {

        String bottomLeftCornerLatitude = bottomLeftCornerLatitudeTextField.getText();
        String bottomLeftCornerLongitude = bottomLeftCornerLongitudeTextField.getText();
        String upperRightCornerLatitude = upperRightCornerLatitudeTextField.getText();
        String upperRightCornerLongitude = upperRightCornerLongitudeTextField.getText();

        boolean allCoordinatesAreFilled =
                !bottomLeftCornerLatitude.isEmpty() &&
                !bottomLeftCornerLongitude.isEmpty() &&
                !upperRightCornerLatitude.isEmpty() &&
                !upperRightCornerLongitude.isEmpty();

        if (!allCoordinatesAreFilled) {
            logTextArea.appendText('\n' + "Uzupełnij wszystkie współrzędne i " +
                    "sprawdź poprawność ponownie!");
        } else {
            logTextArea.appendText('\n' + "Trwa analiza mapy, proszę czekać...");
        }

        if (allCoordinatesAreFilled && mapService.checkMapColors()) {

            map.setBottomLeftCornerLatitude(Float.parseFloat(bottomLeftCornerLatitude));
            map.setBottomLeftCornerLongitude(Float.parseFloat(bottomLeftCornerLongitude));
            map.setUpperRightCornerLatitude(Float.parseFloat(upperRightCornerLatitude));
            map.setBottomLeftCornerLongitude(Float.parseFloat(upperRightCornerLongitude));

            correctnessOfAllowedColors.setText("Obszary dozwolone: OK");
            correctnessOfForbiddenColors.setText("Obszary zabronione: OK");
            logTextArea.appendText('\n' + "Kolory mapy oraz format współrzędnych są poprawne. Wybierz plik trasy...");
            chooseTrackFileButton.setDisable(false);
        } else {
            correctnessOfAllowedColors.setText("Obszary dozwolone: BŁĄD!");
            correctnessOfForbiddenColors.setText("Obszary zabronione: BŁĄD!");
            logTextArea.appendText('\n' + "Błąd konfiguracji mapy!");
        }
    }

    @FXML
    public void chooseTrackFile() {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Wybierz plik trasy");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(
                "TRK", "*.trk"));
        File trackFileTmp = fileChooser.showOpenDialog(null);

        if (trackFileTmp != null) {
            System.out.println("Track file: " + trackFileTmp.getAbsolutePath());
            track.setTrackFile(trackFileTmp);
            trackFileNameTextField.setText(trackFileTmp.getName());

            generateForbiddenZonesButton.setDisable(false);
            logTextArea.appendText('\n' + "Załadowano plik: " + trackFileTmp.getName() + ". Wygeneruj zestaw stref zakazanych...");
        }
    }

    @FXML
    public void generateForbiddenZones() {

        FileChooser saveFileChooser = new FileChooser();
        saveFileChooser.setTitle("Zapisz plik ze strefami zakazanymi");
        saveFileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("TXT", "*.txt"));
        saveFileChooser.setInitialFileName("strefy zakazane");
        File textFileTmp = saveFileChooser.showSaveDialog(null);

        //updateConfiguration();

        if (textFileTmp != null) {
            System.out.println("Text file: " + textFileTmp.getAbsolutePath());
            text.setTextFile(textFileTmp);
            try {
                trackService.processFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void updateConfiguration() {

        String dropStartTime = dropStartTimeTextField.getText();
        String dropStopTime = dropStopTimeTextField.getText();
        String minimumAngle = minimumAngleTextField.getText();
        String ignoredTurnsMinValue = ignoredTurnsMinValueTextField.getText();
        String ignoredTurnsMaxValue = ignoredTurnsMaxValueTextField.getText();
        String timeBuffer = timeBufferTextField.getText();
        boolean dropOnTurns = dropOnTurnsCheckBox.isSelected();

        if (!dropStartTime.isEmpty()) {
            properties.setDropStartTime(LocalTime.parse(dropStartTime));
        }
        if (!dropStopTime.isEmpty()) {
            properties.setDropStopTime(LocalTime.parse(dropStopTime));
        }
        if (!minimumAngle.isEmpty()) {
            properties.setMinimumAngle(Integer.parseInt(minimumAngle));
        }
        if (!ignoredTurnsMinValue.isEmpty()) {
            properties.setIgnoredTurnsMinValue(Integer.parseInt(ignoredTurnsMinValue));
        }
        if (!ignoredTurnsMaxValue.isEmpty()) {
            properties.setIgnoredTurnsMaxValue(Integer.parseInt(ignoredTurnsMaxValue));
        }
        if (!timeBuffer.isEmpty()) {
            properties.setTimeBuffer(Integer.parseInt(timeBuffer));
        }

        properties.setDropOnTurns(dropOnTurns);
    }
}
