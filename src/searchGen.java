import java.io.*;
import java.util.Random;


public class searchGen
{
    public static void main(String[] args)
    {
        try
        {
            File outFile = new File("testSearchK1to100.in");
            PrintWriter Writer = new PrintWriter(outFile);

            for ( int i = 1 ; i <= 100 ; i++)
            {
                Writer.println("S education 4 66 " + i );
            }

            Writer.close();
        }
        catch ( Exception e )
        {
            System.err.println(e.getMessage());
        }

    } // end of main()

}
