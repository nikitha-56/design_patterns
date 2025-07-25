package LoggerSystem;
import java.util.*;

class LoggerSystem{

    public interface Logger{
       void login(String message);
    }

    static class DatabaseLogin implements Logger{
        public void login(String message){
            System.out.println("[Database]"+ message);
        }
    }

     static class ConsoleLogin implements Logger{
        public void login(String message){
            System.out.println("[console]"+ message);
        }
    }

    static class FileLogin implements Logger{
        public void login(String message){
              System.out.println("[File]"+ message);
        }
    }

    static class LoggerFactory{
        public static Logger generateLogger(String type){
            if(type.equalsIgnoreCase("database")){
                return new DatabaseLogin();
            }
            else if(type.equalsIgnoreCase("console")){
                return new ConsoleLogin();
            }
            else if(type.equalsIgnoreCase("file")){
                return new FileLogin();
            }
            else{
                System.out.println("no such type exists");
                return null;
            }
        }
    }

    static class LoggerManager{
       private static LoggerManager instance;
       private Logger logger;

      private LoggerManager(Logger logger){
        this.logger=logger;
       }
       public static LoggerManager getInstance(Logger logger){
        if(instance==null){
            instance=new LoggerManager(logger);
        }
        return instance;
       }

       public void log(String message){
        logger.login(message);
       }
    }


      public static void main(String args[]){
           Logger l1=LoggerFactory.generateLogger("file");
           LoggerManager manager=LoggerManager.getInstance(l1);
           manager.log("user logged in");

      }
}