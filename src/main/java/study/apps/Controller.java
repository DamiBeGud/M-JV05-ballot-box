package study.apps;
import java.util.*;
import org.springframework.web.bind.annotation.*;
record PollInput(String id,String question,List<String> options){}
record VoteInput(String voter,String option){}
record Poll(String id,String question,List<String> options){}
interface Store {void ready();Poll create(Poll poll);void vote(String poll,String voter,String option);Map<String,Long> results(String poll);}
class Domain {
 final Store store;Domain(Store store){this.store=store;}
 Poll create(PollInput input){String id=Rules.id(input.id()),question=Rules.text(input.question(),160);if(input.options()==null||input.options().size()<2||input.options().size()>5)throw new Problem(400,"invalid_options");List<String> options=input.options().stream().map(Rules::id).toList();if(new HashSet<>(options).size()!=options.size())throw new Problem(400,"duplicate_options");return store.create(new Poll(id,question,options));}
 void vote(String poll,VoteInput input){store.vote(Rules.id(poll),Rules.id(input.voter()),Rules.id(input.option()));}
}
@RestController
class Controller {
 final Domain domain;Controller(Domain domain){this.domain=domain;}
 @GetMapping("/health/live")Map<String,String> live(){return Map.of("status","ok");}
 @GetMapping("/health/ready")Map<String,String> ready(){domain.store.ready();return Map.of("status","ready");}
 @PostMapping("/polls")@ResponseStatus(org.springframework.http.HttpStatus.CREATED)Poll create(@RequestBody PollInput input){return domain.create(input);}
 @PostMapping("/polls/{id}/votes")@ResponseStatus(org.springframework.http.HttpStatus.CREATED)Map<String,String> vote(@PathVariable String id,@RequestBody VoteInput input){domain.vote(id,input);return Map.of("status","recorded");}
 @GetMapping("/polls/{id}/results")Map<String,Long> results(@PathVariable String id){return domain.store.results(Rules.id(id));}
}
