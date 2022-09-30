package com.piseth.java.school.service;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import com.piseth.java.school.model.Gender;
import com.piseth.java.school.model.Person;

public interface PersonService {
	
	Map<Gender, Long> countNumberOfPeopleByGender();
	
	List<Person> findByNumberOfPetMoreThan();
	
	List<Person> findPeopleHaveCat();
	
	Gender findGenderLikeCatMost();

	boolean whoDoNotFeedPet();

	Person youngestPerson();
	
}
