package Model;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import com.google.gson.GsonBuilder;

public class JURL {
    private static FileSystemCollection<Request> requests; // collection of requests that must be static
    private static Folder<Request> root; // root folder of collection

    /**
     * load all saved requests from file
     */
    @SuppressWarnings("unchecked")
    public static void loadRequests() {
        // addressing savee file(!should be defined in constants!)
        File file = new File("./Data/JURL/REQUESTS.FS");
        if (file.exists()) {
            // if addressed file exists then read and load ist
            Object obj;
            // reading file
            try {
                FileInputStream fileStream = new FileInputStream(file);
                ObjectInputStream objectStream = new ObjectInputStream(fileStream);
                obj = objectStream.readObject();
                objectStream.close();
                fileStream.close();
                // validating file as request collections then loading to ram(!might not be
                // proper!)
                if ((obj instanceof FileSystemCollection<?>)
                        && ((FileSystemCollection<?>) obj).genericInstanceClass == Request.class) {
                    requests = (FileSystemCollection<Request>) obj;
                } else {
                    System.out.println("Saved request might be corrupted");
                    System.out.println(((FileSystemCollection<?>) obj).genericInstanceClass);
                    System.out.println(Request.class);
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        } else
            // use an empty file system collection for storing data
            requests = new FileSystemCollection<Request>("Requests", Request.class);
    }

    /**
     * saving changed version of request collection to file (!when actually an
     * instance of many instances of class is finished - need thread memory
     * management i think!) !!critical!!
     */
    public static void saveRequests() {
        // addressing savee file(!should be defined in constants!)
        File file = new File("./Data/JURL/REQUESTS.FS");
        try {
            new File("./Data/JURL").mkdirs();
            FileOutputStream fileStream = new FileOutputStream(file);
            ObjectOutputStream objectStream = new ObjectOutputStream(fileStream);
            objectStream.writeObject(requests);
            objectStream.close();
            fileStream.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // loading set of requests
        loadRequests();
        // root folder of request collection
        root = requests.getRoot();
        if (args.length == 0)
            return;
        // running one of the five main commands:
        // 1-create 2-remove 3-fire 4-list 5-http URL
        switch (args[0]) {
            case "create":
            // creating folders for gropuing saved requests
            {
                if (args.length > 1) {
                    try {
                        requests.createFolder(args[1]);
                        saveRequests();
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                } else
                    System.out.println("folder argument is missing!");
                break;
                // return;
            }
            case "remove":
            // removing saved requests or non-empty folders
            {
                try {
                    if (args.length > 2 && args[1] == "-f")
                        for (int i = 2; i < args.length; i++)
                            requests.removeFolder(args[i]);
                    else
                        for (int i = 1; i < args.length; i++)
                            requests.removeFile(args[i]);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                break;
                // return;
            }
            case "fire":
            // running one of saved requests
            {
                // implement addressing in fscollection and name validation
                // if folder(s)
                // if file(s)
                break;
                // return;
            }
            case "list":
            // listing a given folders request and
            {
                try {
                    ArrayList<Node> childs = requests.openFolder(args.length > 1 ? args[1] : "").getChilds();
                    if (childs != null)
                        for (Node child : childs)
                            if (child.isFolder)
                                // specifying folders with (Folder) tag
                                System.out.println(child.name + " (Folder)");
                            else
                                // specifuing request with their descriptions
                                System.out.println(child.name + " description");
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                break;
                // return;
            }
            default: {
                // creating(and running) a request
                Request current;
                try {
                    // validating url
                    current = new Request(args[0]);
                } catch (MalformedURLException e) {
                    System.out.println("malformed url, please enter url in correct format");
                    return;
                } catch (IOException e) {
                    System.out.println("enter a proper url and check your connection!");
                    return;
                }
                // listing all arguments in list
                ArrayList<String> arguments = new ArrayList<String>();
                for (String arg : args)
                    if (arg.equals(args[0]))
                        continue;
                    else
                        arguments.add(arg);
                // saving request before any other command
                if (arguments.contains("-S") || arguments.contains("--save")) {
                    int index;
                    if (arguments.contains("-S"))
                        index = arguments.indexOf("-S");
                    else
                        index = arguments.indexOf("--save");
                    try {
                        requests.createFile(arguments.get(++index), current);
                    } catch (IndexOutOfBoundsException e) {
                        System.out.println("name argument is missing!");
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }

                // giving query to url
                if (arguments.contains("-Q") || arguments.contains("--query")) {

                }

                // follow redirect
                if (arguments.contains("-f"))
                    current.setFollowRedirect(true);

                // getting other options
                Iterator<String> iter = arguments.iterator();
                while (iter.hasNext()) {
                    String arg = iter.next();
                    switch (arg) {
                        case "--header":
                        case "-H": {
                            if (!iter.hasNext()) {
                                System.out.println("headers arguments are not entered!");
                                return;
                            }
                            Map<String, List<String>> head = new HashMap<String, List<String>>();
                            arg = iter.next();
                            String[] input = arg.split(";");
                            for (String entry : input) {
                                String[] val = entry.split(":", -1);
                                // if header is a null string
                                if (val[0].equals("")) {
                                    System.out.println("header Key missing!");
                                    return;
                                }
                                // if header specifies no value data
                                if (val.length == 1) {
                                    System.out.println("header " + val[0] + " not specified by a value!");
                                    return;
                                }
                                // listing header key values
                                ArrayList<String> values = new ArrayList<String>();
                                for (int i = 1; i < val.length; i++)
                                    values.add(val[i]);
                                head.put(val[0], values);
                            }
                            current.setHeaders(head);
                            break;
                        }
                        case "--method":
                        case "-M": {
                            // if argument value is given correctly
                            if (!iter.hasNext()) {
                                System.out.println("method argument is missing!");
                                return;
                            }
                            try {
                                current.setMethod(iter.next());
                            } catch (ProtocolException e) {
                                System.out.println("unexpected method argument entered!");
                                return;
                            }
                            break;
                        }
                        // message body
                        case "--json": {
                            if (!iter.hasNext()) {
                                System.out.println("JSON string argument Missing!");
                                return;
                            }
                            current.setJSONBody(iter.next());
                            break;
                        }
                        case "--data":
                        case "-d": {
                            if (!iter.hasNext()) {
                                System.out.println("form data parameters missing!");
                                return;
                            }
                            String[] input = iter.next().split("&");
                            Map<String, String> param = new HashMap<String, String>();
                            for (String entry : input) {
                                String[] val = entry.split("=", 2);
                                // if header is a null string
                                if (val[0].equals("")) {
                                    System.out.println("header Key missing!");
                                    return;
                                }
                                // if header specifies no value data
                                if (val.length == 1) {
                                    System.out.println("header " + val[0] + " not specified!");
                                    return;
                                }
                                // setting header entry in data
                                param.put(val[0], val[1]);
                            }
                            current.setBodyParameters(param);
                            break;
                        }
                        case "--upload":
                        case "-U": {
                            if (!iter.hasNext()) {
                                System.out.println("no address entered for uploading");
                                return;
                            }
                            current.addBinFile(iter.next());
                            break;
                        }
                        //
                        case "--save":
                        case "-S":
                            break;
                        case "--query":
                        case "-Q":
                            break;
                        case "--output":
                        case "-O":
                            if (iter.hasNext()) {
                                current.setOutputPath(iter.next());
                            } else {
                                current.setOutputPath("./Data/JURL/OUTPUTS/output_" + (new Date().toString()));
                            }
                            break;
                        case "--help":
                        case "-h":
                            //
                            break;
                        case "-i":
                            break;
                        case "-f":
                            break;
                        default: {
                            System.out.println("unexpected command/argument " + arg + " entered!");
                            return;
                        }
                    }

                }

                // sending request
                try {
                    // writing if multipart stream builder id prepared
                    System.out.println(current.getConnection().getRequestMethod());
                    // current.writeBodyStream();
                    ByteArrayInputStream responseStream = new ByteArrayInputStream(current.sendRequest(/* 5000 */));
                    char[] reader = new char[responseStream.available()];
                    new InputStreamReader(responseStream).read(reader);
                    System.out.println(reader);
                } catch (UnknownHostException e) {
                    // TODO Auto-generated catch block
                    // e.printStackTrace();
                    System.out.println("Unfortunaltely can not resolve the Host!");
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    // e.printStackTrace();
                    try {
                        if (current.getConnection().getResponseCode() >= 500)
                            System.out.println("Server does not Provide Service");
                    } catch (IOException ioe) {
                        // TODO Auto-generated catch block
                        // e.printStackTrace();
                        System.out.println(ioe);
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    System.out.println("some thing unknown happened to connection!");
                }

                // showing header Responses
                if (arguments.contains("-i")) {
                    System.out.println(current.getResponseHeaders());
                }

                // try {
                // // current.getConnection().setRequestMethod("GET");
                // System.out.println(current.getConnection().getRequestMethod());
                // System.out.println(current.getConnection().getResponseMessage());
                // char[] reader = new
                // char[current.getConnection().getInputStream().available()];
                // new InputStreamReader(current.getConnection().getInputStream()).read(reader);
                // System.out.println(reader);
                // } catch (IOException e) {
                // // TODO Auto-generated catch block
                // e.printStackTrace();
                // }
                break;
            }
        }

        saveRequests();
    }
}