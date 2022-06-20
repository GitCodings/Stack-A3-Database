package com.gitcodings.stack.database.model.response;

import com.gitcodings.stack.database.model.data.Student;

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
