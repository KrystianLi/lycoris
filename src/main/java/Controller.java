import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
/**
 * lycoris controller
 */
public class Controller {
    @FXML
    private AnchorPane setting;
    @FXML
    private AnchorPane assist;
    @FXML
    private AnchorPane info;
    @FXML
    private AnchorPane landray;



    @FXML
    private void settingButton() {
        setting.setVisible(true);
        info.setVisible(false);
        assist.setVisible(false);
        landray.setVisible(false);
    }
    @FXML
    private void infoButton() {
        setting.setVisible(false);
        info.setVisible(true);
        assist.setVisible(false);
        landray.setVisible(false);
    }

    @FXML
    private void asistButton() {
        setting.setVisible(false);
        info.setVisible(false);
        assist.setVisible(true);
        landray.setVisible(false);
    }

    @FXML
    private void landrayButton() {
        setting.setVisible(false);
        info.setVisible(false);
        assist.setVisible(false);
        landray.setVisible(true);
    }


}
