package dao;

import dao.modelo.Periodico;
import lombok.extern.log4j.Log4j2;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Log4j2
public class DaoPeriodicos {

    private static final String SELECT_PERIODICOS =
            "select * From periodicos";

    private static final String QUERY_INSERTAR_PERIODICO =
            "insert into periodicos  (nombre,precio,director,id_administrador) " +
                    "values(?,?,?,?) ";

    private static final String QUERY_DELETE_PERIODICO =
            "delete from periodicos where id = ?";

    public static final String UPDATE_PERIODICO =
            "update periodicos set nombre= ?,precio = ?, director = ?, id_administrador =? where id = ?";


    public List<Periodico> getPeriodicos() {
        List<Periodico> periodicos = new ArrayList<>();
        Connection con = null;
        DBConnection db = new DBConnection();
        Statement stmt = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {

            con = db.getConnection();
            stmt = con.createStatement();

            pst = con.prepareStatement(
                    SELECT_PERIODICOS);
            rs = pst.executeQuery();
            while (rs.next()) {
                Periodico periodico = new Periodico(Integer.parseInt(rs.getString("id"))
                        , rs.getString("nombre")
                        , Double.parseDouble(rs.getString("precio"))
                        , rs.getString("director")
                        , Integer.parseInt(rs.getString("id_administrador")));
                periodicos.add(periodico);
            }

        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            db.cerrarResultSet(rs);
            db.cerrarStatement(stmt);
            db.cerrarConexion(con);
        }

        return periodicos;
    }

    public boolean addPeriodico(Periodico periodico) {
        var añadido = false;
        DBConnection db = new DBConnection();
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            con = db.getConnection();
            con.setAutoCommit(false);
            stmt = con.prepareStatement
                    (QUERY_INSERTAR_PERIODICO,
                            Statement.RETURN_GENERATED_KEYS);

            stmt.setString(1, periodico.getNombre());
            stmt.setDouble(2, periodico.getPrecio());
            stmt.setString(3, periodico.getDirector());
            stmt.setInt(4, periodico.getIdAdministrador());


            stmt.executeUpdate();
            añadido = true;
            rs = stmt.getGeneratedKeys();

            long id = 0;
            if (rs != null && rs.next()) {
                id = rs.getLong(1);
            }

            periodico.setId((int) id);

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

    public void borrarPeriodico(Periodico periodico) {
        DBConnection db = new DBConnection();
        Connection con = null;
        PreparedStatement stmt = null;

        try {
            con = db.getConnection();
            con.setAutoCommit(false);
            stmt = con.prepareStatement
                    (QUERY_DELETE_PERIODICO);
            stmt.setInt(1,
                    (int) periodico.getId());
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

    public void actualizarPeriodico(String nombre, double precio, String director, int id_administrador, int id) {
        DBConnection db = new DBConnection();
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            con = db.getConnection();

            stmt = con.prepareStatement(UPDATE_PERIODICO);

            stmt.setString(1, nombre);
            stmt.setDouble(2, precio);
            stmt.setString(3, director);
            stmt.setInt(4, id_administrador);
            stmt.setInt(5,id);
            stmt.executeUpdate();
        } catch (Exception e) {
            Logger.getLogger("Main").info(e.getMessage());
        } finally {
            db.cerrarConexion(con);
        }
    }
}
