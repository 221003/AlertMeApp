package com.example.alertmeapp.dummy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<DummyItem> ITEMS = new ArrayList<DummyItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, DummyItem> ITEM_MAP = new HashMap<String, DummyItem>();

    private static final int COUNT = 10;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createDummyItem(i));
        }
    }

    private static void addItem(DummyItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static DummyItem createDummyItem(int position) {
        return new DummyItem(String.valueOf(position), "Item " + position, makeDetails(position));
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class DummyItem {
        public final Long userId;
        public final Long alertTypeId;
        public final String title;
        public final String description;
        public final int number_of_votes;
        public final double latitude;
        public final double longitude;
        public final String expire_date;

        public DummyItem(Long userId, Long alertTypeId, String title, String description, int number_of_votes, double latitude, double longitude, String expire_date) {
            this.userId = userId;
            this.alertTypeId = alertTypeId;
            this.title = title;
            this.description = description;
            this.number_of_votes = number_of_votes;
            this.latitude = latitude;
            this.longitude = longitude;
            this.expire_date = expire_date;
        }

        @Override
        public String toString() {
            return "DummyItem{" +
                    "userId=" + userId +
                    ", alertTypeId=" + alertTypeId +
                    ", title='" + title + '\'' +
                    ", description='" + description + '\'' +
                    ", number_of_votes=" + number_of_votes +
                    ", latitude=" + latitude +
                    ", longitude=" + longitude +
                    ", expire_date='" + expire_date + '\'' +
                    '}';
        }
    }
}