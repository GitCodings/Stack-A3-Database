package com.github.klefstad_teaching.cs122b.database.rest;

import com.github.klefstad_teaching.cs122b.database.model.data.Student;
import com.github.klefstad_teaching.cs122b.database.model.response.StudentRequest;
import com.github.klefstad_teaching.cs122b.database.model.response.StudentResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class SQLConnectionController
{
    private final DataSource dataSource;

    @Autowired
    public SQLConnectionController(DataSource dataSource)
    {
        this.dataSource = dataSource;
    }

    @GetMapping("/sqlConnection/student")
    public ResponseEntity<StudentResponse> student(
        @RequestParam("firstName") String firstName)
    {
        List<Student> students = new ArrayList<>();

        try (
            Connection connection = dataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(
                "SELECT first_name, last_name, year, gpa " +
                "FROM activity.student " +
                "WHERE first_name = ?;"
            )
        ) {
            ps.setString(1, firstName);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    students.add(
                        new Student()
                            .setFirstName(rs.getString("first_name"))
                            .setLastName(rs.getString("last_name"))
                            .setYear(rs.getInt("year"))
                            .setGpa(rs.getDouble("gpa"))
                    );
                }
            }
        } catch (SQLException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        StudentResponse response =
            new StudentResponse()
                .setStudents(students);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/sqlConnection/student")
    public ResponseEntity<StudentResponse> student(@RequestBody StudentRequest request)
    {
        Student student = request.getStudent();

        try (
            Connection connection = dataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO activity.student (first_name, last_name, year, gpa) " +
                "VALUES (?, ?, ?, ?);"
            )
        ) {
            ps.setString(1, student.getFirstName());
            ps.setString(2, student.getLastName());
            ps.setInt(3, student.getYear());
            ps.setDouble(4, student.getGpa());

            int rowsUpdated = ps.executeUpdate();

            if (rowsUpdated > 0) {
                return ResponseEntity.status(HttpStatus.OK).build();
            } else {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }

        } catch (SQLException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
