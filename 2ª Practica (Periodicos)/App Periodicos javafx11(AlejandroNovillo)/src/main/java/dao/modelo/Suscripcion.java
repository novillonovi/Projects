package dao.modelo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class Suscripcion {
    private int id;
    private int idLector;
    private int idPeriodico;
    private LocalDate fechaInicio;
    private LocalDate fechaBaja;

    public Suscripcion(int idLector, int idPeriodico, LocalDate fechaInicio, LocalDate fechaBaja) {
        this.idLector = idLector;
        this.idPeriodico = idPeriodico;
        this.fechaInicio = fechaInicio;
        this.fechaBaja = fechaBaja;
    }
    public Suscripcion(int id,int idLector, int idPeriodico, LocalDate fechaInicio) {
        this.id=id;
        this.idLector = idLector;
        this.idPeriodico = idPeriodico;
        this.fechaInicio = fechaInicio;
    }

    @Override
    public String toString() {
        return "Suscripcion: " +
                "Id= " + id +
                " || Id Lector= " + idLector +
                " || Id Periodico= " + idPeriodico +
                " || Fecha de Inicio= " + fechaInicio +
                " || Fecha de Baja= " + fechaBaja;
    }
}

