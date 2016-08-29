package cityproject;

/**
 * @author george oster
 */
public class ShortestPathStack {
    
ShortestPathStackElement head = new ShortestPathStackElement();
ShortestPathStackElement element;
int size;
    
public ShortestPathStack()
 {
  size = 0;
 }
    
public void push(ShortestPathStackElement se)      
 {
  element = se; 
  element.setNext(head);
  head = element;
  size++;   
 }
    
public ShortestPathStackElement pop()
{
element = head;
head = head.next;
size--;    
return element;
}    
    
public int getSize()
 {
  return size;
 }
 
public boolean isEmpty()
 {
 boolean yesorno = false;
 
 if (size == 0)
 {yesorno = true;}
 
 return yesorno;
 }
    
public void displayStack()
{
 System.out.println("here is the stack as it currently stands:");
 ShortestPathStackElement temp = head;
 
 while (temp.getElement().getName() != null)
  {
    System.out.println(temp.getElement().toString());
    temp = temp.getNext();
  }   
}
   
    
    
    
}
