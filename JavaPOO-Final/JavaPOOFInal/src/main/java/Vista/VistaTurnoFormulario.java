package Vista;

import Controlador.TurnoController;
import Modelo.*;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class VistaTurnoFormulario {

    private final TurnoController ctrl = App.turnoCtrl;

    public void mostrar(Turno turno) {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(turno == null ? "Nuevo Turno" : "Editar Turno");

        Label titulo = new Label(turno == null ? "NUEVO TURNO" : "EDITAR TURNO");
        titulo.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        DatePicker datePicker = new DatePicker(turno != null ? turno.getFecha() : LocalDate.now());

        ComboBox<String> comboHora = new ComboBox<>();
        for (int h = 8; h <= 18; h++) {
            comboHora.getItems().add(String.format("%02d:00", h));
            if (h < 18) comboHora.getItems().add(String.format("%02d:30", h));
        }
        comboHora.setValue(turno != null ? turno.getHora() : "09:00");

        ComboBox<Medico> comboMedico = new ComboBox<>(FXCollections.observableArrayList(ctrl.listarMedicos()));
        comboMedico.setValue(turno != null ? turno.getMedico() : null);

        ComboBox<Paciente> comboPaciente = new ComboBox<>(FXCollections.observableArrayList(ctrl.listarPacientes()));
        comboPaciente.setValue(turno != null ? turno.getPaciente() : null);

        ComboBox<Estado> comboEstado = new ComboBox<>(FXCollections.observableArrayList(Estado.values()));
        comboEstado.setValue(turno != null ? turno.getEstado() : Estado.PENDIENTE);

        TextArea txtObservaciones = new TextArea(turno != null ? turno.getObservaciones() : "");
        txtObservaciones.setPrefRowCount(3);

        Button btnGuardar = new Button("Guardar");
        btnGuardar.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold;");
        btnGuardar.setOnAction(e -> {
            if (datePicker.getValue() == null || comboMedico.getValue() == null || comboPaciente.getValue() == null) {
                new Alert(Alert.AlertType.WARNING, "Complete todos los campos obligatorios").show();
                return;
            }

            // Validación de solapamiento
            LocalDateTime fechaHora = LocalDateTime.of(datePicker.getValue(), LocalTime.parse(comboHora.getValue()));
            Agenda agenda = ctrl.obtenerAgenda(comboMedico.getValue(), datePicker.getValue());
            if (agenda.estaOcupado(fechaHora, turno != null ? turno.getId() : 0)) {
                new Alert(Alert.AlertType.WARNING, "El horario ya está ocupado por otro turno").show();
                return;
            }

            Turno t = turno != null ? turno : new Turno();
            t.setFecha(datePicker.getValue());
            t.setHora(comboHora.getValue());
            t.setMedico(comboMedico.getValue());
            t.setPaciente(comboPaciente.getValue());
            t.setEstado(comboEstado.getValue());
            t.setObservaciones(txtObservaciones.getText());

            ctrl.guardar(t);

            // LLAMADA CLAVE: Refresca la tabla principal de turnos
            App.refrescarTurnos();


            stage.close();
        });

        Button btnCancelar = new Button("Cancelar");
        btnCancelar.setOnAction(e -> stage.close());

        VBox layout = new VBox(15,
                titulo, new Separator(),
                new Label("Fecha:"), datePicker,
                new Label("Hora:"), comboHora,
                new Label("Médico:"), comboMedico,
                new Label("Paciente:"), comboPaciente,
                new Label("Estado:"), comboEstado,
                new Label("Observaciones:"), txtObservaciones,
                new Separator(),
                new HBox(20, btnGuardar, btnCancelar)
        );
        layout.setPadding(new Insets(30));

        stage.setScene(new Scene(layout, 500, 650));
        stage.showAndWait();
    }
}