import nearestNeigh.Category;
import nearestNeigh.Point;
import nearestNeigh.KDTreeNN;
import java.io.*;
import java.util.*;
import nearestNeigh.NaiveNN;
import nearestNeigh.NearestNeigh;


public static main(String[] args)
{

  long startTime = System . nanoTime ( ) ;
  // enter fucntion to test

  long endTime = System.nanoTime();

  double estimatedTime = ((double)(endTime − startTime)) / Math.pow(10, 9);
  System.out.println("time taken = " + estimatedTime + " sec");
}
