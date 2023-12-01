package Runner;

import java.util.logging.Level;

public class Answer extends Level{
    public static final Answer ANSWER = new Answer("ANSWER", 850);

    private Answer(String name, int level) {
        super(name, level);
    }
}