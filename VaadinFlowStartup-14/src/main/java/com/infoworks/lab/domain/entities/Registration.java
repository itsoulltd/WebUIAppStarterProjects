package com.infoworks.lab.domain.entities;

import com.infoworks.lab.domain.models.Gender;
import com.it.soul.lab.sql.SQLExecutor;
import com.it.soul.lab.sql.query.models.Property;

import javax.validation.constraints.*;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class Registration extends Persistable<Long, Long> {

	@NotBlank(message = "Email must not be null or empty!")
	@Email(message = "Please enter valid Email address!")
	private String email;
	@Size(min = 8, message = "Password must be at least 8 characters")
	private String password;
	@Size(min = 8, message = "ConfirmPassword must be at least 8 characters")
	private String confirmPassword;

	private String tenantID;
	private String firstName;
	private String lastName;

	@Pattern(regexp = "\\+?[0-9\\-\\s]{7,20}", message = "Invalid phone number")
	private String contact;

	private String accountPrefix = "CASH";
	private String accountType = "USER";
	private String accountName;

	private String sex = Gender.NONE.name();
	private int age = 18;
	private Date dob = new java.sql.Date(new Date().getTime());

	public Registration() {}

	public Registration(@NotNull(message = "Name must not be null") String name
			, Gender sex
			, @Min(value = 18, message = "Min Value is 18.") int age) {
		this.firstName = name;
		this.sex = sex.name();
		this.age = age;
		updateDOB(age, false);
	}

	private void updateDOB(@Min(value = 18, message = "Min Value is 18.") int age, boolean isPositive) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(Objects.nonNull(getDob()) ? getDob() : new Date());
		int year = calendar.get(Calendar.YEAR) - ((isPositive) ? -age : age);
		calendar.set(Calendar.YEAR, year);
		setDob(calendar.getTime());
	}

	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = new java.sql.Date(dob.getTime());
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Registration user = (Registration) o;
		return Objects.equals(email, user.email);
	}

	@Override
	public int hashCode() {
		return Objects.hash(email);
	}

	public Property getPropertyTest(String key, SQLExecutor exe, boolean skipPrimary) {
		return getProperty(key, exe, skipPrimary);

	}

	public String getTenantID() {
		return tenantID;
	}

	public void setTenantID(String tenantID) {
		this.tenantID = tenantID;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getAccountPrefix() {
		return accountPrefix;
	}

	public void setAccountPrefix(String accountPrefix) {
		this.accountPrefix = accountPrefix;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public String getAccountName() {
		if (accountName == null) {
			//CASH@[...14char...] = 20
			String parts = getEmail().substring(0, getEmail().indexOf("@"));
			if (parts.length() > 14) parts = parts.substring(0, 14);
			accountName = parts;
		}
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

}
