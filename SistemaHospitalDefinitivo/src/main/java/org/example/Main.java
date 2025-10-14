package org.example;

import jakarta.persistence.*;
import org.example.entidades.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("=====SISTEMA DE GESTION HOSPITALARIA CON JPA. FERRERO =====\n");
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hospital-persistence-unit");
        EntityManager em = emf.createEntityManager();

        try {


            // 1. Inicializar y persistir hospital, ademas de tres citas
            inicializarYPersistirDatos(em);

            // 2. Consultas JPQL en el hospital
            consultasJPQL(em);

            // 3. Actualizar cita
            actualizarCita(em);

            // 4. Contar medicos, citas, pacientes y salas
            hospitalEstadisticas(em);

            System.out.println("===SISTEMA INICIADO CORRECTAMENTE===\n");
        } catch (Exception e) {
            System.err.println("Error del sistema: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
            emf.close();
        }
    }


    public static void inicializarYPersistirDatos(EntityManager em) {
        System.out.println("Inicializando y persistiendo datos del hospital...\n");

        em.getTransaction().begin();

        try {
            // Acá creo mi hospital
            Hospital hospital = Hospital.builder()
                    .nombre("Hospital Español")
                    .direccion("Av San Martin 965")
                    .telefono("2613213298")
                    .build();

            //Creo mis departamentos

            Departamento cardiologia = Departamento.builder()
                    .nombre("Cardiologia")
                    .especialidad(EspecialidadMedica.CARDIOLOGIA)
                    .build();

            Departamento pediatria = Departamento.builder()
                    .nombre("Pediatria")
                    .especialidad(EspecialidadMedica.PEDIATRIA)
                    .build();

            Departamento traumatologia = Departamento.builder()
                    .nombre("Traumatologia")
                    .especialidad(EspecialidadMedica.GINECOLOGIA)
                    .build();

            //Asigno los departamentos al hospital creado

            hospital.agregarDepartamento(cardiologia);
            hospital.agregarDepartamento(pediatria);
            hospital.agregarDepartamento(traumatologia);

            //Creo salas para los deptos

            Sala salaCardiologia = cardiologia.crearSala("CARD-101", "Quirofano");
            Sala salaPediatria = pediatria.crearSala("PEDI-101", "Pediatria");
            Sala salaTraumatologia = traumatologia.crearSala("TRAU-101", "Traumatologia");

            //Creo medicos

            Medico medico1 = Medico.builder()
                    .nombre("Alfonso")
                    .apellido("Perez")
                    .dni(String.valueOf(24438749))
                    .fechaNacimiento(LocalDate.of(1975, 8, 25))
                    .tipoSangre(TipoSangre.O_NEGATIVO)
                    .matricula(new Matricula("MP-12345"))
                    .especialidad(EspecialidadMedica.CARDIOLOGIA)
                    .build();

            Medico medico2 = Medico.builder()
                    .nombre("Patricia")
                    .apellido("Nuñez")
                    .dni(String.valueOf(27435742))
                    .fechaNacimiento(LocalDate.of(1985, 9, 22))
                    .tipoSangre(TipoSangre.A_NEGATIVO)
                    .matricula(new Matricula("MP-12346"))
                    .especialidad(EspecialidadMedica.PEDIATRIA)
                    .build();

            Medico medico3 = Medico.builder()
                    .nombre("Paula")
                    .apellido("Gomez")
                    .dni("29432749")
                    .fechaNacimiento(LocalDate.of(1989, 12, 13))
                    .tipoSangre(TipoSangre.AB_POSITIVO)
                    .matricula(new Matricula("MP-12347"))
                    .especialidad(EspecialidadMedica.TRAUMATOLOGIA)
                    .build();

            //Asigno medicos a los departamentos
            cardiologia.agregarMedico(medico1);
            pediatria.agregarMedico(medico2);
            traumatologia.agregarMedico(medico3);


            //Creo pacientes

            Paciente paciente1 = Paciente.builder()
                    .nombre("Lucca")
                    .apellido("Ferrero")
                    .dni("46161090")
                    .fechaNacimiento(LocalDate.of(2004, 11, 11))
                    .tipoSangre(TipoSangre.O_NEGATIVO)
                    .telefono("2612427902")
                    .direccion("Av San Martin 302")
                    .build();

            Paciente paciente2 = Paciente.builder()
                    .nombre("Martin")
                    .apellido("Ferreira")
                    .dni("49161090")
                    .fechaNacimiento(LocalDate.of(2015, 6, 9))
                    .tipoSangre(TipoSangre.AB_POSITIVO)
                    .telefono("2612437002")
                    .direccion("Mitre 809")
                    .build();

            Paciente paciente3 = Paciente.builder()
                    .nombre("Maria")
                    .apellido("Palacios")
                    .dni("40161090")
                    .fechaNacimiento(LocalDate.of(1995, 12, 3))
                    .tipoSangre(TipoSangre.AB_NEGATIVO)
                    .telefono("2612622102")
                    .direccion("Rio Diamante 402")
                    .build();

            //Asigno mis pacientes al hospital

            hospital.agregarPaciente(paciente1);
            hospital.agregarPaciente(paciente2);
            hospital.agregarPaciente(paciente3);

            //Uso historias clinicas autogeneradas

            HistoriaClinica historiaPaciente1 = paciente1.getHistoriaClinica();
            historiaPaciente1.agregarDiagnostico("Soplidos en el pecho");
            historiaPaciente1.agregarTratamiento("Controles regulares");
            historiaPaciente1.agregarAlergia("Sin alergias encontradas");

            HistoriaClinica historiaPaciente2 = paciente2.getHistoriaClinica();
            historiaPaciente2.agregarDiagnostico("Tos al correr por tiempos prolongados, pobre respiracion");
            historiaPaciente2.agregarTratamiento("PAF");
            historiaPaciente2.agregarAlergia("Sin alergias encontradas");

            HistoriaClinica historiaPaciente3 = paciente3.getHistoriaClinica();
            historiaPaciente3.agregarDiagnostico("Lesion de hueso");
            historiaPaciente3.agregarTratamiento("Cirugia programada");
            historiaPaciente3.agregarAlergia("Sin alergias encontradas");

            //Persisto hospital
            em.persist(hospital);

            //Creo mis 3 citas con sus observaciones y estados
            Cita cita1 = Cita.builder()
                    .paciente(paciente1)
                    .medico(medico1)
                    .sala(salaCardiologia)
                    .fechaHora(LocalDateTime.now().plusDays(1).withHour(9).withMinute(0))
                    .costo(new BigDecimal("12000"))
                    .estado(EstadoCita.PROGRAMADA)
                    .observaciones("Control general")
                    .build();

            Cita cita2 = Cita.builder()
                    .paciente(paciente2)
                    .medico(medico2)
                    .sala(salaPediatria)
                    .fechaHora(LocalDateTime.now().plusDays(3).withHour(9).withMinute(30))
                    .costo(new BigDecimal("9000"))
                    .estado(EstadoCita.CANCELADA)
                    .observaciones("Control general")
                    .build();

            Cita cita3 = Cita.builder()
                    .paciente(paciente3)
                    .medico(medico3)
                    .sala(salaTraumatologia)
                    .fechaHora(LocalDateTime.now().plusDays(7).withHour(2).withMinute(15))
                    .costo(new BigDecimal("15000"))
                    .estado(EstadoCita.PROGRAMADA)
                    .observaciones("Control general")
                    .build();

            // Persisto citas
            em.persist(cita1);
            em.persist(cita2);
            em.persist(cita3);

            em.getTransaction().commit();

            System.out.println("Datos del hospital y citas persistidos correctamente.\n");

        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        }
    }

    public static void consultasJPQL(EntityManager em) {
        System.out.println("Iniciando consultas JPQL...\n");

        try {
            // Recupero todos los hospitales
            TypedQuery<Hospital> qHospitales = em.createQuery("SELECT h FROM Hospital h", Hospital.class);
            List<Hospital> hospitales = qHospitales.getResultList();
            System.out.println("\n HOSPITALES:");
            hospitales.forEach(h -> System.out.println("- " + h.getNombre() + " | " + h.getDireccion()));

            // Recuperar medicos filtrados por especialidad
            TypedQuery<Medico> cardioMedicos = em.createQuery(
                    "SELECT m FROM Medico m WHERE m.especialidad = :esp", Medico.class);
            cardioMedicos.setParameter("esp", EspecialidadMedica.CARDIOLOGIA);
            List<Medico> medicosCardio = cardioMedicos.getResultList();
            System.out.println("\n MEDICOS (Cardiología):");
            medicosCardio.forEach(m ->
                    System.out.println("- " + m.getNombre() + " " + m.getApellido() + " | " + m.getEspecialidad()));

            TypedQuery<Medico> pediaMedicos = em.createQuery(
                    "SELECT m FROM Medico m WHERE m.especialidad = :esp", Medico.class);
            pediaMedicos.setParameter("esp", EspecialidadMedica.PEDIATRIA);
            List<Medico> medicosPedia = pediaMedicos.getResultList();
            System.out.println("\n MEDICOS (Pediatria):");
            medicosCardio.forEach(m ->
                    System.out.println("- " + m.getNombre() + " " + m.getApellido() + " | " + m.getEspecialidad()));

            TypedQuery<Medico> traumaMedicos = em.createQuery(
                    "SELECT m FROM Medico m WHERE m.especialidad = :esp", Medico.class);
            traumaMedicos.setParameter("esp", EspecialidadMedica.TRAUMATOLOGIA);
            List<Medico> medicoTrauma = traumaMedicos.getResultList();
            System.out.println("\n MEDICOS (Cardiología):");
            medicosCardio.forEach(m ->
                    System.out.println("- " + m.getNombre() + " " + m.getApellido() + " | " + m.getEspecialidad()));

            // Recupero todos los pacientes
            TypedQuery<Paciente> hospitalPacientes = em.createQuery("SELECT p FROM Paciente p", Paciente.class);
            List<Paciente> pacientes = hospitalPacientes.getResultList();
            System.out.println("\n PACIENTES:");
            pacientes.forEach(p -> System.out.println("- " + p.getNombre() + " " + p.getApellido()));

            // Recupero las citas
            TypedQuery<Cita> hospitalCitas = em.createQuery(
                    "SELECT c FROM Cita c ORDER BY c.fechaHora", Cita.class);
            List<Cita> citas = hospitalCitas.getResultList();
            System.out.println("\n CITAS:");
            citas.forEach(c ->
                    System.out.println("- " + c.getFechaHora() +
                            " | Paciente: " + c.getPaciente().getNombre() + " " + c.getPaciente().getApellido() +
                            " | Medico: " + c.getMedico().getNombre() + " " + c.getMedico().getApellido() +
                            " | Estado: " + c.getEstado()));

            System.out.println("\n=== FIN CONSULTAS JPQL ===\n");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void actualizarCita(EntityManager em){
        System.out.println("Inciando actualizacion de cita...\n");

        //Recupero mis citas
        try {
            TypedQuery<Cita> hospitalCitas = em.createQuery(
                    "SELECT c FROM Cita c WHERE c.estado = :estado ORDER BY c.fechaHora", Cita.class);
            hospitalCitas.setParameter("estado", EstadoCita.PROGRAMADA);
            List<Cita> citas = hospitalCitas.getResultList();

            Cita cita = citas.get(0);
            System.out.println("Actualizando cita ID: " + cita.getId() +
                    " | Paciente: " + cita.getPaciente().getNombre() +
                    " | Médico: " + cita.getMedico().getNombre() +
                    " | Estado: " + cita.getEstado());

            em.getTransaction().begin();
            cita.setEstado(EstadoCita.CANCELADA);
            em.merge(cita);
            em.getTransaction().commit();

            System.out.println("Cita actualizada con ID: " + cita.getId() +
                    " | Paciente: " + cita.getPaciente().getNombre() +
                    " | Médico: " + cita.getMedico().getNombre() +
                    " | Estado: " + cita.getEstado());

            System.out.println("Cita actualizada de forma correcta");
        }catch (Exception e) {
            e.printStackTrace();
        }

    }
    
    public static void hospitalEstadisticas(EntityManager em) {
        System.out.println("\n ====ESTADISTICAS HOSPITAL====");

        // Medicos por especialidad
        List<Object[]> medicosPorEspecialidad = em.createQuery(
                "SELECT m.especialidad, COUNT(m) FROM Medico m GROUP BY m.especialidad",
                Object[].class
        ).getResultList();
        medicosPorEspecialidad.forEach(f ->
                System.out.println("Especialidad: " + f[0] + ", cantidad de Medicos: " + f[1])
        );

        // Citas por estado
        List<Object[]> citasPorEstado = em.createQuery(
                "SELECT c.estado, COUNT(c) FROM Cita c GROUP BY c.estado",
                Object[].class
        ).getResultList();
        citasPorEstado.forEach(f ->
                System.out.println("Estado: " + f[0] + ", cantidad de Citas: " + f[1])
        );

        // Total pacientes
        Long pacientes = em.createQuery("SELECT COUNT(p) FROM Paciente p", Long.class).getSingleResult();
        System.out.println("Total de pacientes: " + pacientes);

        // Total salas
        Long salas = em.createQuery("SELECT COUNT(s) FROM Sala s", Long.class).getSingleResult();
        System.out.println("Total de salas: " + salas);

        System.out.println("Estadisticas del hospital mostradas correctamente\n");
    }
}

