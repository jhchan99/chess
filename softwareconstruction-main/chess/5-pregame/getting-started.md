# Getting Started

📁 [Starter code](starter-code)

The Starter Code has 2 folders, `clientTests` and `ui`. Do the following:

1. Create the folder `client/src/main/java/ui`. This is where you will put your client application code.
1. Copy the [EscapeSequences.java](starter-code/ui/EscapeSequences.java) file into your project's `client/src/main/java/ui` folder. This file defines values that you can use to control the coloration of your console output.
1. Create the folder `client/src/test/java/clientTests`. This is where you will put your client unit tests.
1. Mark the `client/src/test/java` directory as `Test sources root` by right clicking on the folder and selecting the `Mark Directory as` option. This enables IntelliJ to run this code as tests.
1. Copy the [ServerFacadeTests.java](starter-code/clientTests/ServerFacadeTests.java) into the `client/src/test/java/clientTests` folder. This test class is meant to get you started on your server facade tests. It includes code for starting and stopping your HTTP server so that your tests can make server requests.

This should result in the following additions to your project.

```txt
└── client
    └── src
        ├── main
        │   └── java
        │       └── ui
        │           └── EscapeSequences.java
        └── test
            └── java
                └── ClientTests
                    └── ServerFacadeTests.java
```
