package domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by Jakub Filipiak on 20.03.2019.
 */
@AllArgsConstructor
@ToString
public class Coordinates {

    @Setter @Getter
    private int x;

    @Setter @Getter
    private int y;
}
