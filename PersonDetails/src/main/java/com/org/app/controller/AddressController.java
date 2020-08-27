package com.org.app.controller;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.org.app.exception.RecordNotFoundException;
import com.org.app.model.Address;
import com.org.app.model.Person;
import com.org.app.repo.AddressRepository;
import com.org.app.repo.PersonRepository;

import io.swagger.annotations.Api;

@Api(value = "address")
@RestController
@RequestMapping("/address")
public class AddressController {

	private static final Logger logger = LogManager.getLogger(AddressController.class);

	@Autowired
	private AddressRepository addressRepo;

	@Autowired
	private PersonRepository personrepo;

	@PostMapping("/{personId}")
	public ResponseEntity<Address> createAddress(@RequestBody Address address, @PathVariable("PersonId") Long id) {
		if (logger.isDebugEnabled()) {
			logger.debug("entering createAddress(Address,Long)");
			logger.debug("address: " + address);
			logger.debug("id: " + id);
		}
		try {
			Optional<Person> personData = personrepo.findById(id);

			if (personData == null) {
				throw new RecordNotFoundException("Invalid person id : " + id);
			}

			if (personData.isPresent()) {
				address.setCity(address.getCity());
				address.setState(address.getState());
				address.setPostalCode(address.getPostalCode());
				address.setStreet(address.getStreet());
				address.setPersonId(personData.get().getId());
				Address addressResult = addressRepo.save(address);
				if (logger.isDebugEnabled()) {
					logger.debug("exiting createAddress()");
					logger.debug("returning: " + new ResponseEntity<>(addressResult, HttpStatus.CREATED));
				}
				return new ResponseEntity<>(addressResult, HttpStatus.CREATED);
			}
		} catch (Exception e) {
			if (logger.isDebugEnabled()) {
				logger.debug("exiting createAddress()");
				logger.debug("returning: " + new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR));
			}
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("exiting createAddress()");
			logger.debug("returning: " + new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR));
		}
		return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@PutMapping("/{personId}")
	public ResponseEntity<Address> updateAddress(@PathVariable("PersonId") Long id, @RequestBody Address address) {
		if (logger.isDebugEnabled()) {
			logger.debug("entering updateAddress(Long,Address)");
			logger.debug("id: " + id);
			logger.debug("address: " + address);
		}
		Optional<Address> PersonData = addressRepo.findByPersonId(id);
		if (PersonData.isPresent()) {
			Address addressUpdate = new Address();
			addressUpdate.setCity(address.getCity());
			addressUpdate.setState(address.getState());
			addressUpdate.setPostalCode(address.getPostalCode());
			addressUpdate.setStreet(address.getStreet());
			address.setPersonId(PersonData.get().getId());
			if (logger.isDebugEnabled()) {
				logger.debug("exiting updateAddress()");
			}
			return new ResponseEntity<>(addressRepo.save(addressUpdate), HttpStatus.OK);
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("exiting updateAddress()");
				logger.debug("returning: " + new ResponseEntity<>(HttpStatus.NOT_FOUND));
			}
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<HttpStatus> deleteAddress(@PathVariable("id") long id) {
		if (logger.isDebugEnabled()) {
			logger.debug("entering deleteAddress(long)");
			logger.debug("id: " + id);
		}
		try {
			addressRepo.deleteById(id);
			if (logger.isDebugEnabled()) {
				logger.debug("exiting deleteAddress()");
				logger.debug("returning: " + new ResponseEntity<>(HttpStatus.NO_CONTENT));
			}
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			if (logger.isDebugEnabled()) {
				logger.debug("exiting deleteAddress()");
				logger.debug("returning: " + new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
			}
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
