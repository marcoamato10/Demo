package it.hyaholding.demo.service;

import it.hyaholding.demo.entity.Person;
import it.hyaholding.demo.entity.Sex;
import it.hyaholding.demo.exception.CFNotFoundException;
import it.hyaholding.demo.exception.FailedCFException;
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

    public Person addPerson(Person person) throws FailedToAddException, FailedCFException {
        if (person.getName() == null || person.getSurname() == null || person.getAddress() == null || person.getFiscalCode() == null || person.getSex() == null) {
            throw new FailedToAddException("Non puoi lasciare campi vuoti");
        } else if (personRepository.existsByFiscalCode(person.getFiscalCode())) {
            throw new FailedToAddException("Codice fiscale già esistente");
        } else if (!person.getFiscalCode().matches("[A-Z]{6}[0-9]{2}[A-Z][0-9]{2}[A-Z][0-9]{3}[A-Z]$")) {
            throw new FailedCFException("Inserire il codice fiscale nel formato valido");
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
        } catch (FailedCFException c) {
            String errorMessageTwo = new StringBuilder().append("Codice fiscale non valido: ").append(c.getMessage()).toString();
            log.warning(errorMessageTwo);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    public Person updatePerson(Person person) throws FailedToAddException, CFNotFoundException {
        if (personRepository.existsByFiscalCode(person.getFiscalCode())) {
            Person temp = personRepository.findByFiscalCode(person.getFiscalCode());
            if (person.getName() != null)
                temp.setName(person.getName());
            if (person.getSurname() != null)
                temp.setSurname(person.getSurname());
            if (person.getAddress() != null)
                temp.setAddress(person.getAddress());
            if (person.getSex() != null)
                temp.setSex(person.getSex());
            if (person.getName() == null || person.getSurname() == null || person.getAddress() == null || person.getFiscalCode() == null || person.getSex() == null) {
                throw new FailedToAddException("Non puoi lasciare campi vuoti");
            } else {
                return personRepository.save(temp);
            }
        } else {
            throw new CFNotFoundException("Codice fiscale inesistente");
        }
    }

    public ResponseEntity<Person> checkerUpdatePerson(Person person) {
        try {
            return ResponseEntity.ok(updatePerson(person));
        } catch (FailedToAddException f) {
            String errorMessage = new StringBuilder().append("Modifica annullata: ").append(f.getMessage()).toString();
            log.warning(errorMessage);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (CFNotFoundException b) {
            String errorMessageThree = new StringBuilder().append("Codice fiscale non valido: ").append(b.getMessage()).toString();
            log.warning(errorMessageThree);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    public Person deleteByFiscalCode(String fiscalCode) throws CFNotFoundException {
        if (personRepository.existsByFiscalCode(fiscalCode)) {
            return personRepository.deleteByFiscalCode(fiscalCode);
        } else {
            throw new CFNotFoundException("Codice fiscale inesistente");
        }
    }

    public ResponseEntity<Person> checkerDeletePerson(String fiscalCode) {
        try {
            return ResponseEntity.ok(deleteByFiscalCode(fiscalCode));
        } catch (CFNotFoundException b) {
            log.warning(b.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    public List<Person> getAll() {
        return personRepository.findAll();
    }

    public Person getByFiscalCode(String fiscalCode) throws CFNotFoundException {
        if (personRepository.existsByFiscalCode(fiscalCode)) {
            return personRepository.findByFiscalCode(fiscalCode);
        } else
            throw new CFNotFoundException("Codice fiscale inesistente");
    }

    public ResponseEntity<Person> checkFiscalCode(String fiscalCode) {
        try {
            return ResponseEntity.ok(getByFiscalCode(fiscalCode));
        } catch (CFNotFoundException b) {
            log.warning(b.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
}