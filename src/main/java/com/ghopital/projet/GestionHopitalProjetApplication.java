package com.ghopital.projet;

import com.ghopital.projet.dto.ROLES;
import com.ghopital.projet.entity.Role;
import com.ghopital.projet.entity.HospitalService;
import com.ghopital.projet.entity.User;
import com.ghopital.projet.exception.ResourceNotFoundException;
import com.ghopital.projet.repository.RoleRepository;
import com.ghopital.projet.repository.ServiceRepository;
import com.ghopital.projet.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@EnableAspectJAutoProxy
public class GestionHopitalProjetApplication implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(GestionHopitalProjetApplication.class, args);
    }

    @Autowired
    RoleRepository roleRepository;
    @Autowired
    ServiceRepository serviceRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        addRoles();
        addUsers();
        addServices();
    }

    private void addRoles() {
        // add roles to db
        if (roleRepository.findAll().isEmpty()) {
            String[] roleNames = {
                    ROLES.ROLE_ADMIN.name(),
                    ROLES.ROLE_MANAGEMENT_EMPLOYEES.name(),
                    ROLES.ROLE_MANAGEMENT_MATERIALS.name(),
                    ROLES.ROLE_MANAGEMENT_PHARMACY.name(),
                    ROLES.ROLE_MANAGEMENT_PRODUCT.name()
            };
            for (String roleName : roleNames) {
                Role role = new Role();
                role.setName(roleName);
                roleRepository.save(role);
            }
        }
    }

    private void addUsers() {
        // add user admin
        if(userRepository.findAll().size() == 0) {
            User admin = new User();
            admin.setFirstName("HICHAM");
            admin.setLastName("BOUTTAJ");
            admin.setCin("JC624701");
            admin.setEmail("bouttajhicham@gmail.com");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setRole(roleRepository.findById(1L).orElseThrow(
                    () -> new ResourceNotFoundException("Role", "id", "1")
            ));
            userRepository.save(admin);
        }
    }

    private void addServices() {
        // add services to db
        if(serviceRepository.findAll().isEmpty()) {
            String[] serviceNames = {
                    "OPHTHALMOLOGIE", "RADIOLOGIE", "CH.PLAS", "CHIRURGIE",
                    "MAXILLO-FACIALE", "LABORATOIRE", "UREGENCES", "ANES/REA",
                    "TRAUMATO", "NEURO/CHIR", "ORL", "NEUROLOGIE", "GYNECOLOGIE",
                    "UROLOGIE", "NEPHROLOGIE", "CARDIO/RYTHMO", "PNEUMO", "RHUMATOLOGIE",
                    "ANAPATH", "MED ESTHETIQUE", "GASTROLOGIE", "CARDIO/CATHE",
                    "DERMATOLOGIE", "CONS/G", "MED/INTERNE", "DENTAIRE", "ENDOCRINO",
                    "D.M.O (RHUMATO)", "PSY", "REEDUCATION", "ONCOLOGIE"};

            for (String serviceName : serviceNames) {
                HospitalService hospitalService = new HospitalService();
                hospitalService.setName(serviceName);
                serviceRepository.save(hospitalService);
            }
        }
    }
}
