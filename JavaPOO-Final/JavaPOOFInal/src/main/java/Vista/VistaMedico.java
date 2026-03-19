
package Vista;

import Controlador.MedicoController;
import Modelo.Medico;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class VistaMedico {

    private final MedicoController ctrl = App.medicoCtrl;
    private final ObservableList<Medico> datos = FXCollections.observableArrayList();

    public Pane getView() {
        VBox panel = new VBox(20);
        panel.setPadding(new Insets(20));

        Label titulo = new Label("Listado de Médicos");
        titulo.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        TableView<Medico> tabla = new TableView<>();
        tabla.setItems(datos);
        refrescarTabla();

        TableColumn<Medico, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));

        TableColumn<Medico, String> colApellido = new TableColumn<>("Apellido");
        colApellido.setCellValueFactory(new PropertyValueFactory<>("apellido"));

        TableColumn<Medico, String> colEspecialidad = new TableColumn<>("Especialidad");
        colEspecialidad.setCellValueFactory(new PropertyValueFactory<>("especialidad"));

        TableColumn<Medico, String> colMatricula = new TableColumn<>("Matrícula");
        colMatricula.setCellValueFactory(new PropertyValueFactory<>("matricula"));

        tabla.getColumns().addAll(colNombre, colApellido, colEspecialidad, colMatricula);
        tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        Button btnNuevo = new Button("Nuevo Médico");
        btnNuevo.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white;");
        btnNuevo.setOnAction(e -> mostrarFormulario(null));

        Button btnEditar = new Button("Editar Médico");
        btnEditar.setStyle("-fx-background-color: #2980b9; -fx-text-fill: white;");
        btnEditar.setOnAction(e -> {
            Medico seleccionado = tabla.getSelectionModel().getSelectedItem();
            if (seleccionado != null) mostrarFormulario(seleccionado);
            else new Alert(Alert.AlertType.WARNING, "Seleccione un médico").show();
        });

        Button btnEliminar = new Button("Eliminar Médico");
        btnEliminar.setStyle("-fx-background-color: #c0392b; -fx-text-fill: white;");
        btnEliminar.setOnAction(e -> {
            Medico seleccionado = tabla.getSelectionModel().getSelectedItem();
            if (seleccionado != null) {
                if (new Alert(Alert.AlertType.CONFIRMATION, "¿Borrar al Dr. " + seleccionado.getApellido() + "?").showAndWait().get() == ButtonType.OK) {
                    new MedicoController().eliminar(seleccionado.getId());
                    datos.setAll(new MedicoController().listar());
                }
            } else new Alert(Alert.AlertType.WARNING, "Seleccione un médico").show();
        });


        HBox botones = new HBox(15, btnNuevo, btnEditar, btnEliminar);
        botones.setAlignment(Pos.CENTER_LEFT);

        panel.getChildren().addAll(titulo, tabla, botones);
        return panel;
    }

    private void mostrarFormulario(Medico medico) {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(medico == null ? "Nuevo Médico" : "Editar Médico");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField txtNombre = new TextField(medico != null ? medico.getNombre() : "");
        TextField txtApellido = new TextField(medico != null ? medico.getApellido() : "");
        TextField txtDni = new TextField(medico != null ? medico.getDni() : "");
        TextField txtMail = new TextField(medico != null ? medico.getMail() : "");
        TextField txtTel = new TextField(medico != null ? medico.getTelefono() : "");
        TextField txtMatricula = new TextField(medico != null ? medico.getMatricula() : "");
        TextField txtEspecialidad = new TextField(medico != null ? medico.getEspecialidad() : "");

        grid.addRow(0, new Label("Nombre:"), txtNombre);
        grid.addRow(1, new Label("Apellido:"), txtApellido);
        grid.addRow(2, new Label("DNI:"), txtDni);
        grid.addRow(3, new Label("Email:"), txtMail);
        grid.addRow(4, new Label("Teléfono:"), txtTel);
        grid.addRow(5, new Label("Matrícula:"), txtMatricula);
        grid.addRow(6, new Label("Especialidad:"), txtEspecialidad);

        Button btnGuardar = new Button("Guardar");
        btnGuardar.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white;");
        btnGuardar.setOnAction(e -> {
            if (txtNombre.getText().isEmpty() || txtApellido.getText().isEmpty() || txtMatricula.getText().isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "Nombre, Apellido y Matrícula son obligatorios").show();
                return;
            }

            Medico m = medico != null ? medico : new Medico();
            m.setNombre(txtNombre.getText());
            m.setApellido(txtApellido.getText());
            m.setDni(txtDni.getText());
            m.setMail(txtMail.getText());
            m.setTelefono(txtTel.getText());
            m.setMatricula(txtMatricula.getText());
            m.setEspecialidad(txtEspecialidad.getText());

            ctrl.guardar(m);
            refrescarTabla();
            App.refrescarMedicos();
            stage.close();
        });

        VBox layout = new VBox(15, new Label(medico == null ? "Nuevo Médico" : "Editar Médico"), grid, btnGuardar);
        layout.setPadding(new Insets(20));
        stage.setScene(new Scene(layout, 420, 420));
        stage.show();
    }

    void refrescarTabla() {
        datos.setAll(ctrl.listar());
    }
}