package nl.lolmewn.rug.quakemonitor;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import nl.lolmewn.rug.quakemonitor.net.Server;
import nl.lolmewn.rug.quakemonitor.rest.RestServer;

public class MainApp extends Application {

    private Server server;
    private RestServer rest;
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Scene.fxml"));

        Scene scene = new Scene(root);
        scene.getStylesheets().add("/styles/Styles.css");

        stage.setTitle("Earthquake monitor");
        stage.setScene(scene);
        stage.show();
        stage.setOnCloseRequest((e)->{
            System.exit(0);
        });
        
        System.out.println("GUI launched, launching server...");
        this.server = new Server(5000);
        this.server.start();
        
        this.rest = new RestServer(server);
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
