package com.infoworks.lab.domain.entities;

import com.infoworks.lab.domain.models.Gender;
import com.it.soul.lab.sql.SQLExecutor;
import com.it.soul.lab.sql.entity.Ignore;
import com.it.soul.lab.sql.entity.PrimaryKey;
import com.it.soul.lab.sql.entity.TableName;
import com.it.soul.lab.sql.query.models.Property;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

@Entity(name = "User")
@TableName(value = "User")
public class User extends Persistable<Integer, Long> {

	@PrimaryKey(name="id", auto=true)
	@Id @Column(length = 100)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@NotNull
	private Integer id = 0;

	@NotNull(message = "Name must not be null")
	private String name;

	private String sex = Gender.NONE.name();

	@Min(value = 18, message = "Min Value is 18.")
	private int age = 18;


	@NotNull(message = "DOB Must Not Null") @Past(message = "Date Of Birth Must Be Greater Then Now")
	private Date dob = new java.sql.Date(new Date().getTime());

	//@Type(type = "org.hibernate.type.NumericBooleanType")
	private boolean active;

	@Ignore
	private static int _autoIncrement = -1;

	public User() {
		this.id = ++_autoIncrement;
	}

	public User(@NotNull(message = "Name must not be null") String name
			, Gender sex
			, @Min(value = 18, message = "Min Value is 18.") int age) {
		this();
		this.name = name;
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

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public boolean getActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
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
		User user = (User) o;
		return Objects.equals(id, user.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	public Property getPropertyTest(String key, SQLExecutor exe, boolean skipPrimary) {
		return getProperty(key, exe, skipPrimary);

	}

}
