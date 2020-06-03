package Model;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.TreeSet;

public class JURL {
    private static TreeSet<Request> list;

    public static void main(String[] args) {
        // loading set of requests
        // validating url
        Request current;
        try {
            current = new Request(args[0].substring(0, 6) == "http://" ? args[0] : "http://" + args[0]);
        } catch (MalformedURLException e) {
            System.out.println("Malformed Url, please enter url in correct format");
            e.printStackTrace();
            Thread.currentThread().stop();
        } catch (IOException e) {
            System.out.println("Enter Proper Url!");
            e.printStackTrace();
            Thread.currentThread().stop();
        }
        // getting other options
        for (int i = 1; i < args.length; i++) {
            String arg = args[i];
            if (arg.charAt(0) == '-' && arg.charAt(1) == '-') {
                switch(arg.substring(2)){
                    case "--method":
                }

            } else if (arg.charAt(0) == '-') {

            } else {
                System.out.println("Unknown Argument/Option " + arg + " Entered!");
                Thread.currentThread().stop();
            }
        }
    }
}