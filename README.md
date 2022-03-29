# CS122B Activity 3 - Database

- [NamedParameterJDBCTemplate](#namedparameterjdbctemplate)
- [Update Queries](#update-queries)
- [Select Queries](#select-queries)
- [Lambda](#lambda)

## NamedParameterJDBCTemplate

Spring has a very convenient class `NamedParameterJDBCTemplate` that does a lot of work with JDBC to allow for Database queries without a lot of boilerplate. Usually to deal with database quries in Java we would use `Connection` directly. But this comes with a lot of additional steps, works, and clean up that we would rather avoid. 

**Note** `Connection` is only here for **demonstration purposes only**. Please only use `NamedParameterJDBCTemplate` as this will save you a lot of time in these projects


## Update Queries

To update the database (`INSERT`, `UPDATE`, `DELETE`, `REPLACE`, ...) we use the `NamedParameterJDBCTemplate::update()` function which takes two arguments and returns the amount of rows updated:

 - `String` - The Sql string to execute
 - `MapSqlParameterSource` - a 'map' of values to replace in the sql string (we **never** place values directly in the sql string as this leaves us open for sql injection, therefore we use this to keep a record of all the values to replace)

```java
String sql = 
    "INSERT INTO activity.student (first_name, last_name, year, gpa)" +
    "VALUES (:firstName, :lastName, :year, :gpa);"; //notice we mark varaibles with the ':var' format
    
MapSqlParameterSource source = 
    new MapSqlParameterSource()  //For ever ':var' we list a value and `Type` for value
        .addValue("firstName", student.getFirstName(), Types.VARCHAR) // Notice the lack of ':'  in the string here
        .addValue("lastName", student.getLastName(), Types.VARCHAR)
        .addValue("year", student.getYear(), Types.INTEGER)
        .addValue("gpa", student.getGpa(), Types.DECIMAL);
        
int rowsUpdated = this.template.update(sql, source);  
// If no error is thrown then the query has been executed, and we can check how many rows were updated with the returned int
```

## Select Queries

To get values from the database (`SELECT`) we use `NamedParameterJDBCTemplate::query()` for a list of values *or* `NamedParameterJDBCTemplate::queryForObject()` if we expect there to be **exactly one value** (throws if not exactly one value) which takes three arguments (the same two as update as well as one additional one for mapping)

 - `String` - The Sql string to execute
 - `MapSqlParameterSource` - a 'map' of values to replace in the sql string (we **never** place values directly in the sql string as this leaves us open for sql injection, therefore we use this to keep a record of all the values to replace)
 - `lambda` - a Java Lambda (or a reference to a method) that takes two values (`ResultSet` rs, `int` rowNum) and returns any object.

```java
String sql = 
    "SELECT id, first_name, last_name, year, gpa " +
    "FROM activity.student " +
    "WHERE first_name = :firstName;"; //notice we mark varaibles with the ':var' format
    
MapSqlParameterSource source = 
    new MapSqlParameterSource() //For ever ':var' we list a value and `Type` for value
        .addValue("firstName", firstName, Types.VARCHAR); // Notice the lack of ':'  in the string here
        
        
List<Student> students =
    this.template.query(
        sql, 
        source,
        // For every row this lambda will be called to turn it into a Object (in this case `Student`)
        (rs, rowNum) ->          
            new Student()
                .setId(rs.getLong("id"))
                .setFirstName(rs.getString("first_name"))
                .setLastName(rs.getString("last_name"))
                .setYear(rs.getInt("year"))
                .setGpa(rs.getDouble("gpa"))
    );
```

### Lambda

For the select queries we use a lambda to map the row into an object. If you want you can use a method instead if the lambda has too much work or if you just prefer to use methods:

```java
public List<Student> getStudents(String firstName)
{
    String sql = 
        "SELECT id, first_name, last_name, year, gpa " +
        "FROM activity.student " +
        "WHERE first_name = :firstName;"; //notice we mark varaibles with the ':var' format

    MapSqlParameterSource source = 
        new MapSqlParameterSource() //For ever ':var' we list a value and `Type` for value
            .addValue("firstName", firstName, Types.VARCHAR); // Notice the lack of ':'  in the string here


    List<Student> students =
        this.template.query(
            sql, 
            source,
            this::mapStudentRows
        );
    
    return students;
}
    
    
public Student mapStudentRows(ResultSet rs, int rowNum)
    throws SQLException // it is expected that this can throw a SQLException, so mark the method as such 
{
    return new Student()
        .setId(rs.getLong("id"))
        .setFirstName(rs.getString("first_name"))
        .setLastName(rs.getString("last_name"))
        .setYear(rs.getInt("year"))
        .setGpa(rs.getDouble("gpa"));
}
```
