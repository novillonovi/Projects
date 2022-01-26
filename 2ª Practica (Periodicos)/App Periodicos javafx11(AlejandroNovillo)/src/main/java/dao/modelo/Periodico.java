package dao.modelo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Periodico {
    private int id;
    private String nombre;
    private double precio;
    private String director;
    private int idAdministrador;

    public Periodico(String nombre, double precio, String director, int idAdministrador) {
        this.nombre = nombre;
        this.precio = precio;
        this.director = director;
        this.idAdministrador = idAdministrador;
    }

    @Override
    public String toString() {
        return "Periodico: " +
                "Id= " + id +
                " || Nombre= " + nombre  +
                " || Precio= " + precio +
                " || Director= " + director +
                " || Id del Admin= " + idAdministrador;
    }
}
