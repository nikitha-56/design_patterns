
import java.util.*;

class User{
   private String name;
    private long phoneNo;
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
    boolean isVehicleAvailable;
    Vehicle(VehicleType type,String vehicleNo){
        this.type=type;
        this.vehicleNo=vehicleNo;
        this.isVehicleAvailable=true;
    }
    public boolean isVehicleAvailable(Vehicle v){
      return v.isVehicleAvailable;
    }

    public void markVehicleAsNotAvailable(){
        this.isVehicleAvailable=false;
    }
    public void markVehicleAsAvailable(){
        this.isVehicleAvailable=true;
    }

}

class RentalManager{
    private static RentalManager instance;
    HashMap<VehicleType,ArrayList<Vehicle>> vehicles;
    HashMap<VehicleType,Integer> vehicleExists;
    public List<User> users;
    public List<Observer> observers;
    RentalManager(){
        vehicles=new HashMap<>();
        users=new ArrayList<>();
        vehicleExists=new HashMap<>();
        observers=new ArrayList<>();
    }
    public static  RentalManager getInstance(){
        if(instance==null){
        instance=new RentalManager();
        }
        return instance; 
    }

    public void addUser(User u){
        users.add(u);
    }

    public void removeUser(User u){
        users.remove(u);
    }

    public  void increaseVehicleCnt(VehicleType v){
         vehicleExists.put(v,vehicleExists.getOrDefault(v,0)+1);
    }

    public  void decreaseVehicleCnt(VehicleType v){
         int cnt=vehicleExists.get(v);
        if(cnt==0){
            System.out.println("No vehicles of" +v+ "are present to remove");
        }
        else if(cnt==1){
            vehicleExists.remove(v);
        }
        else{
            vehicleExists.put(v,cnt-1);
        }
    }

    public boolean isVehicleAvailable(VehicleType v){
        if(vehicleExists.containsKey(v) && vehicleExists.get(v)>0){
             return true;
        }
        return false;
    }


    public void addVehicle(Vehicle v){
        increaseVehicleCnt(v.type);
         vehicles.computeIfAbsent(v.type,k->new ArrayList<>()).add(v);
    }

    public boolean removeVehicle(Vehicle v){
        ArrayList<Vehicle> list=vehicles.get(v.type);
        if(list==null || !list.remove(v)){
            return false;
        }

        if(list.isEmpty()){
            vehicles.remove(v.type);
        }
        return true;
    }

    public Vehicle assignVehcileToUser(VehicleType type,User u){
          if(!vehicleExists.containsKey(type)){
              System.out.println("No such vehicles available right now");
              return null;
          }
          else{
            for(Vehicle v:vehicles.get(type)){
            if(v.isVehicleAvailable){
            v.markVehicleAsNotAvailable();
                  System.out.println("Vehicle exists and assigned to User");
                  decreaseVehicleCnt(v.type);
                 return v;
          }
          }
        }
          return null;
    }

    public void addObserver(Observer o){
         observers.add(o);
    }
     public void removeObserver(Observer o){
         observers.remove(o);
    }

    public int calculatePrice(VehicleType v,int hrs){
       int priceperhr=v.getValue();
       return priceperhr*hrs;
    }

    public void returnVehicle(Vehicle v){
      v.markVehicleAsAvailable();
      increaseVehicleCnt(v.type);
      System.out.println("Vehicle returned successfully"); 

    }

    public void notifyObservers(User u,VehicleType type){
        for(Observer o:observers){
            o.notify(u,type);
        }
    }
}

 interface Observer{
      void notify(User u,VehicleType type);
    }

class NotifyToManager implements Observer{
    public void notify(User u,VehicleType v){
    System.out.println("Vehicle type of "+ v + "booked succuessfully by user "+ u.getName());
    }
}

class NotifyToUser implements Observer{
    public void notify(User u,VehicleType v){
    System.out.println("Your Rental of "+ v+ "is successfull");
    }
}

    interface Payment{
        void pay(int amt);
    }

    class UpiPayment implements Payment{
        public void pay(int amt){
            System.out.println("Paid using upi of amount "+amt);
        }
    }

    class CashPayment implements Payment{
        public void pay(int amt){
            System.out.println("Paid using Cash of amount "+amt);
        }
    }

    class PaymentFactory{
        public static Payment generatePayment(String type){
            if(type.equalsIgnoreCase("upi")){
                  return new UpiPayment();
            }
            else if(type.equalsIgnoreCase("Cash")){
                  return new CashPayment();
            }

            else{
                System.out.println("No such Payment exists");
                return null;
            }
        }
    }

class CarRental{

    public Scanner sc=new Scanner(System.in);
    RentalManager manager=RentalManager.getInstance();
    public User collectUser(){
         System.out.println("Enter the user name: ");
         String name=sc.next();
         System.out.println("Enter the user phone Number: ");
         long pn=sc.nextLong();
         User u=new User(name,pn);
         manager.addUser(u);

         System.out.println("New user "+name+" added succuessfully");
         return u;
    }
    public void collectVehicles(){
        System.out.println("Enter the no of vehilces: ");
         int n=sc.nextInt();
         for(int i=0;i<n;i++){
         System.out.println("Enter the vehcile no: ");
         String vn=sc.next();
         System.out.println("Enter the Vehicle type: ");
         String input=sc.next();
         VehicleType type=VehicleType.valueOf(input.toUpperCase());
         manager.addVehicle(new Vehicle(type, vn));
         System.out.println("New Vehcile of type "+ type +" added succuessfully");
         }
        }

public static void main(String[] args){
      CarRental rental=new CarRental();
      rental.collectVehicles();
    RentalManager manager=RentalManager.getInstance();
    Observer o1=new NotifyToUser();
  Observer o2=new NotifyToManager();
  manager.addObserver(o2);
   manager.addObserver(o1);
    User u1=rental.collectUser();
 Vehicle assigned= manager.assignVehcileToUser(VehicleType.CAR, u1);
 if(assigned!=null){
    manager.notifyObservers(u1, assigned.type);
  int price=manager.calculatePrice(VehicleType.CAR, 2);
  Payment payment=PaymentFactory.generatePayment("Upi");
  payment.pay(price);
   manager.returnVehicle(assigned);

 }
}
}

