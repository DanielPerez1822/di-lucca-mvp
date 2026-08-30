package com.odontosys.odontosys_api.infrastructure.config;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.odontosys.odontosys_api.domain.model.Procedure;
import com.odontosys.odontosys_api.domain.model.Role;
import com.odontosys.odontosys_api.domain.model.User;
import com.odontosys.odontosys_api.domain.port.out.PasswordEncoderPort;
import com.odontosys.odontosys_api.domain.port.out.ProcedureRepositoryPort;
import com.odontosys.odontosys_api.domain.port.out.UserRepositoryPort;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = Logger.getLogger(DataInitializer.class.getName());

    private final UserRepositoryPort userRepository;
    private final PasswordEncoderPort passwordEncoder;
    private final ProcedureRepositoryPort procedureRepository;

    public DataInitializer(UserRepositoryPort userRepository,
                           PasswordEncoderPort passwordEncoder,
                           ProcedureRepositoryPort procedureRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.procedureRepository = procedureRepository;
    }

    @Override
    public void run(String... args) {
        seedAdminUser();
        seedDentistUser();
        seedDefaultProcedures();
    }

    private void seedDentistUser() {
        String dentistEmail = "dentista@dilucca.com";
        userRepository.findByEmail(dentistEmail).ifPresentOrElse(
                user -> {
                    user.updatePassword(passwordEncoder.encode("Dentist@12345"));
                    userRepository.save(user);
                    log.info(">>> Contraseña de DENTIST actualizada exitosamente: " + dentistEmail);
                },
                () -> {
                    User dentist = User.create(
                            dentistEmail,
                            passwordEncoder.encode("Dentist@12345"),
                            "Dr. Carlos",
                            "Mendoza",
                            "+57 3100000000",
                            "1111111111",
                            Set.of(Role.DENTIST)
                    );
                    userRepository.save(dentist);
                    log.info(">>> Usuario DENTIST por defecto creado exitosamente: " + dentistEmail);
                }
        );
    }

    private void seedAdminUser() {
        String adminEmail = "admin@dilucca.com";
        userRepository.findByEmail(adminEmail).ifPresentOrElse(
                user -> {
                    user.updatePassword(passwordEncoder.encode("Admin@12345"));
                    userRepository.save(user);
                    log.info(">>> Contraseña de ADMIN actualizada exitosamente: " + adminEmail);
                },
                () -> {
                    User admin = User.create(
                            adminEmail,
                            passwordEncoder.encode("Admin@12345"),
                            "Administrador",
                            "DI-LUCCA",
                            "+57 3000000000",
                            "0000000000",
                            Set.of(Role.ADMIN)
                    );
                    userRepository.save(admin);
                    log.info(">>> Usuario ADMIN por defecto creado exitosamente: " + adminEmail);
                }
        );

        String secretaryEmail = "recepcion@dilucca.com";
        userRepository.findByEmail(secretaryEmail).ifPresentOrElse(
                user -> {
                    user.updatePassword(passwordEncoder.encode("Admin@12345"));
                    userRepository.save(user);
                    log.info(">>> Contraseña de SECRETARY actualizada exitosamente: " + secretaryEmail);
                },
                () -> {
                    User secretary = User.create(
                            secretaryEmail,
                            passwordEncoder.encode("Admin@12345"),
                            "Laura",
                            "Gómez (Recepcionista)",
                            "+57 3200000000",
                            "1000000003",
                            Set.of(Role.SECRETARY_ASSISTANT)
                    );
                    userRepository.save(secretary);
                    log.info(">>> Usuario SECRETARY_ASSISTANT por defecto creado exitosamente: " + secretaryEmail);
                }
        );
    }

    private void seedDefaultProcedures() {
        if (procedureRepository.findAll().isEmpty()) {
            List<Procedure> initialProcedures = List.of(
                    Procedure.create("Consulta general", "Consulta odontológica de valoración general", new BigDecimal("50000.00"), 30),
                    Procedure.create("Limpieza dental profiláctica", "Limpieza dental con ultrasonido y pulido", new BigDecimal("80000.00"), 30),
                    Procedure.create("Aplicación de flúor", "Aplicación tópica de flúor para prevención de caries", new BigDecimal("30000.00"), 15),
                    Procedure.create("Obturación con resina", "Calza dental con resina compuesta fotopolimerizable", new BigDecimal("120000.00"), 45),
                    Procedure.create("Extracción simple", "Extracción dental simple sin complicaciones", new BigDecimal("100000.00"), 30),
                    Procedure.create("Extracción quirúrgica", "Extracción dental quirúrgica (cordales, raíces)", new BigDecimal("250000.00"), 60),
                    Procedure.create("Endodoncia (conducto)", "Tratamiento de conducto radicular", new BigDecimal("350000.00"), 90),
                    Procedure.create("Blanqueamiento dental", "Blanqueamiento dental con luz LED y peróxido", new BigDecimal("350000.00"), 60),
                    Procedure.create("Radiografía periapical", "Radiografía dental individual", new BigDecimal("25000.00"), 10)
            );

            initialProcedures.forEach(procedureRepository::save);
            log.info(">>> Catálogo inicial de " + initialProcedures.size() + " procedimientos dentales creado exitosamente.");
        } else {
            log.info(">>> El catálogo de procedimientos ya contiene datos.");
        }
    }
}
