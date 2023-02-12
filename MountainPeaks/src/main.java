import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;


/**
 * This file can receive data from file (Elevation.txt), generate a two-dimensional array with that data, and do the specified function.
 * @author Yash Patel , student id: 000842226
 *
 */

public class main {

    private static int rows; // rows of the 2D int array
    private static int cols; // columns of the 2D int array
    private static int minPeak; // Minimum Peak Height of the 2D int array
    private static int radius;  // exclusion radius of the 2D array
    private static int[][] array; // the array use to store the elevation data
    private static double minDistanceWithinPeaks; // the minimum distance of the closest peaks



    public static void main(String[] args) {

        final String FILENAME = "src/ELEVATIONS.txt";
        try {
            Scanner fileInput = new Scanner(new File(FILENAME));
            rows = fileInput.nextInt();
            cols = fileInput.nextInt();
            minPeak = fileInput.nextInt();
            radius = fileInput.nextInt();
            array = new int[rows][cols]; // 2d array

            for (int row = 0; row<array.length;row++)
                for(int col=0; col < array[row].length;col++)
                    array[row][col] = fileInput.nextInt();

            fileInput.close();
        }
        catch (FileNotFoundException ex)
        {
            System.out.println("File not Found");
            System.exit(0);
        }
        // Start time of the program
        long startTime = System.nanoTime();

        //Using single method for each question
        // Answer1
        int[] mostOccured = FindLowest(array);
//        int moTimes = flRepeated(array);
        // Answer2
        Peak[] peaks = FindLocalPeak(array, minPeak, radius);
        // Answer3
        Peak[] closestPeaks = FindClosestLocalPeak(peaks);
        // Answer4
        int[] commonestEle = findCommonestEve(array);

        // end time of the program
        long endTime = System.nanoTime();
        long totalTime = (endTime - startTime) / 1000 ;
        System.out.println("\nMy time to solve the entire problem: " + totalTime);


        System.out.println("Part 1 answer: \nThe lowest value in the data set is: " + mostOccured[0] + " " +
                "and it appears " + mostOccured[1] + " times.");
        System.out.println("Part 2 answer: \nThe total number of peaks is : " + peaks.length);
        for (int i=0;i<peaks.length;i++)
        {
            System.out.println("Peak number = " + i + ": Row: " + peaks[i].getRow() + ", Column: "+ peaks[i].getCol()+ ", peak: "+ peaks[i].getValue());
        }
        System.out.println("\nPart 3 answer: \nThe closest peaks are at Row: " + closestPeaks[0].getRow() + ", Column: " +
                closestPeaks[0].getCol() + " elevation = " + closestPeaks[0].getValue() + " and  Row: " +
                closestPeaks[1].getRow() + ", Column: " + closestPeaks[1].getCol() + " elevation = " + closestPeaks[1].getValue());
        // using printf as it's easy to round the distance into .2 decimal
        System.out.printf("The minimum distance between two peaks =  %.2f m\n", minDistanceWithinPeaks);

        System.out.println("\nPart 4 answer: \nThe most common number in the data is:  " + commonestEle[0] + " " +
                "it occurs '" + commonestEle[1] + "' times");
    }

    /**
     * This method will find the lowest value from the data file and number of times it has occured.
     * @param intArray 2d array where data is stored
     * @return return the lowest value and number of times it has occured.
     */
    private static int[] FindLowest(int[][] intArray) {

        int min = rows*cols; // using row*cols as the min number
        int[] lowest = new int[min];
        // find the lowest value in the data set and count the frequency of it
        for (int row = 0; row < intArray.length; row++) {
            for (int col = 0; col < intArray.length; col++) {
                if(intArray[row][col] <= min){
                    min = intArray[row][col];
                    lowest[min] ++;
                }
            }
        }
        return new int[]{min, lowest[min]};
    }

//    /**
//     * This method will find the number of times the lowest value has occured.
//     * @param intArray 2d array where data is stored
//     * @return return the number of times the lowest value has occured.
//     */
//    private static int flRepeated(int [][] intArray)
//    {
//        int min = 111300;
//        int[] lowest = new int[min];
//        // find the lowest value in the data set and count the frequency of it
//        for (int row = 0; row < rows; row++) {
//            for (int col = 0; col < cols; col++) {
//                if(intArray[row][col] <= min){
//                    lowest[min] ++;
//                }
//            }
//        }
//        return lowest[min];
//
//    }

    /**
     * the method will find the local peak greater than the 'minimum height' given in the file
     * @param array array where the file data is stored
     * @param minPeakNum minimum peak given in the elevation file.
     * @param radius the radius of the array(given in the elevation file)
     * @return Will return an array with all the peak, and it's position(rows, cols)
     */
    private static Peak[] FindLocalPeak(int[][] array, int minPeakNum,int radius) {
        int counter = 0; // count the number of peaks
        boolean flag; // check if that's a peak
        Peak[] peaks = new Peak[rows*cols]; // a new peak array used to store local peaks(rows * cols)
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if(array[row][col] >= minPeakNum){
                    if(row >= radius && row < rows-radius && col>= radius && col< cols-radius){
                        flag = true;
                        for(int i= row-radius; ( i<= row + radius) && flag; i++){
                            for(int j= col-radius; ( j<= col+radius) && flag; j++){
                                if(i != row || j != col){ // exclude the value itself
                                    if(array[row][col] <= array[i][j]){
                                        flag = false;
                                    }}}}
                        // Once we have the flag false it will only create peak array which are within the given radius.
                        if(flag){ // find the peak and store it into the array
                            Peak peak = new Peak(array[row][col], row, col);
                            peaks[counter] = peak;
                            counter++;
                        }
                    }
                }
            }
        }
        peaks = Arrays.copyOf(peaks,counter);
        return peaks;
    }

    /**
     * The method will find the closest local peak and at what position it has occured.
     * @param peaks the peak array
     * @return return the peak with it's postions and
     */

    private static Peak[] FindClosestLocalPeak(Peak[] peaks) {
        minDistanceWithinPeaks = minPeak; // use the minimum height to compare the distance between two peaks.
        Peak peak1 = null, peak2 = null;
        for (int i = 0; i < peaks.length-1; i++) {
            for (int j = i+1 ; j < peaks.length; j++) {
                // use formula to calculate the distance between any two local peaks
//                double distance = Math.sqrt(Math.sqrt((peaks[i].getRow()-peaks[j].getRow())))+
//                        Math.sqrt((peaks[i].getColumn()-peaks[j].getColumn()));
                // using 'peak' class to get the peak rows and cols.
                double distance = Math.sqrt((peaks[i].getRow()-peaks[j].getRow()) * (peaks[i].getRow()-peaks[j].getRow())+
                        (peaks[i].getCol()-peaks[j].getCol()) * (peaks[i].getCol()-peaks[j].getCol()));

                // get the minimum distance and store the pair of peaks
                if(distance <= minDistanceWithinPeaks){
                    // minimum distance is static so i could use this while printing in the output
                    minDistanceWithinPeaks = distance;
                    // Will get the closest peak and store the value in the peak object
                    peak1 = peaks[i];
                    peak2 = peaks[j];

                }
            }
        }
        // return both the peaks
        return new Peak[]{peak1, peak2};
    }

    /**
     * Finding the most occured value in the data and number of times it has occured
     * @param array the file data which we will be using
     * @return  return both the most common value and how many times the value has occured
     */
    private static int[] findCommonestEve(int[][] array) {
        // create a 1 dimension CommonArray that would be easy to find the most appear number from the elevation file
        int[] CommonArray = new int[rows * cols];
        // store the value in the CommonArray with all numbers from the rows and cols
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                // turn the 2d array into 1d array
                CommonArray[array[row][col]]++;
            }
        }
        // find the number appears most frequently
        int numberOfTimes = 0; // maximum times appearing
        int MostOccuredNumber = 0; // used to store the value that frequently occurred
        for (int i = 0; i < CommonArray.length; i++) {
            if (CommonArray[i] > numberOfTimes) {
                numberOfTimes = CommonArray[i];
                MostOccuredNumber = i;
            }
        }
        return new int[]{MostOccuredNumber, numberOfTimes};
    }
}
