# ♕ Phase 4: Chess Database

- [Chess Application Overview](../chess.md)
- [Getting Started](getting-started.md)
- [Starter Code](starter-code)

In this part of the Chess project, you will create a MySQL implementation of your Data Access interface and start calling it from your services. This enables the ability to store data persistently in a MySQL database instead of storing data in main memory. You will also write unit tests using JUnit for your DAO classes. This involves the following steps:

1. Install the MYSQL database management system (DBMS) on your development machine.
1. Modify `db.properties` to contain your username and password.
1. Design your database tables (i.e., your database `schema`)
1. Implement a MySQL implementation your Data Access Interface. Initially you can just stub out all of the methods.
1. Add the ability to create your database and tables, if they don't exist, when your server starts up.
1. Iteratively write a test for each of your Data Access interface methods along with the backing MySQL code.
1. Ensure that all provided pass off tests work properly, including the PersistenceTests added for this assignment, and the StandardAPITests from the previous assignment.

## Making Database Connections

The getting started code found in the `server/src/main/dataAccess/databaseManager.java` file reads the database configuration information from `db.properties` and contains a static function for creating database connections. The following code gives you an example of how you can use this code.

```java
public void example() throws Exception {
   try (var conn = DatabaseManager.getConnection()) {
      try (var preparedStatement = conn.prepareStatement("SELECT 1+1")) {
         var rs = preparedStatement.executeQuery();
         rs.next();
         System.out.println(rs.getInt(1));
      }
   }
}
```

Make sure that you wrap your calls to get a connection with a `try-with-resources` block so that the connection gets cleaned up.

## Initializing Your Database and Tables

As you design your database schema, carefully consider data types, primary and foreign keys, autogenerated IDs, default values, and whether a field can be `null`.

⚠ The `DatabaseManager` class also has a method for creating a database if it does not exist. You do not need to use this code, but it is required that your code creates both your database and tables if they do not exist on start up. This allows the pass off tests to run without manual intervention to set up your database.

The [Pet Shop](../../petshop/server/src/main/dataaccess/MySqlDataAccess.java) provides an example of how to initialize your database on start up if you are wondering how this is done.

## Password Hashing

In order to protect the security of your user's password, you must encrypt their password using the bcrypt algorithm. When a user provides a password, hash it before storing it in the database.

```java
void storeUserPassword(String username, String password) {
   BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
   String hashedPassword = encoder.encode(clearTextPassword);

   // write the hashed password in database along with the user's other information
   writeHashedPasswordToDatabase(username, hashedPassword);
}
```

Then when a user attempts to login, repeat the hashing process on the user supplied login password and then compare the resulting hash to the previously stored hash of the original password. If the two hashes match then you know the supplied password is correct.

```java
boolean verifyUser(String username, String providedClearTextPassword) {
   // read the previously hashed password from the database
   var hashedPassword = readHashedPasswordFromDatabase(username);

   BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
   return encoder.matches(providedClearTextPassword, hashedPassword);
}
```

The above code demonstrates the necessary concepts to implement secure password storage, but it will need to be adapted to your particular implementation. You do not need to create a different table to store your passwords. The hashed password may be store along with your other user information in your `user` table.

## ChessGame Serialization/Deserialization

The easiest way to store the state of a ChessGame in MySQL is to serialize it to a JSON string, and then store the string in your database. Whenever your server needs to update the state of a game, it should:

1. Select the game’s state (JSON string) from the database
2. Deserialize the JSON string to a ChessGame Java object
3. Update the state of the ChessGame object
4. Re-serialize the Chess game to a JSON string
5. Update the game’s JSON string in the database

You will want to carefully consider the need for a Gson type adapter when you do your serialization. If your classes have any interface fields then you will need to tell Gson how to instantiate a concrete class for the interface when it is deserializing. You might want to review the [instruction](../../instruction/db-jdbc/db-jdbc.md) on this topic.

## Relevant Instruction Topics

- [JSON and Serialization](../../instruction/json/json.md): Serialization objects to the database.
- [Relational Databases](../../instruction/): How relational databases work.
- [MYSQL](../../instruction/mysql/mysql.md): Getting MySQL installed.
- [SQL](../../instruction/db-sql/): Using SQL statements.
- [JDBC](../../instruction/db-jdbc/): Using SQL from Java including type adapters.

## ☑ Deliverable

### Pass Off Tests

The tests provided for this assignment are in the PersistenceTests class. These tests make HTTP requests to test your server.

Additionally, run the StandardAPITests from the previous phase to make sure they still run successfully.

### Database Unit Tests

The pass off tests do not examine your game board. That means it is critical that you write tests that fully test everything you are persisting to the database. This includes tests that store an initial board, add players, make moves, and update the game state.

As part of your unit test deliverable you need to meet the following requirements.

1. Write a positive and a negative JUNIT test case for each public method on your DAO classes, except for Clear methods which only need a positive test case. A positive test case is one for which the action happens successfully (e.g., creating a new user in the database). A negative test case is one for which the operation fails (e.g., creating a User that has the same username as an existing user).
1. Ensure that all of your unit tests work, including the new DAO tests and the Service tests you wrote in the previous assignment.

### Pass Off, Submission, and Grading

All of the tests in your project must succeed in order to complete this phase.

To pass off this assignment use the course auto-grading tool. If your code passes then your grade will automatically be entered in Canvas.

### Grading Rubric

**⚠ NOTE**: You are required to commit to GitHub with every minor milestone. For example, after you successfully pass a test. This should result in a commit history that clearly details your work on this phase. If your Git history does not demonstrate your efforts then your submission may be rejected.

| Category       | Criteria                                                                                                                                                                            |       Points |
| -------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | -----------: |
| GitHub History | At least 10 GitHub commits evenly spread over the assignment period that demonstrate proof of work                                                                                  | Prerequisite |
| Functionality  | All pass off test cases succeed                                                                                                                                                     |          100 |
| Unit Tests     | All test cases pass<br/>Each public method on DAO classes has two test cases, one positive test and one negative test<br/>Every test case includes an Assert statement of some type |           25 |
|                | Total                                                                                                                                                                               |          125 |