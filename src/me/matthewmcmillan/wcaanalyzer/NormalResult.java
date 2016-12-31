package me.matthewmcmillan.wcaanalyzer;

public class NormalResult extends Result {
    public NormalResult(String resultString, Competition comp, String round) {
        super(resultString, comp, round);
    }

    @Override
    void customParseResultString(String resultString) {
        if (resultString.contains(":")) {
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
    }

    public String getRound() {
        return round;
    }

    public Competition getComp() {
        return comp;
    }

    private int toNanoSeconds() {
        return nanoSeconds + seconds * 100 + minutes * 6000;
    }

    @Override
    public String customToString() {
        String minutesString = (minutes == 0) ? "" : minutes + ":";
        String secondsString = (minutes != 0 && seconds < 10) ? "0" + seconds : "" + seconds;
        secondsString = (secondsString.equals("0")) ? "00" : secondsString;
        String nanoString = (nanoSeconds < 10) ? ".0" + nanoSeconds : "." + nanoSeconds;
        return minutesString + secondsString + nanoString;
    }

    private int minutes;
    private int seconds;
    private int nanoSeconds;

    @Override
    public int customCompareTo(Result other) {
        return new Integer(this.toNanoSeconds()).compareTo(new Integer (((NormalResult)other).toNanoSeconds()));
    }
}
