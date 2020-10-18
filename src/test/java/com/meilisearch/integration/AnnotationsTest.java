package com.meilisearch.integration;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.meilisearch.sdk.Client;
import com.meilisearch.sdk.Config;
import com.meilisearch.sdk.MeiliIndexService;
import com.meilisearch.sdk.utils.Owner;
import com.meilisearch.sdk.utils.Pet;
import com.meilisearch.sdk.utils.Toys;

public class AnnotationsTest extends AbstractIT {

	private MeiliIndexService service;

	public MeiliIndexService getService() {
		if (service == null) {
			service = new MeiliIndexService();
			service.setClient(new Client(new Config("http://localhost:7700", "")));
		}

		return service;
	}

	/**
	 * Test Add single document
	 */
	@Test
	public void testAddSingleEntity() throws Exception {

		Toys toy = new Toys();
		toy.setName("Bone");

		getService().index(toy);

	}

	/**
	 * Test Add single document
	 */
	@Test
	public void testComplexEntity() throws Exception {

		Toys bone = new Toys();
		bone.setName("Bone");
		bone.setPrice(new BigDecimal("4.20"));

		Toys stick = new Toys();
		stick.setName("Stick");
		stick.setPrice(new BigDecimal("6.90"));

		List<Toys> toys = new ArrayList<Toys>();
		toys.add(bone);
		toys.add(stick);

		Pet rex = new Pet();
		rex.setName("Rex");
		rex.setToys(toys);
		rex.setType("Dog");

		Map<String, Pet> pets = new HashMap<String, Pet>();
		pets.put("Rex", rex);
		pets.put("Rex2", rex);

		Owner owner = new Owner();
		owner.setName("Antoine");
		owner.setPassword("password123");
		owner.setPhoneNumber("123456789");
		owner.setPets(pets);

		getService().index(owner);

	}

}
