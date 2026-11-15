package study.apps;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.core.JdbcTemplate;
@Configuration
class Wiring {
 @Bean Domain domain(){return new Domain(new PostgresStore(new JdbcTemplate(Database.source())));}
}
