package Vista;

import Controlador.*;
import Modelo.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class App extends Application {

    // CONTROLADORES
    public static final TurnoController turnoCtrl = new TurnoController();
    public static final MedicoController medicoCtrl = new MedicoController();
    public static final PacienteController pacienteCtrl = new PacienteController();
    public static final ReporteController reporteCtrl = new ReporteController();

    private BorderPane root;
    private Button currentSelectedButton;
    private static VistaMedico vistaMedico;
    private static VistaPaciente vistaPaciente;
    private static VistaTurnoPrincipal vistaTurno; // Hacemos la instancia estática

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Centro Médico Da Vinci - Sistema de Turnos");
        root = new BorderPane();

        // INICIALIZACIÓN CRÍTICA: Inicializamos las instancias estáticas aquí
        vistaMedico = new VistaMedico();
        vistaPaciente = new VistaPaciente();
        vistaTurno = new VistaTurnoPrincipal();

        root.setLeft(crearSidebar());
        root.setCenter(new Label("Bienvenido - Seleccione una opción del menú"));

        Scene scene = new Scene(root, 1100, 650);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox crearSidebar() {
        VBox sidebar = new VBox(20);
        sidebar.setPadding(new Insets(30));
        sidebar.setStyle("-fx-background-color: #2c3e50;");
        sidebar.setPrefWidth(220);

        Label titulo = new Label("CENTRO MÉDICO");
        titulo.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold;");

        // Usamos las instancias inicializadas en start()
        Button btnMedicos = boton("Médicos", () -> root.setCenter(vistaMedico.getView()));
        Button btnPacientes = boton("Pacientes", () -> root.setCenter(vistaPaciente.getView()));
        Button btnTurnos = boton("Turnos", () -> root.setCenter(vistaTurno.getView()));
        Button btnReportes = boton("Reportes",() -> root.setCenter(new VistaReporte().getView()));
        Button btnSalir = boton("Salir", () -> System.exit(0));

        sidebar.getChildren().addAll(titulo, btnMedicos, btnPacientes, btnTurnos, btnReportes, new Separator(), btnSalir);
        return sidebar;
    }

    private Button boton(String texto, Runnable accion) {
        Button btn = new Button(texto);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setStyle("-fx-background-color: #34495e; -fx-text-fill: white; -fx-font-size: 14px;");
        btn.setOnAction(e -> {
            if (currentSelectedButton != null) {
                currentSelectedButton.setStyle("-fx-background-color: #34495e; -fx-text-fill: white;");
            }
            btn.setStyle("-fx-background-color: #1f3a5a; -fx-text-fill: white; -fx-font-weight: bold;");
            currentSelectedButton = btn;
            accion.run();
        });
        return btn;
    }

    // Métodos estáticos de refresco
    public static void refrescarMedicos() {
        if (vistaMedico != null) vistaMedico.refrescarTabla();
    }

    public static void refrescarPacientes() {
        if (vistaPaciente != null) vistaPaciente.refrescarTabla();
    }

    // Método estático para refrescar los turnos
    public static void refrescarTurnos() {
        if (vistaTurno != null) vistaTurno.refrescar();
    }


    public static void main(String[] args) {
        launch(args);
    }
}