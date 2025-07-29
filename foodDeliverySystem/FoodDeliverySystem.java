import java.util.*;

import javax.swing.plaf.synth.SynthOptionPaneUI;

interface Payment{
     void pay(int amt) ;
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

        else return null;
    }
}
class User{
    private final String name;
    private final long phoneNo;
    private String address;
   

    User(String name,long phoneNo,String address){
        this.name=name;
        this.phoneNo=phoneNo;
        this.address=address;
    }

    public String getName(){
        return name;
    }

    public String getAddress(){
        return address;
    }

    public void modifyAddress(String address){
        this.address=address;
    }
   
}


class Driver{
     private final String name;
    private final long phoneNo;
    boolean canAcceptRide;

    Driver(String name,long phoneNo){
        this.name=name;
        this.phoneNo=phoneNo;
        canAcceptRide=true;
    }

    public String getName(){
        return name;
    }
    public long getMobileNo(){
        return phoneNo;
    }

    public boolean canAcceptRide(){
        return canAcceptRide;
    }

    public boolean acceptRide(){
        if(canAcceptRide==true){
            System.out.println("Driver is on the way for pickup");
            canAcceptRide=false;
        }
        return false;
    }

    public void completedRide(){
        System.out.println("Completed delivering the order");
        canAcceptRide=true;
    }
}

interface Observer{
    void notify(String msg);
}

 class NotificationTouser implements Observer{
    public void notify(String msg){
        System.out.println("you order is on the way "+msg);
    }
}

class NotificationToDriver implements Observer{
    public void notify(String msg){
        System.out.println("you order is ready.come and pick up "+msg);
    }
}

class NotificationToRest implements Observer{
    public void notify(String msg){
        System.out.println("Driver is on the way .be ready with your order "+msg);
    }
}

class NotificationToApp implements Observer{
    public void notify(String msg){
        System.out.println("one order confirmed "+msg);
    }
}

enum orderstatus{
    CONFIRMED,ONTHEWAY,COMPLETED,CANCELLED
}

class Order{
    private final String orderId;
    Restaurent rest;
    User user;
    orderstatus ostatus;
    Driver driver;
    
    Order(Restaurent rest,User user){
        this.rest=rest;
        this.user=user;
        this.orderId=UUID.randomUUID().toString();
    }

    public boolean acceptOrder(){
        if(rest.canAcceptOrder()){
            ostatus=orderstatus.CONFIRMED;
            System.out.println("Order accepted");
            return true;
        }
        else{
              ostatus=orderstatus.CANCELLED;
            System.out.println("Rest is busy cant accept order");
            
        }
        return false;
    }

    public void completedPreparing(){
        ostatus=orderstatus.ONTHEWAY;
    }

    public void completeddelivery(){
        rest.markOrderAsCompleted();
        ostatus=orderstatus.COMPLETED;
    }

    public void cancelOrder(){
        ostatus=orderstatus.CANCELLED;
    }

    public void assignDriver(Driver driver){
        this.driver=driver;
        if(driver!=null) driver.acceptRide();
        else System.out.println("No drivers available right now");

    }

    public Driver getDriver(){
        return driver;
    }
}

class AppManager{
     List<Restaurent> restaurents;
     List<Observer> observers;
     List<Driver> drivers=new ArrayList<>();
     AppManager(){
        restaurents=new ArrayList<>();
        observers=new ArrayList<>();
     }
     public void addRestaurent(Restaurent rest){
        System.out.println("new restaurent " +rest.getName()+ "added ");
        restaurents.add(rest);
     }
     public void removeRestaurent(Restaurent rest){
        System.out.println(rest.getName() +" is removed");
        restaurents.remove(rest);
     }

     public void addDriver(Driver d){
        drivers.add(d);
     }

     public Driver getAvailaDriver(){
        for(Driver d:drivers){
            if(d.canAcceptRide()){
                return d;
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

     public void notifyAll(String msg){
           for(Observer o:observers){
                 o.notify(msg);
           }
     }
    
}

class Cart{
    List<String> cartitems;
    Cart(){
        cartitems=new ArrayList<>();
    }
    int total=0;
    public void addItemToCart(Restaurent rest,User user,String item){
         if(rest.menu.containsKey(item)){
            System.out.println(item.toString() + "added to cart from the restaurent "+ rest.getName());
             total+=rest.menu.get(item);
             return;
         }
         else{
            System.out.println("No such item exists in the restaurent");
         }

    }

    public int getTotal(){
        return total;
    }

    public void clearCart(){
        cartitems.clear();
    }
    
}

class Restaurent{
   HashMap<String,Integer> menu=new HashMap<>(); 
    private final String restName;
    private final String address;
    private int maxOrdersCanAccept;
    int currentOrders;

    Restaurent(String restName,String address,int maxOrdersCanAccept){
        this.restName=restName;
        this.address=address;
        this.maxOrdersCanAccept=maxOrdersCanAccept;
    }

    public String getName(){
        return restName;
    }

    public void addItem(String itemName,int price){
         if(!menu.containsKey(itemName)) menu.put(itemName,price);
         else System.out.println("such Item is already existed");
    }

    public void removeItem(String name){
        if(menu.containsKey(name)){
            menu.remove(name);
            return;
        }
        else
        System.out.println("No such item exists to delete");
    }

    
    public boolean canAcceptOrder(){
        if(currentOrders<maxOrdersCanAccept){
            currentOrders++;
            return true;
        }
        return false;
    }

    public void markOrderAsCompleted(){
        if(currentOrders>0) currentOrders--;
    }
}
class FoodDeliverySystem{
    public static void main(String[] args) {
       AppManager manager=new AppManager();
       User nikki=new User("nikii",234923,"vja");
       User cherry=new User("cherry",234923,"vja");
       Driver d1=new Driver("rams", 98876);
       Driver d2=new Driver("rambo", 9881276);
       manager.addDriver(d2);
        manager.addDriver(d1);
       Restaurent rr=new Restaurent("rr", "beside pvp", 12);
       Restaurent slivers=new Restaurent("silvers", "opposite pvp", 22);
       manager.addRestaurent(slivers);
       manager.addRestaurent(rr);
       rr.addItem("chicken biryani", 200);
       rr.addItem("mutton biryani", 300);
       rr.addItem("chicken biryani", 200);
       manager.addObserver(new NotificationToRest());
       manager.addObserver(new NotificationTouser());
       Cart cart=new Cart();
       cart.addItemToCart(rr, cherry,"chicken biryani" );
        cart.addItemToCart(rr, cherry,"chicken biryani" );
        int amt=cart.getTotal();
        System.out.println("Total Bill is :" +amt);
        Order order=new Order(rr,cherry);
        Driver driver=manager.getAvailaDriver();
        order.assignDriver(driver);
        Payment payment=Factory.generatePayment("UPI");
        if(payment!=null)payment.pay(amt);
        cart.clearCart();
        manager.notifyAll(" ");
        driver.completedRide();
    }
}