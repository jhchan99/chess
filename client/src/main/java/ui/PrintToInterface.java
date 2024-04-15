package ui;

import static ui.EscapeSequences.*;

public class PrintToInterface {

    // class to hold all print messages for the client to use

    // for websocket facade


    public static void printMessage(String message) {
        System.out.println(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_GREEN + message + RESET_TEXT_COLOR);
    }

    public static void printError(String message) {
        // print out an error message
        System.out.println(SET_TEXT_COLOR_RED + message + RESET_TEXT_COLOR);
    }

    public static void printIndicator() {
        // print out arrows to prompt the user for input
        System.out.print(SET_BG_COLOR_BLUE + SET_TEXT_COLOR_BLACK + ">>>" + RESET_TEXT_COLOR + SET_BG_COLOR_DARK_GREY + " ");
    }

    public static String printPrePostHelp() {
        // print out the help message for the pre and post login clients
        return SET_BG_COLOR_WHITE + SET_TEXT_COLOR_BLACK +
                SET_TEXT_BOLD + "Commands: " + RESET_TEXT_COLOR + "\n" +
                "signup <username> <password> <email> - Register a new user\n" +
                "login <username> <password> - Login as an existing user\n" +
                "quit - Exit the program\n";
    }
}
