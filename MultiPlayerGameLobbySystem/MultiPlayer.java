import java.util.*;
class User{
    private String name;
    private String country;
    boolean gameCreator;
    User(String name,String country){
        this.name=name;
        this.country=country;
        this.gameCreator=false;
    }

    public String getName(){
        return name;
    }

    public String getCountry(){
        return country;
    }

    public void createGame(){
         gameCreator=true;
    } 

    public boolean getCreator(){
        return gameCreator;
    }
}


class GameManager{
    private static GameManager instance;
    private int maxnoOfUsers;
    private int joinedUsers;
    private List<User> users;
    private List<Observer> observers;
    private GameManager(int maxnoOfUsers){
        this.maxnoOfUsers=maxnoOfUsers;
        users=new ArrayList<>();
        observers=new ArrayList<>();
    } 
    public static GameManager getInstance(int maxnoOfUsers){
          if(instance==null){
            instance=new GameManager(maxnoOfUsers);
          }
          return instance;
    }

    public void addObserver(Observer o){
        observers.add(o);
    }

    public void removeObserver(Observer o){
        observers.remove(o);
    }

    public void addUser(User u){
        users.add(u);
       joinedUsers++;
       for(Observer o:observers){
        if(o instanceof NotifyToUser || o instanceof NotifyToGameCreator){
          o.notify(u);
        }
    }
    if(joinedUsers==maxnoOfUsers){
        for(Observer o:observers){
            if(o instanceof NotifyToUsers){
                o.notify(u);
            }
        }
    }
    System.exit(0);
       }
    }

interface Observer{
    void notify(User u);
}

class NotifyToGameCreator implements Observer{
    public void notify(User u){
        System.out.println("Notification to Game creator " + u.getName()+ " joined the game " );
    }
}

class NotifyToUser implements Observer{
    public void notify(User u){
        System.out.println("Notification to user : You joined successfully");
    }
}

class NotifyToUsers implements Observer{
    public void notify(User u){
        System.out.println("Notification to Users : All Users Joined the Game,Starting the Game.....");
        System.exit(0);
    }
}

public class MultiPlayer {
    public static Scanner sc=new Scanner(System.in);
    GameManager manager=GameManager.getInstance(4);
    public User collectUserDetails(){
        System.out.print("Enter User name :");
       String name=sc.next();
       System.out.print("Enter country: ");
       String country=sc.next();
       User u=new User(name, country);
       return u;
        }

    public static void main(String[] args) {
        GameManager manager=GameManager.getInstance(4);
         MultiPlayer game=new MultiPlayer();
         List<User> users=new ArrayList<>();
         while(true){
        System.out.println("All operations in the game :");
        System.out.println("1.Join the game,2.Create a Game,3.Add Observers,4 .Exit");
        System.out.print("Enter the operation:" );
        int n=sc.nextInt();
        switch(n){
            case 1:User u=game.collectUserDetails();
                    users.add(u);
                    manager.addUser(u);
                    break;
            case 2:
            System.out.print("Enter the user details to make the game creator :");
            String name=sc.next();
            boolean found=false;
              for(User us:users){
                if(us.getName().equalsIgnoreCase(name)){
                    us.createGame();
                    System.out.println(name + "is the game creator");
                    found=true;
                    break;
                }
              }
              if(!found){
                System.out.println("User not found");
              }
              break;
            case 3:
            System.out.println("Enter the observer number to add:");
            System.out.println("1.NotifyToGameCreator");
            System.out.println("2.NotifyToUser");
             System.out.println("3.NotifyToUsers");
         int type=sc.nextInt();
          Observer o=null;
          switch(type){
            case 1:o=new NotifyToGameCreator(); break;
            case 2:o=new NotifyToUser(); break;
            case 3:o=new NotifyToUsers() ; break;
            default :System.out.println("Invalid observer type "); return;
          }
          manager.addObserver(o);
              System.out.println("Observer added");
             break;
        case 4:System.out.println("Exiting the game...");
             return;

        default:
        System.out.println("Invalid .pls try again");
        return;
        }
    }
         
    }
}