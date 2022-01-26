package dao;

import dao.modelo.Periodico;
import dao.modelo.Suscripcion;
import lombok.extern.log4j.Log4j2;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Log4j2
public class DaoSuscripciones {
    private static final String QUERY_INSERTAR_SUSCRIPCION =
            "insert into suscripciones  (id_lector,id_periodico,fecha_inicio) " +
                    "values(?,?,?) ";
    private static final String SELECT_SUSCRIPCIONES =
            "select * From suscripciones";

    private static final String QUERY_DELETE_SUSCRIPCION =
            "delete from suscripciones where id = ?";

    public static final String UPDATE_SUSCRIPCION =
            "update suscripciones set id_lector= ?,id_periodico = ?, fecha_inicio = ? where id = ?";

    public List<Suscripcion> getSuscripciones() {
        List<Suscripcion> suscripciones = new ArrayList<>();
        Connection con = null;
        DBConnection db = new DBConnection();
        Statement stmt = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            con = db.getConnection();
            stmt = con.createStatement();

            pst = con.prepareStatement(
                    SELECT_SUSCRIPCIONES);
            rs = pst.executeQuery();
            while (rs.next()) {

                Suscripcion suscripcion = new Suscripcion(rs.getInt("id")
                        , rs.getInt("id_lector")
                        , rs.getInt("id_periodico")
                        , rs.getDate("fecha_inicio").toLocalDate());
                suscripciones.add(suscripcion);
            }

        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            db.cerrarResultSet(rs);
            db.cerrarStatement(stmt);
            db.cerrarConexion(con);
        }
        return suscripciones;
    }

    public boolean addSuscripcion(Suscripcion suscripcion) {
        var añadido = false;
        DBConnection db = new DBConnection();
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            con = db.getConnection();
            con.setAutoCommit(false);
            stmt = con.prepareStatement
                    (QUERY_INSERTAR_SUSCRIPCION,
                            Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, suscripcion.getIdLector());
            stmt.setInt(2, suscripcion.getIdPeriodico());
            stmt.setDate(3, java.sql.Date.valueOf(suscripcion.getFechaInicio()));
            stmt.executeUpdate();
            añadido = true;
            rs = stmt.getGeneratedKeys();

            long id = 0;
            if (rs != null && rs.next()) {
                id = rs.getLong(1);
            }

            suscripcion.setId((int) id);

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

    public void borrarSuscripcion(Suscripcion suscripcion) {
        DBConnection db = new DBConnection();
        Connection con = null;
        PreparedStatement stmt = null;

        try {
            con = db.getConnection();
            con.setAutoCommit(false);
            stmt = con.prepareStatement
                    (QUERY_DELETE_SUSCRIPCION);
            stmt.setInt(1,
                    (int) suscripcion.getId());
            stmt.executeUpdate();


            stmt = con.prepareStatement
                    (QUERY_DELETE_SUSCRIPCION);
            stmt.setInt(1,
                    (int) suscripcion.getId());

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

    public void actualizarSuscripcion(int id_lector, int id_periodico, LocalDate fecha_inicio, int id) {
        DBConnection db = new DBConnection();
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            con = db.getConnection();

            stmt = con.prepareStatement(UPDATE_SUSCRIPCION);

            stmt.setInt(1, id_lector);
            stmt.setInt(2, id_periodico);
            stmt.setDate(3, Date.valueOf(fecha_inicio));
            stmt.setInt(4, id);
            stmt.executeUpdate();
        } catch (Exception e) {
            Logger.getLogger("Main").info(e.getMessage());
        } finally {
            db.cerrarConexion(con);
        }
    }
}
