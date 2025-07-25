package splitwize;
import java.util.*;
public class SplitWize {


    static class Split{
      User user;
      float amt;
      Split(User user,float amt){
        this.user=user;
        this.amt=amt;
      }
    }

    static class Expense{
         float amount;
         User paidBy;
         List<Split> splits;

         Expense(float amount, User paidBy, List<Split> splits) {
        this.amount = amount;
        this.paidBy = paidBy;
        this.splits = splits;
    }
    }

    static class ExpenseManager{
          
    }

    interface Payment{
        void pay(float amt);
    }

    static class UpiPayment implements Payment{
         public void pay(float amt){
            System.out.println("paid using upi paid"+ amt);
        }
    }

    static class CardPayment implements Payment{
         public void pay(float amt){
            System.out.println("paid using card paid"+ amt);
        }
    }

    static class PaymentFacory{
        public Payment generPayment(String type){
            if(type.equalsIgnoreCase("upi")){
                  return new UpiPayment();
            }
            else if(type.equalsIgnoreCase("card")){
                  return new CardPayment();
            }
            else{
                System.out.println("no such payment exists");
                return null;
            }
        }
    }


    static class User{
        String name;
        User(String name){
            this.name=name;
        }
    }
    public static void main(String[] args) {
        User u1=new User("nikki");
        User u2=new User("cherry");
    
        Split split=new Split();
        split.addUser(u1);
        split.addUser(u2);
        float amt=split.Splitamt(1000,2);

        PaymentFacory factory=new PaymentFacory();
        Payment p1=factory.generPayment("upi");
        p1.pay(amt);
       

    }
}
