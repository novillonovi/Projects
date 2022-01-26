package dao.modelo;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class TiposUsuarios {
    private long id;
    private final String tipo;
    private final String descripcion;

    public TiposUsuarios(String tipo, String descripcion) {
        this.tipo = tipo;
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return tipo;
    }

}
