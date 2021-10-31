package uk.gov.dwp.cmg.utils;

import org.apache.commons.lang3.RandomStringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static net.andreinc.mockneat.types.enums.CharsType.ALPHA_NUMERIC;
import static net.andreinc.mockneat.types.enums.StringType.NUMBERS;
import static net.andreinc.mockneat.unit.text.Strings.strings;
import static net.andreinc.mockneat.unit.types.Chars.chars;

public class DataGenerator {

    private String generateRandomNumber(int length) {
        return RandomStringUtils.randomNumeric(length);
    }

    Date date;

    public static double getRandomDoubleBetweenRange(double min, double max) {
        double x = (Math.random() * ((max - min) + 1)) + min;
        x = Math.round(x * 100.0) / 100.0;
        return x;
    }

    public static double round(Double amt) {
        BigDecimal bd = new BigDecimal(Double.toString(amt));
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static String idGenerator() {

        String num = strings()
                .size(1)
                .type(NUMBERS)
                .get();

        StringBuilder buff = new StringBuilder();

        buff.append("-").append(chars().upperLetters().get());

        String someNumbers = strings().size(4).types(NUMBERS).get();

        char someChar = chars().upperLetters().get();

        return (num + buff + someNumbers + someChar);

    }

    public static String processorIdGenerator() {

        String theFirstString = strings().get();

        return (theFirstString);

    }

    public static String formatDateAsUTC(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.ms'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdf.format(date);
    }
}


