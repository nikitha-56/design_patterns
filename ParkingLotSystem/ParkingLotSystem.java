
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
public class ParkingLotSystem {
    
    static class Vehicle{
        String vehicleId;
        String vehicleOwner;
        LocalDateTime entryTime;
        LocalDateTime exitTime;
        Vehicletype type;
        Vehicle(String vehicleId,String vehicleOwner,Vehicletype type){
            this.vehicleId=vehicleId;
            this.vehicleOwner=vehicleOwner;
            this.type=type;
        }

    }

    public static enum Vehicletype{
        CAR(20),BIKE(15),SCOOTY(10),TRUCK(20);
        int val;
         Vehicletype(int val){
          this.val=val;
        }

        public int getPricePerHour(){
            return val;
        }
        
    }
    static class Floor{
        int noOfSlots;
        int fno;
        Floor(int fno,int noOfSlots){
            this.fno=fno;
            this.noOfSlots=noOfSlots;
        }
    }

    static class CalculatePrice{
        public float calculatePrice(Vehicle Vehicle,LocalDateTime exitTime){
            long hours=Duration.between(Vehicle.entryTime,exitTime).toHours();
            if(hours==0) hours=1;
            return hours*Vehicle.type.getPricePerHour();
        }
    }

    static class  ParkingLotManager{
         List<Floor> floors=new ArrayList<>();
         List<Vehicle> vehicles=new ArrayList<>();
         void addVehicle(Vehicle vehicle){
               vehicles.add(vehicle);
               vehicle.entryTime=LocalDateTime.now();
         }

         void removeVehicle(Vehicle vehicle){
            vehicles.remove(vehicle);
         }

    }

    static class Slot{
        int sno;
        boolean isSlotAvailable;
        Slot(int sno,boolean isSlotAvailable){
            this.sno=sno;
            this.isSlotAvailable=isSlotAvailable;
        }

    }

    interface Payment{
         void pay(int amt);
    }

     static class UpiPayment implements Payment{
        public void pay(int amt){
            System.out.println("Paid using upi");
        }
    }

     public static class CardPayment implements Payment{
        public void pay(int amt){
            System.out.println("Paid using card");
        }
    }

    static class PaymentFactory{
        public static Payment generatePayment(String type){
            if(type.equalsIgnoreCase("upi")){
                return new UpiPayment();
            }
            else if(type.equalsIgnoreCase("card")){
                return new CardPayment();
            }
            else{
                System.out.println("no such payment type exists");
                return null;
            }
        }
    }



    public static void main(String[] args) {
         Vehicle v1=new Vehicle("ap2132", "nikki",Vehicletype.CAR);
          Vehicle v2=new Vehicle("ap2212", "cheddy",Vehicletype.SCOOTY);
          ParkingLotManager manager=new ParkingLotManager();
          manager.addVehicle(v2);
          manager.addVehicle(v1);

          v1.exitTime=v1.entryTime.plusHours(3);
          v2.exitTime=v2.entryTime.plusHours(4);
          CalculatePrice calculate=new CalculatePrice();
          float price=calculate.calculatePrice(v2, v2.exitTime);
          System.out.println("totalprice :"+price);
          PaymentFactory p1=new PaymentFactory();
          Payment payamt=p1.generatePayment("upi");
          payamt.pay((int)price);


    }
}
