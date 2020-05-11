package com.example.springBootDemo.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.example.springBootDemo.model.Person;

@Repository("fakeDao")
public class FakePersonDataAccessService implements PersonDao {

	private static List<Person> DB = new ArrayList<>();
	
	@Override
	public int insertPerson(UUID id, Person person) {
//		String formatPhone = person.getMobilePhone().replaceFirst("(\\d{3})(\\d{3})(\\d+)","($1) $2-$3");
		DB.add(new Person(id, person.getName(), person.getEmail(), person.getMobilePhone()));
		return 1;
	}

	@Override
	public List<Person> selectAllPeople() {
		DB.stream().map(person -> person.getMobilePhone()).forEach(System.out::println);
		return DB;
	}

	@Override
	public Optional<Person> selectPersonById(UUID id) {
		return DB.stream()
				.filter(person -> person.getId().equals(id))
				.findFirst();
	}

	@Override
	public int deletePersonById(UUID id) {
		Optional<Person> personMaybe = selectPersonById(id);
		if(!personMaybe.isPresent()) {
			return 0;
		}
		DB.remove(personMaybe.get());
		return 1;
	}

	@Override
	public int updatePersonById(UUID id, Person update) {
		return selectPersonById(id)
				.map(person -> {
					int indexOfPersonToUpdate = DB.indexOf(person);
					if(indexOfPersonToUpdate >= 0) {
//						String newPhone = update.getMobilePhone().replaceFirst("(\\d{3})(\\d{3})(\\d+)","($1) $2-$3");
						DB.set(indexOfPersonToUpdate, 
								new Person(id, 
										   update.getName(), 
										   update.getEmail(), 
										   update.getMobilePhone()));
						return 1;
					}
					return 0;
				})
				.orElse(0);
	}
}
