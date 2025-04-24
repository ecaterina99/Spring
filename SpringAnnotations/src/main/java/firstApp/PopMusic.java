package firstApp;

import org.springframework.stereotype.Component;

@Component("somePopMusic")

public class PopMusic implements Music{
    @Override
    public String getSong() {
        return "Love of my life";
    }
}
