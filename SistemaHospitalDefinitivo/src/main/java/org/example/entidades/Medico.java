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

@SuperBuilder
@Getter
@ToString(callSuper = true, exclude = {"departamento", "citas"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity


public class Medico extends Persona implements Serializable {

    @Embedded
    private  Matricula matricula;
    private  EspecialidadMedica especialidad;
    @ManyToOne
    @JoinColumn(name = "departamento_id")
    private Departamento departamento;
    @OneToMany(mappedBy = "medico")
    private List<Cita> citas;


    protected Medico(MedicoBuilder<?, ?> builder) {
        super(builder);
        this.citas = new ArrayList<>();
        this.matricula = builder.matricula;
        this.especialidad = Objects.requireNonNull(builder.especialidad, "La especialidad no puede ser nula");
        this.departamento = builder.departamento;
    }

    public void setDepartamento(Departamento departamento) {
        if (this.departamento != departamento) {
            this.departamento = departamento;
        }
    }

    public void addCita(Cita cita) {
        this.citas.add(cita);
    }

    public List<Cita> getCitas() {
        return Collections.unmodifiableList(new ArrayList<>(citas));
    }
}
