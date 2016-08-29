package cityproject;

/**
 *
 * @author george oster
 */
public class ShortestPathStackElement {
    
City city;
ShortestPathStackElement next = null;


public ShortestPathStackElement()
 {
  city = new City();
 }
    
public ShortestPathStackElement(City c)
{
 city = c;
}
    
public void setElement(City c)
 {
 city = c;
 }
    
public City getElement()
 {
 return city;
 }  

public void setNext(ShortestPathStackElement se)
 {
 next = se;
 }

public ShortestPathStackElement getNext()
 {
 return next;
 }


    
}
