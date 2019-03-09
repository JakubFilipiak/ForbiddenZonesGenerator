package controller;

import domain.Map;
import domain.Text;
import domain.Track;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import service.MapService;
import service.TrackService;

import java.io.File;
import java.io.IOException;

/**
 * Created by Jakub Filipiak on 06.03.2019.
 */
public class Controller {

    private Map map = Map.INSTANCE;
    private MapService mapService = MapService.INSTANCE;
    private Track track = Track.INSTANCE;
    private TrackService trackService = TrackService.INSTANCE;
    private Text text = Text.INSTANCE;

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

    private ComboBox existingConfigurationsComboBox;

    public void loadExistingConfiguration() {

    }

    @FXML
    public void chooseMapFile() {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Wybierz plik mapy");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(
                "PNG", "*.png"));
        File mapFileTmp = fileChooser.showOpenDialog(null);

        if (mapFileTmp != null) {
            System.out.println("Map file: " + mapFileTmp.getAbsolutePath());
            map.setMapFile(mapFileTmp);
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
        }
    }

    @FXML
    public void generateForbiddenZones() {

        FileChooser saveFileChooser = new FileChooser();
        saveFileChooser.setTitle("Zapisz plik ze strefami zakazanymi");
        saveFileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("TXT", "*.txt"));
        saveFileChooser.setInitialFileName("strefy zakazane");
        File textFileTmp = saveFileChooser.showSaveDialog(null);

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
}
