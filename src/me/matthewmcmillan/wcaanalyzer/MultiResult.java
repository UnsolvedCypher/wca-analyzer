package me.matthewmcmillan.wcaanalyzer;

public class MultiResult implements Result{
    public MultiResult(String resultString, String comp, String round) {
        if (resultString.equals("DNF")) {
            DNF = true;
        } else if (resultString.equals("DNS")) {
            DNS = true;
        } else {
            String cubesFraction = resultString.split(" ")[0];
            String time = resultString.split(" ")[1];
            this.minutes = Integer.parseInt(time.split(":")[0]);
            this.seconds = Integer.parseInt(time.split(":")[1]);
            this.solved = Integer.parseInt(cubesFraction.split("/")[0]);
            this.total = Integer.parseInt(cubesFraction.split("/")[1]);
            this.points = solved - (total - solved);
        }
        this.comp = comp;
        this.round = round;
    }
    private String round, comp;

    public String getRound() {
        return round;
    }

    public String getComp() {
        return comp;
    }

    @Override
    public String toString() {
        if (DNF) {
            return "DNF";
        } else if (DNS) {
            return "DNS";
        } else {
            return solved + "/" + total + " " + minutes + ":" + seconds;
        }
    }

    private int minutes, seconds, solved, total, points;
    private boolean DNF = false, DNS = false;

    private int toSeconds() {
        return minutes * 60 + seconds;
    }

    @Override
    public int compareTo(Result other) {
        if (other == null) {
            return -1;
        } else if ((DNF || DNS) && !(other.isDNF() || other.isDNS())) {
            return 1;
        } else if (!(DNF || DNS) && (other.isDNF() || other.isDNS())) {
            return -1;
        } else if ((DNS && other.isDNS()) || (DNF && other.isDNF())) {
            return 0;
        } else if (DNS && other.isDNF()) {
            return 1;
        } else if (DNF && other.isDNS()) {
            return -1;
        } else {
            return compareMulti((MultiResult)other);
        }
    }

    private int compareMulti(MultiResult other) {
        if (this.points != other.points) {
            return new Integer(this.toSeconds()).compareTo(new Integer(other.toSeconds()));
        } else {
            return new Integer(this.points).compareTo(other.points);
        }
    }

    public boolean isDNF() {
        return DNF;
    }

    public boolean isDNS() {
        return DNS;
    }
}
