package controllers;


import dao.modelo.TipoArticulo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import servicios.*;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminGlobalpag2 implements Initializable {
    @FXML
    public ListView listViewTiposArticulo;
    @FXML
    public TextField textFieldTipoArticulo;

    private ServiciosTiposArticulo serviciosTiposArticulo = new ServiciosTiposArticulo();
    private Alert alert;
    private Principal principalController;
    public void setPrincipalController(Principal principalController) {
        this.principalController = principalController;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cargarListView();
    }

    private void cargarListView() {
        listViewTiposArticulo.getItems().clear();
        listViewTiposArticulo.getItems().addAll(serviciosTiposArticulo.getTiposArticulo());
    }

    public void AñadirTipoArticulo(ActionEvent actionEvent) {
        var error = "";
        if (!textFieldTipoArticulo.getText().equals("")) {
            TipoArticulo tipoArticulo = new TipoArticulo(textFieldTipoArticulo.getText());
            boolean correcto = serviciosTiposArticulo.addTipoArticulo(tipoArticulo);
            if (!correcto) {
                error = "Error al añadir Tipo de Articulo";
            } else {
                listViewTiposArticulo.getItems().add(tipoArticulo);
                error = "Tipo de Articulo agregado";
            }
        } else {
            error = "Error al crear Tipo de Articulo";
        }
        alert = new Alert(Alert.AlertType.INFORMATION);
        alert.getDialogPane().lookupButton(ButtonType.OK).setId("alertOK");
        alert.setContentText(error);
        alert.showAndWait();
        cargarListView();
    }

    public void BorrarTipoArticulo(ActionEvent actionEvent) {
        var error = "";
        if (!listViewTiposArticulo.getSelectionModel().isEmpty()) {
            serviciosTiposArticulo.borrarTipoArticulo((TipoArticulo) listViewTiposArticulo.getSelectionModel().getSelectedItem());
            error = "Tipo de Articulo Borrado correctamente";
            cargarListView();
        } else {
            error = "Elige el Tipo de Articulo para poder borrarlo";
            alert.showAndWait();
        }
        alert = new Alert(Alert.AlertType.INFORMATION);
        alert.getDialogPane().lookupButton(ButtonType.OK).setId("alertOK");
        alert.setContentText(error);
        alert.showAndWait();
        cargarListView();
    }

    public void ActualizarTipoArticulo(ActionEvent actionEvent) {
        var error = "";
        if (!textFieldTipoArticulo.getText().equals("") ){
            serviciosTiposArticulo.actualizarTipoArticulo(textFieldTipoArticulo.getText(), ((TipoArticulo) listViewTiposArticulo.getSelectionModel().getSelectedItem()).getId());
            error = "Datos del Tipo de Articulo actualizados";
        } else {
            error = "Error. Rellene todos los campos para poder actualizar los datos del Tipo de articulo";
        }
        alert = new Alert(Alert.AlertType.INFORMATION);
        alert.getDialogPane().lookupButton(ButtonType.OK).setId("alertOK");
        alert.setContentText(error);
        alert.showAndWait();
        cargarListView();
    }

    public void VolverLogin(ActionEvent actionEvent) {
        principalController.menuLogIn();
    }

    public void volverAdminGlobal1(ActionEvent actionEvent) {
        principalController.menuAdminGlobal();
    }

    public void pasarDelListViewATextField(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 1) {
            textFieldTipoArticulo.setText(((TipoArticulo) listViewTiposArticulo.getSelectionModel().getSelectedItem()).getTipo());

        }
    }
}
