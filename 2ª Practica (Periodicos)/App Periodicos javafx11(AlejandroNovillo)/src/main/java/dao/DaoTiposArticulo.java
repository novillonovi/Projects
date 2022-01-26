package dao;

import dao.modelo.Periodico;
import dao.modelo.TipoArticulo;
import lombok.extern.log4j.Log4j2;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Log4j2
public class DaoTiposArticulo {
    private static final String SELECT_TIPOSARTICULO =
            "select * From tipos_articulo";

    private static final String QUERY_INSERTAR_TIPOSARTICULO =
            "insert into tipos_articulo  (tipo) " +
                    "values(?) ";

    private static final String QUERY_DELETE_TIPOSARTICULO =
            "delete from tipos_articulo where id = ?";

    public static final String UPDATE_DELETE_TIPOSARTICULO =
            "update tipos_articulo set tipo= ? where id = ?";

    public List<TipoArticulo> getTiposArticulo() {
        List<TipoArticulo> tipoArticulos = new ArrayList<>();
        Connection con = null;
        DBConnection db = new DBConnection();
        Statement stmt = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            con = db.getConnection();
            stmt = con.createStatement();

            pst = con.prepareStatement(
                    SELECT_TIPOSARTICULO);
            rs = pst.executeQuery();
            while (rs.next()) {
                TipoArticulo tipoArticulo = new TipoArticulo(Integer.parseInt(rs.getString("id"))
                        , rs.getString("tipo"));
                tipoArticulos.add(tipoArticulo);
            }

        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            db.cerrarResultSet(rs);
            db.cerrarStatement(stmt);
            db.cerrarConexion(con);
        }

        return tipoArticulos;
    }

    public boolean addTipoArticulo(TipoArticulo tipoArticulo) {
        var añadido = false;
        DBConnection db = new DBConnection();
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            con = db.getConnection();
            con.setAutoCommit(false);
            stmt = con.prepareStatement
                    (QUERY_INSERTAR_TIPOSARTICULO,
                            Statement.RETURN_GENERATED_KEYS);

            stmt.setString(1, tipoArticulo.getTipo());
            stmt.executeUpdate();
            añadido = true;
            rs = stmt.getGeneratedKeys();

            long id = 0;
            if (rs != null && rs.next()) {
                id = rs.getLong(1);
            }

            tipoArticulo.setId((int) id);

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

    public void borrarTipoArticulo(TipoArticulo tipoArticulo) {
        DBConnection db = new DBConnection();
        Connection con = null;
        PreparedStatement stmt = null;

        try {
            con = db.getConnection();
            con.setAutoCommit(false);
            stmt = con.prepareStatement
                    (QUERY_DELETE_TIPOSARTICULO);
            stmt.setInt(1,
                    (int) tipoArticulo.getId());
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

    public void actualizarTipoArticulo(String tipoTipoArticulo,int id) {
        DBConnection db = new DBConnection();
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            con = db.getConnection();

            stmt = con.prepareStatement(UPDATE_DELETE_TIPOSARTICULO);

            stmt.setString(1, tipoTipoArticulo);
            stmt.setInt(2,id);
            stmt.executeUpdate();
        } catch (Exception e) {
            Logger.getLogger("Main").info(e.getMessage());
        } finally {
            db.cerrarConexion(con);
        }
    }
}
