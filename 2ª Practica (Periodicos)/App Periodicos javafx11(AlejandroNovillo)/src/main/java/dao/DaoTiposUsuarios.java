package dao;

import dao.modelo.TiposUsuarios;
import lombok.extern.log4j.Log4j2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Log4j2
public class DaoTiposUsuarios {

    private static final String SELECT_TIPOS_USUARIOS =
            "select tipo, descripcion from tipos_usuario";


    public List<TiposUsuarios> getTipoUsuarios() {
        List<TiposUsuarios> tiposUsuarios = new ArrayList<>();
        Connection con = null;
        DBConnection db = new DBConnection();
        Statement stmt = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {

            con = db.getConnection();
            stmt = con.createStatement();

            pst = con.prepareStatement(
                    SELECT_TIPOS_USUARIOS);
            rs = pst.executeQuery();
            while (rs.next()) {
                TiposUsuarios t = new TiposUsuarios(rs.getString("tipo")
                        , rs.getString("descripcion"));

                tiposUsuarios.add(t);
            }

        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            db.cerrarResultSet(rs);
            db.cerrarStatement(stmt);
            db.cerrarConexion(con);
        }

        return tiposUsuarios;
    }

}

