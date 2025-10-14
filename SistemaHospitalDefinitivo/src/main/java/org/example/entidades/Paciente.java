package org.example.entidades;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Getter
@ToString(callSuper = true, exclude = {"hospital", "citas", "historiaClinica"})
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)


@Entity


public class Paciente extends Persona implements Serializable {

    @Setter(AccessLevel.NONE)
    private String telefono;
    @Setter(AccessLevel.NONE)
    private String direccion;

    @Setter(AccessLevel.NONE)
    @OneToOne(mappedBy = "paciente", cascade = CascadeType.ALL, orphanRemoval = true)
    private HistoriaClinica historiaClinica;
    @ManyToOne
    @JoinColumn(name = "hospital_id")
    private Hospital hospital;

    @OneToMany( mappedBy = "paciente")
    private List<Cita> citas;

    protected Paciente(PacienteBuilder<?, ?> builder) {
        super(builder);
        this.citas =new ArrayList<>();
        this.telefono = validarString(builder.telefono, "El teléfono no puede ser nulo ni vacío");
        this.direccion = validarString(builder.direccion, "La dirección no puede ser nula ni vacía");
        this.historiaClinica = new HistoriaClinica(this);

    }

    public void setHospital(Hospital hospital) {
        if (this.hospital != hospital) {
            if (this.hospital != null) {
                this.hospital.getInternalPacientes().remove(this);
            }
            this.hospital = hospital;
            if (hospital != null) {
                hospital.getInternalPacientes().add(this);
            }
        }
    }

    public void addCita(Cita cita) {
        this.citas.add(cita);
    }

    public List<Cita> getCitas() {
        return Collections.unmodifiableList(new ArrayList<>(citas));
    }

    private String validarString(String valor, String mensajeError) {
        Objects.requireNonNull(valor, mensajeError);
        if (valor.trim().isEmpty()) {
            throw new IllegalArgumentException(mensajeError);
        }
        return valor;
    }

    public void setHistoriaClinica(HistoriaClinica historiaClinica) {
        this.historiaClinica = historiaClinica;

        // 1. Sincronizar el Lado Dueño (solo si no es nulo)
        if (historiaClinica != null && historiaClinica.getPaciente() != this) {
            // 2. Llama al setter de HistoriaClinica para establecer la FK
            historiaClinica.setPaciente(this);
        }
    }


}

