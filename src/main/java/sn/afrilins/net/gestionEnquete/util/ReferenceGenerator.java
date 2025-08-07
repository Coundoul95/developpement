package sn.afrilins.net.gestionEnquete.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;


public class ReferenceGenerator {
    // Map qui conserve un compteur par préfixe + date (reset quotidien)
    private static final ConcurrentHashMap<String, AtomicInteger> counters = new ConcurrentHashMap<>();

    public static String generateReference() {
        return generateReference("REF");
    }

    public static String generateReference(String prefix) {
        LocalDate today = LocalDate.now();
        String year = String.valueOf(today.getYear());
        String monthDay = today.format(DateTimeFormatter.ofPattern("MMdd"));

        // Encodage court du timestamp
        String shortUnique = Long.toString(System.currentTimeMillis() % 100000000, 36).toUpperCase();

        return String.format("%s-%s-%s-%s", prefix, year, monthDay, shortUnique);
    }
//    public static String generateReference(String prefix) {
//        LocalDate today = LocalDate.now();
//        String year = String.valueOf(today.getYear());
//        String monthDay = today.format(DateTimeFormatter.ofPattern("MMdd"));
//        String key = prefix + "-" + year + "-" + monthDay;
//
//        // Récupère ou crée le compteur pour la clé du jour
//        AtomicInteger counter = counters.computeIfAbsent(key, k -> new AtomicInteger(0));
//
//        // Incrémente le compteur et formate en 3 chiffres
//        String increment = String.format("%03d", counter.incrementAndGet());
//
//        return String.format("%s-%s-%s-%s", prefix, year, monthDay, increment);
//    }
}
