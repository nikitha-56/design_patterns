import java.util.*;

interface PaymentStrategy{
    void pay(int amt);
}

class UpiPayment implements PaymentStrategy{
    public void pay(int amt){
        System.out.println("Paid using upi "+amt);
    }
}

class CashPayment implements PaymentStrategy{
    public void pay(int amt){
        System.out.println("Paid using cash "+amt);
    }
}

interface OrderObserver{
    void onOrderPlaced(User user,int amt);
}

class NotificationService implements OrderObserver{
    public void onOrderPlaced(User user,int amt){
        System.out.println("sending Notification to "+ user.name+ " you order of "+ amt+ "places successfully");
    }
}

class DeliveryService implements OrderObserver{
    public void onOrderPlaced(User user,int amt){
        System.out.println(user.name +", Your delivery agent is on the way");
    }
}


class Restaurent{
    String name;
    String location;
    boolean isOpen;
    HashMap<String,Boolean> menu=new HashMap<>();
    HashMap<String,Integer> items=new HashMap<>();
    Restaurent(String name,String location,boolean isOpen){
        this.name=name;
        this.location=location;
        this.isOpen=isOpen;
    }
    public void addItem(String name,int price){
        menu.put(name,true);
        items.put(name,price);
    }

    public void removeItem(String name){
        if(menu.containsKey(name)){
            menu.remove(name);
            items.remove(name);
        }
        else
        System.out.println("no such item exists in restaurents");
    }

    public String getRestaurent(){
         return name;
    }

    public boolean isOpen(){
           return isOpen;
    }

    public int getItemPrice(String itemName){
        return items.getOrDefault(itemName,0);
    }

}

class User{
    String name;
    String mobileNo;
    Restaurent restaurent;
    List<String> cart=new ArrayList<>();
    User(String name,String mobileNo){
        this.name=name;
        this.mobileNo=mobileNo;
    }

    public void selectRestaurent(RestaurentManager manager,String restname){
           for(Restaurent r:manager.restaurents){
            if(r.name.equals(restname)){
                if(r.isOpen()){
                    this.restaurent=r;
                     System.out.println("You selected the restaurent "+ restname);
                     System.out.println("menu ");
                     for(Map.Entry<String,Integer> entry:r.items.entrySet()){
                         System.out.println(entry.getKey() + " price "+ entry.getValue());
                     }
                     return ;
                }
                else{
                    System.out.println("Sorry The restaurent is not available right now");
                    return;
                }
            }
           }
           System.out.println("No such restaurent exists");
    }
    public void addToCart(String name){
        if(restaurent!=null && restaurent.isOpen()){
            if(restaurent.menu.containsKey(name)){
            cart.add(name);
            System.out.println("you added "+name + "to cart");
        }
        else{
            System.out.println("Item not found in menu");
        }
    }
        
    }

}

class Order{

    List<OrderObserver> observers=new ArrayList<>();

    public void addObserver(OrderObserver observer){
        observers.add(observer);
    }
    public int placeorder(User user,PaymentStrategy strategy){
        if(user.cart.isEmpty()){
            System.out.println("you cart is Empty");
            return -1;
        }
       int total=0;
       System.out.println("Items in you cart");
       for(String item:user.cart){
        int price=user.restaurent.getItemPrice(item);
              System.out.println(item+ " - " + price);
              total+=price;
       } 
       System.out.println("Total is "+ total); 
       strategy.pay(total);

       for(OrderObserver observer:observers){
              observer.onOrderPlaced(user, total);
       }
return total;
}
}

class DeliveryAgent{
    String name;
    long mobileNo;
    DeliveryAgent(String name,long mobileNo){
        this.name=name;
        this.mobileNo=mobileNo;
    }
}

class RestaurentManager{
       public  List<Restaurent> restaurents=new ArrayList<>();
    void addRestaurent(Restaurent restaurent){
        restaurents.add(restaurent);
    }

    void removeRestaurent(Restaurent restaurent){
        restaurents.remove(restaurent);
    }

    public boolean getRestaurent(Restaurent res){
        if(restaurents.contains(res)){
            return true;
        }
        return false;
    }

}

class PaymentFactory{
    public static PaymentStrategy generatePayment(String type){
        if(type.equalsIgnoreCase("upi")){
            return new UpiPayment();
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

public class FoodDeliveryApp {
    public static void main(String[] args) {
       RestaurentManager manager=new RestaurentManager();
       Restaurent abc=new Restaurent("abc", "vijayawada", true);
       Restaurent rr=new Restaurent("rr", "vijayawada", true);
       
       rr.addItem("chicken biryani",250);
        rr.addItem("mutton biryani",350);
        abc.addItem("Dosa", 40);
         abc.addItem("idli", 30);
        manager.addRestaurent(rr);
        manager.addRestaurent(abc);
User nikki=new User("nikki", "9390212342");
nikki.selectRestaurent(manager, "rr");
nikki.addToCart("chicken biryani");
nikki.addToCart("mutton biryani");
Order order=new Order();
 order.addObserver(new NotificationService());
      order.addObserver(new DeliveryService());
      PaymentStrategy strategy=PaymentFactory.generatePayment("UPI");
      order.placeorder(nikki,strategy);
      User cherry=new User("chery","222222222222");
      cherry.selectRestaurent(manager, "abc");
cherry.addToCart("idli");
Order order2=new Order();
order2.addObserver(new DeliveryService());
 PaymentStrategy strategy2=PaymentFactory.generatePayment("Cash");
      order.placeorder(cherry,strategy2);
    
    }
}
