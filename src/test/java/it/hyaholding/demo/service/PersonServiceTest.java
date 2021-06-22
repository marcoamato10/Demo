package it.hyaholding.demo.service;

import it.hyaholding.demo.entity.Person;
import it.hyaholding.demo.entity.Sex;
import it.hyaholding.demo.exception.CFNotFoundException;
import it.hyaholding.demo.exception.FailedToAddException;
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
        person = personRepository.save(person);
    }

    @AfterEach
    public void tearDown() {
        personRepository.deleteAll();
        person = null;
    }

    @Test
    @Order()
    public void getAllBySexAfterEighteen() {
    }

    @Test
    public void checkerFindAllBySex() {
    }

    @Test
    public void getBySexAfterEighteen() {
    }

    @Test
    public void checkerFindBySex() {
    }

    @Test
    @Order(1)
    public void addPerson() {
        Person fetchedPerson = personRepository.findById(person.getId()).get(); //fetched = recuperato
        assertEquals(person.getId(), fetchedPerson.getId());
    }

    @Test
    @Order(2)
    public void checkerAddPerson() {
        assertNotNull(personService.checkerAddPerson(person),"Non è null");
    }

    @Test
    @Order(3)
    public void updatePerson() throws CFNotFoundException, FailedToAddException {
        Person fetchedPerson = personRepository.findByFiscalCode(person.getFiscalCode()); //persona 1
        Person fetchedPersonMod = personRepository.findByFiscalCode(person.getFiscalCode()); //persona 2
        fetchedPersonMod.setName("carlo");
        fetchedPersonMod.setSurname("vruni");
        fetchedPersonMod.setSex(Sex.MALE);
        fetchedPersonMod.setAddress("vkkkv");
        fetchedPersonMod.setFiscalCode(fetchedPerson.getFiscalCode());
        fetchedPerson= personService.updatePerson(fetchedPersonMod);
        assertEquals(fetchedPerson.getName(), fetchedPersonMod.getName());
        assertEquals(fetchedPerson.getSurname(), fetchedPersonMod.getSurname());
        assertEquals(fetchedPerson.getSex(), fetchedPersonMod.getSex());
        assertEquals(fetchedPerson.getAddress(), fetchedPersonMod.getAddress());
    }

    @Test
    public void checkerUpdatePerson() {
    }

    @Test
    public void deleteByFiscalCode() {
    }

    @Test
    public void checkerDeletePerson() {
    }

    @Test
    public void getAll() {
    }

    @Test
    public void getByFiscalCode() {
    }

    @Test
    public void checkFiscalCode() {
    }
}