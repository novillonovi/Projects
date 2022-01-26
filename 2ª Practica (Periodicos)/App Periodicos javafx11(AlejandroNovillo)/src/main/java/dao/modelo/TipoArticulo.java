package dao.modelo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TipoArticulo {
    private int id;
    private String tipo;

    public TipoArticulo(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return "Tipo de Articulo:" +
                "Id= " + id +
                " || Tipo= " + tipo;
    }
}
