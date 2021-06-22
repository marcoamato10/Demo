package it.hyaholding.demo.repository;

import it.hyaholding.demo.entity.Person;
import it.hyaholding.demo.entity.Sex;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PersonRepository extends MongoRepository<Person, String> {

    List<Person> findAllBySex(Sex sex);

    Person findBySex(Sex sex);

    Person findByFiscalCode(String fiscalCode);

    boolean existsByFiscalCode(String fiscalCode);

    Person deleteByFiscalCode(String fiscalCode);
}
