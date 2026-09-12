package com.airtribe.meditrack.entity;

import com.airtribe.meditrack.util.Validator;

// common base for Doctor and Patient - holds the fields and validation
// they both need, each subclass fills in the rest
public abstract class Person {

    protected String id;
    protected String name;
    protected int age;
    protected String phone;
    protected String address;

    protected Person(String id, String name, int age, String phone, String address) {
        Validator.validateName(name);
        Validator.validateAge(age);
        Validator.validatePhone(phone);

        this.id = id;
        this.name = name;
        this.age = age;
        this.phone = phone;
        this.address = address;
    }

    public abstract String getRole();

    public abstract void displayDetails();

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        Validator.validateName(name);
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        Validator.validateAge(age);
        this.age = age;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        Validator.validatePhone(phone);
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Person)) {
            return false;
        }
        Person other = (Person) o;
        return id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return getRole() + " " + id + " - " + name + " (age " + age + ")";
    }
}
