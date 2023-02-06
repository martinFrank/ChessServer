package com.github.martinfrank.games.chessserver.server.model.chess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Board {

    private transient final Field[][] fields = createFields();
    public final transient Map<Field, Figure> lineUp = new HashMap<>();
    public final List<Figure> beatenFigures = new ArrayList<>();

    private Field[][] createFields() {
        Field[][] fields = new Field[8][8];
        for (int column = 0; column < 8; column ++){
            for (int row = 0; row < 8; row ++){

            }
        }
        return fields;
    }

}
