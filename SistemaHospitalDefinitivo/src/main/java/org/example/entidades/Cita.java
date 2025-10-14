package org.example.entidades;

import jakarta.persistence.*;
import lombok.*;
import org.example.servicio.CitaException;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)

@Entity

public class Cita implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "paciente_id")
    private Paciente paciente;

    @NonNull
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "medico_id")
    private Medico medico;

    @NonNull
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "sala_id")
    private Sala sala;

    @NonNull
    private LocalDateTime fechaHora;

    @NonNull
    private BigDecimal costo;

    private EstadoCita estado = EstadoCita.PROGRAMADA;
    private String observaciones = "";
    @Builder
    public Cita(Long id, Paciente paciente, Medico medico, Sala sala, LocalDateTime fechaHora, BigDecimal costo, EstadoCita estado, String observaciones) {
        this.id = id;
        this.paciente = paciente;
        this.medico = medico;
        this.sala = sala;
        this.fechaHora = fechaHora;
        this.costo = costo;
        this.estado = estado;
        this.observaciones = observaciones;
    }

    public String toCsvString() {
        return String.format("%s,%s,%s,%s,%s,%s,%s",
                paciente.getDni(),
                medico.getDni(),
                sala.getNumero(),
                fechaHora.toString(),
                costo.toString(),
                estado.name(),
                observaciones.replaceAll(",", ";"));
    }

    public static Cita fromCsvString(String csvString,
                                     Map<String, Paciente> pacientes,
                                     Map<String, Medico> medicos,
                                     Map<String, Sala> salas) throws CitaException {
        String[] values = csvString.split(",");
        if (values.length != 7) {
            throw new CitaException("Formato de CSV inválido para Cita: " + csvString);
        }

        String dniPaciente = values[0];
        String dniMedico = values[1];
        String numeroSala = values[2];
        LocalDateTime fechaHora = LocalDateTime.parse(values[3]);
        BigDecimal costo = new BigDecimal(values[4]);
        EstadoCita estado = EstadoCita.valueOf(values[5]);
        String observaciones = values[6].replaceAll(";", ",");

        Paciente paciente = pacientes.get(dniPaciente);
        Medico medico = medicos.get(dniMedico);
        Sala sala = salas.get(numeroSala);

        if (paciente == null) {
            throw new CitaException("Paciente no encontrado: " + dniPaciente);
        }
        if (medico == null) {
            throw new CitaException("Médico no encontrado: " + dniMedico);
        }
        if (sala == null) {
            throw new CitaException("Sala no encontrada: " + numeroSala);
        }

        Cita cita = new Cita(paciente, medico, sala, fechaHora, costo);
        cita.setEstado(estado);
        cita.setObservaciones(observaciones);

        return cita;
    }
}
