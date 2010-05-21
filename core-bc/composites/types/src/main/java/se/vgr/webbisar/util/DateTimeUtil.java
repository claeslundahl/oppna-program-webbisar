/**
 * 
 */
package se.vgr.webbisar.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 */
public class DateTimeUtil {
    public static final String W3C_TIME = "yyyy-MM-dd'T'HH:mm:ssZ";
    public static final String SWEDISH_DATE_TIME = "yyyy-MM-dd HH:mm";

    public static final String formatDateSwedish(Date date) {
        String formattedDate = "";
        if (date != null) {
            formattedDate = new SimpleDateFormat(SWEDISH_DATE_TIME).format(date);
        }
        return formattedDate;
    }

    public static final String formatDateW3C(Date date) {
        String formattedDate = "";
        if (date != null) {
            String timeStamp = new SimpleDateFormat(W3C_TIME).format(date);
            formattedDate = timeStamp.substring(0, 22) + ":" + timeStamp.substring(22);
        }
        return formattedDate;
    }

    public static final String getLastModifiedW3CDateTime(Date modifyTimestamp, Date createTimestamp) {
        Date lastModified = modifyTimestamp;

        if (modifyTimestamp == null) {
            lastModified = createTimestamp;
        }

        return formatDateW3C(lastModified);
    }
}
