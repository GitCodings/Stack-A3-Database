package com.gitcodings.stack.database.model.response;

import com.gitcodings.stack.database.model.data.Student;

public class StudentRequest
{
    private Student student;

    public Student getStudent()
    {
        return student;
    }

    public StudentRequest setStudent(Student student)
    {
        this.student = student;
        return this;
    }
}
