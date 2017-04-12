
import nearestNeigh.Category;
import nearestNeigh.Point;
import nearestNeigh.KDTreeNN;
import java.io.*;
import java.util.*;
import nearestNeigh.NaiveNN;
import nearestNeigh.NearestNeigh;

/**
 * This is to be the main class when we run the program in file-based point.
 * It uses the data file to initialise the set of points.
 * It takes a command file as input and output into the output file.
 *
 * @author Jeffrey, Youhan
 */
public class testTimes
{


    /**
     * Name of class, used in error messages.
     */
    protected static final String progName = "NearestNeighFileBased";

    /**
     * Print help/usage message.
     */
    public static void usage(String progName)
    {
        System.err.println(progName + ": <approach> [data fileName] [command fileName] [output fileName]");
        System.err.println("<approach> = <naive | kdtree>");
        System.exit(1);
    } // end of usage

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {

      // start of program for run time
      double count = 1;

        // read command line arguments
        if (args.length != 4)
        {
            System.err.println("Incorrect number of arguments.");
            usage(progName);
        }

        // initialise search agent
        NearestNeigh agent = null;
        switch (args[0])
        {
            case "naive":
                agent = new NaiveNN();
                break;
            case "kdtree":
                agent = new KDTreeNN();
                break;
            default:
                System.err.println("Incorrect argument value.");
                usage(progName);
        }

        // read in data file of initial set of points
        String dataFileName = args[1];
        List<Point> points = new ArrayList<Point>();
        try
        {
            File dataFile = new File(dataFileName);
            Scanner scanner = new Scanner(dataFile);
            while (scanner.hasNext())
            {
                String id = scanner.next();
                Category cat = Point.parseCat(scanner.next());
                Point point = new Point(id, cat, scanner.nextDouble(), scanner.nextDouble());
                points.add(point);
            }
            scanner.close();
            agent.buildIndex(points);
        }
        catch (FileNotFoundException e)
        {
            System.err.println("Data file doesn't exist.");
            usage(progName);
        }

        String commandFileName = args[2];
        String outputFileName = args[3];
        File commandFile = new File(commandFileName);
        File outputFile = new File(outputFileName);

        File timeTakenFile = new File("../testing/timeTakenFile.out");

        // parse the commands in commandFile
        try
        {
            Scanner scanner = new Scanner(commandFile);
            PrintWriter writer = new PrintWriter(outputFile);

            PrintWriter timeWriter = new PrintWriter(timeTakenFile);
            long startTime = System.nanoTime ( ) ;
            int debug = 1;
            // operating commands
            while (scanner.hasNext())
            {
                String command = scanner.next();
                String id;
                Category cat;
                // remember lat = latitude (approximately correspond to x-coordinate)
                // remember lon = longitude (approximately correspond to y-coordinate)
                double lat;
                double lon;
                int k;
                Point point;

System.out.println(debug);
debug++;

                switch (command)
                {
                    // search
                    case "S":
                        cat = Point.parseCat(scanner.next());
                        lat = scanner.nextDouble();
                        lon = scanner.nextDouble();
                        k = scanner.nextInt();
                        point = new Point("searchTerm", cat, lat, lon);
                        List<Point> searchResult = agent.search(point, k);

                        long startSearchTime = System.nanoTime();

                        for (Point writePoint : searchResult) {
                            writer.println(writePoint.toString());
                        }

                        long endSearchTime = System.nanoTime();
                        double estimatedSearchTime = ((double)(endSearchTime - startSearchTime)) / Math.pow(10, 9);

                        timeWriter.println( estimatedSearchTime );

                        count++;
                        break;
                    // add
                    case "A":
                        id = scanner.next();
                        cat = Point.parseCat(scanner.next());
                        lat = scanner.nextDouble();
                        lon = scanner.nextDouble();
                        point = new Point(id, cat, lat, lon);

                        long startAddTime = System.nanoTime();

                        if (!agent.addPoint(point)) {
                            writer.println("Add point failed.");
                        }

                        long endAddTime = System.nanoTime();
                        double estimatedAddTime = ((double)(endAddTime - startAddTime)) / Math.pow(10, 9);
                        System.out.println("add time taken = " + estimatedAddTime + " sec");

                        timeWriter.println("add time taken = " + estimatedAddTime + " sec");
                        count++;
                        break;
                    // delete
                    case "D":
                        id = scanner.next();
                        cat = Point.parseCat(scanner.next());
                        lat = scanner.nextDouble();
                        lon = scanner.nextDouble();
                        point = new Point(id, cat, lat, lon);

                        long startDeleteTime = System.nanoTime();

                        if (!agent.deletePoint(point)) {
                            writer.println("Delete point failed.");
                        }

                        long endDeleteTime = System.nanoTime();
                        double estimatedDeleteTime = ((double)(endDeleteTime - startDeleteTime)) / Math.pow(10, 9);
                        System.out.println("delete time taken = " + estimatedDeleteTime + " sec");

                        timeWriter.println("delete time taken = " + estimatedDeleteTime + " sec");
                        count++;
                        break;
                    // check
                    case "C":
                        id = scanner.next();
                        cat = Point.parseCat(scanner.next());
                        lat = scanner.nextDouble();
                        lon = scanner.nextDouble();
                        point = new Point(id, cat, lat, lon);

                        long startCheckTime = System.nanoTime();

                        writer.println(agent.isPointIn(point));

                        long endCheckTime = System.nanoTime();
                        double estimatedCheckTime = ((double)(endCheckTime - startCheckTime)) / Math.pow(10, 9);
                        System.out.println("check time taken = " + estimatedCheckTime + " sec");

                        timeWriter.println("check time taken = " + estimatedCheckTime + " sec");
                        count++;
                        break;
                    default:
                        System.err.println("Unknown command.");
                        System.err.println(command + " " + scanner.nextLine());
                }
            }
            long endTime = System.nanoTime();

            scanner.close();
            writer.close();

            // end time of programe


            double estimatedTime = ((double)(endTime - startTime)) / Math.pow(10, 9);
            System.out.println("Total time taken = " + estimatedTime + " sec");

            timeWriter.println("Total time taken = " + estimatedTime + " sec\n");
            timeWriter.println("average time taken :     \n" + (estimatedTime/count));

            timeWriter.close();
        }
        catch (FileNotFoundException e)
        {
            System.err.println("Command file doesn't exist.");
            usage(progName);
        }



    }
}
