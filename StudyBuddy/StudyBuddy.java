import java.time.LocalDateTime;
import java.util.*;

class Student{
  private String sid;
  private String sname;
  private String subject;
  private LocalDateTime createSessiontime;
  private String mode;
  Student(String sid,String sname){
    this.sid=sid;
    this.sname=sname;
  }

  public String getSid(){
    return sid;
  }

  public String getSname(){
    return sname;
  }

}

class Manager{
    private static Manager instance;
    Session session;
    List<Student> students;
    List<Session> sessions;
    List<Observer> observers;
    Manager(){
        sessions=new ArrayList<>();
        students=new ArrayList<>();
        observers=new ArrayList<>();
    }
    public static Manager getInstance(){
        if(instance==null){
        instance=new Manager();
        }
        return instance;
    }

    public void addStudent(Student s){
        students.add(s);
    }
   
    public void addObserver(Observer o){
        observers.add(o);
    }
    public void creatingSession(Student s,String sid,LocalDateTime st,LocalDateTime et,String sub,Mode mode){
        Session newSession=new Session();
         newSession.createSession(s,sid, st, sub, et ,mode);
         sessions.add(newSession);
    }

    public void checkAllActiveSessions(){
        for(Session session:sessions){
            if(session.getSatus().equals(Status.CREATED)){
                   System.out.println(session.getId() +" "+ session.getSubject());
            }
        }
   }

    public void checkAllCompleteSessions(){
        for(Session session:sessions){
            if(session.getSatus().equals(Status.LOCKED)){
                   System.out.println(session.getId() +" "+ session.getSubject());
            }
        }
   }

   public void checkStudentsInSession(Session session){
    List<Student> part=new ArrayList<>();
          if(sessions.contains(session)){
            part= session.getStudents();
          }

          for(Student s:part){
           System.out.println(s.getSname());
          }
   }

   public void markSessionAsEnded(){
    for(Session session:sessions){
        LocalDateTime ctime=LocalDateTime.now();
        if(ctime.isAfter(session.getEndTime())){
            session.markComplete();
            for(Observer o:observers){
                o.notify(session.getId()+" : session Ended");
            }
        }
    }
   }

   public void JoinSession(String sessionId,String sub,LocalDateTime st,Student s){
    for(Session session:sessions){
        if(session.getId().equals(sessionId)){
            if(st.isAfter(session.getEndTime())){
                return;
            }
            session.JoinSession(sessionId,sub,st,s);
        }
    }
   }

}

 enum Mode{
        ONLINE,OFFLINE
    }

    enum Status{
        CREATED,LOCKED
    }

class Session{
    private List<Student> students;
    private String sessionId;
    private String subject;
      private LocalDateTime stime;
    private LocalDateTime endTime;
   private Mode mode;
   private Status status;

   Session(){
     students=new ArrayList<>();
   }
  public void  createSession(Student s,String sessionId,LocalDateTime stime,String subject,LocalDateTime etime,Mode mode){
    this.sessionId=sessionId;
    this.stime=stime;
    this.endTime=etime;
    this.subject=subject;
    this.mode=mode;
    students.add(s);
    status=Status.CREATED;
   }

   public String getSubject(){
    return subject;
   }

   public LocalDateTime getEndTime(){
    return endTime;
   }

   public String getId(){
    return sessionId;
   }
   public Status getSatus(){
    return status;
   }

   public void markComplete(){
    status=status.LOCKED;
   }

   public List<Student> getStudents(){
    return students;
   }

   public void JoinSession(String sessionId,String subject,LocalDateTime currtime,Student s){
    if(students.size()>=2){
        System.out.println("Session Capacity reached.Sorry you cant Join..");
        return;
    }
       students.add(s); 
       if(students.size()==2) status=status.LOCKED;
   }

}

interface Observer{
    void notify(String msg);
}

class NotifyToManager implements Observer{
    public void notify(String msg){
        System.out.println("Notification to manager :" + msg);
    }
}

class NotifyToStudent implements Observer{
    public void notify(String msg){
        System.out.println("Notification to Student :" + msg);
    }
}



public class StudyBuddy {
    public static void main(String[] args) {
        Student s1=new Student("22bcb", "nikki");
         Student s2=new Student("22bcb7090", "cherry");
          Student s3=new Student("22bcb23", "dev");
          Manager manager=Manager.getInstance();
          manager.addStudent(s1);
          manager.addStudent(s2);
          manager.addStudent(s3);
          Observer o1=new NotifyToManager();
          Observer o2=new NotifyToStudent();
          manager.addObserver(o2);
          manager.addObserver(o1);

          manager.creatingSession(s3,"12",LocalDateTime.now(),LocalDateTime.now().plusSeconds(5),"DSA", Mode.ONLINE);
         manager.creatingSession(s1,"13",LocalDateTime.now(),LocalDateTime.now().plusMinutes(20),"DBMS", Mode.ONLINE);
         manager.creatingSession(s2,"1",LocalDateTime.now(),LocalDateTime.now().plusMinutes(5),"oops", Mode.OFFLINE);
          manager.checkAllActiveSessions();
          manager.checkAllCompleteSessions();
                    manager.markSessionAsEnded();
          manager.JoinSession("12","DSA",LocalDateTime.now(),s2);
        
    }
}
