package it.polimi.ingsw;

import it.polimi.ingsw.client.cli.Cli;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Launch the CLI with ip and port chosen
 */
public class ClientAppCli {
    public static void main(String[] args) {
        PrintStream ps = new PrintStream(new FileOutputStream(FileDescriptor.out), true, StandardCharsets.UTF_8);
        System.out.println("Eriantys Client | Welcome!");

        String ip;
        int port = 0;

        Scanner scanner = new Scanner(System.in);
        System.out.println("Insert ip server:");
        ip = scanner.nextLine();
        while(ip.length() < 1) {
            System.out.println("Ip not valid, try again:");
            ip = scanner.nextLine();
        }
        System.out.println("Insert port: ");
        String read = scanner.nextLine();
        while (read.length() < 2) {
            System.out.println("Port not valid, try again: ");
            read = scanner.nextLine();
        }
        try {
            port = Integer.parseInt(read);
        } catch (InputMismatchException e) {
            System.err.println("Numeric format requested, application will now close...");
            System.exit(0);
        } catch (NumberFormatException e) {
            System.err.println("Numeric format requested, application will now close...");
            System.exit(0);
        }

        Cli client = new Cli(ip, port);

        try {
            client.run();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
