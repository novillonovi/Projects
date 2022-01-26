package controllers;


import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import servicios.ServiciosLectores;
import servicios.ServiciosTiposUsuarios;
import servicios.ServiciosUsuarios;

import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ResourceBundle;

public class LogIn {
    public TextField textFieldPassword;
    public TextField textFieldUser;
    private Principal principalController;
    private ServiciosUsuarios serviciosUsuarios = new ServiciosUsuarios();
    private Alert alert;
//    public String nombreUsuario;

/*    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }*/

    public void setPrincipalController(Principal principalController) {
        this.principalController = principalController;
    }

    public void logIn(ActionEvent actionEvent) throws NoSuchAlgorithmException {
        var error = "";
        if (textFieldPassword.getText().isBlank() || textFieldUser.getText().isBlank()) {
            error = "Error, rellena los datos";
        } else {
            MessageDigest md = MessageDigest.getInstance("SHA3-512");
            byte[] passwordEnBytes = md.digest(textFieldPassword.getText().getBytes());
            var passwordHasheada = serviciosUsuarios.hashearPassword(passwordEnBytes);
            if (serviciosUsuarios.getPasswordUsuario(textFieldUser.getText()).equals(passwordHasheada) && serviciosUsuarios.getIdTipoUsuario(textFieldUser.getText()).equals(1)) {
//                setNombreUsuario(textFieldUser.getText());
                principalController.menuAdminGlobal();
                error = "Has Accedido como Administrador Global";
            } else if (serviciosUsuarios.getPasswordUsuario(textFieldUser.getText()).equals(passwordHasheada) && serviciosUsuarios.getIdTipoUsuario(textFieldUser.getText()).equals(2)) {
//                setNombreUsuario(textFieldUser.getText());
                principalController.menuAdminPeriodicos();
                error = "Has Accedido como Administrador de Periodicos";
            } else if (serviciosUsuarios.getPasswordUsuario(textFieldUser.getText()).equals(passwordHasheada) && serviciosUsuarios.getIdTipoUsuario(textFieldUser.getText()).equals(3)) {
//                setNombreUsuario(textFieldUser.getText());
                principalController.menuLector();
                error = "Has Accedido como Lector";
            } else {
                error = "Error, los datos no son correctos.";
            }
            textFieldPassword.setText("");
            textFieldUser.setText("");
        }
        alert = new Alert(Alert.AlertType.INFORMATION);
        alert.getDialogPane().lookupButton(ButtonType.OK).setId("alertOK");
        alert.setContentText(error);
        alert.showAndWait();
    }
}
