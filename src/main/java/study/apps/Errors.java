package study.apps;
import java.util.Map;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
class Problem extends RuntimeException {
 final int status; Problem(int status,String code){super(code);this.status=status;}
}
@RestControllerAdvice
class Errors {
 @ExceptionHandler(Problem.class) ResponseEntity<?> problem(Problem e){return ResponseEntity.status(e.status).body(Map.of("error",e.getMessage()));}
 @ExceptionHandler(org.springframework.dao.DataAccessException.class) ResponseEntity<?> database(){return ResponseEntity.status(503).body(Map.of("error","database_unavailable"));}
}
class Rules {
 static String text(String value,int max){if(value==null||value.isBlank()||value.length()>max)throw new Problem(400,"invalid_text");return value.trim();}
 static String id(String value){String s=text(value,64);if(!s.matches("[A-Za-z0-9-]+"))throw new Problem(400,"invalid_identifier");return s;}
 static long amount(Long value){if(value==null||value<1||value>1000000)throw new Problem(400,"invalid_amount");return value;}
 static void authorize(String header,String token){
  if(token==null||token.isEmpty())throw new Problem(503,"token_unconfigured");
  if(header==null||!header.startsWith("Bearer "))throw new Problem(401,"unauthorized");
  try{var md=java.security.MessageDigest.getInstance("SHA-256");if(!java.security.MessageDigest.isEqual(md.digest(header.substring(7).getBytes(java.nio.charset.StandardCharsets.UTF_8)),md.digest(token.getBytes(java.nio.charset.StandardCharsets.UTF_8))))throw new Problem(401,"unauthorized");}
  catch(java.security.NoSuchAlgorithmException e){throw new IllegalStateException("digest_unavailable");}
 }
}
