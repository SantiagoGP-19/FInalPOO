
package Vista;

import Controlador.TurnoController;
import Modelo.Estado;
import Modelo.Turno;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.beans.property.SimpleStringProperty;

import static Modelo.Estado.*;

public class VistaTurnoPrincipal {

    private final TurnoController ctrl = App.turnoCtrl;
    private final ObservableList<Turno> datos = FXCollections.observableArrayList();

    public Pane getView() {
        VBox panel = new VBox(20);
        panel.setPadding(new Insets(20));

        Label titulo = new Label("Gestión de Turnos");
        titulo.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        TableView<Turno> tabla = new TableView<>();
        tabla.setItems(datos);
        refrescar(); // Carga inicial

        // Columnas
        TableColumn<Turno, String> colFecha = new TableColumn<>("Fecha");
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));

        TableColumn<Turno, String> colHora = new TableColumn<>("Hora");
        colHora.setCellValueFactory(new PropertyValueFactory<>("hora"));

        TableColumn<Turno, String> colMedico = new TableColumn<>("Médico");
        colMedico.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getMedico().toString()));

        TableColumn<Turno, String> colPaciente = new TableColumn<>("Paciente");
        colPaciente.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getPaciente().toString()));

        TableColumn<Turno, Estado> colEstado = new TableColumn<>("Estado");
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colEstado.setCellFactory(column -> new TableCell<Turno, Estado>() {
            @Override
            protected void updateItem(Estado item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item.toString());
                    switch (item) {
                        case PENDIENTE -> setStyle("-fx-background-color: #f1c40f; -fx-text-fill: black; -fx-alignment: CENTER; -fx-font-weight: bold;");
                        case CONFIRMADO -> setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-alignment: CENTER; -fx-font-weight: bold;");
                        case AUSENTE -> setStyle("-fx-background-color: #e67e22; -fx-text-fill: white; -fx-alignment: CENTER; -fx-font-weight: bold;");
                        case ATENDIDO -> setStyle("-fx-background-color: #2980b9; -fx-text-fill: white; -fx-alignment: CENTER; -fx-font-weight: bold;");
                        case CANCELADO -> setStyle("-fx-background-color: #c0392b; -fx-text-fill: white; -fx-alignment: CENTER; -fx-font-weight: bold;");
                        default -> setStyle("-fx-alignment: CENTER;");
                    }
                }
            }
        });

        tabla.getColumns().addAll(colFecha, colHora, colMedico, colPaciente, colEstado);
        tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Botones
        Button btnNuevo = new Button("Nuevo Turno");
        btnNuevo.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white;");
        btnNuevo.setOnAction(e -> new VistaTurnoFormulario().mostrar(null));

        Button btnEditar = new Button("Editar Turno");
        btnEditar.setStyle("-fx-background-color: #2980b9; -fx-text-fill: white;");
        btnEditar.setOnAction(e -> {
            Turno seleccionado = tabla.getSelectionModel().getSelectedItem();
            if (seleccionado != null) {
                new VistaTurnoFormulario().mostrar(seleccionado);
            } else {
                new Alert(Alert.AlertType.WARNING, "Seleccione un turno").show();
            }
        });

        Button btnEliminar = new Button("Eliminar Turno");
        btnEliminar.setStyle("-fx-background-color: #c0392b; -fx-text-fill: white;");
        btnEliminar.setOnAction(e -> {
            Turno seleccionado = tabla.getSelectionModel().getSelectedItem();
            if (seleccionado != null) {
                if (new Alert(Alert.AlertType.CONFIRMATION, "¿Eliminar el turno?").showAndWait().get() == ButtonType.OK) {
                    ctrl.eliminar(seleccionado.getId());
                    refrescar();
                }
            }else new Alert(Alert.AlertType.WARNING, "Seleccione un turno").show();
        });

        HBox botones = new HBox(15, btnNuevo, btnEditar, btnEliminar);
        botones.setAlignment(Pos.CENTER_LEFT);

        panel.getChildren().addAll(titulo, tabla, botones);
        return panel;
    }

    public void refrescar() {
        datos.setAll(ctrl.listarTodos());
    }
}