import HttpServlet.HttpTask;
import org.apache.commons.cli.*;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class httpserver {
    static final Logger logger = Logger.getLogger(httpserver.class);
    private static final String PROGRAM_NAME = "TomcatClone";
    private static final int DEFAULT_PORT = 8085;
    private static final HelpFormatter FORMATTER = new HelpFormatter();
    private static final int DEFAULT_THREAD_COUNT = 1;
    private static ExecutorService threadPool;
    private static CommandLine cmd = null;

    public static void main(String[] args) throws IOException {
        Options options = intializeOptions();
        CommandLineParser cmdParser = new DefaultParser();
        try {
            cmd = cmdParser.parse(options, args);
        } catch (ParseException e) {
            FORMATTER.printHelp(PROGRAM_NAME, options, true);
            return;
        }

        if (cmd.hasOption("help")) {
            FORMATTER.printHelp(PROGRAM_NAME, options, true);
            return;
        }

        int numberOfThreads = DEFAULT_THREAD_COUNT;
        if (cmd.hasOption("t"))
            numberOfThreads = Integer.parseInt(cmd.getOptionValue("t"));
        threadPool = Executors.newFixedThreadPool(numberOfThreads);

        startServer();
    }

    private static Options intializeOptions() {
        Options options = new Options();
        Option path = Option.builder("path").argName("Server Path").required().hasArg().desc("The path for the server content").build();
        options.addOption(path);
        Option port = Option.builder("p").longOpt("port").argName("Port").hasArg().desc("The port for the server to use").build();
        options.addOption(port);
        Option threads = Option.builder("t").longOpt("threads").argName("Threads number").hasArg().desc("The number of threads to process requests").build();
        options.addOption(threads);
        return options;
    }

    private static void startServer() throws IOException {
        String port = cmd.getOptionValue("p", String.valueOf(DEFAULT_PORT));
        int portToUse = Integer.parseInt(port);

        try (ServerSocket socket = new ServerSocket(portToUse);) {
            Socket s = null;
            logger.info("Server started on port " + portToUse);
            while ((s = socket.accept()) != null) {
                logger.info("New connection from " + s.getInetAddress().getHostAddress());
                threadPool.submit(new HttpTask(s, cmd));
            }
        }
    }
}
