
package Vista;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Login extends Application {

    @Override
    public void start(Stage stage) {
        stage.setTitle("Seguridad - Acceso al Sistema");

        Label lblTitulo = new Label("Iniciar Sesión");
        lblTitulo.setFont(new Font("Arial", 24));
        lblTitulo.setStyle("-fx-text-fill: #34495e; -fx-font-weight: bold;");

        TextField txtUsuario = new TextField();
        txtUsuario.setPromptText("Usuario");
        txtUsuario.setMaxWidth(250);

        PasswordField txtPassword = new PasswordField();
        txtPassword.setPromptText("Contraseña");
        txtPassword.setMaxWidth(250);

        Label lblMensaje = new Label();
        lblMensaje.setStyle("-fx-text-fill: red;");

        Button btnEntrar = new Button("Ingresar");
        btnEntrar.setStyle("-fx-background-color: #2c3e50; -fx-text-fill: white; -fx-font-size: 14px; -fx-cursor: hand;");
        btnEntrar.setMinWidth(150);

        btnEntrar.setOnAction(e -> validarLogin(txtUsuario.getText(), txtPassword.getText(), stage, lblMensaje));

        // Punto 4: Enter para login
        VBox layout = new VBox(15);
        layout.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                validarLogin(txtUsuario.getText(), txtPassword.getText(), stage, lblMensaje);
            }
        });

        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: #ecf0f1;");
        layout.getChildren().addAll(lblTitulo, txtUsuario, txtPassword, btnEntrar, lblMensaje);

        Scene scene = new Scene(layout, 350, 300);
        stage.setScene(scene);
        stage.show();
    }

    private void validarLogin(String user, String pass, Stage stage, Label lblMensaje) {
        if (user.equals("admin") && pass.equals("admin")) {
            System.out.println("Login Exitoso. Abriendo sistema...");
            stage.close();
            try {
                new App().start(new Stage());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            lblMensaje.setText("Usuario o contraseña incorrectos.");
            // No borramos usuario para comodidad
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}