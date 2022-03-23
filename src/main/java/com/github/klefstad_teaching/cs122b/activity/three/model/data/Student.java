package com.github.klefstad_teaching.cs122b.activity.three.model.data;

public class Student
{
    private String  firstName;
    private String  lastName;
    private Integer year;
    private Double  gpa;

    public String getFirstName()
    {
        return firstName;
    }

    public Student setFirstName(String firstName)
    {
        this.firstName = firstName;
        return this;
    }

    public String getLastName()
    {
        return lastName;
    }

    public Student setLastName(String lastName)
    {
        this.lastName = lastName;
        return this;
    }

    public Integer getYear()
    {
        return year;
    }

    public Student setYear(Integer year)
    {
        this.year = year;
        return this;
    }

    public Double getGpa()
    {
        return gpa;
    }

    public Student setGpa(Double gpa)
    {
        this.gpa = gpa;
        return this;
    }
}
