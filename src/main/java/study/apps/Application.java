package study.apps;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@SpringBootApplication
public class Application {
 public static void main(String[] args){
  if(args.length==1&&args[0].equals("migrate")){try{Database.migrate();return;}catch(Exception e){System.err.println("native_migration_failed");System.exit(1);}}
  SpringApplication.run(Application.class,args);
 }
}
