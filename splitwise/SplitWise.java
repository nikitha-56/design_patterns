import java.util.*;

class User{
    private final String name;
    private final String email;
    private final long phoneNo;
    User(String name,String email,long phoneNo){
        this.name=name;
        this.email=email;
        this.phoneNo=phoneNo;
    }

    public String getName(){
        return name;
    }

    public long phoneNo(){
        return phoneNo;
    }
}

class Split{
    private final int totalmoney;
    private final User paidBy;

    Split(int totalmoney,User paidBy){
        this.totalmoney=totalmoney;
        this.paidBy=paidBy;
    }
    List<User> users=new ArrayList<>();

    void addUser(User user){
        users.add(user);
    }

    public void removeUser(User user){
        users.remove(user);
    }

    public int splitBill(){
        if(users.size()<=0) {
          return 0;
        }
        return totalmoney/users.size();
    }

    public void displayMessage(){
        for(User user:users){
            if(!paidBy.equals(user)){
                System.out.println(user.getName() + " should pay "+splitBill());
            }
        }
    }
}

class AppManager{
    List<Split> splits;
    List<Observer> observers;

    AppManager(){
        splits=new ArrayList<>();
        observers=new ArrayList<>();
    }
    public void addObserver(Observer o){
        observers.add(o);
    }

    public void removeObserver(Observer o){
        observers.remove(o);
    }

    public void addSplit(Split s){
        splits.add(s);
    }

    public void removeSplit(Split s){
        splits.remove(s);
    }

    public void notifyAll(String method,User user,int amt){
        for(Observer o:observers){
            o.notify(method,user,amt);
        }
    }
}

interface Payment{
    void pay(int amt);
}

class UpiPayment implements Payment{
    public void pay(int amt){
        System.out.println("Paid using upi"+ amt);
    }
}

class CardPayment implements Payment{
    public void pay(int amt){
        System.out.println("Paid using card"+ amt);
    }
}

class CashPayment implements Payment{
    public void pay(int amt){
        System.out.println("Paid using cash"+ amt);
    }
}

interface Observer{
    void notify(String method,User user,int amt);
}

class NotifyToUser implements Observer{
    public void notify(String method,User user,int amt){
        System.out.println("notification to user :" +user.getName() +" your payment using "+ method +" of"+ amt+" is successfull");
    }
}

class NotifyToPersonWhoPaid implements Observer{
    public void notify(String method,User user,int amt){
        System.out.println("notification to payer :" + user.getName() + " paid "+ amt + " using "+method +" is successfull");
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

class UserManager{
    private  final List<User> allusers=new ArrayList<>();
    private  final List<User> paidUsers=new ArrayList<>();

    public  void addUser(User user){
        allusers.add(user);
    }

    public  List<User> getAllUsers(){
        return allusers;
    }

    public void markAsPaid(User user){
        paidUsers.add(user);
    }

    public boolean hasPaid(User user){
        return paidUsers.contains(user);
    }
    public  List<User> getPaidUsers(){
        return paidUsers;
    }

}

class SplitWise{
    Scanner sc=new Scanner(System.in);
    private final UserManager usermanager=new UserManager();
    private final AppManager manager=new AppManager();


    public void run(){
        List<User> users=collectUsers();
        if(users.isEmpty()){
            System.out.println("No users added ,Exiting..");
            return;
        }

        int amt=getAmount();
        User paidUser=getPayer(users);

        if(paidUser==null){
            System.out.println("Payer not found, exiting..");
            return;
        }

        Split split=new Split(amt,paidUser);
        for(User u:users){
            split.addUser(u);
        }

        int splitamt=split.splitBill();
        if(splitamt==0){
            System.out.println("Split amt is zero, Exiting..");
            return;
        }

        split.displayMessage();

        String method=getPaymentMethod();
        Payment payment =Factory.generatePayment(method);

        if(payment==null){
            System.out.println("No such payment exists ,Exiting..");
            return ;
        }

        payment.pay(splitamt);

        notifyObservers(users,paidUser,method,splitamt);

    }

private List<User> collectUsers(){
        System.out.println("Enter the number of users: ");
    int n=sc.nextInt();
    for(int i=0;i<n;i++){
         System.out.print("Enter user name :");
         String name=sc.next();
          System.out.print("Enter email of the user :");
         String email=sc.next();
          System.out.print("Enter phoneno of the user :");
         long phoneNo=sc.nextLong();

        usermanager.addUser(new User(name, email, phoneNo));

    }
    return usermanager.getAllUsers();
}

private int getAmount(){
          System.out.println("Enter the total amount to split: ");
    return sc.nextInt();
}

private User getPayer(List<User> users){
    System.out.println("enter the user who paid :");
    String payerName=sc.next();
    for(User user:users){
    if(user.getName().equalsIgnoreCase(payerName)){
        return user;
    }
    }
    return null;
}

private String getPaymentMethod(){
        System.out.println("Enter the payment method :UPI /CASH/CARD");
        return sc.next();
}

private void notifyObservers(List<User> users,User paidUser,String method,int amt){
      
   manager.addObserver(new NotifyToPersonWhoPaid());
   manager.addObserver(new NotifyToUser());

    
         for(User u:users){
        if(!u.getName().equalsIgnoreCase(paidUser.getName())){
            manager.notifyAll(method,u,amt);
        }
    
    }
}


public static void main(String[] args) {
 new SplitWise().run();
}
}
