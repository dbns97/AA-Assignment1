package nearestNeigh;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is required to be implemented.  Naive approach implementation.
 *
 * @author Jeffrey, Youhan
 */
public class NaiveNN implements NearestNeigh{

    List<Point> index = new ArrayList<Point>();

    @Override
    public void buildIndex(List<Point> points) {
        // To be implemented.
        
        for ( int i = 0; i < points.size(); i++ )
        {
            index.add( points.get(i) ) ;
        }
    }

    @Override
    public List<Point> search(Point searchTerm, int k) 
    {
        List<Point> closestPoints = new ArrayList<Point>();
        double closestDist;
        int listIndex;
           
        while ( closestPoints.size() < k )
        {
            listIndex = 0;
            closestDist = searchTerm.distTo( index.get(0) );
            
            // search list for smallest distance 
            for( int i=0; i < index.size(); i++ )
            {
                if( searchTerm.cat == index.get(i).cat )
                {
                    if( searchTerm.distTo( index.get(i) ) < closestDist )
                    {
                        closestDist = searchTerm.distTo( index.get(i) );
                        listIndex = i;
                    }
                }
                
            }
            
            // add to return array
            closestPoints.add( index.get( listIndex ) );
            // remove the smallest from list it isnt compared anymore
            index.remove( listIndex );
            
        }
        
        // re add the removed items
        for( int i=0; i < closestPoints.size(); i++ )
        {
            index.add( closestPoints.get(i) );
        }
        
        return closestPoints;
    }

    @Override
    public boolean addPoint(Point point) {
        // To be implemented.
        for(int i=0; i < index.size(); i++)
        {
            if( point.equals(index.get(i)) )
            {
                return false;
            }
        }
        // if here means there isnt any points in index the same as point want to add
        // can now add to index
        index.add(point);
        return true;
    }

    @Override
    public boolean deletePoint(Point point) {
        // To be implemented.
        for(int i=0; i < index.size(); i++)
        {
            if( point.equals(index.get(i)) )
            {
                index.remove(i);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isPointIn(Point point) {
        // To be implemented.
        for(int i=0; i < index.size(); i++)
        {
            if( point.equals(index.get(i)) )
            {
                return true;
            }
        }
        return false;
    }

}
