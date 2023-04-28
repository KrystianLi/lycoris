import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
/**
 * lycoris controller
 */
public class Controller {
    @FXML
    private AnchorPane info;
    @FXML
    private AnchorPane landray;
    @FXML
    private AnchorPane pane3;

    @FXML
    private void infoButton() {
        info.setVisible(true);
        landray.setVisible(false);
        pane3.setVisible(false);
    }

    @FXML
    private void landrayButton() {
        info.setVisible(false);
        landray.setVisible(true);
        pane3.setVisible(false);
    }

    @FXML
    private void handleButton3() {
        info.setVisible(false);
        landray.setVisible(false);
        pane3.setVisible(true);
    }


}
