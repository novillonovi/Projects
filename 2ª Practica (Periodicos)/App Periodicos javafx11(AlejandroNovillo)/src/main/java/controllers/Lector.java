package controllers;

import dao.modelo.Usuario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import servicios.ServiciosLectores;
import servicios.ServiciosUsuarios;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class Lector{
    public Label labelNombreUsuario;
    public Label labelPassword;
    public Label labelEmail;
    public Label labelID;
    public TextField txtNombreUsuario;
    public TextField txtPassword;
    public TextField txtMail;
    public TextField txtNombre;
    public DatePicker datePickerLector;
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    Principal principalController;
    ServiciosUsuarios serviciosUsuarios = new ServiciosUsuarios();
    ServiciosLectores serviciosLectores =new ServiciosLectores();

    public void setPrincipalController(Principal principalController) {
        this.principalController = principalController;
    }

    public void volverAtras(ActionEvent actionEvent) {
        principalController.menuLogIn();
    }

/*    public void cargarDatosLector(String nombreUsuario) {
        labelNombreUsuario.setText(serviciosUsuarios.getUsuarioPorNombre(nombreUsuario).getUsuario());
        labelEmail.setText(serviciosUsuarios.getUsuarioPorNombre(nombreUsuario).getEMail());
        labelPassword.setText("****");
        labelID.setText(String.valueOf(serviciosUsuarios.getUsuarioPorNombre(nombreUsuario).getIdTipoUsuario()));
    }*/


/*    @FXML
    private void actualizarLector() throws NoSuchAlgorithmException {
        String error = "";

        if (datePickerLector.getValue() == null || txtNombreUsuario.getText().equals("") || txtPassword.getText().equals("")) {
            error = "Complete los campos fecha y nombre para poder crear un Usuario Lector";
        } else {
            MessageDigest md = MessageDigest.getInstance("SHA3-512");
            byte[] passwordEnBytes = md.digest(txtPassword.getText().getBytes());
            var passwordHasheada = serviciosUsuarios.hashearPassword(passwordEnBytes);

            if (((Usuario) serviciosUsuarios.getUsuarioPorNombre(nombreUsuario)).getPassword().equals(passwordHasheada)) {
                serviciosLectores.actualizarLector(((Usuario)serviciosUsuarios.getUsuarioPorNombre(nombreUsuario)).getId(), txtNombreUsuario.getText(), txtPassword.getText(), txtMail.getText(), txtNombre.getText(), datePickerLector.getValue());
            } else {
                serviciosLectores.actualizarLector(((Usuario)serviciosUsuarios.getUsuarioPorNombre(nombreUsuario)).getId(), txtNombreUsuario.getText(), passwordHasheada, txtMail.getText(), txtNombre.getText(), datePickerLector.getValue());
            }
            error = "Lector actualizado";
        }

        alert.getDialogPane().lookupButton(ButtonType.OK).setId("alertOK");
        alert.setContentText(error);
        alert.showAndWait();
    }*/
}
