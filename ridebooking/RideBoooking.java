
import java.util.*;

import javax.swing.plaf.synth.SynthOptionPaneUI;

interface Payment{
    void pay(int amt);
}

class UpiPayment implements Payment{
    public void pay(int amt){
        System.out.println("Paid using upi "+amt);
    }
}

class CardPayment implements Payment{
    public void pay(int amt){
        System.out.println("Paid using card "+amt);
    }
}

class CashPayment implements Payment{
    public void pay(int amt){
        System.out.println("Paid using cash "+amt);
    }
}

class Factory{
    public static Payment generatePayment(String type){
        if(type.equalsIgnoreCase("upi")){
             return new UpiPayment();
        }
        else if(type.equalsIgnoreCase("card")){
             return new CardPayment();
        }
        else if(type.equalsIgnoreCase("cash")){
             return new CashPayment();
        }
        else{
            System.out.println("No such payment exists");
            return null;
        }
    }
}

class User{
    private final String name;
    private final long phoneNo;
    User(String name,long phoneNo){
        this.name=name;
        this.phoneNo=phoneNo;
    }

    public String getName(){
        return name;
    }

    public long getPhoneNo(){
        return phoneNo;
    }


}

enum vehicleType{
    AUTO,BIKE,SCOOTY,CAB
}

class Driver{
   private final  String driverName;
    private final long phoneNo;
    private final String vehicleNo;
    private boolean isAvailable;
    private final vehicleType  vehicletype;
    Driver(String driverName,long phoneNo,String vehicleNo,vehicleType vehicle){
        this.driverName=driverName;
        this.phoneNo=phoneNo;
        this.vehicleNo=vehicleNo;
        this.vehicletype=vehicle;
        isAvailable=true;
    }


    public long getPhoneNo(){
       return phoneNo;
    }

    public vehicleType vehicletype(){
        return vehicletype;
    }
    public boolean isAvailablern(){
        return isAvailable;
    }

    public boolean markAsAvailable(){
        return isAvailable=true;
    }

    public String getName(){
        return driverName;
    }

    public void acceptRide(){
       if(isAvailable){
        System.out.println("Accepted ride by "+ driverName);
        isAvailable=false;
       }
    }

    public void rejectRide(){
       System.out.println("Driver rejected the ride ");
    }

    public int enterDistance(){
       Scanner sc=new Scanner(System.in);
        System.out.println("Enter the dist in kms :");
        return sc.nextInt();
    }

}

class CalculateFare{
  public static int priceCalculate(vehicleType vehicletype,int dist){
    switch(vehicletype){
        case CAB: return dist*15;
        case AUTO: return dist*10;       
        case SCOOTY: return dist*12;             
         case BIKE: return dist*14;              
        default:return -1;
    }
  }
}

enum rideStatus{
    AVAILABLE,ONTHEWAY,COMPLETED,CANCELLED
}

class RideBook{
  private final String bookingId;
  private final User user;
  private final Driver driver;
  private rideStatus status;
  int dist;
  RideBook(User user,Driver driver,int dist){
    this.bookingId=UUID.randomUUID().toString();
    this.user=user;
    this.driver=driver;
    this.status=rideStatus.AVAILABLE;
    this.dist=dist;
  }

  public rideStatus getStatus(){
    return status;
  }

  public void startRide(){
    this.status=rideStatus.ONTHEWAY;
    System.out.println("ride started and status is " +this.status);
  }

  public void cancelRide(){
    this.status=rideStatus.CANCELLED;
    System.out.println("Ride cancelled");
    driver.markAsAvailable();
  }

  public void completeRide(){
    this.status=rideStatus.COMPLETED;
  }
  public String getDriver(){
    return driver.getName();
  } 

   public float priceOfRide(){
        float amt=CalculateFare.priceCalculate(driver.vehicletype(),dist);
        System.out.println("You cost is "+amt);
        return amt;
    }

}

interface Observer{
     void notify(String msg) ;
}

 class NotificationToUser implements Observer{
    public void notify(String msg){
        System.out.println("You driver is on the way "+msg);
    }
}

 class NotificationToDriver implements Observer{
    public void notify(String msg){
        System.out.println("You passsenger is waiting for you "+msg);
    }
}

class NotificationTomanager implements Observer{
    public void notify(String msg){
        System.out.println("one booking successfull "+msg);
    }
}

class AppManager{
    List<Driver> drivers;
    List<Observer> observers;


    AppManager(){
        drivers=new ArrayList<>();
        observers=new ArrayList<>();
    }

    public void addDriver(Driver driver){
        System.out.println("Added driver "+driver.getName());
        drivers.add(driver);
    }

    public void removeDriver(Driver driver){
        System.out.println("removed driver "+driver.getName());
        drivers.remove(driver);
    }

    public Driver findAvailableDriver(vehicleType type){
          for(Driver d:drivers){
            if(d.isAvailablern() && d.vehicletype()==type){
               return d;
            }
          }
          return null;
    }

    public void addObserver(Observer o){
        System.out.println("Added observer");
       observers.add(o);
    }

    public void removeObserver(Observer o){
       observers.remove(o);
    }

    public   void notifyAll(String msg){
        for(Observer o:observers){
            o.notify(msg);
        }
    }

}

public class RideBoooking {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        AppManager manager=new AppManager();
        Driver d1=new Driver("ram",939222,"ap2132",vehicleType.AUTO);
         Driver d2=new Driver("nags",939242,"ts232",vehicleType.CAB);

        manager.addDriver(d1);
        manager.addDriver(d2);

        User u1=new User("nikki", 93902);
        System.out.println("Enter preferred Vehicle. Available Vehicles are auto , cab, bike,scooty");
        vehicleType selectedType=vehicleType.valueOf(sc.next().toUpperCase());
        Driver driver=manager.findAvailableDriver(selectedType);
        if(driver==null) {
            System.out.println("No vehicles available right now");
            return;
        }
        int dist=driver.enterDistance();
        RideBook b1=new RideBook(u1,driver,dist);
        b1.startRide();
        d1.acceptRide();
        int amt= (int)b1.priceOfRide();
        Observer o1=new NotificationToDriver();
        manager.addObserver(o1);
        manager.addObserver(new NotificationToUser());
         manager.notifyAll("ride");
        System.out.println("Enter payment method : UPI/CARD/CASH : ");
        Payment payment=Factory.generatePayment(sc.next());
        if(payment!=null){
            payment.pay(amt);
        }
        b1.completeRide();

    }
}
