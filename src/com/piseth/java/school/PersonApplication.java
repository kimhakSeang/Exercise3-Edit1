package com.piseth.java.school;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import com.piseth.java.school.ServiceDao.DaoService;
import com.piseth.java.school.ServiceDao.DaoServiceImpl;
import com.piseth.java.school.model.Gender;
import com.piseth.java.school.model.Person;
import com.piseth.java.school.service.PersonService;
import com.piseth.java.school.service.PersonServiceImpl;

public class PersonApplication {
	
	private final PersonService personService = new PersonServiceImpl();
	private DaoService serviceDao= new DaoServiceImpl();
	private Connection connection = serviceDao.connection();

	public static void main(String[] args) {
		PersonApplication service= new PersonApplication();
		  // Count people by gender 
		  service.showNumberOfPeopleByGender();
		  
		  // Find people who feed pets
		  service.peopleWhoHavePetMoreThanOneType();
		  
		  // People that feed cat 
		  service.peopleWhoHaveCat();
		  
		   //Find gender that like cat most than other one
		  service.showGenderOfPeopleWhoLikeCatTheMost();
		  
		  //Do someone don't feet pet ?
		  service.showPersonDoesNotHavePet();
		
		  // youngest person
		  service.findYoungestPerson();
		
	}

	
	public void showNumberOfPeopleByGender() {
		Map<Gender, Long> countByGender = personService.countNumberOfPeopleByGender();
		System.out.println("Male   = "+countByGender.get(Gender.MALE));
		System.out.println("Female = "+countByGender.get(Gender.FEMALE));
	}
	
	public void peopleWhoHavePetMoreThanOneType() {
		System.out.println("========== More than one pet ==========");
		List<Person> people = personService.findByNumberOfPetMoreThan();
		people.forEach(System.out::println);
	}
	
	public void peopleWhoHaveCat() {
		System.out.println("=========== People that have cat =========");
		List<Person> peopleFromDao = personService.findPeopleHaveCat();
		peopleFromDao.forEach(s->System.out.println(s.getId()+"\t"+s.toString()));
	}
	
	public void showGenderOfPeopleWhoLikeCatTheMost() {
		Gender gender = personService.findGenderLikeCatMost();
		System.out.println("Gender That Like Cat Most is "+gender);
	}
	
	public void showPersonDoesNotHavePet() {
		Boolean havePet = personService.whoDoNotFeedPet();
		System.out.println(havePet);
	}
	
	public void findYoungestPerson() {
		System.out.println("=========Youngest========");
		Person p=personService.youngestPerson();
		System.out.println(p.toString());
	}

	

}
