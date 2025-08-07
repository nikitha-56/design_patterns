import java.util.*;

    class User{
        private String name;
        private String bankName;
        private long cardId;
        private int amt;
        User(String name,String bankName,long cardId){
            this.name=name;
            this.bankName=bankName;
            this.cardId=cardId;
            this.amt=0;
        }

        public String getName(){
            return name;
        }
        public String getBankName(){
            return bankName;
        }
        public long getCardId(){
            return cardId;
        }

        public int getAmt(){
            return amt;
        }

        public void addAmt(int amtin){
            amt+=amtin;
        }

        public void decreaseAmt(int wamt){
            amt-=wamt;
        }

    }

    class Transaction{
          enum Type{WITHDRAWN,DEPOSIT}
         private Type type;
         private int amt;
         private Date timestamp;

         Transaction(Type type,int amt){
            this.type=type;
            this.amt=amt;
            this.timestamp=new Date();
         }

         public Type getType(){
            return type;
         }

         public int getAmt(){
            return amt;
         }

         public Date getTimeStamp(){
            return timestamp;
         }

        @Override
         public String toString(){
            return type + " of $ " + amt+ " at"+ timestamp;
         }


    }

    class AtmManager{
        private static AtmManager instance;
        HashMap<String,User> users;
        private  Logger logger;
       private HashMap<User,List<Transaction>> transactions;
       List<Observer> observers;
      private  AtmManager(Logger logger){
            users=new HashMap<>();
              transactions=new HashMap<>();
              observers=new ArrayList<>();
              this.logger=logger;
        }
        public static AtmManager getInstance(Logger logger){
            if(instance==null){
                instance=new AtmManager(logger);
            }
            return instance;
        }

        public void addUser(User u){
          if(!users.containsKey(u.getName())) {
            users.put(u.getName(),u);
        }
    }

        public void removeUser(User u){
            users.remove(u.getName());
        }

        public void allTransactions(User u){
            List<Transaction> trs=transactions.get(u);
            if(trs==null || trs.isEmpty()){
                System.out.println("No transactions yet..");
                return;
            }
            for(Transaction t:trs){
            System.out.println(t);
            }
        }
  
        public void  withdraw(User u,int withdrawAmt){
            if(users.containsKey(u.getName())){
                int userbal=u.getAmt();
            if(withdrawAmt>0 && userbal>withdrawAmt){
                logger.log("user "+ u.getName() +" withdraw $ "+ withdrawAmt);
                int res=userbal-withdrawAmt;
                System.out.println("Balance amt is "+res);
                 u.decreaseAmt(withdrawAmt);
                  transactions.computeIfAbsent(u,k->new ArrayList<>()).add(new Transaction(Transaction.Type.WITHDRAWN, withdrawAmt));
                  notifyAll(withdrawAmt,Transaction.Type.WITHDRAWN, u);
                  return;
            }
            else{
                System.out.println("Insufficient balance ..Try again");
                return;
            }
        }
        System.out.println("User not found ");
        }

        public void depositAmt(User u,int depositAmt){
            if(users.containsKey(u.getName())){
                logger.log("user "+ u.getName() +" deposited $ "+ depositAmt);
                 u.addAmt(depositAmt);
                transactions.computeIfAbsent(u, k->new ArrayList<>()).add(new Transaction(Transaction.Type.DEPOSIT, depositAmt));
                notifyAll(depositAmt,Transaction.Type.DEPOSIT, u);
                return;
            }
            else{
                System.out.println("User not found");
            }
        }

        public void checkBalance(User u){
           System.out.println("Your current Balance is : "+ u.getAmt());
        }

        public void addObserver(Observer o){
            observers.add(o);
        }

         public void removeObserver(Observer o){
            observers.remove(o);
        }

        public void notifyAll(int amt,Transaction.Type type,User u){
            for(Observer o:observers){
                o.notify(amt,type,u);
            }
        }
    }
    interface Observer{
        void notify(int amt,Transaction.Type type,User u);
    }

    class NotifyToAtmManager implements Observer{
        public void notify(int amt,Transaction.Type type,User u){
            System.out.println(" Transaction type- "+type +  ": " +u.getName() + type +" the  amt of " +amt);
        }
    }

    class NotifyToAtmUser implements Observer{
        public  void notify(int amt,Transaction.Type type,User u){
            System.out.println(" Transaction type- "+type + ":"+  u.getName() +" your "+ type+" is successfull of amt : "+ amt );
        }
    }

     interface Logger{
        void log(String messsage);
    }

     class ConsoleLogger implements Logger{
        @Override
     public void log(String message){
         System.out.println("[LOG]" + message);
        }
    }
    public class atm {
      public static Scanner sc=new Scanner(System.in);
       Logger logger=new ConsoleLogger();
       AtmManager manager=AtmManager.getInstance(logger);
        public static User collectUserDetail(){
            System.out.println("Enter the user details :");
            System.out.print("Enter your name :");
            String name=sc.next();
            System.out.print("Enter bank name :");
            String bname=sc.next();
            System.out.print("Enter card number :");
            long cardNo=sc.nextLong();
            return new User(name, bname, cardNo);
        }

        public User findUserByName(String name){
           return manager.users.get(name);
        }
          

    public static void main(String[] args) {
        atm atm1=new atm();
        Observer o1=new NotifyToAtmUser();
         Observer o2=new NotifyToAtmManager();
         atm1.manager.addObserver(o1);
         atm1.manager.addObserver(o2);
        while(true){
     System.out.println("Select the option : 1.AddUser 2.Withdraw money 3 .Deposit money 4.check balance 5.check all transcations  6.exitt");
        System.out.print("select the option :");
        int n=sc.nextInt();
        switch(n){
            case 1:
                  User u=atm.collectUserDetail();
                  atm1.manager.addUser(u);
                  break;
            case 2:
                  System.out.print("Enter your user name");
                  String name=sc.next();
                 User user=atm1.findUserByName(name);
                 if(user!=null){
                    System.out.print("Enter the amt to withdraw :");
                    int amtToWithdraw=sc.nextInt();
                    atm1.manager.withdraw(user, amtToWithdraw);
                    break;
                 }
                 else{
                    System.out.println("User not found");
                 }
                 break;
                
            case 3:
                  System.out.print("Enter your user name");
                  String named=sc.next();
                  User user1=atm1.findUserByName(named);
                 if(user1!=null){
                     System.out.print("Enter the amt you want to deposit :");
                       int amt=sc.nextInt();
                    atm1.manager.depositAmt(user1, amt);
                   break;
                 } 
                  else{
                       System.out.println("User not found");
                  }
                  break;
           case 4:
                       System.out.print("Enter your user name");
                      String username=sc.next();
                  User username1=atm1.findUserByName(username);
                     if(username1!=null){
                        atm1.manager.checkBalance(username1);
                        break;
                     }
                      else{
                       System.out.println("User not found");
                  }
                     break;
          case 5:
                   System.out.print("Enter your user name");
                      String usern=sc.next();
                      User checkUser=atm1.findUserByName(usern);
                    if(checkUser!=null){
                   atm1.manager.allTransactions(checkUser);
                         break;
                     }
                     else{
                         System.out.println("User not found");
                     }
                     break;

           case 6: System.out.println("Exiting the program......");
                     return;
        }
    }
    }
}
