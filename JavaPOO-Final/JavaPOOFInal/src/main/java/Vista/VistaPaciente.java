package Vista;

import Controlador.PacienteController;
import Modelo.Paciente;
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

public class VistaPaciente {

    private final PacienteController ctrl = App.pacienteCtrl;
    private final ObservableList<Paciente> datos = FXCollections.observableArrayList();

    public Pane getView() {
        VBox panel = new VBox(20);
        panel.setPadding(new Insets(20));

        Label titulo = new Label("Listado de Pacientes");
        titulo.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        TableView<Paciente> tabla = new TableView<>();
        tabla.setItems(datos);
        refrescarTabla();

        TableColumn<Paciente, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));

        TableColumn<Paciente, String> colApellido = new TableColumn<>("Apellido");
        colApellido.setCellValueFactory(new PropertyValueFactory<>("apellido"));

        TableColumn<Paciente, String> colDni = new TableColumn<>("DNI");
        colDni.setCellValueFactory(new PropertyValueFactory<>("dni"));

        TableColumn<Paciente, String> colOS = new TableColumn<>("Obra Social");
        colOS.setCellValueFactory(new PropertyValueFactory<>("obraSocial"));

        tabla.getColumns().addAll(colNombre, colApellido, colDni, colOS);
        tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        Button btnNuevo = new Button("Nuevo Paciente");
        btnNuevo.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white;");
        btnNuevo.setOnAction(e -> mostrarFormulario(null));

        Button btnEditar = new Button("Editar Paciente");
        btnEditar.setStyle("-fx-background-color: #2980b9; -fx-text-fill: white;");
        btnEditar.setOnAction(e -> {
            Paciente seleccionado = tabla.getSelectionModel().getSelectedItem();
            if (seleccionado != null) mostrarFormulario(seleccionado);
            else new Alert(Alert.AlertType.WARNING, "Seleccione un paciente").show();
        });
        Button btnEliminar = new Button("Eliminar Paciente");
        btnEliminar.setStyle("-fx-background-color: #c0392b; -fx-text-fill: white;");
        btnEliminar.setOnAction(e -> {
            Paciente seleccionado = tabla.getSelectionModel().getSelectedItem();
            if (seleccionado != null) {
                if (new Alert(Alert.AlertType.CONFIRMATION, "¿Borrar a " + seleccionado.getApellido() + "?").showAndWait().get() == ButtonType.OK) {
                    new PacienteController().eliminar(seleccionado.getId());
                    datos.setAll(new PacienteController().listar());
                }
            }else new Alert(Alert.AlertType.WARNING, "Seleccione un paciente").show();
        });


        HBox botones = new HBox(15, btnNuevo, btnEditar,btnEliminar);
        botones.setAlignment(Pos.CENTER_LEFT);

        panel.getChildren().addAll(titulo, tabla, botones);
        return panel;
    }

    private void mostrarFormulario(Paciente paciente) {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(paciente == null ? "Nuevo Paciente" : "Editar Paciente");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField txtNombre = new TextField(paciente != null ? paciente.getNombre() : "");
        TextField txtApellido = new TextField(paciente != null ? paciente.getApellido() : "");
        TextField txtDni = new TextField(paciente != null ? paciente.getDni() : "");
        TextField txtMail = new TextField(paciente != null ? paciente.getMail() : "");
        TextField txtTel = new TextField(paciente != null ? paciente.getTelefono() : "");
        TextField txtOS = new TextField(paciente != null ? paciente.getObraSocial() : "");
        TextField txtAfiliado = new TextField(paciente != null ? paciente.getNumeroAfiliado() : "");

        grid.addRow(0, new Label("Nombre:"), txtNombre);
        grid.addRow(1, new Label("Apellido:"), txtApellido);
        grid.addRow(2, new Label("DNI:"), txtDni);
        grid.addRow(3, new Label("Email:"), txtMail);
        grid.addRow(4, new Label("Teléfono:"), txtTel);
        grid.addRow(5, new Label("Obra Social:"), txtOS);
        grid.addRow(6, new Label("N° Afiliado:"), txtAfiliado);


        Button btnGuardar = new Button("Guardar");
        btnGuardar.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white;");
        btnGuardar.setOnAction(e -> {
            if (txtNombre.getText().isEmpty() || txtApellido.getText().isEmpty() || txtDni.getText().isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "Nombre, Apellido y DNI son obligatorios").show();
                return;
            }

            Paciente p = paciente != null ? paciente : new Paciente();
            p.setNombre(txtNombre.getText());
            p.setApellido(txtApellido.getText());
            p.setDni(txtDni.getText());
            p.setMail(txtMail.getText());
            p.setTelefono(txtTel.getText());
            p.setObraSocial(txtOS.getText());
            p.setNumeroAfiliado(txtAfiliado.getText());

            ctrl.guardar(p);
            refrescarTabla();
            App.refrescarPacientes();
            stage.close();
        });

        VBox layout = new VBox(15, new Label(paciente == null ? "Nuevo Paciente" : "Editar Paciente"), grid, btnGuardar);
        layout.setPadding(new Insets(20));
        stage.setScene(new Scene(layout, 420, 420));
        stage.showAndWait();
    }

    void refrescarTabla() {
        datos.setAll(ctrl.listar());
    }
}