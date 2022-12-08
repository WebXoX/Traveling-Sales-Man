public class Cities {       //class to store city data
    int city_Id;            //city id
    double city_X_Location; //x position
    double city_Y_Location; //y position
    boolean  visited ;  //visited or not
    Cities()    //default constructor
    {
        city_Id =0;
        city_X_Location = 0;
        city_Y_Location = 0;
        visited= false;
    }
    Cities(int Id, double X , double Y  )   //constructor that fills the data
    {
        this.city_Id =Id;
        this.city_X_Location = X;
        this.city_Y_Location = Y;
        this.visited= false;

    }

    public void Visited(Cities c) //function to change visited to true
    {
        c.visited=true;
    }
    /*returns x and y location respectively*/
    public double Get_X()
    {
        return(city_X_Location);
    }
    public double Get_Y()
    {
        return(city_Y_Location);
    }

    public void PrintCity(Cities C)//prints city details
    {
        System.out.println(C.city_Id);
        System.out.println(C.city_X_Location);
        System.out.println(C.city_Y_Location);
    }
    public double Find_Distance( double X,double Y)//calculates distance between cities and returns distance
    {
        return (Math.sqrt(Math.pow(this.city_X_Location -X , 2) + Math.pow(this.city_Y_Location-Y , 2)));
    }


}
