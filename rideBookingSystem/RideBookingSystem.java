import java.util.*;

interface FareStrategy{
    double calculateFare();
}

class NormalFare implements FareStrategy{
    public double calculateFare(){
        return 500;
    }
}

class PremiumFare implements FareStrategy{
    public double calculateFare(){
        return 1000;
    }
}

interface Payment{
    void pay(int amt);
}

 class UpiPayment implements Payment{
    public void pay(int amt){
        System.out.println("Paid using upi "+amt);
    }
}

 class CashPayment implements Payment{
    public void pay(int amt){
        System.out.println("Paid using cash"+amt);
    }
}

enum PaymentType{
    UPI,CASH
}

 class PaymentFactory{
      public Payment generatePayment(PaymentType type){
          return switch(type){
            case UPI-> new UpiPayment();
            case CASH-> new CashPayment();
          };
      }
 }
  class Vehicle{
    String ownerName;
    String vehicleNo;
    boolean available;
    Vehicle(String ownerName,String vehicleNo,boolean available){
        this.ownerName=ownerName;
        this.vehicleNo=vehicleNo;
        this.available=available;
    }

    public boolean isAvailable(){
        return available;
    }

    public void setAvailable(boolean available){
        this.available=available;
    }
}

class User{
    String name;
    User(String name){
        this.name=name;
    }
}

 class Trip{
       private User user;
       private Vehicle vehicle;
       private double fare;
private FareStrategy farestrategy;
       public Trip(User user,Vehicle vehicle,FareStrategy fareStrategy){
            this.user=user;
            this.vehicle=vehicle;
            this.farestrategy=fareStrategy;
        }

        public User getUser(){
            return user;
        }

        public Vehicle getVehicle(){
            return vehicle;
        }

         public double getFare(){
            return farestrategy.calculateFare();
         }

    }
    

 class RidingManager{
   private final HashMap<Vehicle,String> availablevehicles=new HashMap<>();
   private final PaymentFactory paymentFactory=new PaymentFactory();

    public void addVehicle(Vehicle vehicle){
        availablevehicles.put(vehicle,"available");
       }

    public  Trip bookRide(User user,FareStrategy strategy){
        for(Map.Entry<Vehicle,String> entry:availablevehicles.entrySet()){
            if(entry.getKey().available){
                Vehicle vehicle=entry.getKey();
                vehicle.setAvailable(false);
                System.out.println("cab booked and on the way"+user.name);
                return new Trip(user, vehicle, strategy);
            }
        }
        System.out.println("No cabs available");
        return null;
    }

    public  void destinationReached(Vehicle vehicle){
        System.out.println("your destination reached");
        vehicle.setAvailable(true);;
    }

    public  int totalAmt(int amt){
        System.out.println("total fair is "+amt);
        return amt;
    }

    public void processPayment(PaymentType type,int amt){
        Payment payment=paymentFactory.generatePayment(type);
        if(payment!=null){
            payment.pay(amt);
        }
    }

}



public class RideBookingSystem {
    public static void main(String[] args) {
        Vehicle car1=new Vehicle("abc","ap2123",true);
        Vehicle car2=new Vehicle("abcd","ap22223",true);
        User u1=new User("nikki");
         User u2=new User("cherry");
        RidingManager manager=new RidingManager();
        manager.addVehicle(car2);
         manager.addVehicle(car1);
        Trip trip1=manager.bookRide(u1,new NormalFare());
        Trip trip2=manager.bookRide(u2,new PremiumFare());

       if(trip2!=null){
        manager.destinationReached(trip2.getVehicle());
        int amt=(int) trip2.getFare();
        manager.processPayment(PaymentType.UPI, amt);
       }
    }
}
