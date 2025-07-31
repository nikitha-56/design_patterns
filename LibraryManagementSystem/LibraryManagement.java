import java.util.*;
class User{
    private final String userId;
    private final String userName;
    User(String userName,String userId){
        this.userName=userName;
        this.userId=userId;
    }

    public String getUserId(){
        return userId;
    }

    public String getName(){
        return userName;
    }

    public void getAllbooks(LibraryManager manager){
       manager.displayAllBooks();
       return;
    }
  
}

class Book{
    private final String bookId;
    private final String bookName;
    private int noOfBooks;
    Book(String bookId,String bookName,int noOfBooks){
        this.bookId=bookId;
        this.bookName=bookName;
        this.noOfBooks=noOfBooks;
    }

    public String getbookId(){
        return bookId;
    }
    public String getBookName(){
        return bookName;
    }

    public void decreaseCount(){
        noOfBooks--;
    }
    public void increaseCount(){
        noOfBooks++;
    }

    public boolean isBookAvailable(){
            return noOfBooks>0;
    }

}
class LibraryManager{
    private static LibraryManager instance;
    List<Book> books;
    HashMap<User,List<Book>> usertakenBooks=new HashMap<>();
    List<Observer> observers;
    List<User> users;
    LibraryManager (){
        books=new ArrayList<>();
        observers=new ArrayList<>();
    }
    public static LibraryManager getInstance(){
        if(instance==null){
            instance=new LibraryManager();
        }
        return instance;
    }

    public void addBook(Book book){
         books.add(book);
    }

    public void removeBook(Book book){
        books.remove(book);
    }

    public void displayAllBooks(){
        System.out.println("Available books right Now :");
        for(Book b:books){
            if(b.isBookAvailable()){
            System.out.println(b.getBookName());
            }
        }
    }

    public void addObserver(Observer o){
          observers.add(o);
    }

    public void removeObserver(Observer o){
          observers.remove(o);
    }

    public void notifyAll(String msg,User user,Book book){
        for(Observer o:observers){
            o.update(msg,user,book);
        }
    }

    public void borrowBook(User user,Book book){
        if(book.isBookAvailable()){
            book.decreaseCount();
            usertakenBooks.computeIfAbsent(user,k->new ArrayList<>()).add(book);
            notifyAll("Borrowed",user,book);
        }
        else{
            System.out.println("Book not available");
        }
    }

    public void returnBook(User user,Book book){
        book.increaseCount();
        notifyAll("returned", user, book);
    }
    }

    interface Observer{
        void update(String msg,User user,Book book);
    }

    class NotifyToUser implements Observer{
        public void update(String msg,User user,Book book){
           System.out.println("Notification to User : " + user.getName() +"  "+ msg+" " + book.getBookName() );
        }
    }

    class NotifyToLibrarian implements Observer{
        public void update(String msg,User user,Book book){
           System.out.println("Notification to Librarian : "+ user.getName() + " "+ msg +" " + book.getBookName() );
        }
    }



public class LibraryManagement {

    private final Scanner sc=new Scanner(System.in);
    private final List<User> users=new ArrayList<>();
    private final List<Book> books=new ArrayList<>();

    private User getUserByName(){
        sc.nextLine();
        System.out.print("Enter the user name: ");
        String name=sc.nextLine();
        for(User u:users){
          if(u.getName().equalsIgnoreCase(name)){
            return u;
          }
        }
        return null;
    }

    private Book getBookByName(){
        System.out.println("Enter book name: ");
        String bookName=sc.nextLine();
        for(Book b:books){
            if(b.getBookName().equalsIgnoreCase(bookName)){
                return b;
            }
        }
       return null;
    }



    private List<User> collectUsers(){
        System.out.print("Enter the no of users :");
        int n=sc.nextInt();
        sc.nextLine();
        for(int i=0;i<n;i++){
        System.out.print("Enter the username: ");
        String name=sc.nextLine();
        System.out.print("Enter the user Id :");
        String id=sc.nextLine();
        

        users.add(new User(name, id));
        }
        return users;
    }

    public List<Book> addBooks(LibraryManager manager){
        System.out.println("Enter how many books need to add: ");
        int n=sc.nextInt();
        sc.nextLine();
        for(int i=0;i<n;i++){
           System.out.print("Enter book name : ");
           String name=sc.nextLine();
           System.out.print("Enter book id :");
           String bookId=sc.nextLine();
           System.out.print("Enter total Number of books available :");
           int cap=sc.nextInt();
           sc.nextLine();

           Book b=new Book(bookId,name,cap);
           books.add(b);
           manager.addBook(b);
        }
        return books;
    }

    public void run(){
        collectUsers();
        LibraryManager manager=LibraryManager.getInstance();
         manager.addObserver(new NotifyToUser());
        manager.addObserver(new NotifyToLibrarian());
        addBooks(manager);
        while(true){
            System.out.println("LIBRARY MENU");
            System.out.println("1.View all books");
            System.out.println("2.Borrow a book");
            System.out.println("3.Return a book");
            System.out.println("4.Exit");
            System.out.println("Choose an option ");
            int choice=sc.nextInt();
            switch(choice){
                case 1:
                manager.displayAllBooks();
                break;
                case 2:{
                User user=getUserByName();
                Book book=getBookByName();
                if(user!=null && book!=null){
                  manager.borrowBook(user,book);
                }
                else{
                    System.out.println("Invalid user or book");
                }
                
                break;
            }
                case 3:
                User user=getUserByName();
                Book book=getBookByName();
                if(user!=null && book!=null){
                manager.returnBook(user,book);
                }
                else{
                    System.out.println("Invalid user or book ");
                }
                break;

                case 4:
                System.out.println("Exiting");
                return;
                default:
                System.out.println("Invalid choice .");

            }
        }
    }


    public static void main(String[] args) {
          new LibraryManagement().run();

    }
}
