package com.morello.training.security.data;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.CreditCardNumber;

/**
 * <p>
 * This component and its source code representation are copyright protected
 * and proprietary to Kim Morello, Worldwide
 *
 * This component and source code may be used for instructional and
 * evaluation purposes only. No part of this component or its source code
 * may be sold, transferred, or publicly posted, nor may it be used in a
 * commercial or production environment, without the express written consent
 * of Trivera Technologies, LLC
 *
 * Copyright (c) 2015 Kim Morello
 * </p>
 * @author Kim Morello.
 */

public class User {

	public User() {
	}

	@Size(min=0, max=40, message="Full Name is 40 characters or less")
	@Pattern(regexp="^[0-9A-Za-z\\.\\-_]*$", message="Alphanumeric, '.', hyphen, or underscore only please!")
	private String fullName;

	@Size(min=6, max=20, message="User Name is 20 characters or less")
	@Pattern(regexp="^[0-9A-Za-z\\.\\-_]*$", message="Alphanumeric, '.', hyphen, or underscore only please!")
	private String userName;

	@Size(min=0, max=30, message="City should be 30 characters or less!")
	private String street;

	@Size(min=5, max=10)
	@Pattern(regexp="^[0-9]{5}(?:-[0-9]{4})?$", message="Zip or Zip-plus4")
	private String zipcode;

	@Size(min=0, max=30, message="City should be 30 characters or less!")
	private String city;

	@CreditCardNumber(message="Invalid Credit Card Number!")
	private String ccn;

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String newFullName) {
		fullName = newFullName;
	}

	public void setStreet(String newStreet) {
		street = newStreet;
	}

	public String getStreet() {
		return street;
	}

	public void setZipcode(String newZipcode) {
		zipcode = newZipcode;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setCity(String newCity) {
		city = newCity;
	}

	public String getCity() {
		return city;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getCcn() {
		return ccn;
	}

	public void setCcn(String newCcn) {
		this.ccn = newCcn;
	}
}
