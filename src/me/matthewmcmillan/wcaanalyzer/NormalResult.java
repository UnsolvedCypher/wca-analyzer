package me.matthewmcmillan.wcaanalyzer;

public class NormalResult implements Result{
    public NormalResult(String resultString, String comp, String round) {
        if (resultString.equals("DNF")) {
            DNF = true;
        } else if (resultString.equals("DNS")) {
            DNS = true;
        } else if (resultString.contains(":")) {
            minutes = Integer.parseInt(resultString.split(":")[0]);
            seconds = Integer.parseInt(resultString.split(":")[1].split("\\.")[0]);
            nanoSeconds = Integer.parseInt(resultString.split(":")[1].split("\\.")[1]);
        } else {
            try {
                minutes = 0;
                seconds = Integer.parseInt(resultString.split("\\.")[0]);
                nanoSeconds = Integer.parseInt(resultString.split("\\.")[1]);
            } catch (Exception e) {
                System.out.println("error parsing " + resultString + ", current comp is " + comp);
            }
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

    private int toNanoSeconds() {
        return nanoSeconds + seconds * 100 + minutes * 6000;
    }

    @Override
    public String toString() {
        if (DNF) {
            return "DNF";
        } else if (DNS) {
            return "DNS";
        } else {
            String minutesString = (minutes == 0) ? "" : minutes + ":";
            String secondsString = (minutes != 0 && seconds < 10) ? "0" + seconds : "" + seconds;
            secondsString = (secondsString.equals("0")) ? "00" : secondsString;
            String nanoString = (nanoSeconds < 10) ? ".0" + nanoSeconds : "." + nanoSeconds;
            return minutesString + secondsString + nanoString;
        }
    }

    private int minutes;
    private int seconds;
    private int nanoSeconds;
    private boolean DNF = false, DNS = false;

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
            return new Integer(this.toNanoSeconds()).compareTo(new Integer (((NormalResult)other).toNanoSeconds()));
        }
    }

    public boolean isDNF() {
        return DNF;
    }

    public boolean isDNS() {
        return DNS;
    }
}
