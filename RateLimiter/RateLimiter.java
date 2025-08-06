import java.util.*;
public class RateLimiter {

    class User{
        private String name;
        private Deque<Long> timestamps;
        public int requests;
        User(String name){
            this.name=name;
             requests=0;
             timestamps=new LinkedList<>();
        }

        public String getName(){
            return name;
        }

        public boolean allowRequest(int maxReq,int timeWindowInSec){
            long currTime=System.currentTimeMillis()/1000;
            while(!timestamps.isEmpty() && currTime-timestamps.peekFirst()>=timeWindowInSec){
                timestamps.removeFirst();
            }
            if(timestamps.size()<maxReq){
                System.out.println(currTime);
                timestamps.addLast(currTime);
                return true;
            }
            return false;
        }
    }

    class RateLimitManager{
        private int maxRequests;
        private int timeWindowInSec;
        Map<String,User> users;
         RateLimitManager(int maxReq,int timeWindowinSec){
           this.maxRequests=maxReq;
           this.timeWindowInSec=timeWindowinSec;
           users=new HashMap<>();
        }
        RateLimitManager(int maxReq){
            this.maxRequests=maxReq;
        }
        

        public void request(String userName){
           users.putIfAbsent(userName,new User(userName));
           User u=users.get(userName);
          if( u.allowRequest(maxRequests, timeWindowInSec)){
               System.out.println("Allowed :  Requested is allowed  for " + u.getName() );
          }
          else{
             System.out.println("Blocked : Request is blocked for "+ u.getName());
          }

        }
        
     
    }
    public static void main(String[] args) throws InterruptedException{
        RateLimiter r1=new RateLimiter();
         User u1=r1.new User("nikki");
        RateLimitManager manager=r1.new RateLimitManager(3,10);

        String user="nikki";
        for(int i=0;i<5;i++){
            manager.request(user);
            Thread.sleep(1000);
        }
    }
}
