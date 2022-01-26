package controllers;

import dao.modelo.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import servicios.*;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.net.URL;
import java.util.ResourceBundle;

public class AdminGlobal implements Initializable {
    @FXML
    private ComboBox comboTipoUsuario;
    @FXML
    private ListView listViewUsuarios;
    @FXML
    private TextField txtNombreUsuarios;
    @FXML
    private TextField txtUserUsuarios;
    @FXML
    private TextField txtMailUsuarios;
    @FXML
    private DatePicker dateFechaUsuarios;
    @FXML
    private PasswordField txtPasswordUsuarios;
    @FXML
    private TextField txtNombrePeriodico;
    @FXML
    private TextField txtPrecioPeriodico;
    @FXML
    private TextField txtDirectorPeriodico;
    @FXML
    private ComboBox comboBoxIdAdminPeriodico;
    @FXML
    private ListView listViewPeriodicos;
    @FXML
    private TextField txtTitularArticulo;
    @FXML
    private TextArea textareaDescripcionArticulo;
    @FXML
    private TextField txtAutorArticulo;
    @FXML
    private ComboBox comboBoxIdPeriodicoArticulo;
    @FXML
    private ComboBox comboBoxIdTipoArticulo;
    @FXML
    private ListView listViewArticulos;
    @FXML
    private ComboBox comboBoxIdLectorSuscripciones;
    @FXML
    private ComboBox comboBoxIdPeriodicoSuscripciones;
    @FXML
    private ListView listViewSuscripciones;
    @FXML
    private DatePicker dateFechaInicioSuscripciones;


    private ServiciosLectores serviciosLectores = new ServiciosLectores();
    private ServiciosUsuarios serviciosUsuarios = new ServiciosUsuarios();
    private ServiciosPeriodicos serviciosPeriodicos = new ServiciosPeriodicos();
    private ServiciosTiposUsuarios serviciosTiposUsuarios = new ServiciosTiposUsuarios();
    private ServiciosArticulo serviciosArticulo = new ServiciosArticulo();
    private ServiciosTiposArticulo serviciosTiposArticulo = new ServiciosTiposArticulo();
    private ServiciosSuscripciones serviciosSuscripciones = new ServiciosSuscripciones();
    private Principal principalController;
    private Alert alert;

    public void setPrincipalController(Principal principalController) {
        this.principalController = principalController;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cargarListViews();
    }

    public void borrarUsuario(ActionEvent actionEvent) throws NoSuchAlgorithmException {
        var error = "";
        if (!listViewUsuarios.getSelectionModel().isEmpty()) {
            serviciosUsuarios.borrarUsuario((Usuario) listViewUsuarios.getSelectionModel().getSelectedItem());
            error = "Usuario Borrado correctamente";
            cargarListViews();
        } else {
            error = "Elige usuario para poder borrarlo";
        }
        alert = new Alert(Alert.AlertType.INFORMATION);
        alert.getDialogPane().lookupButton(ButtonType.OK).setId("alertOK");
        alert.setContentText(error);
        alert.showAndWait();
        cargarListViews();
    }

    public void add(ActionEvent actionEvent) throws NoSuchAlgorithmException {
        var error = "";
        int idTipoUsuario = 0;
        if (txtPasswordUsuarios.getText().equals("") || txtMailUsuarios.getText().equals("") || txtUserUsuarios.getText().equals("")) {
            error = "Error al crear Usuario, faltan datos por añadir";
        } else {
            MessageDigest md = MessageDigest.getInstance("SHA3-512");
            byte[] passwordEnBytes = md.digest(txtPasswordUsuarios.getText().getBytes());
            var passwordHasheada = serviciosUsuarios.hashearPassword(passwordEnBytes);
            if (comboTipoUsuario.getSelectionModel().getSelectedIndex() == 0) {
                idTipoUsuario = 1;
                addUsuario(passwordHasheada, idTipoUsuario);
            } else if (comboTipoUsuario.getSelectionModel().getSelectedIndex() == 1) {
                idTipoUsuario = 2;
                addUsuario(passwordHasheada, idTipoUsuario);
            } else if (comboTipoUsuario.getSelectionModel().getSelectedIndex() == 2) {
                idTipoUsuario = 3;
                addLector(passwordHasheada, idTipoUsuario);
            } else {
                error = "Error, seleccione un tipo de usuario";
            }
        }
        alert = new Alert(Alert.AlertType.INFORMATION);
        alert.getDialogPane().lookupButton(ButtonType.OK).setId("alertOK");
        alert.setContentText(error);
        alert.showAndWait();
    }


    private void addLector(String passwordHasheada, int idTipoUsuario) {
        String error = "";
        if (txtNombreUsuarios.getText().equals("") || dateFechaInicioSuscripciones.getValue() == null) {
            error = "Faltan campos para poder añadir un lector";
        } else {
            Usuario usuario = new Usuario(txtUserUsuarios.getText(), passwordHasheada, txtMailUsuarios.getText(), txtNombreUsuarios.getText(), dateFechaUsuarios.getValue(), idTipoUsuario);
            boolean correcto = serviciosLectores.addLector(usuario);

            if (!correcto) {
                error = "Error al añadir usuario, usuario existente";
            } else {
                listViewUsuarios.getItems().add(usuario);
                error = "Usuario agregado";
            }
        }
        alert = new Alert(Alert.AlertType.INFORMATION);
        alert.getDialogPane().lookupButton(ButtonType.OK).setId("alertOK");
        alert.setContentText(error);
        alert.showAndWait();
        cargarListViews();
    }

    private void addUsuario(String passwordHasheada, int idTipoUsuario) {
        Usuario usuario = new Usuario(txtUserUsuarios.getText(), passwordHasheada, txtMailUsuarios.getText(), idTipoUsuario);
        boolean correcto = serviciosUsuarios.addUsuario(usuario);
        String error = "";
        if (!correcto) {
            error = "Error al añadir usuario, usuario existente";
        } else {
            listViewUsuarios.getItems().add(usuario);
            error = "Usuario agregado";
        }
        alert = new Alert(Alert.AlertType.INFORMATION);
        alert.getDialogPane().lookupButton(ButtonType.OK).setId("alertOK");
        alert.setContentText(error);
        alert.showAndWait();
        cargarListViews();
    }

    public void actualizarUsuarioOLector(ActionEvent actionEvent) throws NoSuchAlgorithmException {
        if (listViewUsuarios.getSelectionModel().isEmpty()) {
            String error = "Error, seleccione un Usuario";
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.getDialogPane().lookupButton(ButtonType.OK).setId("alertOK");
            alert.setContentText(error);
            alert.showAndWait();
        } else if (((Usuario) listViewUsuarios.getSelectionModel().getSelectedItem()).getIdTipoUsuario() == 1 || ((Usuario) listViewUsuarios.getSelectionModel().getSelectedItem()).getIdTipoUsuario() == 2) {
            actualizarUsuario();
        } else if (((Usuario) listViewUsuarios.getSelectionModel().getSelectedItem()).getIdTipoUsuario() == 3) {
            actualizarLector();
        }
    }

    private void actualizarUsuario() throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA3-512");
        byte[] passwordEnBytes = md.digest(txtPasswordUsuarios.getText().getBytes());
        var passwordHasheada = serviciosUsuarios.hashearPassword(passwordEnBytes);

        if (((Usuario) listViewUsuarios.getSelectionModel().getSelectedItem()).getPassword().equals(passwordHasheada)) {
            serviciosUsuarios.actualizarUsuario(((Usuario) listViewUsuarios.getSelectionModel().getSelectedItem()).getId(), txtUserUsuarios.getText(), txtPasswordUsuarios.getText(), txtMailUsuarios.getText());
        } else {
            serviciosUsuarios.actualizarUsuario(((Usuario) listViewUsuarios.getSelectionModel().getSelectedItem()).getId(), txtUserUsuarios.getText(), passwordHasheada, txtMailUsuarios.getText());
        }
        cargarListViews();
        String error = "Usuario actualizado";
        alert = new Alert(Alert.AlertType.INFORMATION);
        alert.getDialogPane().lookupButton(ButtonType.OK).setId("alertOK");
        alert.setContentText(error);
        alert.showAndWait();
        cargarListViews();
    }

    private void actualizarLector() throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA3-512");
        byte[] passwordEnBytes = md.digest(txtPasswordUsuarios.getText().getBytes());
        String error = "";
        var passwordHasheada = serviciosUsuarios.hashearPassword(passwordEnBytes);
        if (dateFechaUsuarios.getValue() == null || txtNombreUsuarios.getText().equals("")) {
            error = "Complete los campos fecha y nombre para poder actualizar un Usuario Lector";
        } else {
            if (((Usuario) listViewUsuarios.getSelectionModel().getSelectedItem()).getPassword().equals(passwordHasheada)) {
                serviciosLectores.actualizarLector(((Usuario) listViewUsuarios.getSelectionModel().getSelectedItem()).getId(), txtUserUsuarios.getText(), txtPasswordUsuarios.getText(), txtMailUsuarios.getText(), txtNombreUsuarios.getText(), dateFechaUsuarios.getValue());
            } else {
                serviciosLectores.actualizarLector(((Usuario) listViewUsuarios.getSelectionModel().getSelectedItem()).getId(), txtUserUsuarios.getText(), passwordHasheada, txtMailUsuarios.getText(), txtNombreUsuarios.getText(), dateFechaUsuarios.getValue());
            }
            error = "Lector actualizado";
        }
        alert = new Alert(Alert.AlertType.INFORMATION);
        alert.getDialogPane().lookupButton(ButtonType.OK).setId("alertOK");
        alert.setContentText(error);
        alert.showAndWait();
        cargarListViews();
    }

    public void darDeAltaPeriodico(ActionEvent actionEvent) {
        var error = "";
        boolean correcto = true;
        if (!txtNombrePeriodico.getText().equals("") && !txtPrecioPeriodico.getText().equals("") && !comboBoxIdAdminPeriodico.getSelectionModel().isEmpty()) {
            try {
                Double.parseDouble(txtPrecioPeriodico.getText());
            } catch (Exception e) {
                correcto = false;
            }
            if (correcto == true) {
                Periodico periodico = new Periodico(txtNombrePeriodico.getText(), Double.parseDouble(txtPrecioPeriodico.getText()), txtDirectorPeriodico.getText(), ((Usuario) comboBoxIdAdminPeriodico.getSelectionModel().getSelectedItem()).getId());
                correcto = serviciosPeriodicos.addPeriodico(periodico);
                if (correcto == true) {
                    listViewPeriodicos.getItems().add(periodico);
                    error = "Periodico añadido";
                } else {
                    error = "Error al añadir Periodico falta por rellenar algun campo o el nombre del periodico ya existe";
                }

            } else {
                error = "Error al añadir Periodico, el Precio no es valido";
            }
        } else {
            error = "Error, Faltan campos por rellenar";
        }
        alert = new Alert(Alert.AlertType.INFORMATION);
        alert.getDialogPane().lookupButton(ButtonType.OK).setId("alertOK");
        alert.setContentText(error);
        alert.showAndWait();
        cargarListViews();
    }

    public void borrarPeriodico(ActionEvent actionEvent) {
        var error = "";
        if (!listViewPeriodicos.getSelectionModel().isEmpty()) {
            serviciosPeriodicos.borrarPeriodico((Periodico) listViewPeriodicos.getSelectionModel().getSelectedItem());
            error = "Periodico Borrado correctamente";
            cargarListViews();
        } else {
            error = "Elige periodico para poder borrarlo";
        }
        alert = new Alert(Alert.AlertType.INFORMATION);
        alert.getDialogPane().lookupButton(ButtonType.OK).setId("alertOK");
        alert.setContentText(error);
        alert.showAndWait();
        cargarListViews();
    }

    public void actualizarPeriodico(ActionEvent actionEvent) {
        var error = "";
        boolean correcto = true;
        if (!txtNombrePeriodico.getText().equals("") && !txtPrecioPeriodico.getText().equals("") && !comboBoxIdAdminPeriodico.getSelectionModel().isEmpty() && !listViewPeriodicos.getSelectionModel().isEmpty()) {
            try {
                Double.parseDouble(txtPrecioPeriodico.getText());
            } catch (Exception e) {
                correcto = false;
            }
            if (correcto == true) {
                serviciosPeriodicos.actualizarPeriodico(txtNombrePeriodico.getText(), Double.valueOf(txtPrecioPeriodico.getText()), txtDirectorPeriodico.getText(), ((Usuario) comboBoxIdAdminPeriodico.getSelectionModel().getSelectedItem()).getId(), ((Periodico) listViewPeriodicos.getSelectionModel().getSelectedItem()).getId());
                error = "Periodico Actualizado";
            } else {
                error = "Error al actualizar Periodico, el Precio no es valido";
            }
        } else {
            error = "Error, Faltan campos por rellenar";
        }
        alert = new Alert(Alert.AlertType.INFORMATION);
        alert.getDialogPane().lookupButton(ButtonType.OK).setId("alertOK");
        alert.setContentText(error);
        alert.showAndWait();
        cargarListViews();
    }


    public void addArticulo(ActionEvent actionEvent) {
        var error = "";
        if (!txtAutorArticulo.getText().equals("") && !textareaDescripcionArticulo.getText().equals("") && !txtTitularArticulo.getText().equals("") && !comboBoxIdPeriodicoArticulo.getSelectionModel().isEmpty() && !comboBoxIdTipoArticulo.getSelectionModel().isEmpty()) {
            Articulo articulo = new Articulo(txtTitularArticulo.getText(), textareaDescripcionArticulo.getText(), ((Periodico) comboBoxIdPeriodicoArticulo.getSelectionModel().getSelectedItem()).getId(), ((TipoArticulo) comboBoxIdTipoArticulo.getSelectionModel().getSelectedItem()).getId(), txtAutorArticulo.getText());
            boolean correcto = serviciosArticulo.addArticulo(articulo);
            if (!correcto) {
                error = "Error al añadir Articulo, faltan campos por rellenar";
            } else {
                listViewArticulos.getItems().add(articulo);
                error = "Articulo agregado";
            }
        } else {
            error = "Error al crear Articulo";
        }
        alert = new Alert(Alert.AlertType.INFORMATION);
        alert.getDialogPane().lookupButton(ButtonType.OK).setId("alertOK");
        alert.setContentText(error);
        alert.showAndWait();
        cargarListViews();
    }

    public void borrarArticulo(ActionEvent actionEvent) {
        var error = "";
        if (!listViewArticulos.getSelectionModel().isEmpty()) {
            serviciosArticulo.borrarArticulo((Articulo) listViewArticulos.getSelectionModel().getSelectedItem());
            error = "Articulo Borrado correctamente";
            cargarListViews();
        } else {
            error = "Elige articulo para poder borrarlo";
        }
        alert = new Alert(Alert.AlertType.INFORMATION);
        alert.getDialogPane().lookupButton(ButtonType.OK).setId("alertOK");
        alert.setContentText(error);
        alert.showAndWait();
        cargarListViews();
    }

    public void actualizarArticulo(ActionEvent actionEvent) {
        var error = "";
        if (!txtAutorArticulo.getText().equals("") && !textareaDescripcionArticulo.getText().equals("") && !txtTitularArticulo.getText().equals("") && !comboBoxIdPeriodicoArticulo.getSelectionModel().isEmpty() && !comboBoxIdTipoArticulo.getSelectionModel().isEmpty() && !listViewArticulos.getSelectionModel().isEmpty()) {
            serviciosArticulo.actualizarArticulo(txtTitularArticulo.getText(), textareaDescripcionArticulo.getText(), ((Periodico) comboBoxIdPeriodicoArticulo.getSelectionModel().getSelectedItem()).getId(), ((TipoArticulo) comboBoxIdTipoArticulo.getSelectionModel().getSelectedItem()).getId(), txtAutorArticulo.getText(), ((Articulo) listViewArticulos.getSelectionModel().getSelectedItem()).getId());
            error = "Datos del Articulo actualizados";
        } else {
            error = "Error. Rellene todos los campos o seleccione un articulo para poder actualizar los datos del articulo";
        }
        alert = new Alert(Alert.AlertType.INFORMATION);
        alert.getDialogPane().lookupButton(ButtonType.OK).setId("alertOK");
        alert.setContentText(error);
        alert.showAndWait();
        cargarListViews();
    }

    public void addSuscripcion(ActionEvent actionEvent) {
        var error = "";
        if (!comboBoxIdLectorSuscripciones.getSelectionModel().isEmpty() && !comboBoxIdPeriodicoSuscripciones.getSelectionModel().isEmpty() && !dateFechaInicioSuscripciones.getValue().toString().equals("")) {
            Suscripcion suscripcion = new Suscripcion(((Usuario) comboBoxIdLectorSuscripciones.getSelectionModel().getSelectedItem()).getId(), (((Periodico) comboBoxIdPeriodicoSuscripciones.getSelectionModel().getSelectedItem()).getId()), dateFechaInicioSuscripciones.getValue(), null);
            boolean correcto = serviciosSuscripciones.addSuscripcion(suscripcion);
            if (!correcto) {
                error = "Error al añadir Suscripcion";
            } else {
                listViewArticulos.getItems().add(suscripcion);
                error = "Suscripcion añadida";
            }
        } else {
            error = "Error al crear Suscripcion, faltan campos por rellenar";
        }
        alert = new Alert(Alert.AlertType.INFORMATION);
        alert.getDialogPane().lookupButton(ButtonType.OK).setId("alertOK");
        alert.setContentText(error);
        alert.showAndWait();
        cargarListViews();
    }

    public void borrarSuscripcion(ActionEvent actionEvent) {
        var error = "";
        if (!listViewSuscripciones.getSelectionModel().isEmpty()) {
            serviciosSuscripciones.borrarSuscripcion((Suscripcion) listViewSuscripciones.getSelectionModel().getSelectedItem());
            error = "Suscripcion borrada correctamente";
            cargarListViews();
        } else {
            error = "Elige Suscripcion para poder borrarla";
        }
        alert = new Alert(Alert.AlertType.INFORMATION);
        alert.getDialogPane().lookupButton(ButtonType.OK).setId("alertOK");
        alert.setContentText(error);
        alert.showAndWait();
        cargarListViews();
    }

    public void actualizarSuscripcion(ActionEvent actionEvent) {
        var error = "";
        if (!comboBoxIdLectorSuscripciones.getSelectionModel().isEmpty() && !comboBoxIdPeriodicoSuscripciones.getSelectionModel().isEmpty() && !(dateFechaInicioSuscripciones.getValue() == null) && !listViewSuscripciones.getSelectionModel().isEmpty()) {
            serviciosSuscripciones.actualizarSuscripcion(((Usuario) comboBoxIdLectorSuscripciones.getSelectionModel().getSelectedItem()).getId(), ((Periodico) comboBoxIdPeriodicoSuscripciones.getSelectionModel().getSelectedItem()).getId(), dateFechaInicioSuscripciones.getValue(), (((Suscripcion) listViewSuscripciones.getSelectionModel().getSelectedItem()).getId()));
            error = "Datos de la Suscripcion actualizados";
        } else {
            error = "Error. Rellene todos los campos para poder actualizar los datos de la Suscripcion";
        }
        alert = new Alert(Alert.AlertType.INFORMATION);
        alert.getDialogPane().lookupButton(ButtonType.OK).setId("alertOK");
        alert.setContentText(error);
        alert.showAndWait();
        cargarListViews();
    }

    private void cargarListViews() {
        comboTipoUsuario.getItems().clear();
        comboTipoUsuario.getItems().addAll(serviciosTiposUsuarios.getTipos());
        listViewUsuarios.getItems().clear();
        listViewUsuarios.getItems().addAll(serviciosUsuarios.getUsuarios());
        listViewPeriodicos.getItems().clear();
        listViewPeriodicos.getItems().addAll(serviciosPeriodicos.getPeriodicos());
        comboBoxIdAdminPeriodico.getItems().clear();
        comboBoxIdAdminPeriodico.getItems().addAll(serviciosUsuarios.getUsuarioAdminPeriodicos());
        listViewArticulos.getItems().clear();
        listViewArticulos.getItems().addAll(serviciosArticulo.getArticulos());
        comboBoxIdTipoArticulo.getItems().clear();
        comboBoxIdTipoArticulo.getItems().addAll(serviciosTiposArticulo.getTiposArticulo());
        comboBoxIdPeriodicoArticulo.getItems().clear();
        comboBoxIdPeriodicoArticulo.getItems().addAll(serviciosPeriodicos.getPeriodicos());
        listViewSuscripciones.getItems().clear();
        listViewSuscripciones.getItems().addAll(serviciosSuscripciones.getSuscripciones());
        comboBoxIdLectorSuscripciones.getItems().clear();
        comboBoxIdLectorSuscripciones.getItems().addAll(serviciosUsuarios.getUsuariosLectores());
        comboBoxIdPeriodicoSuscripciones.getItems().clear();
        comboBoxIdPeriodicoSuscripciones.getItems().addAll(serviciosPeriodicos.getPeriodicos());
    }

    public void volverAtras(ActionEvent actionEvent) {
        principalController.menuLogIn();
    }

    public void pasarDelListViewATextFieldUsuarios(javafx.scene.input.MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 1) {
            txtNombreUsuarios.setText(((Usuario) listViewUsuarios.getSelectionModel().getSelectedItem()).getNombre());
            txtUserUsuarios.setText(((Usuario) listViewUsuarios.getSelectionModel().getSelectedItem()).getUsuario());
            txtPasswordUsuarios.setText(((Usuario) listViewUsuarios.getSelectionModel().getSelectedItem()).getPassword());
            txtMailUsuarios.setText(((Usuario) listViewUsuarios.getSelectionModel().getSelectedItem()).getEMail());
            dateFechaUsuarios.setValue(((Usuario) listViewUsuarios.getSelectionModel().getSelectedItem()).getFechaNacimiento());
        }
    }

    public void pasarDelListViewATextFieldPeriodicos(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 1) {
            txtNombrePeriodico.setText(((Periodico) listViewPeriodicos.getSelectionModel().getSelectedItem()).getNombre());
            txtPrecioPeriodico.setText(String.valueOf(((Periodico) listViewPeriodicos.getSelectionModel().getSelectedItem()).getPrecio()));
            txtDirectorPeriodico.setText(((Periodico) listViewPeriodicos.getSelectionModel().getSelectedItem()).getDirector());
        }
    }

    public void pasarDelListViewATextFieldArticulos(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 1) {
            txtTitularArticulo.setText(((Articulo) listViewArticulos.getSelectionModel().getSelectedItem()).getTitular());
            textareaDescripcionArticulo.setText(((Articulo) listViewArticulos.getSelectionModel().getSelectedItem()).getDescripcion());
            txtAutorArticulo.setText(((Articulo) listViewArticulos.getSelectionModel().getSelectedItem()).getAutor());
        }
    }

    public void irPag2(ActionEvent actionEvent) {
        principalController.menuAdminGlobal2();
    }
}
