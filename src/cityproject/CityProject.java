/* CityProject.java  -- project class file with the project's main method 
 *
 *  * The CityProject software package creates a graph of cities in the Unitied 
 * States with links between the cities. Each city is a vertex in the graph.
 * Each link between cities is an edge in the graph. The data for the cities and
 * links are read into arrays from CSV data files, which should be in the 
 * project folder. (CSV files, can be created, read, and edited using MS Excel.)
 *
 * The package's main class is the CityProject class. Other classes include:
 * 
 *   * AjacencyNode - a node for a linked list of Cities.  Each City has a list 
 *     of adjacnt cities, created from the links data file. Each list node has 
 *     a destination City, distance data, and a pointer to the next node. 
 *
 *   * City extends Vertex - Each City is a Vertex with added properties.  
 *     Each City has a unique name, and X and Y coordinates for location on a 
 *     1500 by 900 JPanel.
 *
 *   * Edge - an edge in a graph, with a source, destination, and length.
 *   
 *   * CityMap extends JPanel - a map of the graph on a 1500 by 900 GUI JPanel.
 *
 *   * Vertex - each Vertex in a graph.
 * 
 * The main method in the CityProject class calls methods to reads City and Edge 
 * data from data files into arrays, set up the adjacency list in each instance 
 * of City, print a list of Vertex cities and their Edges, then draw a map of the graph.
 *
 * created for use by students in CSCI 211 at Community Colle of Philadelphia
 * copyright 2014 by C. herbert.  last edited Nov. 23, 2014 by C. Herbert
 */
package cityproject;

import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.io.*;

public class CityProject {

    // main metod for the project
    public static void main(String[] args) {

        City[] cities = new City[200]; //array of cities (Vertices) max = 200
        for (int i = 0; i < cities.length; i++) {
            cities[i] = new City();
        }

        Edge[] links = new Edge[2000];// array of links  (Edges)  max = 2000
        for (int i = 0; i < links.length; i++) {
            links[i] = new Edge();
        }

        int cityCount; //    actual number of cities
        int linkCount; //    actual number of links

        // load cities into an array from a datafile
        cityCount = readCities(cities);

        // load links into an array from a datafile
        linkCount = readLinks(links, cities);

        // create the adjacency list for each city based on the link array
        createAdjacencyLists(cityCount, cities, linkCount, links);

        // print adjacency lists for all cities
        PrintAdjacencyLists(cityCount, cities);

        // instatiate a new scrollable map of the cities and links        
        DrawScrollableMap(cityCount, cities, linkCount, links);

// ***********************BEGIN GEORGE'S CODE**********************************        
        Scanner qwerty = new Scanner(System.in);

        String source;
        String destination;
        City sourceCity = new City();
        City destinationCity = new City();

        //get the source and destination from the user
        System.out.println("\n\nHello, and welcome to CSCI-211 Final Project");
        System.out.println("This program will fail if you mistype.  My assignment was to implement\n"+
                           "dijkstra's algorithm, not to design for edge cases or ease of use.\n"+
                           "I recommend copying and pasting the city and state names from the list above.");
        
        System.out.print("Type the full city and state you wish to start from: ");
        source = qwerty.nextLine();
        System.out.print("\nType the full city and state you wish to go to: ");
        destination = qwerty.nextLine();

        // NOTE: this will fail if the user mistypes. This assignment
        //was to implement dijkstra's algorithm, not to design for edge cases.
        
        
        //get the cities which match the source and destination the user entered.
        for (City citie : cities) {
            if (source.equals(citie.getName())) {
                sourceCity = citie;
            }
            if (destination.equals(citie.getName())) {
                destinationCity = citie;
            }
        }

        //call dijkstra's algorithm which does all the work
        dijkstra(cities, sourceCity, destinationCity, links, cityCount);

// ***********************END GEORGE'S CODE**********************************      
    } // end main
    //************************************************************************

    /**
     * *************************************************************************
     * readCities() reads city data into an array from a data file. The data
     * file should be a CSV file with the city name and coordinates. City names
     * should be unique. The coordinates are x and y coordinates for drawing on
     * a 1500 by 900 JPanel or JPanel. Each City will be a vertex in a graph of
     * the cities. The array reference is a parameter. The methods returns the
     * number of array elements used (the number of cities).                              
    **************************************************************************
     */
    static int readCities(City[] cities) {

        int count = 0; // number of cities[] elements with data

        String[][] cityData = new String[123][3]; // holds data from the city file
        String delimiter = ",";                   // the delimiter in a csv file
        String line;                              // a String to hold each line from the file

        String fileName = "cities.csv";           // the file to be opened  

        try {
            // Create a Scanner to read the input from a file
            Scanner infile;
            infile = new Scanner(new File(fileName));

            /* This while loop reads lines of text into an array. it uses a Scanner class 
             * boolean function hasNextLine() to see if there is another line in the file.
             */
            while (infile.hasNextLine()) {
                // read the line 
                line = infile.nextLine();

                // split the line into separate objects and store them in a row in the array
                cityData[count] = line.split(delimiter);

                // read data from the 2D array into an array of City objects
                cities[count].setName(cityData[count][0]);
                cities[count].setX(Integer.parseInt(cityData[count][1]));
                cities[count].setY(Integer.parseInt(cityData[count][2]));

                count++;
            }// end while

            infile.close();

        } catch (FileNotFoundException e) {
            // error message dialog box with custom title and the error icon
            JOptionPane.showMessageDialog(null, "File I/O error:" + fileName, "File Error", JOptionPane.ERROR_MESSAGE);
        }
        return count;

    } // end loadCities() ******************************************************

    /**
     * *************************************************************************
     * readLinks() reads link data into an array from a data file. The data file
     * should be a CSV file with source city name, destination city name, and
     * length of the link on each line. The source and destination names should
     * match names in the cities data file and array. The array reference is a
     * parameter. The methods returns the number of array elements used (the
     * number of links).
    **************************************************************************
     */
    static int readLinks(Edge[] links, City[] cities) {
        int count = 0; // number of links[] elements with data

        String[][] CityLinkArray = new String[695][3]; // holds data from the link file
        String delimiter = ",";                       // the delimiter in a csv file
        String line;				      // a String to hold each line from the file

        String fileName = "links.csv";                // the file to be opened  

        try {
            // Create a Scanner to read the input from a file
            Scanner infile = new Scanner(new File(fileName));

            /* This while loop reads lines of text into an array. it uses a Scanner class 
             * boolean function hasNextLine() to see if there another line in the file.
             */
            while (infile.hasNextLine()) {
                // read the line 
                line = infile.nextLine();

                // split the line into separate objects and store them in a row in the array
                CityLinkArray[count] = line.split(delimiter);

                // read link data from the 2D array into an array of Edge objects
                // set source to vertex with city name in source column
                links[count].setSource(findCity(cities, CityLinkArray[count][0]));
                // set destination to vertex with city name in destination column
                links[count].setDestination(findCity(cities, CityLinkArray[count][1]));
                //set length to integer valuein length column
                links[count].setLength(Integer.parseInt(CityLinkArray[count][2]));

                count++;

            }// end while

        } catch (FileNotFoundException e) {
            // error message dialog box with custom title and the error icon
            JOptionPane.showMessageDialog(null, "File I/O error:" + fileName, "File Error", JOptionPane.ERROR_MESSAGE);
        }
        return count;
    } // end loadLinks() *******************************************************

    /**
     * *************************************************************************
     * findCity() returns a reference to the City Object for the City with the
     * specified name in the specified array . City names should be unique 
    **************************************************************************
     */
    static City findCity(City[] cities, String n) {
        int index = 0;  // loop counter
        // go through the cities array until the name is found
        // the name will be in the list

        while (cities[index].getName().compareTo(n) != 0) {

            index++;
        }// end while()
        return cities[index];

    } // end findCity() ********************************************************

    /**
     * *************************************************************************
     * Create AdjacencyLists() creates an adjacency list for each city. Each
     * adjacency list is in alphabetical order.
    **************************************************************************
     */
    static void createAdjacencyLists(int cityCount, City[] cities, int linkCount, Edge[] links) {

        AdjacencyNode temp = new AdjacencyNode();

        // iterate city array
        for (int i = 0; i < cityCount; i++) {

            /* Iterate the link array in reverse order.
             each new link will be placed at the head of the list
             resulting in a list in alphabetical order.*/
            for (int j = linkCount - 1; j >= 0; j--) {

                /* if the currentl link's source is the current city, then
                 create a node for the link and inseert it into the 
                 adjancency list as the new head of the list. */
                if (links[j].getSource() == cities[i]) {

                    // temporarily store the current value of the list's head
                    temp = cities[i].getAdjacencyListHead();

                    //create a new node
                    AdjacencyNode newNode = new AdjacencyNode();

                    // add city and distance data
                    newNode.setCity(links[j].getDestination());
                    newNode.setDistance(links[j].getLength());

                    // point newNode to the previous list head
                    newNode.setNext(temp);

                    // set the new head of the list to newNode
                    cities[i].setAdjacencyListHead(newNode);

                }  // end if
            } // end for j  (iterate links)
        } // end for i (iterate cities)

    } // end createAdjacencyLists() ********************************************

    /**
     * *************************************************************************
     * PrintAdjacencyLists() print the adjacency list for each city. The set of
     * lists is alphabetical, as is each list.
    **************************************************************************
     */
    static void PrintAdjacencyLists(int cityCount, City[] cities) {

        System.out.println("List of Edges in the Graph of Cities by Source City");
        // iterate array of cities
        for (int i = 0; i < cityCount; i++) {

            // set current to adjacency list for this city    
            AdjacencyNode current = cities[i].getAdjacencyListHead();

            // print city name
            System.out.println("\nFrom " + cities[i].getName());

            // iterate adjacency list and print each node's data
            while (current != null) {
                System.out.println("\t" + current.toString());
                current = current.getNext();
            } // end while (current != null) 

        }   // end for i 

    } // end PrintAdjacencyLists()**********************************************

    /**
     * *************************************************************************
     * DrawScrollableMap() creates a frame , then places an instance of CityMap
     * on the frame in a ScrollPane.
    **************************************************************************
     */
    static void DrawScrollableMap(int cCount, City[] c, int lCount, Edge[] l) {

        // create a frame for the map
        JFrame mapFrame = new JFrame();

        // set the frame's properties
        mapFrame.setTitle("Selected U.S. Cities");
        mapFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mapFrame.setLayout(new BorderLayout());
        mapFrame.setSize(1200, 600);

        // create an instance of CityMap
        CityMap map = new CityMap(cCount, c, lCount, l);

        // put the map on a ScrollPane in the frame
        mapFrame.add(new JScrollPane(map), BorderLayout.CENTER);
        // show the map
        mapFrame.setVisible(true);

    } // end DrawScrollablemap() ***********************************************

    static void dijkstra(City[] cities, City source, City destination, Edge[] links, int howmanycities) {//begin dijkstra

        AdjacencyNode nextAdjacent = new AdjacencyNode(); //starts at the head of the linked list of adacent cities and evaluates every node in the list
        City[] U = new City[howmanycities]; //the set of unvisited cities
        City temp = new City(); //a placeholder for printing out the final results
        Edge currentLink = new Edge(); //the link used for comparing best distance
        int isUEmpty = 1; //exit variable for the while loop
        int index = 0; // array index indicator for loading the ShortestPathStacK

        //load the unvisisted array
        System.arraycopy(cities, 0, U, 0, howmanycities);

        //set currentVertex to be the source, set bestDistance to be zero, leave immediatePredecessor null
        City currentVertex = source;
        currentVertex.setBestDistance(0);

        //this loop will continue to execute until every City has been visisted
        while (isUEmpty > 0) {//begin while U is not empty
            nextAdjacent = currentVertex.getAdjacencyListHead();

            //iterate through every city that is adjacent to the currentVertex
            //terminates when we reach the end of the linked list of AdjacenyNodes
            while (nextAdjacent != null) {
                //find the edge which goes from currentVertex to current nextAdjacent
                for (Edge link : links) {
                    if ((link.getSource() == currentVertex) && link.getDestination() == nextAdjacent.getCity()) {
                        currentLink = link;
                    }
                }

                // if the new path through currentVertex  < the current best distance, then we swap them and update immediatePredecessor
                if (currentLink.getLength() + currentVertex.getBestDistance() < nextAdjacent.getCity().getBestDistance()) {
                    nextAdjacent.getCity().setBestDistance(currentLink.getLength() + currentVertex.getBestDistance());
                    nextAdjacent.getCity().setImmediatePredecessor(currentVertex);
                }
                //set nextAdjacent to next Node in the list. if the next Node is null then the loop terminates.
                nextAdjacent = nextAdjacent.getNext();
            }//end find current edge
            
            
            // all adjecent cities to currentVertex has been evaluated, so set currentVertex.visited to be true 
            currentVertex.setVisited(true);

            
            
            // set the next currentVertex to be the city which has the shortest bestDistance AND has not already been visisted
            City nextSource = new City();
            for (City U1 : U) {
                if (U1.getBestDistance() < nextSource.getBestDistance() && U1.getVisited() == false) {
                    nextSource = U1;
                }
            }
            currentVertex = nextSource;
            
            //the loop will continue to run if there is at least 1 city that has not been visisted.
            isUEmpty = 0;
            for (City U1 : U) {
                if (U1.getVisited() == false) {
                    isUEmpty++;
                }
            }

        }//end while U is not empty

        
        //place all the cities into a stack starting with the destination and ending with the source
        temp = destination;

        ShortestPathStack resultStack = new ShortestPathStack();
        ShortestPathStackElement[] elements = new ShortestPathStackElement[400];

        // this loop loads the stack, starting at the destination and finishing one city shy of the source
        while (temp != source) {
            elements[index] = new ShortestPathStackElement();
            elements[index].setElement(temp);
            resultStack.push(elements[index]);
            temp = temp.getImmediatePredecessor();
            index++;
        }
        //now we add the final city, which is the source
        elements[index] = new ShortestPathStackElement();
        elements[index].setElement(temp);
        resultStack.push(elements[index]);

        
        
        //Print out the shortest path to the user by popping the stack until the stack is empty.
        System.out.println("\n\nThe shortest path from " + source.getName() + " to " + destination.getName() + " is as follows: ");
        System.out.println("you start at " + resultStack.pop().getElement().getName());
        while (resultStack.getSize() > 0) {
            temp = resultStack.pop().getElement();
            System.out.println("then go to " + temp.getName() + " for a total mileage of: " + temp.getBestDistance());
        }

    }//end dijkstra

} // end class cityProject *****************************************************
