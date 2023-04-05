package com.digdes.school;

import java.util.*;

public class JavaSchoolStarter {
    private List<Map<String, Object>> list = new ArrayList<>();

    public JavaSchoolStarter() {
    }

    public List<Map<String, Object>> execute(String request) throws Exception {
        valid(request);
        String[] str = request.toLowerCase().split(" ");
        List<Map<String, Object>> rsl = switch (str[0]) {
            case "insert" -> insert(request);
            case "delete" -> delete(request);
            case "select" -> select(request);
            case "update" -> update(request);
            default -> list;
        };
        return rsl;
    }

    private List<Map<String, Object>> insert(String request) {
        List<Map<String, Object>> rsl = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        String[] str = request.split(" ");
        for (int i = 2; i < str.length; i++) {
            String[] data = str[i].split("=");
            if (data[0].equalsIgnoreCase("'id'")) {
                if (data[1].endsWith(",")) {
                    map.put("id", Long.parseLong(data[1].substring(0, data[1].length() - 1)));
                } else {
                    map.put("id", Long.parseLong(data[1]));
                }
            }
            if (data[0].equalsIgnoreCase("'lastname'")) {
                if (data[1].endsWith(",")) {
                    map.put("lastName", data[1].substring(1, data[1].length() - 2));
                } else {
                    map.put("lastName", data[1].substring(1, data[1].length() - 1));
                }
            }
            if (data[0].equalsIgnoreCase("'age'")) {
                if (data[1].endsWith(",")) {
                    map.put("age", Long.parseLong(data[1].substring(0, data[1].length() - 1)));
                } else {
                    map.put("age", Long.parseLong(data[1]));
                }
            }
            if (data[0].equalsIgnoreCase("'cost'")) {
                if (data[1].endsWith(",")) {
                    map.put("cost", Double.parseDouble(data[1].substring(0, data[1].length() - 1)));
                } else {
                    map.put("cost", Double.parseDouble(data[1]));
                }
            }
            if (data[0].equalsIgnoreCase("'active'")) {
                if (data[1].endsWith(",")) {
                    map.put("active", Boolean.parseBoolean(data[1].substring(0, data[1].length() - 1)));
                } else {
                    map.put("active", Boolean.parseBoolean(data[1]));
                }
            }
        }
        list.add(map);
        rsl.add(map);
        return rsl;
    }

    private List<Map<String, Object>> update(String request) {
        List<Map<String, Object>> rsl;
        String reqInsert;
        if (request.toLowerCase().contains("where")) {
            String reqSelect = "SELECT WHERE " + request.substring(request.indexOf("where")).replace("where", "").trim();
            rsl = select(reqSelect);
            list.removeAll(rsl);
            reqInsert = request.substring(request.indexOf("VALUES"), request.indexOf("where")).replace("VAlUES", "").replace(",", "").trim();
            updateList(reqInsert, rsl);
            list.addAll(rsl);
        } else {
            reqInsert = request.substring(request.indexOf("VAlUES")).replace("VAlUES", "").replace(",", "").trim();
            updateList(reqInsert, list);
            return list;
        }
        return rsl;
    }

    private List<Map<String, Object>> delete(String request) {
        List<Map<String, Object>> rsl;
        if (request.toLowerCase().contains("where")) {
            rsl = select(request);
            list.removeAll(rsl);
        } else {
            rsl = new ArrayList<>(list);
            list.clear();
        }
        return rsl;
    }

    private List<Map<String, Object>> select(String request) {
        List<Map<String, Object>> rsl = new ArrayList<>();
        if (request.toLowerCase().contains("where") && request.toLowerCase().contains("or")) {
            rsl = selectOr(request);
        } else if (request.toLowerCase().contains("where") && request.toLowerCase().contains("and")) {
            rsl = selectAnd(request);
        } else if (request.toLowerCase().contains("where")) {
            String[] arr = request.split(" ");
            if (arr[2].toLowerCase().contains("'id'")) {
                rsl.addAll(selectLong(request, "id"));
            }
            if (arr[2].toLowerCase().contains("'age'")) {
                rsl.addAll(selectLong(request, "age"));
            }
            if (arr[2].toLowerCase().contains("'cost'")) {
                rsl.addAll(selectDouble(request, "cost"));
            }
            if (arr[2].toLowerCase().contains("'active'")) {
                rsl.addAll(selectBoolean(request, "active"));
            }
            if (arr[2].toLowerCase().contains("'lastname'")) {
                rsl.addAll(selectString(request, "lastName"));
            }
        } else {
            return list;
        }
        return rsl;
    }

    private List<Map<String, Object>> selectLong(String request, String key) {
        List<Map<String, Object>> rsl = new ArrayList<>();
        String[] arr = request.split(" ");
        if (arr[2].contains("!=")) {
            String[] data = arr[2].split("!=");
            for (Map<String, Object> map : list) {
                if (map.get(key) == null) {
                    continue;
                }
                if (!map.get(key).equals(Long.parseLong(data[1]))) {
                    rsl.add(map);
                }
            }
        } else if (arr[2].contains("<=")) {
            String[] data = arr[2].split("<=");
            for (Map<String, Object> map : list) {
                if (map.get(key) == null) {
                    continue;
                }
                if ((Long) map.get(key) <= Long.parseLong(data[1])) {
                    rsl.add(map);
                }
            }
        } else if (arr[2].contains(">=")) {
            String[] data = arr[2].split(">=");
            for (Map<String, Object> map : list) {
                if (map.get(key) == null) {
                    continue;
                }
                if ((Long) map.get(key) >= Long.parseLong(data[1])) {
                    rsl.add(map);
                }
            }
        } else if (arr[2].contains("=")) {
            String[] data = arr[2].split("=");
            for (Map<String, Object> map : list) {
                if (map.get(key) == null) {
                    continue;
                }
                if (map.get(key).equals(Long.parseLong(data[1]))) {
                    rsl.add(map);
                }
            }
        } else if (arr[2].contains("<")) {
            String[] data = arr[2].split("<");
            for (Map<String, Object> map : list) {
                if (map.get(key) == null) {
                    continue;
                }
                if ((Long) map.get(key) < Long.parseLong(data[1])) {
                    rsl.add(map);
                }
            }
        } else if (arr[2].contains(">")) {
            String[] data = arr[2].split(">");
            for (Map<String, Object> map : list) {
                if (map.get(key) == null) {
                    continue;
                }
                if ((Long) map.get(key) > Long.parseLong(data[1])) {
                    rsl.add(map);
                }
            }
        }
        return rsl;
    }

    private List<Map<String, Object>> selectDouble(String request, String key) {
        List<Map<String, Object>> rsl = new ArrayList<>();
        String[] arr = request.split(" ");
        if (arr[2].contains("!=")) {
            String[] data = arr[2].split("!=");
            for (Map<String, Object> map : list) {
                if (map.get(key) == null) {
                    continue;
                }
                if (!map.get(key).equals(Double.parseDouble(data[1]))) {
                    rsl.add(map);
                }
            }
        } else if (arr[2].contains("<=")) {
            String[] data = arr[2].split("<=");
            for (Map<String, Object> map : list) {
                if (map.get(key) == null) {
                    continue;
                }
                if ((Double) map.get(key) <= Double.parseDouble(data[1])) {
                    rsl.add(map);
                }
            }
        } else if (arr[2].contains(">=")) {
            String[] data = arr[2].split(">=");
            for (Map<String, Object> map : list) {
                if (map.get(key) == null) {
                    continue;
                }
                if ((Double) map.get(key) >= Double.parseDouble(data[1])) {
                    rsl.add(map);
                }
            }
        } else if (arr[2].contains("=")) {
            String[] data = arr[2].split("=");
            for (Map<String, Object> map : list) {
                if (map.get(key) == null) {
                    continue;
                }
                if (map.get(key).equals(Double.parseDouble(data[1]))) {
                    rsl.add(map);
                }
            }
        } else if (arr[2].contains("<")) {
            String[] data = arr[2].split("<");
            for (Map<String, Object> map : list) {
                if (map.get(key) == null) {
                    continue;
                }
                if ((Double) map.get(key) < Double.parseDouble(data[1])) {
                    rsl.add(map);
                }
            }
        } else if (arr[2].contains(">")) {
            String[] data = arr[2].split(">");
            for (Map<String, Object> map : list) {
                if (map.get(key) == null) {
                    continue;
                }
                if ((Double) map.get(key) > Double.parseDouble(data[1])) {
                    rsl.add(map);
                }
            }
        }
        return rsl;
    }

    private List<Map<String, Object>> selectBoolean(String request, String key) {
        List<Map<String, Object>> rsl = new ArrayList<>();
        String[] arr = request.split(" ");
        if (arr[2].contains("!=")) {
            String[] data = arr[2].split("!=");
            for (Map<String, Object> map : list) {
                if (map.get(key) == null) {
                    continue;
                }
                if (!map.get(key).equals(Boolean.parseBoolean(data[1]))) {
                    rsl.add(map);
                }
            }
        } else if (arr[2].contains("=")) {
            String[] data = arr[2].split("=");
            for (Map<String, Object> map : list) {
                if (map.get(key) == null) {
                    continue;
                }
                if (map.get(key).equals(Boolean.parseBoolean(data[1]))) {
                    rsl.add(map);
                }
            }
        }
        return rsl;
    }

    private List<Map<String, Object>> selectString(String request, String key) {
        List<Map<String, Object>> rsl = new ArrayList<>();
        String[] arr = request.split(" ");
        if (arr[2].contains("!=")) {
            String[] data = arr[2].split("!=");
            for (Map<String, Object> map : list) {
                if (map.get(key) == null) {
                    continue;
                }
                if (!map.get(key).equals(data[1].substring(1, data[1].length() - 1))) {
                    rsl.add(map);
                }
            }
        } else if (arr[2].contains("=")) {
            String[] data = arr[2].split("=");
            for (Map<String, Object> map : list) {
                if (map.get(key) == null) {
                    continue;
                }
                if (map.get(key).equals(data[1].substring(1, data[1].length() - 1))) {
                    rsl.add(map);
                }
            }
        } else if (arr[3].toLowerCase().contains("ilike") && arr[4].substring(1, arr[4].length() - 1).startsWith("%") && arr[4].substring(1, arr[4].length() - 1).endsWith("%")) {
            for (Map<String, Object> map : list) {
                if (map.get(key) == null) {
                    continue;
                }
                String str = (String) map.get(key);
                String str0 = str.toLowerCase();
                String str1 = arr[4].substring(2, arr[4].length() - 2).toLowerCase();
                if (str0.contains(str1)) {
                    rsl.add(map);
                }
            }
        } else if (arr[3].toLowerCase().contains("like") && arr[4].substring(1, arr[4].length() - 1).startsWith("%") && arr[4].substring(1, arr[4].length() - 1).endsWith("%")) {
            for (Map<String, Object> map : list) {
                if (map.get(key) == null) {
                    continue;
                }
                String str = (String) map.get(key);
                String str1 = arr[4].substring(2, arr[4].length() - 2);
                if (str.contains(str1)) {
                    rsl.add(map);
                }
            }
        } else if (arr[3].toLowerCase().contains("ilike") && arr[4].substring(1, arr[4].length() - 1).startsWith("%")) {
            for (Map<String, Object> map : list) {
                if (map.get(key) == null) {
                    continue;
                }
                String str = (String) map.get(key);
                String str0 = str.toLowerCase();
                String str1 = arr[4].substring(2, arr[4].length() - 1).toLowerCase();
                if (str0.endsWith(str1)) {
                    rsl.add(map);
                }
            }
        } else if (arr[3].toLowerCase().contains("like") && arr[4].substring(1, arr[4].length() - 1).startsWith("%")) {
            for (Map<String, Object> map : list) {
                if (map.get(key) == null) {
                    continue;
                }
                String str = (String) map.get(key);
                String str1 = arr[4].substring(2, arr[4].length() - 1);
                if (str.endsWith(str1)) {
                    rsl.add(map);
                }
            }
        } else if (arr[3].toLowerCase().contains("ilike") && arr[4].substring(1, arr[4].length() - 1).endsWith("%")) {
            for (Map<String, Object> map : list) {
                if (map.get(key) == null) {
                    continue;
                }
                String str = (String) map.get(key);
                String str0 = str.toLowerCase();
                String str1 = arr[4].substring(1, arr[4].length() - 2).toLowerCase();
                if (str0.startsWith(str1)) {
                    rsl.add(map);
                }
            }
        } else if (arr[3].toLowerCase().contains("like") && arr[4].substring(1, arr[4].length() - 1).endsWith("%")) {
            for (Map<String, Object> map : list) {
                if (map.get(key) == null) {
                    continue;
                }
                String str = (String) map.get(key);
                String str1 = arr[4].substring(1, arr[4].length() - 2);
                if (str.startsWith(str1)) {
                    rsl.add(map);
                }
            }
        } else if (arr[3].toLowerCase().contains("ilike")) {
            for (Map<String, Object> map : list) {
                if (map.get(key) == null) {
                    continue;
                }
                String str = (String) map.get(key);
                if (str.equalsIgnoreCase(arr[4].substring(1, arr[4].length() - 1))) {
                    rsl.add(map);
                }
            }
        } else if (arr[3].toLowerCase().contains("like")) {
            for (Map<String, Object> map : list) {
                if (map.get(key) == null) {
                    continue;
                }
                if (map.get(key).equals(arr[4].substring(1, arr[4].length() - 1))) {
                    rsl.add(map);
                }
            }
        }
        return rsl;
    }

    private List<Map<String, Object>> selectOr(String request) {
        List<Map<String, Object>> rsl = new ArrayList<>();
        Set<Map<String, Object>> set = new HashSet<>();
        String[] arr = request.split(" ");
        if (!request.toLowerCase().contains("like") && !request.toLowerCase().contains("ilike")) {
            String request1 = arr[0] + " " + arr[1] + " " + arr[2];
            List<Map<String, Object>> temp1 = select(request1);
            set.addAll(temp1);
            String request2 = arr[0] + " " + arr[1] + " " + arr[4];
            List<Map<String, Object>> temp2 = select(request2);
            set.addAll(temp2);
            rsl.addAll(set);
        } else if ((arr[3].equalsIgnoreCase("like") || arr[3].equalsIgnoreCase("ilike")) && (arr[7].equalsIgnoreCase("like") || arr[7].equalsIgnoreCase("ilike"))) {
            String request1 = arr[0] + " " + arr[1] + " " + arr[2] + " " + arr[3] + " " + arr[4];
            List<Map<String, Object>> temp1 = select(request1);
            set.addAll(temp1);
            String request2 = arr[0] + " " + arr[1] + " " + arr[6] + " " + arr[7] + " " + arr[8];
            List<Map<String, Object>> temp2 = select(request2);
            set.addAll(temp2);
            rsl.addAll(set);
        } else if (arr[3].equalsIgnoreCase("like") || arr[3].equalsIgnoreCase("ilike")) {
            String request1 = arr[0] + " " + arr[1] + " " + arr[2] + " " + arr[3] + " " + arr[4];
            List<Map<String, Object>> temp1 = select(request1);
            set.addAll(temp1);
            String request2 = arr[0] + " " + arr[1] + " " + arr[6];
            List<Map<String, Object>> temp2 = select(request2);
            set.addAll(temp2);
            rsl.addAll(set);
        } else if (arr[5].equalsIgnoreCase("like") || arr[5].equalsIgnoreCase("ilike")) {
            String request1 = arr[0] + " " + arr[1] + " " + arr[4] + " " + arr[5] + " " + arr[6];
            List<Map<String, Object>> temp1 = select(request1);
            set.addAll(temp1);
            String request2 = arr[0] + " " + arr[1] + " " + arr[2];
            List<Map<String, Object>> temp2 = select(request2);
            set.addAll(temp2);
            rsl.addAll(set);
        }
        return rsl;
    }

    private List<Map<String, Object>> selectAnd(String request) {
        List<Map<String, Object>> rsl = new ArrayList<>();
        String[] arr = request.split(" ");
        if (!request.toLowerCase().contains("like") && !request.toLowerCase().contains("ilike")) {
            String request1 = arr[0] + " " + arr[1] + " " + arr[2];
            List<Map<String, Object>> temp1 = select(request1);
            String request2 = arr[0] + " " + arr[1] + " " + arr[4];
            List<Map<String, Object>> temp2 = select(request2);
            for (Map<String, Object> map : temp1) {
                for (Map<String, Object> map2 : temp2) {
                    if (map.equals(map2)) {
                        rsl.add(map);
                    }
                }
            }
        } else if ((arr[3].equalsIgnoreCase("like") || arr[3].equalsIgnoreCase("ilike")) && (arr[7].equalsIgnoreCase("like") || arr[7].equalsIgnoreCase("ilike"))) {
            String request1 = arr[0] + " " + arr[1] + " " + arr[2] + " " + arr[3] + " " + arr[4];
            List<Map<String, Object>> temp1 = select(request1);
            String request2 = arr[0] + " " + arr[1] + " " + arr[6] + " " + arr[7] + " " + arr[8];
            List<Map<String, Object>> temp2 = select(request2);
            for (Map<String, Object> map : temp1) {
                for (Map<String, Object> map2 : temp2) {
                    if (map.equals(map2)) {
                        rsl.add(map);
                    }
                }
            }
        } else if (arr[3].equalsIgnoreCase("like") || arr[3].equalsIgnoreCase("ilike")) {
            String request1 = arr[0] + " " + arr[1] + " " + arr[2] + " " + arr[3] + " " + arr[4];
            List<Map<String, Object>> temp1 = select(request1);
            String request2 = arr[0] + " " + arr[1] + " " + arr[6];
            List<Map<String, Object>> temp2 = select(request2);
            for (Map<String, Object> map : temp1) {
                for (Map<String, Object> map2 : temp2) {
                    if (map.equals(map2)) {
                        rsl.add(map);
                    }
                }
            }
        } else if (arr[5].equalsIgnoreCase("like") || arr[5].equalsIgnoreCase("ilike")) {
            String request1 = arr[0] + " " + arr[1] + " " + arr[4] + " " + arr[5] + " " + arr[6];
            List<Map<String, Object>> temp1 = select(request1);
            String request2 = arr[0] + " " + arr[1] + " " + arr[2];
            List<Map<String, Object>> temp2 = select(request2);
            for (Map<String, Object> map : temp1) {
                for (Map<String, Object> map2 : temp2) {
                    if (map.equals(map2)) {
                        rsl.add(map);
                    }
                }
            }
        }
        return rsl;
    }

    private void updateList(String request, List<Map<String, Object>> arr) {
        String[] temp = request.split(" ");
        for (String s : temp) {
            String[] data = s.split("=");
            if (data[0].equalsIgnoreCase("'id'")) {
                for (Map<String, Object> map : arr) {
                    if (map.containsKey("id")) {
                        map.replace("id", Long.parseLong(data[1]));
                    } else {
                        map.put("id", Long.parseLong(data[1]));
                    }
                }
            }
            if (data[0].equalsIgnoreCase("'lastname'")) {
                for (Map<String, Object> map : arr) {
                    if (map.containsKey("lastName")) {
                        map.replace("lastName", data[1].substring(1, data[1].length() - 1));
                    } else {
                        map.put("lastName", data[1].substring(1, data[1].length() - 1));
                    }
                }
            }
            if (data[0].equalsIgnoreCase("'age'")) {
                for (Map<String, Object> map : arr) {
                    if (map.containsKey("age")) {
                        map.put("age", Long.parseLong(data[1]));
                    } else {
                        map.put("age", Long.parseLong(data[1]));
                    }
                }
            }
            if (data[0].equalsIgnoreCase("'cost'")) {
                for (Map<String, Object> map : arr) {
                    if (map.containsKey("cost")) {
                        map.replace("cost", Double.parseDouble(data[1]));
                    } else {
                        map.put("cost", Double.parseDouble(data[1]));
                    }
                }
            }
            if (data[0].equalsIgnoreCase("'active'")) {
                for (Map<String, Object> map : arr) {
                    if (map.containsKey("active")) {
                        map.replace("active", Boolean.parseBoolean(data[1]));
                    } else {
                        map.put("active", Boolean.parseBoolean(data[1]));
                    }
                }
            }
        }
    }

    private void valid(String request) {
        String str = request.toLowerCase();
        if (!(str.contains("insert") || str.contains("update") || str.contains("delete") || str.contains("select"))) {
            throw new IllegalArgumentException("«апрос должен содержать одну из команд: INSERT, UPDATE, DELETE, SELECT");
        }
    }

    @Override
    public String toString() {
        return "JavaSchoolStarter{" +
                "list=" + list +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JavaSchoolStarter that = (JavaSchoolStarter) o;
        return Objects.equals(list, that.list);
    }

    @Override
    public int hashCode() {
        return Objects.hash(list);
    }
}