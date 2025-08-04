package CarRentalSystem;

import java.util.*;

class User{
    String name;
    long phoneNo;
    User(String name,long phoneNo){
        this.name=name;
        this.phoneNo=phoneNo;
    }
}

enum VehicleType{
    CAR(300),BIKE(100),SCOOTY(50);
    public final int value;
    VehicleType(int value){
        this.value=value;
    }
    public int getValue(){
        return value;
    }
}

class Vehicle{
    VehicleType type;
    String vehicleNo;
    Vehicle(VehicleType type,String vehicleNo){
        this.type=type;
        vehicleNo=vehicleNo;
    }
}

class RentalManager{
    public RentalManager instance;
    HashMap<VehicleType,ArrayList<Vehicle>> vehicles;
    RentalManager(){
        vehicles=new HashMap<>();
    }
    private RentalManager getInstance(RentalManager instance){
        if(instance==null){
         this.instance=instance;
         return instance;
        }
        return instance; 
    }

    public void addVehicle(Vehicle v){
         vehicles.computeIfAbsent(v.type, v.type->new ArrayList<>()).add(vehicle);
    }

    public void removeVehicle(Vehicle v){
        vehicles.remove(v);
    }

    public assignVehcileToUser()



}
class CarRental{


public static void Main(String args[]){

}
}