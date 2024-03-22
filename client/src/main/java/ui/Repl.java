package ui;

import web.ServerFacade;

import static ui.EscapeSequences.*;

import java.util.Scanner;


public class Repl {
    private final PreLogin preLoginClient;
    private final PostLogin postLoginClient;

    public static void setState(State state) {
        Repl.state = state;
    }
    private static State state = State.SIGNEDOUT;

    public Repl(String serverUrl) {
        ServerFacade serverFacade = new ServerFacade(serverUrl);
        preLoginClient = new PreLogin(serverFacade);
        postLoginClient = new PostLogin(serverFacade);
    }

    public void run() {
        System.out.println(" Welcome to Chess. Type 'help' for a list of commands.");
        System.out.print(preLoginClient.help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                if (state == State.SIGNEDIN) {
                    result = postLoginClient.eval(line);
                    System.out.print(result);
                } else {
                    result = preLoginClient.eval(line);
                    if (result.equals("failure: 401")) {
                        System.out.println("Wrong username or password. Please try again.");
                    }
                    System.out.print(result);
                }
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println("Goodbye!");
        System.out.println();
    }


    private void printPrompt() {
        System.out.print("\n" + SET_BG_COLOR_BLACK + ">>> " + SET_TEXT_COLOR_WHITE);

    }


}
