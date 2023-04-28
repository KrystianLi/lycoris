import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import java.util.Objects;

public class Main extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            Pane root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("lycoris.fxml")));
            primaryStage.setScene(new Scene(root));
//            primaryStage.setResizable(false);
            primaryStage.setOnCloseRequest(event -> {
                System.exit(0);
            });
            primaryStage.show();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }


}
