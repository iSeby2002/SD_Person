package demo.person.controllers;

import demo.person.dtos.*;
import demo.person.entities.Person;
import demo.person.services.PersonService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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
        "http://localhost:8083"})
@RequestMapping("/user")
public class PersonContoller {
    private final PersonService personService;

    @Autowired
    public PersonContoller(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping("/getPersons")
    public ResponseEntity<List<Person>> getPersons(){
        List<Person> users = personService.findPersons();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<String> create(@RequestBody CreatePersonDTO createPersonDTO){
        String msg = personService.createPerson(createPersonDTO);
        if(msg.equals("User created with success!")){
            return new ResponseEntity<>(msg, HttpStatus.CREATED);
        }else{
            return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<String> update(@RequestBody UpdatePersonDTO updatePersonDTO){
        String msg = personService.updatePerson(updatePersonDTO);
        if(msg.equals("User updated with success!")){
            return new ResponseEntity<>(msg, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/changePassword")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordDTO changePasswordDTO){
        String msg = personService.changePassword(changePasswordDTO);
        if(msg.equals("Password updated with success!")){
            return new ResponseEntity<>(msg, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete/{personId}")
    public ResponseEntity<String> delete(@PathVariable UUID personId, HttpServletRequest request){
        String msg = personService.deletePerson(personId, request);
        if(msg.equals("User deleted with success!")){
            return new ResponseEntity<>(msg, HttpStatus.OK);
        }else if(msg.equals("Failed to delete devices!")){
            return new ResponseEntity<>("User deleted with success, but it didn't have any devices!", HttpStatus.OK);
        }else if(msg.equals("User not found!")){
            return new ResponseEntity<>(msg, HttpStatus.NOT_FOUND);
        } else{
            return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getClients")
    public ResponseEntity<List<Person>> getClients(){
        List<Person> users = personService.findClients();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/getAdmins")
    public ResponseEntity<List<Person>> getAdmins(){
        List<Person> users = personService.findAdmins();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
}
