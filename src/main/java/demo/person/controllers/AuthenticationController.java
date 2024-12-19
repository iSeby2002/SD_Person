package demo.person.controllers;

import demo.person.dtos.LoginDTO;
import demo.person.dtos.PersonDTO;
import demo.person.dtos.RegisterDTO;
import demo.person.services.JwtService;
import demo.person.services.PersonService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = {
        "http://frontend.localhost",
        "http://user.localhost",
        "http://device.localhost",
        "http://monitoring.localhost",
        "http://chat.localhost",
        "http://localhost",
        "http://localhost:3000",
        "http://localhost:8080",
        "http://localhost:8081",
        "http://localhost:8082",
        "http://localhost:8083",
        "https://heroic-boba-6ce237.netlify.app"})
@RequestMapping("/auth")
public class AuthenticationController {
    private final JwtService jwtService;
    private final PersonService personService;

    public AuthenticationController(JwtService jwtService, PersonService personService) {
        this.jwtService = jwtService;
        this.personService = personService;
    }

    @PostMapping("/register")
    public ResponseEntity<PersonDTO> register(@RequestBody RegisterDTO registerDTO){
        PersonDTO personDTO = personService.registerPerson(registerDTO);
        if(personDTO.getMsg().equals("User registered with success!")){
            return new ResponseEntity<>(personDTO, HttpStatus.CREATED);
        }else{
            return new ResponseEntity<>(personDTO, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<PersonDTO> login(@RequestBody LoginDTO loginDTO){
        PersonDTO personDTO = personService.login(loginDTO);

        String jwtToken = jwtService.generateToken(personDTO.getPerson());
        personDTO.setToken(jwtToken);
        personDTO.setExpiresIn(jwtService.getExpirationTime());

        if(personDTO.getMsg().equals("User logged in with success!")){
            return new ResponseEntity<>(personDTO, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(personDTO, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/createAdmin")
    public ResponseEntity<String> createAdmin(@RequestBody RegisterDTO registerDTO){
        String msg = personService.createAdmin(registerDTO);
        if(msg.equals("Admin created with success!")){
            return new ResponseEntity<>(msg, HttpStatus.CREATED);
        }else{
            return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
        }
    }
}
