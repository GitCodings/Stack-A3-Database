package com.github.klefstad_teaching.cs122b.database.model.response;

import com.github.klefstad_teaching.cs122b.database.model.data.Student;

import java.util.List;

public class StudentResponse
{
    private List<Student> students;

    public List<Student> getStudents()
    {
        return students;
    }

    public StudentResponse setStudents(List<Student> students)
    {
        this.students = students;
        return this;
    }
}
