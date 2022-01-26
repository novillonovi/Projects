package dao;

import dao.modelo.Usuario;
import lombok.extern.log4j.Log4j2;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Log4j2
public class DaoLectores {

    private static final String SELECT_LECTORES =
            "select * from lectores";
    private static String QUERY_INSERTAR_USUARIO =
            "insert into usuarios  (user,password,mail,id_tipo_usuario) " +
                    "values(?,?,?,?) ";

    private static String QUERY_INSERTAR_LECTOR = " insert into lectores " +
            "(id, nombre, fechaNacimiento) VALUES (?,?,?)";

    public static final String UPDATE_LECTOR =
            "update lectores set nombre= ?, fechaNacimiento = ? where id = ?";

    public static final String UPDATE_USUARIO =
            "update usuarios set user= ?,password = ?, mail = ? where id = ?";


    public boolean addLector(Usuario l) {
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

            rs = stmt.getGeneratedKeys();

            long id = 0;
            if (rs != null && rs.next()) {
                id = rs.getLong(1);
            }

            l.setId((int) id);
            stmt = con.prepareStatement
                    (QUERY_INSERTAR_LECTOR,
                            Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, (int) l.getId());
            stmt.setString(2, l.getNombre());
            stmt.setDate(3,
                    java.sql.Date.valueOf(l.getFechaNacimiento()));
            stmt.executeUpdate();
            añadido = true;
            con.commit();
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


    public List<Usuario> getLectores() {
        List<Usuario> lectores = new ArrayList<>();
        Connection con = null;
        DBConnection db = new DBConnection();
        Statement stmt = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {

            con = db.getConnection();
            stmt = con.createStatement();

            pst = con.prepareStatement(
                    SELECT_LECTORES);
            rs = pst.executeQuery();
            while (rs.next()) {
                Usuario l = new Usuario(rs.getString("user")
                        , rs.getString("password")
                        , rs.getString("mail")
                        , rs.getString("nombre")
                        , rs.getDate("fechaNacimiento").toLocalDate()
                        , Integer.parseInt(rs.getString("id_tipo_usuarios")));

                l.setId((int) rs.getLong("l.id"));
                lectores.add(l);
            }

        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            db.cerrarResultSet(rs);
            db.cerrarStatement(stmt);
            db.cerrarConexion(con);
        }

        return lectores;
    }

    public void actualizarLector(int id, String usuario, String password, String eMail, String nombre, LocalDate fechaNacimiento) {
        DBConnection db = new DBConnection();
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            con = db.getConnection();
            stmt = con.prepareStatement
                    (UPDATE_LECTOR);
            stmt.setString(1, nombre);
            stmt.setDate(2, Date.valueOf(fechaNacimiento));
            stmt.setInt(3, id);
            stmt.executeUpdate();

            stmt = con.prepareStatement(UPDATE_USUARIO);
            stmt.setString(1, usuario);
            stmt.setString(2, password);
            stmt.setString(3, eMail);
            stmt.setInt(4,id);
            stmt.executeUpdate();
        } catch (Exception e) {
            Logger.getLogger("Main").info(e.getMessage());
        } finally {
            db.cerrarConexion(con);
        }
    }


}