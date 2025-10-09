package vidio.editor.service;

import java.util.ArrayList;
import java.util.List;

public class modTime {

    public static List<int[]> getTime(String input) {
        List<int[]> result = new ArrayList<>();
        if (input == null || input.isEmpty()) return result;

        String[] ranges = input.split("\\],\\s*\\[");

        for (String range : ranges) {
            range = range.replaceAll("\\[|\\]", "").trim();
            if (range.isEmpty()) continue;
            String[] parts = range.split(",");
            int start = Integer.parseInt(parts[0].trim());
            int end = Integer.parseInt(parts[1].trim());
            result.add(new int[]{start, end});
        }
        return result;
    }

    public static List<int[]> getRemainTime(String exclude, int endTime) {
        List<int[]> excludeRanges = getTime(exclude);
        List<int[]> result = new ArrayList<>();
        int current = 0;

        for (int[] ex : excludeRanges) {
            if (current < ex[0]) {
                result.add(new int[]{current, ex[0] - 1});
            }
            current = ex[1] + 1;
        }

        if (current <= endTime) {
            result.add(new int[]{current, endTime});
        }
        return result;
    }
}


    //테스트
//    public static void main(String[] args) {
//        String input = "[2, 8], [50, 100], [120, 150]";
//        int endTime = 180;
//
//        List<int[]> remaining = getRemainingTime(input, endTime);
//
//        for (int[] range : remaining) {
//            System.out.println("[" + range[0] + ", " + range[1] + "]");
//        }
//    }

