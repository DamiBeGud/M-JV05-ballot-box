package study.apps;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import javax.sql.DataSource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.flywaydb.core.Flyway;
class Database {
 static DataSource source(){
  try{
   URI uri=URI.create(System.getenv("DATABASE_URL"));
   if(!java.util.Set.of("postgresql","postgres").contains(uri.getScheme())||uri.getHost()==null||uri.getPath()==null||!uri.getPath().matches("/[a-zA-Z0-9_]+")||uri.getRawUserInfo()==null)throw new IllegalArgumentException();
   String[] auth=uri.getRawUserInfo().split(":",2);if(auth.length!=2)throw new IllegalArgumentException();
   String query=uri.getRawQuery();if(query!=null&&!query.matches("sslmode=(disable|require|verify-full)"))throw new IllegalArgumentException();
   String host=uri.getHost();if(host.contains(":")&&!host.startsWith("["))host="["+host+"]";
   var ds=new DriverManagerDataSource();ds.setDriverClassName("org.postgresql.Driver");
   ds.setUrl("jdbc:postgresql://"+host+":"+(uri.getPort()<0?5432:uri.getPort())+uri.getPath()+"?connectTimeout=2&socketTimeout=3"+(query==null?"":"&"+query));
   ds.setUsername(URLDecoder.decode(auth[0].replace("+","%2B"),StandardCharsets.UTF_8));
   ds.setPassword(URLDecoder.decode(auth[1].replace("+","%2B"),StandardCharsets.UTF_8));return ds;
  }catch(Exception e){throw new IllegalStateException("DATABASE_URL_invalid_or_missing");}
 }
 static void migrate(){Flyway.configure().dataSource(source()).locations("classpath:db/migration").cleanDisabled(true).load().migrate();}
}
