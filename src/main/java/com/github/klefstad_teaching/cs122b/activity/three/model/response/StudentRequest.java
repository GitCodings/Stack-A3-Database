package com.github.klefstad_teaching.cs122b.activity.three.model.response;

import com.github.klefstad_teaching.cs122b.activity.three.model.data.Student;

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
