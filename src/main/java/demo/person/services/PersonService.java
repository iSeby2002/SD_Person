package demo.person.services;

import demo.person.dtos.*;
import demo.person.entities.Person;
import demo.person.repositories.PersonRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.List;
import java.util.UUID;

@Service
public class PersonService {
    private final PersonRepository personRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    @Value("${DEVICE_SERVICE_URL}")
    private String deviceServiceUrl;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    public PersonService(PersonRepository personRepository, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder) {
        this.personRepository = personRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    public PersonDTO registerPerson(RegisterDTO registerDTO){
        PersonDTO personDTO = new PersonDTO();
        Person existPerson = personRepository.findPersonByUsername(registerDTO.getUsername());
        if(existPerson != null){
            personDTO.setPerson(null);
            personDTO.setMsg("Already exists an account with this username!");
        }else if(!registerDTO.getPassword().equals(registerDTO.getConfirmPassword())){
            personDTO.setPerson(null);
            personDTO.setMsg("Confirm password doesn't match with password!");
        }else{
            Person savedPerson = new Person(registerDTO.getName(), "client", registerDTO.getUsername(), passwordEncoder.encode(registerDTO.getPassword()));
            personRepository.save(savedPerson);
            personDTO.setPerson(savedPerson);
            personDTO.setMsg("User registered with success!");
        }
        return personDTO;
    }

    public PersonDTO login(LoginDTO loginDTO){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDTO.getUsername(),
                        loginDTO.getPassword()
                )
        );

        PersonDTO personDTO = new PersonDTO();
        Person existPerson = personRepository.findPersonByUsername(loginDTO.getUsername());
        if(existPerson == null){
            personDTO.setPerson(null);
            personDTO.setMsg("Does not exists an account with this username!");
        }else {
            if(loginDTO.getPassword().equals(existPerson.getPassword())){
                personDTO.setPerson(null);
                personDTO.setMsg("Incorrect password!");
            }else{
                personDTO.setPerson(existPerson);
                personDTO.setMsg("User logged in with success!");
            }
        }
        return personDTO;
    }

    public List<Person> findPersons(){
        return personRepository.findAll();
    }

    public String createPerson(CreatePersonDTO createPersonDTO){
        String msg;
        Person existPerson = personRepository.findPersonByUsername(createPersonDTO.getUsername());
        if(existPerson != null){
            msg = "Already exists an account with this username!";
        }else if(!createPersonDTO.getPassword().equals(createPersonDTO.getConfirmPassword())){
            msg = "Confirm password doesn't match with password!";
        }else{
            Person savedPerson = new Person(createPersonDTO.getName(), createPersonDTO.getRole(), createPersonDTO.getUsername(), passwordEncoder.encode(createPersonDTO.getPassword()));
            personRepository.save(savedPerson);
            msg = "User created with success!";
        }
        return msg;
    }

    public String updatePerson(UpdatePersonDTO updatePersonDTO){
        String msg;
        Person existPerson = personRepository.findPersonById(updatePersonDTO.getId());
        if(existPerson == null){
            msg = "Does not exists an account with this id!";
        }else if(!updatePersonDTO.getUsername().equals(existPerson.getUsername())){
            Person antherPerson = personRepository.findPersonByUsername(updatePersonDTO.getUsername());
            if(antherPerson != null){
                msg = "Already exists an account with this username!";
            }else{
                existPerson.setName(updatePersonDTO.getName());
                existPerson.setRole(updatePersonDTO.getRole());
                existPerson.setUsername(updatePersonDTO.getUsername());
                personRepository.save(existPerson);
                msg = "User updated with success!";
            }
        }else{
            existPerson.setName(updatePersonDTO.getName());
            existPerson.setRole(updatePersonDTO.getRole());
            personRepository.save(existPerson);
            msg = "User updated with success!";
        }
        return msg;
    }

    public String changePassword(ChangePasswordDTO changePasswordDTO){
        String msg;
        Person existPerson = personRepository.findPersonById(changePasswordDTO.getPersonId());
        if(existPerson == null){
            msg = "Does not exists an account with this id!";
        }else if(!changePasswordDTO.getNewPassword().equals(changePasswordDTO.getConfirmNewPassword())){
            msg = "Confirm password doesn't match with password!";
        }else{
            existPerson.setPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));
            personRepository.save(existPerson);
            msg = "Password updated with success!";
        }
        return msg;
    }

    public String deletePerson(UUID personId, HttpServletRequest request) {
        try {
            // Extrage token-ul din header-ul Authorization al cererii HTTP
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return "Missing or invalid Authorization header!";
            }
            String token = authHeader.substring(7); // Scoate "Bearer " din header

            Person existPerson = personRepository.findPersonById(personId);
            if(existPerson == null){
                return "User not found!";
            }else {
                String url = deviceServiceUrl + "/device/deleteAllDevices/{personId}";
                HttpHeaders headers = new HttpHeaders();
                headers.set("Authorization", "Bearer " + token); // Adaugă token-ul în header
                HttpEntity<Void> entity = new HttpEntity<>(headers);

                // Use exchange to handle response and errors
                ResponseEntity<String> response = restTemplate.exchange(
                        url,
                        HttpMethod.DELETE,
                        entity,
                        String.class,
                        personId
                );
                // Check if the device deletion was successful based on HTTP status
                if (response.getStatusCode() == HttpStatus.OK && response.getBody().equals("Devices deleted successfully!")) {
                    personRepository.delete(existPerson);
                    return "User deleted with success!";
                } else {
                    personRepository.delete(existPerson);
                    return "Failed to delete devices!";
                }
            }
        } catch (RestClientException e) {
            // Handle the case where the microservice call fails (timeout, network issue, etc.)
            return "Error while deleting devices: " + e.getMessage();
        }
    }

    public String createAdmin(RegisterDTO registerDTO){
        Person existPerson = personRepository.findPersonByUsername(registerDTO.getUsername());
        if(existPerson != null){
            return "Already exists an admin account with this username!";
        }else if(!registerDTO.getPassword().equals(registerDTO.getConfirmPassword())){
            return "Confirm password doesn't match with password!";
        }else{
            Person savedPerson = new Person(registerDTO.getName(), "admin", registerDTO.getUsername(), passwordEncoder.encode(registerDTO.getPassword()));
            personRepository.save(savedPerson);
            return "Admin created with success!";
        }
    }

    public List<Person> findClients(){
        return personRepository.findPersonsByRole("client");
    }

    public List<Person> findAdmins(){
        return personRepository.findPersonsByRole("admin");
    }
}
