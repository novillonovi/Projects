package dao;

import dao.modelo.Articulo;
import lombok.extern.log4j.Log4j2;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


@Log4j2
public class DaoArticulos {
    private static final String QUERY_INSERTAR_ARTICULOS =
            "insert into articulos  (titular,descripcion,id_periodico,id_tipo,autor) " +
                    "values(?,?,?,?,?) ";
    private static final String SELECT_ARTICULOS =
            "select * From articulos";
    private static final String QUERY_DELETE_ARTICULO =
            "delete from articulos where id = ?";

    public static final String UPDATE_ARTICULO =
            "update articulos set titular= ?,descripcion = ?, id_periodico = ?, id_tipo =?, autor=? where id = ?";

    public List<Articulo> getArticulos() {
        List<Articulo> articulos = new ArrayList<>();
        Connection con = null;
        DBConnection db = new DBConnection();
        Statement stmt = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {

            con = db.getConnection();
            stmt = con.createStatement();

            pst = con.prepareStatement(
                    SELECT_ARTICULOS);
            rs = pst.executeQuery();
            while (rs.next()) {
                Articulo articulo = new Articulo(Integer.parseInt(rs.getString("id"))
                        , rs.getString("titular")
                        , rs.getString("descripcion")
                        , Integer.parseInt(rs.getString("id_periodico"))
                        , Integer.parseInt(rs.getString("id_tipo"))
                        , rs.getString("autor"));

                articulos.add(articulo);
            }

        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            db.cerrarResultSet(rs);
            db.cerrarStatement(stmt);
            db.cerrarConexion(con);
        }

        return articulos;
    }

    public boolean addArticulo(Articulo articulo) {
        var añadido = false;
        DBConnection db = new DBConnection();
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            con = db.getConnection();
            con.setAutoCommit(false);
            stmt = con.prepareStatement
                    (QUERY_INSERTAR_ARTICULOS,
                            Statement.RETURN_GENERATED_KEYS);

            stmt.setString(1, articulo.getTitular());
            stmt.setString(2, articulo.getDescripcion());
            stmt.setInt(3, articulo.getIdPeriodico());
            stmt.setInt(4, articulo.getIdTipo());
            stmt.setString(5, String.valueOf(articulo.getAutor()));


            stmt.executeUpdate();
            añadido = true;
            rs = stmt.getGeneratedKeys();

            long id = 0;
            if (rs != null && rs.next()) {
                id = rs.getLong(1);
            }

            articulo.setId((int) id);

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

    public void borrarArticulo(Articulo articulo) {
        DBConnection db = new DBConnection();
        Connection con = null;
        PreparedStatement stmt = null;

        try {
            con = db.getConnection();
            con.setAutoCommit(false);
            stmt = con.prepareStatement
                    (QUERY_DELETE_ARTICULO);
            stmt.setInt(1,
                    (int) articulo.getId());
            stmt.executeUpdate();


            stmt = con.prepareStatement
                    (QUERY_DELETE_ARTICULO);
            stmt.setInt(1,
                    (int) articulo.getId());

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

    public void actualizarArticulo(String titular, String descripcion, int id_periodico, int id_tipo,String autor, int id) {
        DBConnection db = new DBConnection();
        Connection con = null;
        PreparedStatement stmt = null;
        try {
            con = db.getConnection();

            stmt = con.prepareStatement(UPDATE_ARTICULO);

            stmt.setString(1, titular);
            stmt.setString(2, descripcion);
            stmt.setInt(3, id_periodico);
            stmt.setInt(4, id_tipo);
            stmt.setString(5, autor);
            stmt.setInt(6,id);
            stmt.executeUpdate();
        } catch (Exception e) {
            Logger.getLogger("Main").info(e.getMessage());
        } finally {
            db.cerrarConexion(con);
        }
    }

}