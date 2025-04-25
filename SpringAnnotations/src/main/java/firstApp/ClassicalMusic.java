package firstApp;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Component("someClassicalMusic")
//@Scope("prototype")
public class ClassicalMusic implements Music {

    @PostConstruct
    public void doMyInit() {
        System.out.println("Doing my initialization");
    }

    @PreDestroy
    public void doMyDestroy() {
        System.out.println("Doing my destruction ClassicalMusic");
    }

    @Override
    public String getSong() {
        return "Spring Morning, Winter Night";
    }
}
