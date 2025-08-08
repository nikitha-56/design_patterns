import java.util.*;

class User{
    private String name;
    private String email;
    private long phoneNo;

    User(String name,String email,long phoneNo){
        this.name=name;
        this.email=email;
        this.phoneNo=phoneNo;
    }

    public String getName(){
        return name;
    }
    public String getEmail(){
        return email;
    }

    public long getPhoneNo(){
          return phoneNo;
    }

}

   class Book{
   private String name;
   private String author;
    private int id;
    private int cnt;
    private String category;
    private int price;

    Book(String name,String author,int id,String category,int cnt,int price){
        this.name=name;
        this.author=author;
        this.id=id;
        this.category=category;
        this.cnt=cnt;
        this.price=price;
    }

    public void increaseCnt(){
        cnt++;
    }

    public void decreaseCnt(){
        cnt--;
    }

    public String getName(){
        return name;
    }

    public String getAuthor(){
        return author;
    }

    public int getId() {
        return id;
    }

    public String getByCategory(){
        return category;
    }

    public boolean isBookAvailable(){
        return cnt>0;
    }

    public int getPrice(){
        return price;
    }

   }

   class Manager{
    Order order;
    private static Manager instance;
    List<User> users;
    List<Book> books;
    List<Observer> observers;
    HashMap<String,List<Book>> basedOnAuthor;
    HashMap<String,List<Book>> basedOncategory;
     HashMap<String,Book> basedOnName;


       Manager(){
        users=new ArrayList<>();
        basedOnAuthor=new HashMap<>();
        basedOncategory=new HashMap<>();
        basedOnName=new HashMap<>();
        observers=new ArrayList<>();
       }

       public static Manager getInstance(){
               if(instance==null){
                instance=new Manager();
               }
               return instance;
       }

       public void getAllBooksBasedOnc(String categry){
          if(basedOncategory.containsKey(categry)){
            for(Book book: basedOncategory.get(categry)){
                System.out.print(book.getName()+" ");
            }
          }
       }

       public void getAllBooksBasedOnAuthor(String author){
          if(basedOnAuthor.containsKey(author)){
            for(Book book: basedOnAuthor.get(author)){
                System.out.print(book.getName()+" ");
            }
          }
       }

       public void getAllBooksBasedOnName(String name){
          if(basedOnName.containsKey(name)){
            System.out.print( basedOnName.get(name).getName()+" ");
          }
       }

       public void addObserver(Observer o){
        observers.add(o);
       }

       public void addUser(User u){
          users.add(u);
       }

       public void removeUser(User u){
          users.remove(u);
       }

       public void addBook(Book b){
        basedOnAuthor.computeIfAbsent(b.getAuthor(),k->new ArrayList<>()).add(b);
        basedOncategory.computeIfAbsent(b.getByCategory(),k->new ArrayList<>()).add(b);
        basedOnName.put(b.getName(),b);
        System.out.println("Added "+b.getName()+" Successfully");
       }

       public void notifyAll(String msg){
          for(Observer o:observers){
            o.notify(msg);
          }
       }

   }

   class PaymentFactory{
      public  static Payment generPayment(String type){
           if(type.equalsIgnoreCase("Upi")){
            return new UpiPayment();
           }
           else if(type.equalsIgnoreCase("card")){
            return new CardPayment();
           }

           else if(type.equalsIgnoreCase("cash")){
            return new CashPayment();
           }
           else{
            System.out.println("No such Payment exisstss..");
           }
           return null;
      }
   }

   class Order{
    private int orderid;
   private User user;
    private List<Book> books;
    private int tamt;
     boolean isPaid;
    Order(int oid,User user,List<Book> books){
        this.orderid=oid;
        this.user=user;
        this.books=books;
        this.tamt=calculateTotalAmt();
        this.isPaid=false;
    }

    public int calculateTotalAmt(){
        int sum=0;
        for(Book book:books){
            sum+=book.getPrice();
        }
           return sum;
    }

    public void confirmOrder(){
        for(Book book:books){
            book.decreaseCnt();
        }
    }

    public void cancelOrder(){
        for(Book book:books){
            book.increaseCnt();
        }
    }

    public int getId(){
        return orderid;
    }

    public void makePayment(Payment paymentmethod){
        paymentmethod.pay(tamt);
        confirmOrder();
        System.out.println("order "+ orderid +" places successfully");
        isPaid=true;
    }

    public int getTotalAmt(){
        return tamt;
    }

   }

   class Cart{
  private HashMap<User,List<Book>> cart;
   int totalAmt;

   Cart(){
    cart=new HashMap<>();
    totalAmt=0;
   }


   public boolean addToCart(User u,Book b){
    if(b.isBookAvailable()){
       cart.computeIfAbsent(u, k->new ArrayList<>()).add(b);
       totalAmt+=b.getPrice();
       System.out.println("Added to cart :"+ b.getName());
       return true;
   }
   System.out.println(b.getName() +" is out of stock");
   return false;

}

   public void removefromCart(User u,Book b){
        if(cart.containsKey(u)) {
          cart.get(u).remove(b);
          return;
        } 
        System.out.println(" not such item available in the cart");
   }

   public void getAllBooks(User u){
         for(Book book:cart.get(u)){
             System.out.print(book.getName()+" ");
         }  
   }

   public void PlaceOrder(User u){
        System.out.print("Your total amt is :"+ totalAmt);
         
   }

   public List<Book> getBooks(User u){
    return cart.getOrDefault(u,new ArrayList<>());
   }
   }

   interface Observer{
      void notify(String msg);
   }

   class NotifyToUser implements Observer{
    public void notify(String msg){
        System.out.println("Notification to user :" + msg);
    }
   }

   class NotifyToManager implements Observer{
    public void notify(String msg){
        System.out.println("Notification to Manager :" + msg);
    }
   }

   interface Payment{
    void pay(int amt);
   }

   class UpiPayment implements Payment{
       public void pay(int amt){
        System.out.println("Paid using upi :" +amt);
       }
   }

   class CashPayment implements Payment{
       public void pay(int amt){
        System.out.println("Paid using cash :" +amt);
       }
   }

   class CardPayment implements Payment{
       public void pay(int amt){
        System.out.println("Paid using card :" +amt);
       }
   }


class BookstoreSystem{
    public Scanner sc=new Scanner(System.in);
    
    public static void main(String[] args) {
    Manager manger=Manager.getInstance();
    Observer o11=new NotifyToManager();
    Observer o2=new NotifyToUser();
manger.addObserver(o11);
manger.addObserver(o2);
    User nikki=new User("nikki","nikithatalluri1@gmail.com",98765);
     User cherry=new User("cherry","cherrytalluri1@gmail.com",98765344);
     manger.addUser(cherry);
    Book book1 = new Book("The Alchemist", "Paulo Coelho", 101, "Fiction", 10, 300);
Book book2 = new Book("Clean Code", "Robert C. Martin", 102, "Programming", 5, 450);
Book book3 = new Book("1984", "George Orwell", 103, "Dystopian", 8, 250);
Book book4 = new Book("Atomic Habits", "James Clear", 104, "Self-Help", 12, 350);
Book book5 = new Book("The Hobbit", "J.R.R. Tolkien", 105, "Fantasy", 7, 400);
manger.addBook(book5);
manger.addBook(book4);
manger.addBook(book3);
manger.addBook(book2);
manger.addBook(book1);
Cart nc=new Cart();
manger.getAllBooksBasedOnName("Atomic Habits");
nc.addToCart(nikki, book5);
nc.addToCart(nikki, book3);
Order o1=new Order(1, nikki, nc.getBooks(nikki));
o1.calculateTotalAmt();
Payment payment=PaymentFactory.generPayment("upi");
o1.makePayment(payment);
manger.notifyAll("Order id  " + o1.getId()+"  placed Succesfully");

    }
}