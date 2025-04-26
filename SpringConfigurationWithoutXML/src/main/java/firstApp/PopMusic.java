package firstApp;

import org.springframework.stereotype.Component;


public class PopMusic implements Music{
    @Override
    public String getSong() {
        return "Love of my life";
    }
}
