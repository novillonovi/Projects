package dao.modelo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class Articulo{
    private int id;
    private String titular;
    private String descripcion;
    private int idPeriodico;
    private int idTipo;
    private String autor;


    public Articulo(int id, String titular, String descripcion, int idPeriodico, int idTipo, String autor) {
        this.id = id;
        this.titular = titular;
        this.descripcion = descripcion;
        this.idPeriodico = idPeriodico;
        this.idTipo = idTipo;
        this.autor = autor;
    }
    public Articulo(String titular, String descripcion, int idPeriodico, int idTipo, String autor) {
        this.titular = titular;
        this.descripcion = descripcion;
        this.idPeriodico = idPeriodico;
        this.idTipo = idTipo;
        this.autor = autor;
    }


    @Override
    public String toString() {
        return "Articulo: " +
                "Id=" + id +
                " || Titular= " + titular +
                " || Descripcion= " + descripcion +
                " || Id Periodico= " + idPeriodico +
                " || Id Tipo= " + idTipo +
                " || Autor= " + autor;
    }


}