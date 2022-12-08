import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

// importing libraries for scanning input and opening files
public class TSM {
    // main method
    public static void main(String[] args) throws FileNotFoundException {

        //getting user input and file inputs in these variables
        int numbCities;
        int times=10;
        int bestDis;   //best distance index
        Scanner scan = new Scanner(System.in); // scanner

        System.out.println("Enter the number of Cities!");
        numbCities = scan.nextInt();    // getting user input
        // variables to store distance and cities objects and paths
        double[] distance = new double[numbCities];
        Cities[] city = new Cities[numbCities];
        Cities [] bestSolution = new Cities[numbCities+1];
        Cities [] solution = new Cities[numbCities + 1];
        Cities[][] closePathSolution = new Cities[numbCities][numbCities + 1];
        Cities[][] farPathSolution = new Cities[numbCities][numbCities + 1];

         /*file object for file path                 !!!#>HERE IS THE PLACE TO CHANGE PATH!!change manually>##*/
        File file = new File("C:\\Users\\joesh\\Downloads\\aiweek0\\CW1\\TestFiles\\test4.txt");

        reader(city,file); //file reading function
        //start of algorithm
        long sPoint = System.nanoTime();    // taking note of time
        //best for search for each point as starting point
        for (int start = 0; start < numbCities; start++) {

            city[start].Visited(city[start]); //start point as visited

            closePathSolution[start][0] = city[start];  //pushing into closePathSolution

            Best(start, city, 1, closePathSolution[start], numbCities); // finds the best using recursion

            closePathSolution[start][numbCities] = city[start];// closing circle of the path
            for (int i = 0; i < numbCities; i++) {  //loop to set paths to not visited
                city[i].visited = false;
            }
        }
        for (int end = 0; end < numbCities; end++) { // loop for finding the farthest distance path
            city[end].Visited(city[end]);   //start point as visited
            farPathSolution[end][0] = city[end];    //pushing into farPathSolution
            far(end, city, 1, farPathSolution[end], numbCities); // finds the path using recursion
            farPathSolution[end][numbCities] = city[end]; // closing circle of the path
            for (int i = 0; i < numbCities; i++) { //loop to set paths to not visited
                city[i].visited = false;
            }
        }
        for (int star = 0; star < numbCities; star++) { // loop to find distance of each path
            distance[star] = totalDistance(numbCities,closePathSolution[star]); //function finds distance of the path
        }

        bestDis = 0;
        for (int i = 0; i < numbCities; i++) {  // loop to find the shortest path
            if (distance[bestDis] > distance[i]) {
                bestDis = i;
            }
        }

        pusher(numbCities,bestSolution,closePathSolution[bestDis]); //function copies/pushes content from path to another
        int c ; //counter
        int flag =0; // used as a condition variable(flag/signal)
        while (flag <2) {   //loop to check on both sets of paths closePathSolution and farPathSolution
            c =0;
            while (c < times) { //this loop runs two op for c amount of times for better results
                for (int j = 0; j < numbCities; j++) {
                    if (flag == 0)  //checks closePathSolution
                        two_Opt(numbCities, closePathSolution[j], totalDistance(numbCities, bestSolution), bestSolution);
                    else    //checks farPathSolution
                        two_Opt(numbCities, farPathSolution[j], totalDistance(numbCities, bestSolution), bestSolution);

                }
                c++;
            }
            pusher(numbCities, solution, bestSolution); //copies the bestSolution so far
            for (int i = 0; i<  times;i++){ //optimizing the bestSolution so far
                two_Opt(numbCities, solution, totalDistance(numbCities, bestSolution), bestSolution);
            }
            flag++;
        }
        //printing the best path
        System.out.printf("Best path :[ ");
        for (int i = 0; i <numbCities+1 ; i++) {
            System.out.printf("%d ",bestSolution[i].city_Id);
        }
        System.out.printf("]\n");
        System.out.printf("Distance: %.4f\n",totalDistance(numbCities,bestSolution));
        //calculating time take
        long ePoint = System.nanoTime();
        long executionTime = ePoint - sPoint;
        System.out.println("Execution time: " + executionTime + " Nanosecond");//printing time taken
        executionTime= TimeUnit.MILLISECONDS.convert(executionTime, TimeUnit.NANOSECONDS);
        System.out.println("Execution time: " + executionTime + " Millisecond");//printing time taken


    }
    /* two_Opt function, numCities is the number of cities, destination is the source of cities
    and where cities are swapped,bestOptDistance is the best distance so far, path is the final path*/
    public static void two_Opt(int numbCities, Cities[] destination, double bestOptDistance, Cities[] Path)
    {
        Cities tmp;
        int swapped = 1;
        double distOpt;
        while (swapped != 0) { //loop runs till swapping is done
            swapped = 0;
            for (int i = 1; i < destination.length - 2; i++) { // loop to swap first(i) city
                for (int j = i + 1; j < destination.length - 1; j++) { //loop to swap other(j) cities with first(i)
                    //condition checks whether the swapping the cities will give a shorter distance between cities
                    if (destination[i].Find_Distance(destination[i - 1].city_X_Location, destination[i - 1].city_Y_Location) + destination[j + 1].Find_Distance(destination[j].city_X_Location, destination[j].city_Y_Location) >=
                            destination[i].Find_Distance(destination[j + 1].city_X_Location, destination[j + 1].city_Y_Location) + destination[i - 1].Find_Distance(destination[j].city_X_Location, destination[j].city_Y_Location)) {
                        tmp = destination[i];       /* if condition is true then it swaps the cities*/
                        destination[i] = destination[j];
                        destination[j] = tmp;
                    }
                    distOpt=totalDistance(numbCities, destination); //checking new distance
                    if (distOpt < bestOptDistance) { //if the swap make the path more optimal it will push the new path into bestSolution
                        pusher(numbCities,Path,destination);//pushing into bestSolution
                        bestOptDistance = distOpt;  //replaces bestDistance
                        swapped++;
                    }
                }
            }
        }
    }

    /* Best for search is an algorithm that chooses the closest city from each position */
    /* start is the index of the current city, cities city is the list of cities, cities
    closest is the closest city, Index is a counter used to run the recursion with numbCities */
    public static void Best(int start, Cities[] city, int index, Cities[] closest, int numbCities) {

        int path = 0;  //counter/index
        int next;       //closest city index
        double[] dis = new double[numbCities];

        while (path < numbCities) { //loop to find distance between start city and other

            if (!city[path].visited) { //checks in the city is visited
                dis[path] = city[start].Find_Distance(city[path].city_X_Location, city[path].city_Y_Location);
            } else              // if visited it will put distance as -1
                dis[path] = -1;
            path++;
        }
        path = 0;
        next = 0;
        while (dis[path] == -1) //makes sure that first index is not a visited city
            path++;
        while (next < numbCities) { // finds the shortest distance away city from start city
            if (dis[path] > dis[next] && dis[path] != -1 && dis[next] != -1) {
                path = next;
            }
            next++;
        }
        closest[index] = city[path];    //pushes the city into closestPathSolution
        city[path].Visited(city[path]);
        index++;
        if (index < numbCities) //recursion condition
            Best(path, city, index, closest, numbCities);   //recursion
    }

    /* far for search is an algorithm that chooses the farthest city from each position */
    /* start is the index of the current city, cities city is the list of cities, cities*/
    /*farthest is the farthest city, Index is a counter used to run the recursion with numbCities */
    public static void far(int start, Cities[] city, int index, Cities[] farthest, int numbCities) {
        int path = 0; //counter/index
        int next;   //closest city index
        double[] dis = new double[numbCities];
        while (path < numbCities) { //loop to find distance between start city and other
            if (!city[path].visited) {  //checks in the city is visited
                dis[path] = city[start].Find_Distance(city[path].city_X_Location, city[path].city_Y_Location);
            } else  // if visited it will put distance as -1
                dis[path] = -1;
            path++;
        }
        path = 0;
        next = 0;
        while (dis[path] == -1) //makes sure that first index is not a visited city
            path++;
        while (next < numbCities) { // finds the farthest distance away city from start city
            if (dis[path] < dis[next] && dis[path] != -1 && dis[next] != -1) {
                path = next;
            }
            next++;
        }
        farthest[index] = city[path];   //pushes the city into farthestPathSolution
        city[path].Visited(city[path]);
        index++;
        // recursion counter/limiter
        if (index < numbCities)
            far(path, city, index, farthest, numbCities);

    }

    /*  Function calculates total distance*/
    public static double totalDistance(int numbCities, Cities []PathSolution) {
        double sum = 0;
            for (int i = 0; i < numbCities + 1; i++) {
                if (i + 1 < numbCities + 1) {
                    sum += PathSolution[i].Find_Distance(PathSolution[i + 1].city_X_Location, PathSolution[i + 1].city_Y_Location);
                }
            }
            return sum;
    }
    /* reads file and inputs the data into object*/
    public static void reader(Cities[] toInput,File file) throws FileNotFoundException { //throws exception for not finding file
        int xAxis, yAxis, cityNumb;
        // try to scan test files
        try {

            Scanner citySc = new Scanner( file );

            while (citySc.hasNextLine()) {

                cityNumb = citySc.nextInt();
                xAxis = citySc.nextInt();
                yAxis = citySc.nextInt();
                toInput[cityNumb - 1] = new Cities(cityNumb, xAxis, yAxis);    // initializing city

            }
            citySc.close(); //closing city
        } catch (FileNotFoundException e) { //catches errors
            e.printStackTrace();
        }
    }
    /*function copies from one to another city list*/
    public static void pusher(int numbCities, Cities[] a,Cities [] b)
    {
        for (int i = 0; i <numbCities+1 ; i++) {
                a[i]=b[i];
        }
    }
}
