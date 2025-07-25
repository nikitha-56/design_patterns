import java.util.*;
class PizzaOrderingSystem {
    static class Pizza{
        private final String base;
         private final String size;
         private final boolean isCheeseNeeded;
        private final boolean topings;
         private final String type;
          Pizza(Builder builder){
            this.base=builder.base;
            this.size=builder.size;
            this.isCheeseNeeded=builder.isCheeseNeeded;
            this.topings=builder.topings;
            this.type=builder.type;
          }

        static  class Builder{
            private final String base;
            private final String size;
            private boolean isCheeseNeeded=true;
             private  boolean topings=true;
             private String type="Nonveg";


           public  Builder(String base,String size){
                this.base=base;
                this.size=size;
            }

            public Builder isCheeseNeeded(boolean isCheeseNeeded){
               this.isCheeseNeeded=isCheeseNeeded;
               return this;
            }

             public Builder topings(boolean topings){
               this.topings=topings;
               return this;
            }

            public Builder type(String type){
                this.type=type;
                return this;
            }

            public Pizza build(){
                return new Pizza(this);
            }

          }

          public void display(){
            System.out.println("PIZZA ");
            System.out.println("base "+base);
             System.out.println("Size: " + size);
            System.out.println("Cheese: " + isCheeseNeeded);
            System.out.println("Toppings: " + topings);
            System.out.println("Type: " + type);
          }

          public int calculatePrize(){
            int prize=100;
            if(size.equalsIgnoreCase("medium")) prize+=50;
              if(size.equalsIgnoreCase("large")) prize+=100;
              if(isCheeseNeeded==true) prize+=30;
              if(topings==true) prize+=30;
              if(type.equalsIgnoreCase("nonveg")) prize+=50;
             return prize;
          }
    }
    interface Payment{
        void pay(int amt);
    }

    static class UpiPayment implements Payment{
        public void pay(int amt){
            System.out.println("paid using upi:" +amt);
        }
    }

     static class CardPayment implements Payment{
        public void pay(int amt){
            System.out.println("paid using card:" +amt);
        }
    }

    static class Customer{
        Payment payment;
        String name;
        String phone;
        String address;
        Customer(String name,String phone,String address){
            this.name=name;
            this.phone=phone;
            this.address=address;
        }

        public void pay(Payment payment,int amt){
          payment.pay(amt);
        }

    }

    static class Order{
        Customer customer;
        List<Pizza> pizzas=new ArrayList<>();

        Order(Customer customer){
            this.customer=customer;
        }

        public void addPizza(Pizza pizza){
            pizzas.add(pizza);
        }
        public int generateBill(){
         System.out.println(customer.name);
           System.out.println(customer.address);
             System.out.println(customer.phone);

             int total=0;
             for(Pizza p:pizzas){
                total+=p.calculatePrize();

                p.display();
             }
             System.out.println("your total is :" +total);
             return total;
        }
    }

    static class PaymentFacory{
        public static Payment generPayment(String type){
            if(type.equalsIgnoreCase("upi")){
                return new UpiPayment();
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

    public static void main(String[] args) {

        Customer c1=new Customer("niiki", "9390257021", "asddes");
        Order order=new Order(c1);
        Pizza pizza1= new Pizza.Builder("maida","medium")
                     .topings(true)
                     .type("veg")
                     .build();
      Pizza pizza2= new Pizza.Builder("maida","medium")
                     .topings(true)
                     .type("nonveg")
                     .build();
        order.addPizza(pizza2);
        order.addPizza(pizza1);
      int tot= order.generateBill();  
      Payment p1=PaymentFacory.generPayment("upi");
       c1.pay(p1,tot);
    }
}
