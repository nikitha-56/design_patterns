import java.time.LocalDateTime;
import java.util.*;

class User{
    private String name;
    private long phoneNo;
    private float wt;
    private float ht;
    private int noOfRulesCompleted;
    User(String name,long phoneNo,float wt,float ht){
        this.name=name;
        this.phoneNo=phoneNo;
        this.wt=wt;
        this.ht=ht;
        noOfRulesCompleted=0;
    }

    public String getName(){
        return name;
    }
    public long getPhoneNo(){
        return phoneNo;
    }

    public float getWt(){
        return wt;
    }

    public float getHt(){
        return ht;
    }
    public int noOfRulesCompleted(){
        return noOfRulesCompleted;
    }

    public void increaseRuleCnt(){
        noOfRulesCompleted+=1;
    }
}

class UserProgress{
    private User user;
    private Challenge challenge;
    private Set<Integer> completedRuleIds;

    UserProgress(User user,Challenge challenge){
        this.user=user;
        this.challenge=challenge;
        this.completedRuleIds=new HashSet<>();
    }

    public void completeRule(int id){
        completedRuleIds.add(id);
    }

    public int getCompletedCnt(){
          return completedRuleIds.size();
    }

    public User getUser(){
        return user;
    }

}

class Rule{
    int ruleId;
    String ruleDescription;
    boolean ruleAsCompleted;
    Rule(int ruleId,String ruleDescription){
        this.ruleId=ruleId;
        this.ruleDescription=ruleDescription;
        this.ruleAsCompleted=false;
    }

}

class Challenge{
    private String chalName;
    private LocalDateTime stime;
    private LocalDateTime etime;
    private List<Rule> rules;
    List<User> users;
    Challenge(String chalName,LocalDateTime st,LocalDateTime et){
        this.chalName=chalName;
        this.stime=st;
        this.etime=et;
        rules=new ArrayList<>();
        users=new ArrayList<>();
    }


    public String getName(){
        return chalName;
    }

    public LocalDateTime getStartTime(){
        return stime;

    }

    public LocalDateTime getEndTime(){
        return etime;
    }

    public List<Rule> getRules(){
        return rules;
    }

    public void addRule(Rule rule){
        rules.add(rule);
    }

    public void addUserToChallenge(User u){
        users.add(u);
    }

    public void removeUserFromChallenge(User u){
        users.remove(u);
    }


}

class Manager{
    private static  Manager instance;
     List<User> users;
     List<Challenge> challenges;
     List<Rule> rules;
     List<Observer> observers;
    Map<User,UserProgress> trackProgress;
    Manager(){
         users=new ArrayList<>();
         challenges=new ArrayList<>();
         observers=new  ArrayList<>();
          trackProgress=new HashMap<>();
          rules=new ArrayList<>();
    }

    public static Manager getInstance(){
        if(instance==null){
            instance=new Manager();
        }
        return instance;
    }

     public void ruleAsCompletedByUser(Rule rule,User u){
        UserProgress progress=trackProgress.get(u);
        if(progress!=null){
       progress.completeRule(rule.ruleId);
       u.increaseRuleCnt();
       System.out.println(u.getName()+" completed"+ progress.getCompletedCnt()+ " rules as of today");
        }
    }

    public void addRule(Challenge c,Rule r){
        c.addRule(r);
    }

    public List<User>  getLeaderBoard(){
       List<User> leaderboard=new ArrayList<>(users);
       leaderboard.sort((u1,u2)->u2.noOfRulesCompleted()-u1.noOfRulesCompleted());
       return leaderboard;
    }
    
    public void addUser(User u){
        users.add(u);
    }

    public void delteUser(User u){
        users.remove(u);
    }

    public void addChallenge(Challenge c){
        challenges.add(c);
    }

    public void removeChallenge(Challenge c){
        challenges.remove(c);
    }

    public void addObserver(Observer o){
        observers.add(o);
    }

    public void removeObserver(Observer o){
        observers.remove(o);
    }

    public void joinChallenge(User u,Challenge c){
       c.addUserToChallenge(u);
       trackProgress.put(u,new UserProgress(u, c));
       for(Observer o:observers){
        o.notify(u.getName()+" joined the challenge");
       }
    }

    public void leaveFromChallenge(User u,Challenge c){
       c.removeUserFromChallenge(u);
    }

}

interface Observer{
    void notify(String msg);
}

class NotifyToUser implements Observer{
    public void notify(String msg){
        System.out.println("Notification to User " +msg);
    }
}

class NotifyToManager implements Observer{
    public void notify(String msg){
        System.out.println("Notification to Manager " +msg);
    }
}


public class PersonalizedFitness {
    public static void main(String[] args) {
        User u1=new User("nikki", 987654, 23, 5);
         User u2=new User("cherry", 987654, 32, 5);
          User u3=new User("dev", 987456654, 32, 6);
          Manager manager=Manager.getInstance();
          Observer o1=new NotifyToUser();
          Observer o2=new NotifyToManager();
         manager.addUser(u3);
         manager.addUser(u2);
         manager.addUser(u1);
         manager.addObserver(o2);
           manager.addObserver(o1);
           Challenge c1=new Challenge("Running for 2 km for 10 days", LocalDateTime.now(), LocalDateTime.now().plusDays(30));
           manager.addChallenge(c1);
           Rule r1=new Rule(1,"walk in the ground");
            Rule r2=new Rule(2,"walk for alteast 30 min");
             Rule r3=new Rule(3,"try to jog if possible");
             manager.addRule(c1, r3);
             manager.addRule(c1, r2);
              manager.addRule(c1, r1);
         manager.joinChallenge(u3, c1);
          manager.joinChallenge(u2, c1);
         manager.ruleAsCompletedByUser(r3, u3);
        List<User> lb= manager.getLeaderBoard();
        for(int i=0;i<lb.size();i++){
            User u=lb.get(i);
            System.out.println(u.getName() + " : rank is "+ (i+1));
        }

    }
}
