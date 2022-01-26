package dao;


import dao.modelo.Usuario;
import lombok.extern.log4j.Log4j2;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Log4j2
public class DaoUsuarios {
    private static final String SELECT_USUARIOS =
            "select * From usuarios";
    private static final String QUERY_INSERTAR_USUARIO =
            "insert into usuarios  (user,password,mail,id_tipo_usuario) " +
                    "values(?,?,?,?) ";
    private static final String QUERY_DELETE_LECTOR =
            "delete from lectores where id = ?";
    private static final String QUERY_DELETE_USUARIO =
            "delete from usuarios where id = ?";

    private static final String SELECT_PASSWORD_USUARIOS_LOGIN =
            "select password from usuarios where user = ?";
    private static final String SELECT_IDTIPODEUSUARIO_USUARIOS_LOGIN =
            "select id_tipo_usuario from usuarios where user = ?";
    public static final String UPDATE_USUARIO =
            "update usuarios set user= ?,password = ?, mail = ? where id = ?";

    private static final String SELECT_USUARIOS_ADMINS_PERIODICOS =
            "select * from usuarios where id_tipo_usuario=2";

    private static final String SELECT_USUARIOS_LECTORES =
            "select * from usuarios where id_tipo_usuario=3";

    private static final String SELECT_USUARIO_POR_NOMBRE =
            "select * from usuarios where id_tipo_usuario=?";


    public List<Usuario> getUsuarios() {
        List<Usuario> usuarios = new ArrayList<>();
        Connection con = null;
        DBConnection db = new DBConnection();
        Statement stmt = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {

            con = db.getConnection();
            stmt = con.createStatement();

            pst = con.prepareStatement(
                    SELECT_USUARIOS);
            rs = pst.executeQuery();
            while (rs.next()) {
                Usuario usuario = new Usuario(Integer.parseInt(rs.getString("id"))
                        , rs.getString("user")
                        , rs.getString("password")
                        , rs.getString("mail")
                        , Integer.parseInt(rs.getString("id_tipo_usuario")));
                usuarios.add(usuario);
            }

        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            db.cerrarResultSet(rs);
            db.cerrarStatement(stmt);
            db.cerrarConexion(con);
        }

        return usuarios;
    }

    public boolean addUsuario(Usuario l) {
        var añadido = false;
        DBConnection db = new DBConnection();
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            con = db.getConnection();
            con.setAutoCommit(false);
            stmt = con.prepareStatement
                    (QUERY_INSERTAR_USUARIO,
                            Statement.RETURN_GENERATED_KEYS);

            stmt.setString(1, l.getUsuario());
            stmt.setString(2, l.getPassword());
            stmt.setString(3, l.getEMail());
            stmt.setString(4, String.valueOf(l.getIdTipoUsuario()));


            stmt.executeUpdate();
            añadido = true;
            rs = stmt.getGeneratedKeys();

            long id = 0;
            if (rs != null && rs.next()) {
                id = rs.getLong(1);
            }

            l.setId((int) id);

        } catch (Exception e) {
            log.error(e.getMessage());
            db.rollbackCon(con);
        } finally {
            db.cerrarResultSet(rs);
            db.cerrarStatement(stmt);
            db.cerrarConexion(con);
        }
        return añadido;
    }

    public void borrarUsuarios(Usuario usuario) {
        DBConnection db = new DBConnection();
        Connection con = null;
        PreparedStatement stmt = null;

        try {
            con = db.getConnection();
            con.setAutoCommit(false);
            stmt = con.prepareStatement
                    (QUERY_DELETE_LECTOR);
            stmt.setInt(1,
                    (int) usuario.getId());
            stmt.executeUpdate();


            stmt = con.prepareStatement
                    (QUERY_DELETE_USUARIO);
            stmt.setInt(1,
                    (int) usuario.getId());

            stmt.executeUpdate();

            con.commit();
        } catch (SQLIntegrityConstraintViolationException e) {
            Logger.getLogger("Main").info("INTE!" + e.getMessage());
            db.rollbackCon(con);
        } catch (SQLException e) {
            db.rollbackCon(con);
            if (e.getMessage().contains("foreign key")) {
                System.out.println(e.getErrorCode());
            }
            Logger.getLogger("Main").info("SQL" + e.getMessage());
        } catch (Exception e) {
            Logger.getLogger("Main").info(e.getMessage());
            db.rollbackCon(con);
        } finally {
            db.cerrarStatement(stmt);
            db.cerrarConexion(con);
        }
    }

    public String getPasswordLogin(String nombreUsuario) {
        String passwordUsuario = "";
        DBConnection db = new DBConnection();
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;


        try {
            con = db.getConnection();
            con.setAutoCommit(false);
            stmt = con.prepareStatement
                    (SELECT_PASSWORD_USUARIOS_LOGIN);
            stmt.setString(1,
                    nombreUsuario);
            rs = stmt.executeQuery();
            while (rs.next()) {
                passwordUsuario = rs.getString("password");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            db.cerrarResultSet(rs);
            db.cerrarStatement(stmt);
            db.cerrarConexion(con);
        }
        return passwordUsuario;

    }

    public Integer getIdTipoUsuarioLogIn(String nombreUsuario) {
        int passwordUsuario = 0;
        DBConnection db = new DBConnection();
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;


        try {
            con = db.getConnection();
            con.setAutoCommit(false);
            stmt = con.prepareStatement
                    (SELECT_IDTIPODEUSUARIO_USUARIOS_LOGIN);
            stmt.setString(1,
                    nombreUsuario);
            rs = stmt.executeQuery();
            while (rs.next()) {
                passwordUsuario = rs.getInt("id_tipo_usuario");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            db.cerrarResultSet(rs);
            db.cerrarStatement(stmt);
            db.cerrarConexion(con);
        }
        return passwordUsuario;
    }

    public void actualizarUsuario(int id, String usuario, String password, String eMail) {
        DBConnection db = new DBConnection();
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            con = db.getConnection();

            stmt = con.prepareStatement(UPDATE_USUARIO);

            stmt.setString(1, usuario);
            stmt.setString(2, password);
            stmt.setString(3, eMail);
            stmt.setInt(4, id);
            stmt.executeUpdate();
        } catch (Exception e) {
            Logger.getLogger("Main").info(e.getMessage());
        } finally {
            db.cerrarConexion(con);
        }
    }

    public List<Usuario> getIdAdministradoresPeriodicos() {
        List<Usuario> usuarios = new ArrayList<>();
        Connection con = null;
        DBConnection db = new DBConnection();
        Statement stmt = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            con = db.getConnection();
            stmt = con.createStatement();

            pst = con.prepareStatement(
                    SELECT_USUARIOS_ADMINS_PERIODICOS);
            rs = pst.executeQuery();
            while (rs.next()) {
                Usuario usuario = new Usuario(Integer.parseInt(rs.getString("id"))
                        , rs.getString("user")
                        , rs.getString("password")
                        , rs.getString("mail")
                        , Integer.parseInt(rs.getString("id_tipo_usuario")));
                usuarios.add(usuario);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            db.cerrarResultSet(rs);
            db.cerrarStatement(stmt);
            db.cerrarConexion(con);
        }
        return usuarios;
    }

    public List<Usuario> getUsuarioLectores() {
        List<Usuario> usuarios = new ArrayList<>();
        Connection con = null;
        DBConnection db = new DBConnection();
        Statement stmt = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            con = db.getConnection();
            stmt = con.createStatement();

            pst = con.prepareStatement(
                    SELECT_USUARIOS_LECTORES);
            rs = pst.executeQuery();
            while (rs.next()) {
                Usuario usuario = new Usuario(Integer.parseInt(rs.getString("id"))
                        , rs.getString("user")
                        , rs.getString("password")
                        , rs.getString("mail")
                        , Integer.parseInt(rs.getString("id_tipo_usuario")));
                usuarios.add(usuario);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            db.cerrarResultSet(rs);
            db.cerrarStatement(stmt);
            db.cerrarConexion(con);
        }
        return usuarios;
    }

    public Usuario getUsuarioPorNombre(String nombreUsuario) {
        Usuario usuario = null;
        DBConnection db = new DBConnection();
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            con = db.getConnection();
            stmt = con.prepareStatement
                    (SELECT_USUARIO_POR_NOMBRE);
            stmt.setString(1,
                    nombreUsuario);
            rs = stmt.executeQuery();
            while (rs.next()) {
                usuario = new Usuario(Integer.parseInt(rs.getString("id"))
                        , rs.getString("user")
                        , rs.getString("password")
                        , rs.getString("mail")
                        , Integer.parseInt(rs.getString("id_tipo_usuario")));
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            db.cerrarResultSet(rs);
            db.cerrarStatement(stmt);
            db.cerrarConexion(con);
        }
        return usuario;
    }
}