package com.gitcodings.stack.database.rest;

import com.gitcodings.stack.database.model.data.Student;
import com.gitcodings.stack.database.model.response.StudentRequest;
import com.gitcodings.stack.database.model.response.StudentResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Types;
import java.util.List;

@RestController
public class JDBCTemplateController
{
    private final NamedParameterJdbcTemplate template;

    @Autowired
    public JDBCTemplateController(NamedParameterJdbcTemplate template)
    {
        this.template = template;
    }

    @GetMapping("/jdbcTemplate/student")
    public ResponseEntity<StudentResponse> student(
        @RequestParam("firstName") String firstName)
    {
        List<Student> students =
            this.template.query(
                "SELECT id, first_name, last_name, year, gpa " +
                "FROM activity.student " +
                "WHERE first_name = :firstName;",

                new MapSqlParameterSource()
                    .addValue("firstName", firstName, Types.VARCHAR),

                (rs, rowNum) ->
                    new Student()
                        .setId(rs.getLong("id"))
                        .setFirstName(rs.getString("first_name"))
                        .setLastName(rs.getString("last_name"))
                        .setYear(rs.getInt("year"))
                        .setGpa(rs.getDouble("gpa"))
            );

        StudentResponse response =
            new StudentResponse()
                .setStudents(students);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/jdbcTemplate/student")
    public ResponseEntity<StudentResponse> student(@RequestBody StudentRequest request)
    {
        Student student = request.getStudent();

        int rowsUpdated = this.template.update(
            "INSERT INTO activity.student (first_name, last_name, year, gpa)" +
            "VALUES (:firstName, :lastName, :year, :gpa);",
            new MapSqlParameterSource()
                .addValue("firstName", student.getFirstName(), Types.VARCHAR)
                .addValue("lastName", student.getLastName(), Types.VARCHAR)
                .addValue("year", student.getYear(), Types.INTEGER)
                .addValue("gpa", student.getGpa(), Types.DECIMAL)
        );

        if (rowsUpdated > 0) {
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
}
