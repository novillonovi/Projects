package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import lombok.SneakyThrows;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Principal implements Initializable {
  @FXML
  private BorderPane root;
  @FXML
  private AnchorPane paneLectores;
  private AnchorPane paneAdminGlobal2;
  private AnchorPane paneAdminGlobal;
  private AnchorPane paneAdminPeriodicos;
  private AnchorPane paneLogIn;
  private AdminGlobal adminGlobalController;
  private AdminGlobalpag2 adminGlobalController2;
  private AdminPeriodicos adminPeriodicosController;
  private Lector lectorController;
  private LogIn logInController;

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
     menuAdminGlobal();
  }

  @SneakyThrows
  public void menuAdminGlobal() {
    FXMLLoader loaderMenu = new FXMLLoader(
            getClass().getResource("/fxml/adminGlobal.fxml"));
    try {
      if (paneAdminGlobal == null) {
        paneAdminGlobal = loaderMenu.load();
        adminGlobalController = loaderMenu.getController();
        adminGlobalController.setPrincipalController(this);
      }
      root.setCenter(paneAdminGlobal);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  @SneakyThrows
  public void menuAdminGlobal2() {
    FXMLLoader loaderMenu = new FXMLLoader(
            getClass().getResource("/fxml/adminGlobalpag2.fxml"));
    try {
      if (paneAdminGlobal2 == null) {
        paneAdminGlobal2 = loaderMenu.load();
        adminGlobalController2 = loaderMenu.getController();
        adminGlobalController2.setPrincipalController(this);
      }
      root.setCenter(paneAdminGlobal2);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @SneakyThrows
  public void menuAdminPeriodicos() {
    FXMLLoader loaderMenu = new FXMLLoader(
            getClass().getResource("/fxml/adminPeriodicos.fxml"));
    try {
      if (paneAdminPeriodicos == null) {
        paneAdminPeriodicos = loaderMenu.load();
        adminPeriodicosController = loaderMenu.getController();
        adminPeriodicosController.setPrincipalController(this);
      }
      root.setCenter(paneAdminPeriodicos);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @SneakyThrows
  public void menuLector() {
    FXMLLoader loaderMenu = new FXMLLoader(
            getClass().getResource("/fxml/lector.fxml"));
    try {
      if (paneLectores == null) {
        paneLectores = loaderMenu.load();
        lectorController = loaderMenu.getController();
        lectorController.setPrincipalController(this);
      }
      root.setCenter(paneLectores);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }


  @SneakyThrows
  public void menuLogIn() {
    FXMLLoader loaderMenu = new FXMLLoader(
            getClass().getResource("/fxml/logIn.fxml"));
    try {
      if (paneLogIn == null) {
        paneLogIn = loaderMenu.load();
        logInController = loaderMenu.getController();
        logInController.setPrincipalController(this);
      }
      root.setCenter(paneLogIn);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
