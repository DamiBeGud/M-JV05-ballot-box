package study.apps;
import java.util.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.dao.*;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
class PostgresStore implements Store {
 final JdbcTemplate jdbc;final TransactionTemplate tx;
 PostgresStore(JdbcTemplate jdbc){this.jdbc=jdbc;tx=new TransactionTemplate(new DataSourceTransactionManager(jdbc.getDataSource()));}
 public void ready(){jdbc.queryForList("SELECT id FROM polls LIMIT 1");jdbc.queryForList("SELECT voter FROM votes LIMIT 1");}
 public Poll create(Poll poll){try{return tx.execute(status->{jdbc.update("INSERT INTO polls(id,question) VALUES(?,?)",poll.id(),poll.question());for(String option:poll.options())jdbc.update("INSERT INTO poll_options(poll_id,code) VALUES(?,?)",poll.id(),option);return poll;});}catch(DuplicateKeyException e){throw new Problem(409,"duplicate_poll");}}
 public void vote(String poll,String voter,String option){try{jdbc.update("INSERT INTO votes(poll_id,voter,option_code) VALUES(?,?,?)",poll,voter,option);}catch(DuplicateKeyException e){throw new Problem(409,"duplicate_vote");}catch(DataIntegrityViolationException e){throw new Problem(400,"unknown_poll_or_option");}}
 public Map<String,Long> results(String poll){Map<String,Long> result=new TreeMap<>();jdbc.query("SELECT o.code,COUNT(v.voter) FROM poll_options o LEFT JOIN votes v ON v.poll_id=o.poll_id AND v.option_code=o.code WHERE o.poll_id=? GROUP BY o.code",r->{result.put(r.getString(1),r.getLong(2));},poll);if(result.isEmpty())throw new Problem(404,"not_found");return result;}
}
