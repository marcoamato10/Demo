package it.hyaholding.demo.controller;

import it.hyaholding.demo.entity.Person;
import it.hyaholding.demo.entity.Sex;
import it.hyaholding.demo.service.PersonService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PersonController {

    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping("/findallbysex")
    public ResponseEntity<List<Person>> findAllBySex(@RequestParam Sex sex) {
        return personService.checkerFindAllBySex(sex);
    }

    @GetMapping("/findbysex")
    public ResponseEntity<Person> findBySex(@RequestParam Sex sex) {
        return personService.checkerFindBySex(sex);
    }

    @PostMapping("/addperson")
    public ResponseEntity<Person> addPerson(Person person) {
        return personService.checkerAddPerson(person);
    }

    /*@PostMapping("/updateperson")
    public ResponseEntity<Person> updatePerson(Person person){
        return personService.checkerUpdatePerson(person);
    }

    @GetMapping("deleteperson")
    public ResponseEntity<Person> deletePerson(Person person){
        return personService.checkerDeletePerson(person);
    }*/

}