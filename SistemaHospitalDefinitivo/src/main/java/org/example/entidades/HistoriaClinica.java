package org.example.entidades;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Setter(AccessLevel.NONE)

@Entity

public class HistoriaClinica implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String numeroHistoria;
    @OneToOne
    @JoinColumn(unique = true, name = "paciente_id", nullable = false)
    private Paciente paciente;
    private LocalDateTime fechaCreacion;
    @ElementCollection
    private List<String> diagnosticos = new ArrayList<>();
    @ElementCollection
    private List<String> tratamientos = new ArrayList<>();
    @ElementCollection
    private List<String> alergias = new ArrayList<>();

    public HistoriaClinica(Paciente paciente) {
        this.paciente = Objects.requireNonNull(paciente, "El paciente no puede ser nulo");
        this.fechaCreacion = LocalDateTime.now();
        this.numeroHistoria = generarNumeroHistoria();
    }

    private String generarNumeroHistoria() {
        return "HC-" + paciente.getDni() + "-" + fechaCreacion.getYear();
    }

    public String getNumeroHistoria() {
        return numeroHistoria;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void agregarDiagnostico(String diagnostico) {
        if (diagnostico != null && !diagnostico.trim().isEmpty()) {
            diagnosticos.add(diagnostico);
        }
    }

    public void agregarTratamiento(String tratamiento) {
        if (tratamiento != null && !tratamiento.trim().isEmpty()) {
            tratamientos.add(tratamiento);
        }
    }

    public void agregarAlergia(String alergia) {
        if (alergia != null && !alergia.trim().isEmpty()) {
            alergias.add(alergia);
        }
    }
    public void setPaciente(Paciente paciente){
        // 1. Desvincular la Historia Clínica antigua (si existe)
        if (this.paciente != null && this.paciente.getHistoriaClinica() == this) {
            this.paciente.setHistoriaClinica(null);
        }

        // 2. Establecer el nuevo Paciente (Establece el Dueño de la FK)
        this.paciente = paciente;

        // 3. Sincronizar el Lado Inverso (Solo si no es un bucle)
        if (paciente != null && paciente.getHistoriaClinica() != this) {
            paciente.setHistoriaClinica(this);
        }
    }

    public List<String> getDiagnosticos() {
        return Collections.unmodifiableList(diagnosticos);
    }

    public List<String> getTratamientos() {
        return Collections.unmodifiableList(tratamientos);
    }

    public List<String> getAlergias() {
        return Collections.unmodifiableList(alergias);
    }

    @Override
    public String toString() {
        return "HistoriaClinica{" +
                "numeroHistoria='" + numeroHistoria + '\'' +
                ", paciente=" + paciente.getNombreCompleto() +
                ", fechaCreacion=" + fechaCreacion +
                '}';
    }
}
