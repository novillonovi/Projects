package controllers;

import javafx.event.ActionEvent;
import javafx.scene.control.ListView;
import servicios.ServiciosUsuarios;

public class AdminPeriodicos {
    public ListView listViewPeriodicos;
    private Principal principalController;
    ServiciosUsuarios serviciosUsuarios =new ServiciosUsuarios();

    public void setPrincipalController(Principal principalController) {
        this.principalController = principalController;
    }

    public void volverAtras(ActionEvent actionEvent) {
        principalController.menuLogIn();
    }

    public void cargarListView() {
        listViewPeriodicos.getItems().clear();
        /*listViewPeriodicos.getItems().addAll(serviciosUsuarios.);*/
    }

    public void darDeBajaPeriodico(){}

    public void darDeAltaPeriodico(){}
}
