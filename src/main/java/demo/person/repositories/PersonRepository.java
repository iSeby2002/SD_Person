package demo.person.repositories;

import demo.person.entities.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PersonRepository extends JpaRepository<Person, UUID> {
    Person findPersonById(UUID id);
    Person findPersonByUsername(String username);
    List<Person> findPersonsByRole(String role);
}
