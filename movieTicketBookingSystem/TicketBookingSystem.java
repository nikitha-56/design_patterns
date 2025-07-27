import java.time.LocalDateTime;
import java.util.*;

class Ticket{
    private final String movieName;
    private final int price;
     private  boolean used;
     private final int seatNo;
     private final Theatre theatre;

    Ticket(String movieName,int price,int seatNo,Theatre theatre){
        this.movieName=movieName;
        this.price=price;
        this.used=false;
        this.seatNo=seatNo;
        this.theatre=theatre;
    }

    public int getSeatNo(){
        return seatNo;
    }

    public Theatre theatre(){
        return theatre;
    }

        public  int getPrice(){
            return price;
        }

        public  String getMovieName(){
            return movieName;
        }

        public boolean isUsed(){
            return used;
        }

        public void markUsed(){
            this.used=true;
        }

}

class Screen{
    private final int screenNo;
   private final  List<Show> shows;
   private final List<Seat> seats;

   public Screen(int screenNo,int seatCapacity){
    this.screenNo=screenNo;
    seats=new ArrayList<>();
    for(int i=0;i<=seatCapacity;i++){
            seats.add(new Seat(i));
    }
    this.shows=new ArrayList<>();
   }

   public int getScreenId(){
    return screenNo;
   }

   public List<Seat> getSeats(){
    return seats;
   }

   public void addShow(Show show){
        shows.add(show);
   }
    
   public List<Show> getShows(){
    return shows;
   }
    
}

class Seat{
    private final int seatno;
    private boolean isAvailable;
    public Seat(int seatno){
           this.seatno=seatno;
           this.isAvailable=true;
    }

    public boolean isAvailable(){
        return isAvailable;
    }

    public boolean reserve(){
        return isAvailable=false;
    }

    public int getSeatNo(){
        return seatno;
    }
}

class Show{
    private final int showId;
    private final String movieName;
    private final LocalDateTime starTime;
    private final List<Seat> seats;
    public Show(int showId,String movieName,LocalDateTime startTime,int seatCapacity){
        this.showId=showId;
        this.movieName=movieName;
        this.starTime=startTime;
        this.seats=new ArrayList<>();
        for(int i=1;i<=seatCapacity;i++){
            seats.add(new Seat(i));
        }
    }

    public boolean reserveSeat(int seatNo){
         if(seatNo<=0 || seatNo>seats.size()) return false;
         Seat seat=seats.get(seatNo-1);
        if(!seat.isAvailable()) return false;
        seat.reserve();
        return true;
    }

    public String getMovieName(){
        return movieName;
    }

}

class Theatre{
    private final List<Screen> screens=new ArrayList<>();
     private String name;
    Theatre(String name){
        this.name=name;
    }
    public void addScreen(Screen screen){
        screens.add(screen);
    }
     
    public void removeScreen(Screen screen){
        screens.remove(screen);
    }

    public String getName(){
        return name;
    }
}

class AppManager{
    List<Theatre> theatres=new ArrayList<>();
    List<NotifObserver> observers=new ArrayList<>();
    public void addTheatre(Theatre theatre){
        theatres.add(theatre);
    }

    public void removeTheatre(Theatre theatre){
        theatres.remove(theatre);
    }
  
    public void addObserver(NotifObserver o){
        observers.add(o);
    }

    public void notifyObservers(String name){
         for(NotifObserver observer:observers){
             observer.notify(name);
         }
    }


}

class User{
    private final String name;
    private final String mobileNo;
    Ticket ticket;
    public User(String name,String mobileNo){
        this.name=name;
        this.mobileNo=mobileNo;
    }

    public Ticket bookTicket(Show show,int seatNo,String movie,int price,Theatre theatre){
        if(!show.reserveSeat(seatNo)){
             System.out.println("Seat doesn't exist");
             return null;
    }
    int amt=price;
    Ticket ticket=new Ticket(movie,amt,seatNo,theatre);
    System.out.println("You reserved for "+movie+ " in " +theatre.getName()+" for "+ amt+". your seatNo is "+seatNo );
    return ticket;
}

public int chooseSeat(String type){
    if(type.equalsIgnoreCase("normal")){
        return new NormalTicket().priceCalculator();
    }
    else{
        return new PrimeiumTicket().priceCalculator();
    }
}
}

interface Payment{
    void pay(int amt);
}

 class UpiPayment implements Payment{
    public void pay(int amt){
        System.out.println("paid using upi "+ amt);
    }
 }

 class CashPayment implements Payment{
    public void pay(int amt){
        System.out.println("paid using cash  "+ amt);
    }
 }

 class CardPayment implements Payment{
    public void pay(int amt){
        System.out.println("paid using card "+ amt);
    }
 }

 class Factory{
     public static Payment generatePayment(String type){
        if(type.equalsIgnoreCase("upi")){
            return new UpiPayment();
        }
        else if(type.equalsIgnoreCase("cash")){
            return new CashPayment();
        }
        else if(type.equalsIgnoreCase("card")){
            return new CardPayment();
        }
        else{
            System.out.println("No such payment exists");
            return null;
        }
     }
 }

 interface calculatePrice{
       int priceCalculator();
 }

 class NormalTicket implements calculatePrice{
    public int priceCalculator(){
        return 150;
    }
 }

 class PrimeiumTicket implements calculatePrice{
    public int priceCalculator(){
        return 500;
    }
 }

 interface NotifObserver{
   void notify(String name);
 }

   class EmailNotification implements NotifObserver{
    public void notify(String name){
           System.out.println("Your Booking successfull for "+ name +" through mail");
    }
 }

  class OwnerNotification implements NotifObserver{
    public void notify(String name){
           System.out.println("booking for  "+ name +"is successfull");
    }
 }

public class TicketBookingSystem {
    public static void main(String[] args) {
        AppManager manager=new AppManager();
        User nikki=new User("nikki", "93902");
        User cherry=new User("cherry", "93021");
        Theatre pvp=new Theatre("PVP");
        Screen s1=new Screen(1, 21);
        pvp.addScreen(s1);
        Show show=new Show(1,"hhvm",LocalDateTime.now(),35);
        s1.addShow(show);
        manager.addObserver(new EmailNotification());
        manager.addObserver(new OwnerNotification());
       int amt= nikki.chooseSeat("normal");
       System.out.println("You ticket price is"+ amt);
        Ticket t1=nikki.bookTicket(show, 21, "hhvm", amt, pvp);
        if(t1!=null){
            System.out.println("Your ticket booking is successfulll");
           Payment payment= Factory.generatePayment("upi");
          if(payment!=null) payment.pay(amt);
          manager.notifyObservers(t1.getMovieName());
        }
        
    }
}
