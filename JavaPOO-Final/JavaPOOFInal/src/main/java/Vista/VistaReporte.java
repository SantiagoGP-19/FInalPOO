package Vista;

import Controlador.MedicoController;
import Modelo.Medico;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import java.time.LocalDate;

public class VistaReporte {

    public VistaReporte() {}

    public Pane getView() {
        VBox layout = new VBox(20);
        layout.setPadding(new Insets(20));

        Label titulo = new Label("Reportes Basicos");
        titulo.setFont(new Font("System Bold", 18));

        VBox ocupacionPanel = crearPanelOcupacion();
        VBox ausentismoPanel = crearPanelAusentismo();

        layout.getChildren().addAll(titulo, ocupacionPanel, new Separator(), ausentismoPanel);
        return layout;
    }

    private VBox crearPanelOcupacion() {
        ObservableList<Medico> medicos = FXCollections.observableArrayList(new MedicoController().listar());

        ComboBox<Medico> cmbMedico = new ComboBox<>(medicos);
        cmbMedico.setPromptText("Seleccionar Medico");
        cmbMedico.setCellFactory(lv -> new ListCell<Medico>() {
            @Override
            protected void updateItem(Medico item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item.getNombre() + " " + item.getApellido());
            }
        });
        cmbMedico.setButtonCell(cmbMedico.getCellFactory().call(null));

        // CAMBIO: TextField por DatePicker
        DatePicker dpFecha = new DatePicker();
        dpFecha.setPromptText("Seleccionar fecha");
        dpFecha.setValue(LocalDate.now()); // Fecha por defecto: hoy

        Button btnGenerar = new Button("Generar Ocupación");
        Label lblResultado = new Label("Ocupación: --%");

        btnGenerar.setOnAction(e -> {
            try {
                Medico medico = cmbMedico.getSelectionModel().getSelectedItem();
                LocalDate fecha = dpFecha.getValue();

                // Validaciones
                if (medico == null) {
                    new Alert(Alert.AlertType.WARNING, "Debe seleccionar un medico").show();
                    return;
                }

                if (fecha == null) {
                    new Alert(Alert.AlertType.WARNING, "Debe seleccionar una fecha").show();
                    return;
                }

                double ocupacion = App.reporteCtrl.obtenerOcupacionDiaria(medico, fecha);
                lblResultado.setText(String.format("Ocupación para %s en %s: %.2f%%", medico, fecha, ocupacion));

            } catch (Exception ex) {
                new Alert(Alert.AlertType.ERROR, "Error al calcular ocupación: " + ex.getMessage()).show();
            }
        });

        HBox inputs = new HBox(10, cmbMedico, dpFecha, btnGenerar);
        inputs.setAlignment(Pos.CENTER_LEFT);

        VBox panel = new VBox(10, new Label("Ocupación por Medico y Día"), inputs, lblResultado);
        panel.setPadding(new Insets(10));
        return panel;
    }

    private VBox crearPanelAusentismo() {
        // CAMBIO: TextField por DatePicker
        DatePicker dpInicio = new DatePicker();
        dpInicio.setPromptText("Fecha de inicio");
        dpInicio.setValue(LocalDate.now().minusDays(7)); // Por defecto: hace 7 d铆as

        DatePicker dpFin = new DatePicker();
        dpFin.setPromptText("Fecha de fin");
        dpFin.setValue(LocalDate.now()); // Por defecto: hoy

        Button btnGenerar = new Button("Calcular Ausentismo");
        Label lblResultado = new Label("Tasa de Ausentismo: --%");

        btnGenerar.setOnAction(e -> {
            try {
                LocalDate inicio = dpInicio.getValue();
                LocalDate fin = dpFin.getValue();

                // Validaciones
                if (inicio == null) {
                    new Alert(Alert.AlertType.WARNING, "Debe seleccionar la fecha de inicio").show();
                    return;
                }

                if (fin == null) {
                    new Alert(Alert.AlertType.WARNING, "Debe seleccionar la fecha de fin").show();
                    return;
                }

                // Validar que la fecha de inicio sea anterior a la de fin
                if (inicio.isAfter(fin)) {
                    new Alert(Alert.AlertType.WARNING, "La fecha de inicio debe ser anterior a la fecha de fin").show();
                    return;
                }

                double tasa = App.reporteCtrl.obtenerTasaAusentismoGeneral(inicio, fin);
                lblResultado.setText(String.format("Tasa de Ausentismo (Periodo %s a %s): %.2f%%", inicio, fin, tasa));

            } catch (Exception ex) {
                new Alert(Alert.AlertType.ERROR, "Error al calcular ausentismo: " + ex.getMessage()).show();
            }
        });

        HBox inputs = new HBox(10, dpInicio, dpFin, btnGenerar);
        inputs.setAlignment(Pos.CENTER_LEFT);

        VBox panel = new VBox(10, new Label("Tasa de Ausentismo General"), inputs, lblResultado);
        panel.setPadding(new Insets(10));
        return panel;
    }
}
