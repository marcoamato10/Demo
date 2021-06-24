package it.hyaholding.demo.service;

import it.hyaholding.demo.entity.Person;
import it.hyaholding.demo.entity.Sex;
import it.hyaholding.demo.exception.*;
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





    public Person addPerson(Person person) throws NullPersonException, FailedToAddException, FailedCFException {
        if (person==null||person.getName() == null || person.getSurname() == null || person.getAddress() == null || person.getFiscalCode() == null || person.getSex() == null) {
            throw new NullPersonException("Non puoi lasciare campi vuoti");
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
        } catch (NullPersonException n) {
            String errorMessageThree = new StringBuilder().append("Errore nell'inserimento: ").append(n.getMessage()).toString();
            log.warning(errorMessageThree);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    public Person updatePerson(Person person) throws CFNotFoundException, NullPersonException {
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
                throw new NullPersonException("Non puoi lasciare campi vuoti");
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
        } catch (NullPersonException f) {
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
            personRepository.deleteByFiscalCode(fiscalCode);
            return null; //personRepository.deleteByFiscalCode(fiscalCode);
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