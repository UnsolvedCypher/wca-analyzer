package me.matthewmcmillan.wcaanalyzer;

public interface Result extends Comparable<Result> {

    String getRound();

    String getComp();

    @Override
    int compareTo(Result other);

    @Override
    String toString();

    boolean isDNF();

    boolean isDNS();
}
