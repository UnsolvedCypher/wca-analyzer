package me.matthewmcmillan.wcaanalyzer;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class WCAReader {
    private Document page;
    public WCAReader(String WCAID) {
        try {
            this.page = Jsoup.connect("https://www.worldcubeassociation.org/results/p.php?i=" + WCAID).timeout(10 * 1000).get();
        } catch (Exception e) {
            System.out.println("unable to connect");
        }
    }

    private LinkedHashMap<String, Event> getEventOrder() {
        try {
            LinkedHashMap<String, Event> events = new LinkedHashMap<>();
            boolean ready = false;
            for (Element eventNameElement : page.select("td.caption")) {
                String name = eventNameElement.text();
                if (ready && !name.equals("Rubik's Cube: Fewest moves") && !name.contains("Multi")) {
                    events.put(name, new Event(name));
                } else if (name.equals("History (Map)")) {
                    ready = true;
                }
            }
            return events;
        } catch (Exception e) {
            System.out.println("couldn't get event order");
            return null;
        }
    }

    public void readComps() throws Exception {
        try {
            HashMap<String, Competition> comps = new HashMap<>();
            Element table = null;
            for (Element data : page.select("tr")) {
                if (data.text().equals("Rubik's Cube")) {
                    table = data.parent().parent();
                }
            }
            String currentEventName = "";
            String currentComp = null;
            boolean ready = false;
            for (Element row : table.select("tr")) {
                if (row.select("td").toString().contains("<td colspan=\"8\">")) {
                    ready = false;

                } else if (row.select("td").hasClass("caption")) {
                    String eventName = row.select("td").select("a").text();
                    if(!eventName.equals("Map")) {
                        currentEventName = eventName;
                    }
                } else if (ready && !currentEventName.contains("Multi") && !currentEventName.equals("Rubik's Cube: Fewest moves")) {
                    Elements data = row.select("td");
                    String newComp = data.get(0).text();
                    if (!newComp.equals("" + (char)160)) {
                        currentComp = newComp;
                    }
                    String round = data.get(1).text();
                    int place = Integer.parseInt(data.get(2).text());
                    String average = data.get(5).text();
                    String rawResults = data.get(7).text();
                    if (comps.get(currentComp) == null) {
                        String compURL = data.get(0).select("a").get(0).attr("href");
                        comps.put(currentComp, new Competition(currentComp, getDateFromCompURL(compURL)));
                    }
                    if (rawResults.contains("/")) {
                        System.out.println("mbld is a piece of shit");
                        System.out.println(currentEventName);
                    }
                    comps.get(currentComp).addAttemptSequence(currentEventName, new AttemptSequence(round, currentComp, place, rawResults, average));
                } else if (!row.select("th").isEmpty()) {
                    for (Element header : row.select("th")) {
                        if (header.text().equals("Competition")) {
                            ready = true;
                        }
                    }
                }
            }
            Main.comps = new ArrayList<Competition>(comps.values());

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error reading competitions for given WCAID");
        }
    }

    public String getName() throws Exception {
        try {
            return page.select("h1").text();
        } catch (Exception e) {
            throw new Exception("Unable to read name from WCAID");
        }
    }

    public static LocalDate getDateFromCompURL(String URL) throws Exception {
        try {
            Document page = Jsoup.connect("https://www.worldcubeassociation.org" + URL).timeout(10 * 1000).get();
            String dateString = page.select("div.competition-info").first().select("dd").first().text();

            // use only first date from comps that span multiple dates
            if (dateString.contains(" - ")) {
                dateString = dateString.substring(0, dateString.indexOf(" - ")) + dateString.substring(dateString.length() - 6);
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d, yyyy");
            LocalDate date = LocalDate.parse(dateString, formatter);
            return date;

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Unable to read information from " + URL);
        }
    }

    public static String asPercent(double d) {
        return (Math.round(10000 * d) / 100) + "%";
    }

    public void readEvents(ArrayList<Competition> comps) {
        LinkedHashMap<String, Event> events = getEventOrder();
        for (Competition comp : comps) {
            for (Event event : comp.getEvents()) {
                Event toAddTo = events.get(event.getName());
                for (AttemptSequence sequence : event.getAttemptSequences()) {
                    toAddTo.addAttemptSequence(sequence);
                }
            }
        }
        Main.events = new ArrayList<>(events.values());
    }

    public static TreeMap<Integer, Integer> getYearTreeMap(ArrayList<Competition> comps) {
        TreeMap<Integer, Integer> yearsAndComps = new TreeMap<>();
        for (Competition comp : comps) {
            int year = comp.getDate().getYear();
            if (yearsAndComps.containsKey(year)) {
                yearsAndComps.put(year, yearsAndComps.get(year) + 1);
            } else {
                yearsAndComps.put(year, 1);
            }
        }
        return yearsAndComps;
    }

}
