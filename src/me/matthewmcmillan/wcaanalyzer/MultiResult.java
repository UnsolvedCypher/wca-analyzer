package me.matthewmcmillan.wcaanalyzer;

public class MultiResult extends Result{
    public MultiResult(String resultString, Competition comp, String round) {
        super(resultString, comp, round);
    }

    @Override
    void customParseResultString(String resultString) {
        String cubesFraction = resultString.split(" ")[0];
        String time = resultString.split(" ")[1];
        this.minutes = Integer.parseInt(time.split(":")[0]);
        this.seconds = Integer.parseInt(time.split(":")[1]);
        this.solved = Integer.parseInt(cubesFraction.split("/")[0]);
        this.total = Integer.parseInt(cubesFraction.split("/")[1]);
        this.points = solved - (total - solved);
    }

    @Override
    public String customToString() {
        return solved + "/" + total + " " + minutes + ":" + seconds;
    }

    private int minutes, seconds, solved, total, points;

    private int toSeconds() {
        return minutes * 60 + seconds;
    }

    int customCompareTo(Result other) {
        MultiResult asMulti = (MultiResult) other;
        if (this.points != asMulti.points) {
            return new Integer(this.toSeconds()).compareTo(new Integer(asMulti.toSeconds()));
        } else {
            return new Integer(this.points).compareTo(asMulti.points);
        }
    }
}
