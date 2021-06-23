package it.hyaholding.demo.service;

import it.hyaholding.demo.entity.Person;
import it.hyaholding.demo.entity.Sex;
import it.hyaholding.demo.exception.CFNotFoundException;
import it.hyaholding.demo.exception.FailedCFException;
import it.hyaholding.demo.exception.FailedToAddException;
import it.hyaholding.demo.exception.NullPersonException;
import it.hyaholding.demo.repository.PersonRepository;
import lombok.extern.java.Log;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.Order;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureDataMongo
@SpringBootTest
@ExtendWith(SpringExtension.class)
@Log
class PersonServiceTest {

    Person person;

    @Autowired
    PersonService personService;

    @Autowired
    PersonRepository personRepository;

    @BeforeEach
    public void setUp() {
        person = new Person();
        person.setName("Amelia");
        person.setSurname("Milone");
        person.setAddress("Via milone");
        person.setFiscalCode("AAAAAA00A00A000M");
        person.setSex(Sex.FEMALE);
    }

    @AfterEach
    public void tearDown() {
        personRepository.deleteAll();
        person = null;
    }


    @Test
    @Order()
    public void addPerson() throws FailedCFException, NullPersonException, FailedToAddException {
        personService.addPerson(person); //fetched = recuperato
        assertTrue(personRepository.existsByFiscalCode(person.getFiscalCode()));
    }

    @Test
    @Order()
    public void FailedToAddException() {  //CF già esistente
        person = personRepository.save(person);
        assertThrows(FailedToAddException.class, () -> {
            personService.addPerson(person);
        });
    }

    @Test
    @Order()
    public void NullPersonException() {  //Campi vuoti
        person = null;
        assertThrows(NullPersonException.class, () -> {
            personService.addPerson(person);
        });

        person = new Person();
        assertThrows(NullPersonException.class, () -> {
            personService.addPerson(person);
        });
    }

    @Test
    @Order()
    public void FailedCFException() {   //CF in formato non valido
        Person persona = new Person();
        persona.setName("Amelio");
        persona.setSurname("Milono");
        persona.setAddress("Via melone");
        persona.setFiscalCode("aaaakl");
        persona.setSex(Sex.FEMALE);
        assertThrows(FailedCFException.class, () -> {
            personService.addPerson(persona);
        });
    }

    @Test
    @Order()
    public void checkerAddPerson() {
        assertNotNull(personService.checkerAddPerson(person), "Non è null");
    }

    @Test
    @Order()
    public void updatePerson() throws CFNotFoundException, NullPersonException {
        personRepository.save(person); //persona 1
        Person personTwo = personRepository.findByFiscalCode(person.getFiscalCode()); //persona 2
        personTwo.setName("carlo");
        personTwo.setSurname("bruni");
        personTwo.setSex(Sex.MALE);
        personTwo.setAddress("vkkkv");
        person = personService.updatePerson(personTwo);
        assertEquals(person.getName(), personTwo.getName());
        assertEquals(person.getSurname(), personTwo.getSurname());
        assertEquals(person.getSex(), personTwo.getSex());
        assertEquals(person.getAddress(), personTwo.getAddress());
    }

    @Test
    @Order()
    public void NullPersonExceptionUpdate() {  //Campi vuoti
        person = null;
        assertThrows(NullPersonException.class, () -> {
            personService.addPerson(person);
        });

        person = new Person();
        assertThrows(NullPersonException.class, () -> {
            personService.addPerson(person);
        });
    }

    @Test
    @Order()
    public void CFNotFoundException() {  //CF non trovato
        person.setFiscalCode(null);
        assertThrows(CFNotFoundException.class, () -> {
            personService.updatePerson(person);
        });
    }

    @Test
    @Order()
    public void checkerUpdatePerson() {
        assertNotNull(personService.checkerUpdatePerson(person));
    }

    @Test
    @Order()
    public void deleteByFiscalCode() throws CFNotFoundException {
        personRepository.save(person);
        Person personTwo = personRepository.findByFiscalCode(person.getFiscalCode());
        personService.deleteByFiscalCode(personTwo.getFiscalCode());
        assertFalse(personRepository.existsByFiscalCode(personTwo.getFiscalCode()));

    }

    @Test
    @Order()
    public void CFNotFoundExceptionDelete() {  //CF non trovato
        person.setFiscalCode(null);
        assertThrows(CFNotFoundException.class, () -> {
            personService.deleteByFiscalCode(person.getFiscalCode());
        });
    }

    @Test
    @Order()
    public void checkerDeletePerson() {
        assertNotNull(personService.checkerDeletePerson(person.getFiscalCode()));
    }

    @Test
    @Order()
    public void getByFiscalCode() throws CFNotFoundException {
        personRepository.save(person);
        Person personTwo = personRepository.findByFiscalCode(person.getFiscalCode());
        assertNotNull(personService.getByFiscalCode(personTwo.getFiscalCode()));
    }

    @Test
    @Order()
    public void CFNotFoundExceptionRead() {  //CF non trovato
        person.setFiscalCode(null);
        assertThrows(CFNotFoundException.class, () -> {
            personService.deleteByFiscalCode(person.getFiscalCode());
        });
    }

    @Test
    @Order()
    public void checkFiscalCode() {
        assertNotNull(personService.checkFiscalCode(person.getFiscalCode()));
    }
}