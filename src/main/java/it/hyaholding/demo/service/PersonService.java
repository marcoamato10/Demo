package it.hyaholding.demo.service;

import it.hyaholding.demo.entity.Person;
import it.hyaholding.demo.entity.Sex;
import it.hyaholding.demo.exception.FailedToAddException;
import it.hyaholding.demo.exception.PermissionDeniedException;
import it.hyaholding.demo.repository.PersonRepository;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;

@Service
@Log
public class PersonService {
    private final PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public List<Person> getAllBySexAfterEighteen(Sex sex) throws PermissionDeniedException {
        LocalTime now = LocalTime.now();
        LocalTime limit = now.withHour(18).withMinute(0).withSecond(0);
        if (now.isAfter(limit)) {
            return personRepository.findAllBySex(sex);
        } else {
            throw new PermissionDeniedException("Non sono le 18:00");
        }
    }

    public ResponseEntity<List<Person>> checkerFindAllBySex(Sex sex) {
        try {
            return ResponseEntity.ok(this.getAllBySexAfterEighteen(sex));
        } catch (PermissionDeniedException e) {
            log.warning(new StringBuilder()
                    .append("Mi disp sono ancora le ")
                    .append(LocalTime.now().minusSeconds(LocalTime.now().getSecond()).minusNanos(LocalTime.now().getNano()))
                    .append(" sks >w<")
                    .toString());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    public Person getBySexAfterEighteen(Sex sex) throws PermissionDeniedException {
        LocalTime now = LocalTime.now();
        LocalTime limit = now.withHour(17).withMinute(0).withSecond(0);
        if (now.isAfter(limit)) {
            return personRepository.findBySex(sex);
        } else {
            throw new PermissionDeniedException("Non sono ancora le 18");
        }
    }

    public ResponseEntity<Person> checkerFindBySex(Sex sex) {
        try {
            return ResponseEntity.ok(this.getBySexAfterEighteen(sex));
        } catch (PermissionDeniedException e) {
            log.warning(new StringBuilder()
                    .append("Mi disp sono ancora le ")
                    .append(LocalTime.now().minusSeconds(LocalTime.now().getSecond()).minusNanos(LocalTime.now().getNano()))
                    .append(" sks >w<")
                    .toString());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    public Person addPerson(Person person) throws FailedToAddException {
        if (person.getName() == null || person.getSurname() == null || person.getAddress() == null || person.getSex() == null) {
            throw new FailedToAddException("Non puoi lasciare campi vuoti");
        } else {
            return personRepository.save(person);
        }
    }

    public ResponseEntity<Person> checkerAddPerson(Person person) {
        try {
            return ResponseEntity.ok(addPerson(person));
        } catch (FailedToAddException f) {
            String errorMessage = new StringBuilder().append("Inserimento fallito: ").append(f.getMessage()).toString();
            log.warning(errorMessage);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }


        //public ResponseEntity<Person> checkerUpdatePerson (Person person){
        //    return ;
       // }

        //public ResponseEntity<Person> checkerDeletePerson (Person person){
        //    return ;
        //}

    }
}