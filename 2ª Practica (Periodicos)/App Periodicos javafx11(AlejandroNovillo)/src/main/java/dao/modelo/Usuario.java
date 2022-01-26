package dao.modelo;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class Usuario {
    private int id;
    private String usuario;
    private String password;
    private String eMail;
    private String nombre;
    private LocalDate fechaNacimiento;
    private int idTipoUsuario;

    public Usuario(String usuario, String password, String eMail, String nombre, LocalDate fechaNacimiento, int idTipoUsuario) {
        this.usuario = usuario;
        this.password = password;
        this.eMail = eMail;
        this.nombre = nombre;
        this.fechaNacimiento = fechaNacimiento;
        this.idTipoUsuario = idTipoUsuario;
    }

    public Usuario(int id, String user, String password, String mail, int id_tipo_usuario) {
        this.id = id;
        this.usuario = user;
        this.password = password;
        this.eMail = mail;
        this.idTipoUsuario = id_tipo_usuario;
    }

    public Usuario(String user, String password, String mail, int id_tipo_usuario) {
        this.usuario = user;
        this.password = password;
        this.eMail = mail;
        this.idTipoUsuario = id_tipo_usuario;
    }

    @Override
    public String toString() {
        String tipoDeUsuarioLetra = "";
        if (idTipoUsuario == 1) {
            tipoDeUsuarioLetra = "Administrador Global";
        } else if (idTipoUsuario == 2) {
            tipoDeUsuarioLetra = "Administrador de Periodicos";
        } else if (idTipoUsuario == 3) {
            tipoDeUsuarioLetra = "Lector";
        }
        return "Usuario: " +
                "Id=" + id +
                "  ||  Nombre de Usuario= " + usuario +
                "  ||  Contraseña= '*****' " +
                "  ||  EMail= " + eMail +
                "  ||  Nivel Permiso de Usuario= " + tipoDeUsuarioLetra;
    }
}
