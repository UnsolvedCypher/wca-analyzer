package me.matthewmcmillan.wcaanalyzer;

import javafx.util.StringConverter;

public class NormalResult extends Result {
    public NormalResult(String resultString, Competition comp, String round) {
        super(resultString, comp, round);
    }

    @Override
    void customParseResultString(String resultString) {
        try {
            minutes = resultString.contains(":") ? Integer.parseInt(resultString.split(":")[0]) : 0;
            resultString = resultString.substring(resultString.indexOf(":") + 1);
            seconds = Integer.parseInt(resultString.split("\\.")[0]);
            centiseconds = Integer.parseInt(resultString.split("\\.")[1]);
        } catch (Exception e) {
            System.out.println("exception with the following input string: ");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private double toSeconds() {
        return centiseconds * 0.01 + seconds + minutes * 60;
    }

    @Override
    double toGraphableValue() {
        return toSeconds();
    }

    @Override
    public String customToString() {
        String minutesString = (minutes == 0) ? "" : minutes + ":";
        String secondsString = (minutes != 0 && seconds < 10) ? "0" + seconds : "" + seconds;
        secondsString = (secondsString.equals("0")) ? "00" : secondsString;
        String nanoString = (centiseconds < 10) ? ".0" + centiseconds : "." + centiseconds;
        return minutesString + secondsString + nanoString;
    }

    private int minutes, seconds, centiseconds;

    @Override
    public int customCompareTo(Result other) {
        return new Double(this.toSeconds()).compareTo(new Double (((NormalResult)other).toSeconds()));
    }

    public static StringConverter<Number> getStringConverter() {
        return new StringConverter<Number>() {
            @Override
            public String toString(Number object) {
                Double asDouble = (Double)object;
                int minutes = (int)Math.floor(asDouble / 60);
                int seconds = (int)Math.floor(asDouble % 60);
                int centiseconds = (int)Math.floor(asDouble * 100 % 100);
                String minutesString = (minutes == 0) ? "" : minutes + ":";
                String secondsString = (minutes != 0 && seconds < 10) ? "0" + seconds : "" + seconds;
                secondsString = (secondsString.equals("0")) ? "00" : secondsString;
                String nanoString = (centiseconds < 10) ? ".0" + centiseconds : "." + centiseconds;
                return minutesString + secondsString + nanoString;
            }

            @Override
            public Double fromString(String string) {
                return new NormalResult(string, null, null).toGraphableValue();
            }
        };
    }

    public static StringConverter<Double> getDoubleStringConverter() {
        return new StringConverter<Double>() {
            @Override
            public String toString(Double object) {
                Double asDouble = object;
                int minutes = (int)Math.floor(asDouble / 60);
                int seconds = (int)Math.floor(asDouble % 60);
                int centiseconds = (int)Math.floor(asDouble * 100 % 100);
                String minutesString = (minutes == 0) ? "" : minutes + ":";
                String secondsString = (minutes != 0 && seconds < 10) ? "0" + seconds : "" + seconds;
                secondsString = (secondsString.equals("0")) ? "00" : secondsString;
                String nanoString = (centiseconds < 10) ? ".0" + centiseconds : "." + centiseconds;
                return minutesString + secondsString + nanoString;
            }

            @Override
            public Double fromString(String string) {
                return new NormalResult(string, null, null).toGraphableValue();
            }
        };
    }
}