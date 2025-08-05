import java.util.*;
class User{
    private String name;
    private String country;
    boolean gameCreator;
    User(String name,String country,boolean gameCreator){
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

    public void createGame(User u){
            u.gameCreator=true;
    } 

    public boolean getCreator(){
        return gameCreator;
    }
}

class GameManager{
    public static GameManager instance;
    int maxnoOfUsers;
    List<User> users;
    List<Observer> observers;
    GameManager(){}
    GameManager(int maxUsers){
        this.maxnoOfUsers=maxUsers;
        users=new ArrayList<>();
    } 
    public GameManager getInstance(GameManager manager){
          if(manager==null){
            instance=new GameManager();
          }
          return instance;
    }

    public void addObserver(Observer o){
        observers.add(o);
    }

    public void removeObserver(Observer o){
        observers.remove(o);
    }

    public boolean decrementUserCount(){
         maxnoOfUsers--;
         if(maxnoOfUsers<=0) return false;
         return true;  
    }

    public void addUser(User u){
        users.add(u);
      boolean status= decrementUserCount();
      for(Observer o:observers){
            if(o.getClass().equals(NotifyToGameCreator.class)){
                 o.notify(u);
            }
      }
      if(status==false){
         for(Observer o:observers){
            if(o.getClass().equals(NotifyToUsers.class)){
           o.notify(u);
         }
        }
      }
    }
}

interface Observer{
    void notify(User u);
}

class NotifyToGameCreator implements Observer{
    public void notify(User u){
        System.out.println(u.getName()+ "joined the game" );
    }
}

class NotifyToUser implements Observer{
    public void notify(User u){
        System.out.println("You joined successfully");
    }
}

class NotifyToUsers implements Observer{
    public void notify(User u){
        System.out.println("All Users Joined the Game,Starting the Game.....");
    }
}

public class MultiPlayer {

    public static void main(String[] args) {
        
    }
}
