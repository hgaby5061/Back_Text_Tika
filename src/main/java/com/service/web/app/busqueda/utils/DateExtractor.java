package com.service.web.app.busqueda.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateExtractor {

    private static final Map<String, String> MESES = new HashMap<>();

    static {
        MESES.put("enero", "01");
        MESES.put("febrero", "02");
        MESES.put("marzo", "03");
        MESES.put("abril", "04");
        MESES.put("mayo", "05");
        MESES.put("junio", "06");
        MESES.put("julio", "07");
        MESES.put("agosto", "08");
        MESES.put("septiembre", "09");
        MESES.put("octubre", "10");
        MESES.put("noviembre", "11");
        MESES.put("diciembre", "12");
    }

    public static String convertDate(String text) {
        // Expresión regular mejorada para capturar diferentes formatos
        String regex = "(?:el\\s+dia\\s+)?(\\d{1,2})(?:ro\\.?|do\\.?|er\\.?|º)?\\.?\\s+de\\s+([a-z]+)\\s+(?:de|del)\\s+(\\d{4})";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text.toLowerCase().trim());

        if (matcher.find()) {
            try {
                String dia = matcher.group(1).replaceAll("\\D", ""); // Remover sufijos ordinales
                String mes = MESES.get(matcher.group(2).toLowerCase());
                String año = matcher.group(3);

                // Formatear con ceros a la izquierda
                dia = String.format("%02d", Integer.parseInt(dia));

                return String.format("%s-%s-%s", año, mes, dia);
            } catch (Exception e) {
                throw new IllegalArgumentException("Formato de fecha no válido: " + text);
            }
        }
        return null;
    }

    /*
     * public static LocalDate extraerFecha(String titulo) {
     * Pattern pattern = Pattern.compile("((\\d{1,2}) de (\\w+) del? (\\d{4}))");
     * Matcher matcher = pattern.matcher(titulo);
     * 
     * if (matcher.find()) {
     * String dia = matcher.group(2);
     * String mes = matcher.group(3);
     * String año = matcher.group(4);
     * 
     * DateTimeFormatter formatter =
     * DateTimeFormatter.ofPattern("d 'de' MMMM 'de' yyyy", new Locale("es", "ES"));
     * String fechaStr = String.format("%s de %s de %s", dia, mes, año);
     * return LocalDate.parse(fechaStr, formatter);
     * }
     * 
     * // ;
     * 
     * // Manejar casos específicos como "1ro de mayo del 2000"
     * pattern = Pattern.compile(
     * "\\b(\\d{1,2})(?:ro|do|er)?\\s+de\\s+([a-zA-Z]+)\\s+(?:de\\s+)?(\\d{4})\\b");
     * matcher = pattern.matcher(titulo);
     * 
     * if (matcher.find()) {
     * String mes = matcher.group(2);
     * String año = matcher.group(3);
     * 
     * DateTimeFormatter formatter =
     * DateTimeFormatter.ofPattern("'1ro de' MMMM 'de' yyyy",
     * new Locale("es", "ES"));
     * String fechaStr = String.format("1ro de %s de %s", mes, año);
     * return LocalDate.parse(fechaStr, formatter);
     * }
     * 
     * // Manejar casos específicos como "el dia 16 de octubre de 1998"
     * pattern = Pattern.compile("(el dia (\\d{1,2}) de (\\w+) de (\\d{4}))");
     * matcher = pattern.matcher(titulo);
     * 
     * if (matcher.find()) {
     * String dia = matcher.group(2);
     * String mes = matcher.group(3);
     * String año = matcher.group(4);
     * 
     * DateTimeFormatter formatter =
     * DateTimeFormatter.ofPattern("'el dia' d 'de' MMMM 'de' yyyy",
     * new Locale("es", "ES"));
     * String fechaStr = String.format("el dia %s de %s de %s", dia, mes, año);
     * return LocalDate.parse(fechaStr, formatter);
     * }
     * 
     * return null;
     * }
     */
}
