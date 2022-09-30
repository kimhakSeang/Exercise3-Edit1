package com.piseth.java.school.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.piseth.java.school.ServiceDao.DaoServiceImpl;
import com.piseth.java.school.model.Gender;
import com.piseth.java.school.model.Person;
import com.piseth.java.school.model.Pet;

public class PersonServiceImpl implements PersonService {
	private Connection connection = new DaoServiceImpl().connection();
	
	@Override
	public Map<Gender, Long> countNumberOfPeopleByGender() {
		String query = "SELECT person_gender,count(person_gender) FROM person group by person_gender;";
		Map<Gender, Long> countGender = new HashMap<>();
		try {
			Statement stat = connection.createStatement();
			ResultSet result = stat.executeQuery(query);
			while (result.next()) {
				countGender.put(result.getString("person_gender").equals("MALE") ? Gender.MALE : Gender.FEMALE,
						result.getLong("count"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return countGender;
	}

	@Override
	public List<Person> findByNumberOfPetMoreThan() {
		String queryPeople = "SELECT p.person_id,p.person_name,p.person_gender,p.person_age,p.pet_id,pet.pet_name\r\n"
				+ "from person as p\r\n"
				+ "INNER join pet on p.pet_id=pet.pet_id";
		List<Person> listPeople = new ArrayList<Person>();
		List<Person> list = getListPeopleFromDao(queryPeople);
		for (int i=0;i<list.size();i++) {
			for(int j=i+1;j<list.size();j++) {
				if(list.get(i).getName().equals(list.get(j).getName())) {System.out.println("ok");
				        list.get(i).getPets().add(list.get(j).getPets().get(0));
						listPeople.add(list.get(i));
				}
			}
		}
		return listPeople;
	}

	@Override
	public List<Person> findPeopleHaveCat() {
		String queryCat = "SELECT p.person_id,p.person_name,p.person_gender,p.person_age,p.pet_id,pet.pet_name\r\n"
				+ "from person as p\r\n"
				+ "INNER join pet on p.pet_id=pet.pet_id\r\n"
				+ "WHERE pet.pet_name like '%Cat%'\r\n";
		return getListPeopleFromDao( queryCat);
	}

	@Override
	public Gender findGenderLikeCatMost() {
		       Map<Gender, Long> map = findPeopleHaveCat().stream()
		    		   			.collect(Collectors.groupingBy(p->p.getGender(),Collectors.counting()));
		return map.get(Gender.MALE) > map.get(Gender.FEMALE) ? Gender.MALE : Gender.FEMALE;
	}

	@Override
	public boolean whoDoNotFeedPet() {
		String queryWhoNoPet = "SELECT p.person_id,p.person_name,p.person_gender,p.person_age,p.pet_id,pet.pet_name\r\n"
				+ "FROM person AS p\r\n"
				+ "FULL OUTER JOIN pet on p.pet_id=pet.pet_id\r\n"
				+ "WHERE p.pet_id IS NULL";
		List<Person> listPeople = getListPeopleFromDao(queryWhoNoPet);
		return listPeople!=null?true:false;
	}

	@Override
	public Person youngestPerson() {
		String queryYoungestPersion = "SELECT p.person_id,p.person_name,p.person_gender,p.person_age,p.pet_id,pet.pet_name\r\n"
				+ "FROM person AS p\r\n"
				+ "FULL outer join pet ON p.pet_id=pet.pet_id \r\n"
				+ "WHERE person_age=(SELECT MIN(person_age) FROM person)";
		return getListPeopleFromDao(queryYoungestPersion).get(0);
	}
	
	
	public List<Person> getListPeopleFromDao(String queryListPeople) {
		List<Person> people = new ArrayList<Person>();
		try {
			connection.setAutoCommit(false);
			Statement stm = connection.createStatement();
			ResultSet result = stm.executeQuery(queryListPeople);
			people = getListPersonResult(result);
			stm.close();
		} catch (SQLException e) {
			System.out.println(e);
		}

		return people;
	}


	private List<Person> getListPersonResult(ResultSet result) throws SQLException {
		List<Person> listPerson = new ArrayList<>();
		while (result.next()) {
			int id = result.getInt("person_id");
			String name = result.getString("person_name");
			Gender gender = result.getString("person_gender").equals("MALE") ? Gender.MALE : Gender.FEMALE;
			int age = result.getInt("person_age");
			String petName=result.getString("pet_name");
			Pet pet= cvtNameToPet(petName);
			List<Pet> list = new ArrayList<Pet>();
			list.add(pet);
            listPerson.add(new Person(id,name,gender,age,list));			
		}
		return listPerson;
	}
	
	private Pet cvtNameToPet(String petName) {
		if(petName!=null) {
			String low = petName.toLowerCase();
			if(low.trim().equals("cat")){
				return Pet.CAT;
			}else if(low.trim().equals("dog")) {
				return Pet.DOG;
			}else if(low.trim().equals("fish")) {
				return Pet.FISH;
			}else if(low.trim().equals("bird")) {
				return Pet.BIRD;
			}
		}
		return null;
	}

}
