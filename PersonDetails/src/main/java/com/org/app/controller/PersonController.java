package com.org.app.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.org.app.exception.RecordNotFoundException;
import com.org.app.model.Person;
import com.org.app.repo.PersonRepository;

import io.swagger.annotations.Api;

@Api(value = "person")
@RestController
@RequestMapping("/persons")
public class PersonController {

	private static final Logger logger = LogManager.getLogger(PersonController.class);

	@Autowired
	private PersonRepository personrepo;

	@GetMapping
	public ResponseEntity<List<Person>> getAllPersons(@RequestParam(required = false) String title) {
		if (logger.isDebugEnabled()) {
			logger.debug("entering getAllPersons(String)");
			logger.debug("title: \"" + title + "\"");
		}
		try {
			List<Person> Persons = new ArrayList<Person>();

			if (title == null)
				personrepo.findAll().forEach(Persons::add);

			if (Persons.isEmpty()) {
				if (logger.isDebugEnabled()) {
					logger.debug("exiting getAllPersons()");
					logger.debug("returning: " + new ResponseEntity<>(HttpStatus.NO_CONTENT));
				}
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			logger.debug("total numberof rows of person ::", Persons.size());
			if (logger.isDebugEnabled()) {
				logger.debug("exiting getAllPersons()");
				logger.debug("returning: " + new ResponseEntity<>(Persons, HttpStatus.OK));
			}
			return new ResponseEntity<>(Persons, HttpStatus.OK);
		} catch (Exception e) {
			if (logger.isDebugEnabled()) {
				logger.debug("exiting getAllPersons()");
				logger.debug("returning: " + new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR));
			}
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<Person> getPersonById(@PathVariable("id") long id) {
		if (logger.isDebugEnabled()) {
			logger.debug("entering getPersonById(long)");
			logger.debug("id: " + id);
		}
		Optional<Person> personData = personrepo.findById(id);

		if (personData == null) {
			throw new RecordNotFoundException("Invalid person id : " + id);
		}

		if (personData.isPresent()) {
			logger.debug("get Person First Name::", personData.get().getFirstName());
			if (logger.isDebugEnabled()) {
				logger.debug("exiting getPersonById()");
			}
			return new ResponseEntity<>(personData.get(), HttpStatus.OK);
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("exiting getPersonById()");
				logger.debug("returning: " + new ResponseEntity<>(HttpStatus.NOT_FOUND));
			}
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping
	public ResponseEntity<Person> createPerson(@RequestBody Person Person) {
		if (logger.isDebugEnabled()) {
			logger.debug("entering createPerson(Person)");
			logger.debug("Person: " + Person);
		}
		try {
			Person person = personrepo.save(Person);
			logger.debug("Create Person details ::", person);
			if (logger.isDebugEnabled()) {
				logger.debug("exiting createPerson()");
				logger.debug("returning: " + new ResponseEntity<>(person, HttpStatus.CREATED));
			}
			return new ResponseEntity<>(person, HttpStatus.CREATED);
		} catch (Exception e) {
			if (logger.isDebugEnabled()) {
				logger.debug("exiting createPerson()");
				logger.debug("returning: " + new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR));
			}
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<Person> updatePerson(@PathVariable("id") Long id, @RequestBody Person Person) {
		if (logger.isDebugEnabled()) {
			logger.debug("entering updatePerson(Long,Person)");
			logger.debug("id: " + id);
			logger.debug("Person: " + Person);
		}
		Optional<Person> personData = personrepo.findById(id);

		if (personData == null) {
			throw new RecordNotFoundException("Invalid person id : " + id);
		}

		if (personData.isPresent()) {
			Person person = personData.get();
			person.setFirstName(Person.getFirstName());
			person.setLastName(Person.getLastName());
			logger.debug("Update Person details of given Id ::", person);
			if (logger.isDebugEnabled()) {
				logger.debug("exiting updatePerson()");
			}
			return new ResponseEntity<>(personrepo.save(person), HttpStatus.OK);
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("exiting updatePerson()");
				logger.debug("returning: " + new ResponseEntity<>(HttpStatus.NOT_FOUND));
			}
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<HttpStatus> deletePerson(@PathVariable("id") long id) {
		if (logger.isDebugEnabled()) {
			logger.debug("entering deletePerson(long)");
			logger.debug("id: " + id);
		}
		try {
			personrepo.deleteById(id);
			logger.debug("Delete Person details of given Id ::", id);
			if (logger.isDebugEnabled()) {
				logger.debug("exiting deletePerson()");
				logger.debug("returning: " + new ResponseEntity<>(HttpStatus.NO_CONTENT));
			}
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			if (logger.isDebugEnabled()) {
				logger.debug("exiting deletePerson()");
				logger.debug("returning: " + new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
			}
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping
	public ResponseEntity<HttpStatus> deleteAllPersons() {
		if (logger.isDebugEnabled()) {
			logger.debug("entering deleteAllPersons()");
		}
		try {
			personrepo.deleteAll();
			if (logger.isDebugEnabled()) {
				logger.debug("exiting deleteAllPersons()");
				logger.debug("returning: " + new ResponseEntity<>(HttpStatus.NO_CONTENT));
			}
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			if (logger.isDebugEnabled()) {
				logger.debug("exiting deleteAllPersons()");
				logger.debug("returning: " + new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
			}
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

}
