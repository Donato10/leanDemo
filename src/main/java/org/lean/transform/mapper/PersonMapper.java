package org.lean.transform.mapper;

import org.lean.model.Person;
import org.lean.transform.dto.PersonDTO;
import org.springframework.stereotype.Component;

@Component
public class PersonMapper implements InboundMapper<Person, PersonDTO>, OutboundMapper<PersonDTO, Person> {

    @Override
    public Person toEntity(PersonDTO personDTO) {
        return Person.builder()
                     .name(personDTO.getName())
                     .lastName(personDTO.getLastNam2())
                     .address(personDTO.getAddress())
                     .cellphone(personDTO.getCellphone())
                     .city(personDTO.getCityName())
                     .build();
    }

    @Override
    public PersonDTO toDto(Person person) {
        return PersonDTO.builder()
                        .name(person.getName())
                        .lastNam2(person.getLastName())
                        .address(person.getAddress())
                        .cellphone(person.getCellphone())
                        .cityName(person.getCity())
                        .build();
    }
}
