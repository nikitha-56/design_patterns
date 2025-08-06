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

        public void decreaseAmt(float wamt){
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
            return type + " of $  " + amt+ " at"+ timestamp;
         }


    }

    class AtmManager{
        private static AtmManager instance;
        ArrayList<User> users;
       private HashMap<User,List<Transaction>> transactions;
      private  AtmManager(){
            users=new ArrayList<>();
              transactions=new HashMap<>();
        }
        public static AtmManager getInstance(){
            if(instance==null){
                instance=new AtmManager();
            }
            return instance;
        }

        public void addUser(User u){
            users.add(u);
        }

        public void removeUser(User u){
            users.remove(u);
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
  
        public void  withdraw(User u,float withdrawAmt){
            if(users.contains(u)){
                float userbal=u.getAmt();
            if(withdrawAmt>0 && userbal>withdrawAmt){
                System.out.println("you Withdrawn "+ withdrawAmt+" rupees");
                float res=userbal-withdrawAmt;
                System.out.println("Balance amt is "+res);
                  transactions.computeIfAbsent(u,k->new ArrayList<>()).add(new Transaction(Transaction.Type.WITHDRAWN, (int) withdrawAmt));
                   u.decreaseAmt(withdrawAmt);
            }
            else{
                System.out.println("Insufficient balance ..Try again");
                return;
            }
        }
        System.out.println("User not found ");
        }

        public void depositAmt(User u,int depositAmt){
            if(users.contains(u)){
                System.out.println("Deposit Successfull "+ depositAmt);
                transactions.computeIfAbsent(u, k->new ArrayList<>()).add(new Transaction(Transaction.Type.DEPOSIT, depositAmt));
                u.addAmt(depositAmt);
                return;
            }
            else{
                System.out.println("User not found");
            }
        }

        public void checkBalance(User u){
           System.out.println("Your current Balance is : "+ u.getAmt());
        }

    }
    public class atm {
      public static Scanner sc=new Scanner(System.in);
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
          

    public static void main(String[] args) {
        AtmManager manager=AtmManager.getInstance();
        atm atm1=new atm();
        System.out.println("Select the option : 1.AddUser 2.Withdraw money 3 .Deposit money 4.check balance 5.check all transcations exitt");
        while(true){
        System.out.print("select the option :");
        int n=sc.nextInt();
        switch(n){
            case 1:
                  User u=atm.collectUserDetail();
                  manager.addUser(u);
                  break;
            case 2:
                  System.out.print("Enter your user name");
                  String name=sc.next();
                  User foundUser=null;
                  for(User us:manager.users){
                     if(us.getName().equalsIgnoreCase(name)){
                        foundUser=us;
                        break;
                     }
                  }
                  if(foundUser!=null){
                     System.out.print("Enter the amt you want to withdraw :");
                  int amt=sc.nextInt();
                      manager.withdraw(foundUser, amt);
                  }
                  else{
                       System.out.println("User not found");
                  }
                  break;

            case 3:
                  System.out.print("Enter your user name");
                  String named=sc.next();
                  User foundUserq=null;
                  for(User us:manager.users){
                     if(us.getName().equalsIgnoreCase(named)){
                        foundUserq=us;
                        break;
                     }
                  }
                  if(foundUserq!=null){
                     System.out.print("Enter the amt you want to deposit :");
                  int amt=sc.nextInt();
                      manager.depositAmt(foundUserq, amt);
                      break;
                  }
                  else{
                       System.out.println("User not found");
                  }
                  break;
           case 4:
                       System.out.print("Enter your user name");
                      String username=sc.next();
                      User uname=null;
                     for(User un:manager.users){
                            if(un.getName().equalsIgnoreCase(username)){
                                 uname=un;
                                 break;
                            }
                     }
                     if(uname!=null){
                        manager.checkBalance(uname);
                        break;
                     }
                      else{
                       System.out.println("User not found");
                  }
                     break;
          case 5:
                   System.out.print("Enter your user name");
                      String usern=sc.next();
                      User uname1=null;
                     for(User un:manager.users){
                            if(un.getName().equalsIgnoreCase(usern)){
                                 uname1=un;
                                 break;
                            }
                     }
                     if(uname1!=null){
                 manager.allTransactions(uname1);
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
