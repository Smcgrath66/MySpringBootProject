package com.example.springBootDemo.dao;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.springBootDemo.model.Person;

@Repository("postgres")
public class PersonDataAccessService implements PersonDao {
//	@SuppressWarnings("unused")
//	private static List<Person> DB = new ArrayList<>();

	private final JdbcTemplate jdbcTemplate;

	@Autowired
	public PersonDataAccessService(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public int insertPerson(UUID id, Person person) {
		final String sql = "insert into person (id, name, email, mobilephone) values (?, ?, ?, ?)";
		return jdbcTemplate.update(sql, id, person.getName(), person.getEmail(), person.getMobilePhone());
	}

	@Override
	public List<Person> selectAllPeople() {
		final String sql = "select id, name, email, mobilephone from person";
		return jdbcTemplate.query(sql, (resultSet, i) -> {
			UUID id = UUID.fromString(resultSet.getString(1));
			String name = resultSet.getString(2);
			String email = resultSet.getString(3);
			// Format phone number
			String mobilePhone = resultSet.getString(4).replaceFirst("(\\d{3})(\\d{3})(\\d+)", "($1) $2-$3");
			System.out.println("DEBUG_BOMB - ID [" + id + "] NAME [" + name + "] EMAIL [" + email + "] MobilePhone ["
					+ mobilePhone + "]");
			return new Person(id, name, email, mobilePhone);
		});
	}

	@Override
	public Optional<Person> selectPersonById(UUID id) {
		final String sql = "select id, name, email, mobilephone from person where id = ?";
		// RowMapper with input param id
		Person person = jdbcTemplate.queryForObject(sql, new Object[] { id }, (resultSet, i) -> {
			UUID personId = UUID.fromString(resultSet.getString(1));
			String name = resultSet.getString(2);
			String email = resultSet.getString(3);
			// Format phone number
			String mobilePhone = resultSet.getString(4).replaceFirst("(\\d{3})(\\d{3})(\\d+)", "($1) $2-$3");
//			System.out.println("DEBUG_BOMB - ID [" + id + "] NAME [" + name + "] EMAIL [" + email + "] MobilePhone ["
//					+ mobilePhone + "]");
			return new Person(personId, name, email, mobilePhone);
		});
		return Optional.ofNullable(person);
	}

	@Override
	public int deletePersonById(UUID id) {
		try {
			final String sql = "delete from person where id = ?";
			return jdbcTemplate.update(sql, id);
		} catch (Exception e) {
			System.out.println(e.toString());
			return 0;
		}
	}

	@Override
	public int updatePersonById(UUID id, Person person) throws Exception {

		try {
			String sql = "update person set name = ?, email = ?, mobilephone = ? where id =?";
			return jdbcTemplate.update(sql, person.getName(), person.getEmail(), person.getMobilePhone(), id);

		} catch (Exception e) {
			System.out.println(e);
			return 0;
		}
	}

}
