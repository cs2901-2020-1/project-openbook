package com.software.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity(name = "Student")
@DiscriminatorValue("Student")
public class Student extends User {
    protected String school;
    protected int grade;

    public Student() {

    }

    public Student(String email, String username, String password, String name, String surname,
                   String school, int grade) {
        super(email, username, password, name, surname);
        this.school = school;
        this.grade = grade;
    }

    @Override
    public String getTipo(){
        return "student";
    }


    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }
}
