import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

public class UserDetails extends Application {

    Stage window;
    Scene scene;
    public static void main(String[] args) {
        launch(args);

    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;
        window.setTitle("Данные пользователя");

        Label inputUserLabel = new Label("Введите имя пользователя");
        TextField inputUserField = new TextField("");
        inputUserField.setMaxWidth(150);

        CheckBox isDomain = new CheckBox("Искать в текущем домене");
        isDomain.setSelected(false);

        Button inputUserButton = new Button("OK");
        inputUserButton.setMaxWidth(150);

        TextArea outputUserDetails = new TextArea();
        outputUserDetails.setMinSize(530,280);
        outputUserDetails.setFont(Font.font("Verdana", FontWeight.NORMAL, 12));

        outputUserDetails.setEditable(false);

        inputUserButton.setOnAction(event->{
            try {
                outputUserDetails.setText("");
                String s;
                String userDetails = "";
                String domain = "";
                if (isDomain.isSelected())
                    domain = "/domain";

                Process p = Runtime.getRuntime().exec("cmd /C net " + domain + "user " + inputUserField.getText());
                System.out.println("cmd /C net " + domain + "user " + inputUserField.getText());
                BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream(), Charset.forName("CP866")));
                BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getInputStream(), Charset.forName("CP866")));
                while ((s = stdInput.readLine()) != null){
                    userDetails = userDetails + s + "\n";
                }
                outputUserDetails.setText(userDetails);


                while ((s = stdError.readLine()) != null){
                    userDetails = userDetails + s + "\n";
                }
                outputUserDetails.setText(userDetails);

            }catch (IOException e){
                outputUserDetails.setText("Ошибка: " + "\n" + e.getMessage());
                e.printStackTrace();
            }
        });

        //разметка

        VBox layout = new VBox(10);
        layout.setAlignment(Pos.TOP_CENTER);
        layout.setPadding(new Insets(10,10,10,10));
        layout.getChildren().addAll(inputUserLabel,inputUserField,isDomain,inputUserButton,outputUserDetails);
        scene = new Scene(layout,650,500);
        window.setScene(scene);
        window.show();
        window.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                System.exit(0);
            }
        });



    }
}
