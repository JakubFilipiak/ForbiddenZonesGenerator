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

    @FXML private ChoiceBox<String> existingConfigurationChoiceBox;

    //

    @FXML private CheckBox processTimeRangeCheckBox;
    @FXML private TextField dropStartTimeTextField;
    @FXML private TextField dropStopTimeTextField;
    @FXML private CheckBox processPointsCheckBox;
    @FXML private TextField pointsInTimeBufferTextField;
    @FXML private TextField pointOutTimeBufferTextField;
    @FXML private CheckBox processTurnsCheckBox;
    @FXML private TextField minimumAngleTextField;
    @FXML private TextField turnsInTimeBufferTextField;
    @FXML private TextField turnsOutTimeBufferTextField;
    @FXML private CheckBox dropOnTurnsCheckBox;
    @FXML private TextField ignoredTurnsMinValueTextField;
    @FXML private TextField ignoredTurnsMaxValueTextField;
    @FXML private CheckBox pointsMultipliedCheckBox;
    @FXML private TextField pointsDividerTextField;
    @FXML private CheckBox finalFileFormCheckBox;



//    @FXML
//    public void readV2Config() throws IOException {
//        mapService.setMapParameters("v2");
//        chooseTrackFileButton.setDisable(false);
//        logTextArea.appendText('\n' + "Załadowano konfigurację: Domyślna v2! " +
//                "Wybierz plik trasy...");
//    }
//
//    @FXML
//    public void readV3Config() throws IOException {
//        mapService.setMapParameters("v3");
//        chooseTrackFileButton.setDisable(false);
//        logTextArea.appendText('\n' + "Załadowano konfigurację: Domyślna v3! " +
//                "Wybierz plik trasy...");
//    }

    @FXML
    public void loadChosenConfiguration() throws IOException {
        String chosenConfiguration = existingConfigurationChoiceBox.getValue();
        if (chosenConfiguration.equals("Domyślna v2")) {
            mapService.setMapParameters("v2");
            chooseTrackFileButton.setDisable(false);
            logTextArea.appendText('\n' + "Załadowano konfigurację: Domyślna v2! " +
                    "Wybierz plik trasy...");
        } else if (chosenConfiguration.equals("Domyślna v2b")) {
            mapService.setMapParameters("v2b");
            chooseTrackFileButton.setDisable(false);
            logTextArea.appendText('\n' + "Załadowano konfigurację: Domyślna v2b " +
                    "- domalowane strefy! Wybierz plik trasy...");
        } else if (chosenConfiguration.equals("Domyślna v3")) {
            mapService.setMapParameters("v3");
            chooseTrackFileButton.setDisable(false);
            logTextArea.appendText('\n' + "Załadowano konfigurację: Domyślna v3! " +
                    "Wybierz plik trasy...");
        }
    }

    @FXML
    public void chooseMapFile() throws IOException {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Wybierz plik mapy");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(
                "PNG", "*.png"));
        File mapFileTmp = fileChooser.showOpenDialog(null);

        if (mapFileTmp != null) {
            System.out.println("Map file: " + mapFileTmp.getAbsolutePath());

            InputStream inputStream = new FileInputStream(mapFileTmp);
            map.setMapFileInputStream(inputStream);

            map.setMapFile(mapFileTmp);
            System.out.println(map.getMapFile());
            System.out.println(map.getMapImage());
            mapFileNameTextField.setText(mapFileTmp.getName());

            chooseTrackFileButton.setDisable(true);
            checkConfigurationCorrectnessButton.setDisable(false);
            logTextArea.appendText('\n' + "Wybrano plik mapy: " + mapFileTmp.getName());
            logTextArea.appendText('\n' + "Wprowadź współrzędne narożników mapy w formie dziesiętnej, np. 52.503133...");
        }
    }

    @FXML
    public void checkConfigurationCorrectness() throws IOException {

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
            map.setBottomLeftCornerLatitude(Float.parseFloat(bottomLeftCornerLatitude));
            map.setBottomLeftCornerLongitude(Float.parseFloat(bottomLeftCornerLongitude));
            map.setUpperRightCornerLatitude(Float.parseFloat(upperRightCornerLatitude));
            map.setUpperRightCornerLongitude(Float.parseFloat(upperRightCornerLongitude));
        }

        if (allCoordinatesAreFilled && mapService.checkMapColors()) {

//            map.setBottomLeftCornerLatitude(Float.parseFloat(bottomLeftCornerLatitude));
//            map.setBottomLeftCornerLongitude(Float.parseFloat(bottomLeftCornerLongitude));
//            map.setUpperRightCornerLatitude(Float.parseFloat(upperRightCornerLatitude));
//            map.setBottomLeftCornerLongitude(Float.parseFloat(upperRightCornerLongitude));

            correctnessOfAllowedColors.setText("Obszary dozwolone: OK");
            correctnessOfForbiddenColors.setText("Obszary zabronione: OK");
            logTextArea.appendText('\n' + "Kolory mapy oraz format współrzędnych są poprawne. Wybierz plik trasy...");
            chooseTrackFileButton.setDisable(false);

            System.out.println("Read custom config!");
            System.out.println("Coordinates:");
            System.out.println(map.getBottomLeftCornerLatitude() + ", " + map.getBottomLeftCornerLongitude());
            System.out.println(map.getUpperRightCornerLatitude() + ", " + map.getUpperRightCornerLongitude());
            System.out.println("---");
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
            logTextArea.appendText('\n' + "Załadowano plik: " + trackFileTmp.getName() + ". Zaktualizuj ustawienia...");
        }
    }

    @FXML
    public void generateForbiddenZones() {

        FileChooser saveFileChooser = new FileChooser();
        saveFileChooser.setTitle("Zapisz plik ze strefami zakazanymi");
        saveFileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("TXT", "*.txt"));
        String txtFileName = track.getTrackFile().getName().replaceFirst("[.][^" +
                ".]+$","") + "-StrefyZakazane";
        saveFileChooser.setInitialFileName(txtFileName);
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

        boolean processTimeRange = processTimeRangeCheckBox.isSelected();
        String dropStartTime = dropStartTimeTextField.getText();
        String dropStopTime = dropStopTimeTextField.getText();

        boolean processPoints = processPointsCheckBox.isSelected();
        String pointsInTimeBuffer = pointsInTimeBufferTextField.getText();
        String pointsOutTimeBuffer = pointOutTimeBufferTextField.getText();

        boolean processTurns = processTurnsCheckBox.isSelected();
        String minimumAngle = minimumAngleTextField.getText();
        String turnsInTimeBuffer = turnsInTimeBufferTextField.getText();
        String turnsOutTimeBuffer = turnsOutTimeBufferTextField.getText();

        boolean dropOnTurns = dropOnTurnsCheckBox.isSelected();
        String ignoredTurnsMinValue = ignoredTurnsMinValueTextField.getText();
        String ignoredTurnsMaxValue = ignoredTurnsMaxValueTextField.getText();

        boolean pointsMultiplied = pointsMultipliedCheckBox.isSelected();
        String pointsDivider = pointsDividerTextField.getText();

        boolean finalFileForm = finalFileFormCheckBox.isSelected();


        if (processTimeRange) {
            properties.setProcessTimeRange(true);
            if (!dropStartTime.isEmpty())
                properties.setDropStartTime(LocalTime.parse(dropStartTime));
            if (!dropStopTime.isEmpty())
                properties.setDropStopTime(LocalTime.parse(dropStopTime));
        } else {
            properties.setProcessTimeRange(false);
        }
        if (processPoints) {
            properties.setProcessPoints(true);
            if (!pointsInTimeBuffer.isEmpty())
                properties.setPointsInTimeBuffer(Integer.parseInt(pointsInTimeBuffer));
            if (!pointsOutTimeBuffer.isEmpty())
                properties.setPointsOutTimeBuffer(Integer.parseInt(pointsOutTimeBuffer));
        } else {
            properties.setProcessPoints(false);
        }
        if (processTurns) {
            properties.setProcessTurns(true);
            if (!minimumAngle.isEmpty())
                properties.setMinimumAngle(Integer.parseInt(minimumAngle));
            if (!turnsInTimeBuffer.isEmpty())
                properties.setTurnsInTimeBuffer(Integer.parseInt(turnsInTimeBuffer));
            if (!turnsOutTimeBuffer.isEmpty())
                properties.setTurnsOutTimeBuffer(Integer.parseInt(turnsOutTimeBuffer));
        } else {
            properties.setProcessTurns(false);
        }
        if (dropOnTurns) {
            properties.setDropOnTurns(true);
            if (!ignoredTurnsMinValue.isEmpty())
                properties.setIgnoredTurnsMinValue(Integer.parseInt(ignoredTurnsMinValue));
            if (!ignoredTurnsMaxValue.isEmpty())
                properties.setIgnoredTurnsMaxValue(Integer.parseInt(ignoredTurnsMaxValue));
        } else {
            properties.setDropOnTurns(false);
        }
        if (pointsMultiplied) {
            properties.setPointsMultiplied(true);
            if (!pointsDivider.isEmpty())
                properties.setPointsDivider(Integer.parseInt(pointsDivider));
        } else {
            properties.setPointsMultiplied(false);
        }
        if (finalFileForm) {
            properties.setFinalFileForm(true);
        } else {
            properties.setFinalFileForm(false);
        }

        logTextArea.appendText('\n' + "Zaktualizowano ustawienia. Wygeneruj zestaw" +
                " stref zakazanych...");
    }
}
