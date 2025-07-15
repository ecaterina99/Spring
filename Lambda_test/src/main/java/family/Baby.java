package family;

import lombok.Data;

@Data
public class Baby {

    private Man father;
    private Woman mother;
    private String name;

    public Baby(Man father, Woman mother) {
        this.father = father;
        this.mother = mother;
    }
}
