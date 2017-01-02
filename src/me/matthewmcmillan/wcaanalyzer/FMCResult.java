package me.matthewmcmillan.wcaanalyzer;

public class FMCResult extends Result {
    public FMCResult(String resultString, Competition comp, String round) {
        super(resultString, comp, round);
    }

    @Override
    void customParseResultString(String resultString) {
        this.moves = Double.parseDouble(resultString);
    }

    private double moves;

    @Override
    public String customToString() {
        if (moves % 1 == 0) {
            return String.valueOf((int)moves);
        } else {
            return String.valueOf(moves);
        }
    }

    @Override
    double toGraphableValue() {
        return moves;
    }

    @Override
    public int customCompareTo(Result other) {
        return new Double(this.moves).compareTo(new Double(((FMCResult)other).moves));
    }
}
