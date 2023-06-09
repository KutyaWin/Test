package com.digdes.school;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;




public class JavaSchoolStarter {

    private final List<Map<String, Object>> data;


    public JavaSchoolStarter(){
        data = new ArrayList<>();

    }

    public List<Map<String, Object>> execute(String request) {
        String[] strings = request.split("\\s+");
        String operation = strings[0].toUpperCase();
        switch (operation) {
            case "INSERT":
                data.addAll(insert(strings));
                break;
            case "UPDATE":
                data.addAll(update(strings));
                break;
            case "SELECT":
                data.addAll(select(strings));
                break;
            case "DELETE":
                data.addAll(delete(strings));
                break;
            default:
                throw new IllegalArgumentException("Unsupported operation: " + operation);
        }
        return data;
    }



    private List<Map<String, Object>> insert(String... strings){
        List<Map<String, Object>> result = new ArrayList<>();
        Map<String, Object> item = new HashMap<>();
        for (int i = 2; i < strings.length; i++) {
            String[] keyValue = strings[i].split("=");
            String key = keyValue[0].replaceAll("'", "");
            String value;
            boolean isEndExpression = i == strings.length - 1;
            value = initValue(keyValue[1], isEndExpression);
            switch (key) {
                case "id":
                    item.put(key, Long.parseLong(value));
                    break;
                case "lastName":
                    item.put(key, value);
                    break;
                case "age":
                    item.put(key, Integer.parseInt(value));
                    break;
                case "cost":
                    item.put(key, Double.parseDouble(value));
                    break;
                case "active":
                    item.put(key, Boolean.parseBoolean(value));
                    break;
                default:
                    throw new IllegalArgumentException("нет такой ячейки " + key);
            }
        }
        result.add(item);
        return result;
    }

    private String initValue(String keyValue, boolean isEndExpression) {
        String value;
        if (isEndExpression) {
            value = replaceInEnd(keyValue);
        } else {
            value = replace(keyValue);
        }
        return value;
    }

    private String replace(String keyValue){
        String value;
        if (keyValue.contains("'")) {
            value = keyValue.replaceAll("'", "").replaceAll(",","");
        } else {
            value = keyValue.replaceAll(",", "");
        }
        return value;
    }
    private String replaceInEnd(String keyValue){
        String value;
        if (keyValue.contains("'")) {
            value = keyValue.replaceAll("'", "");
        } else {
            value = keyValue;
        }
        return value;
    }


    private List<Map<String, Object>> update(String... strings){
            List<Map<String, Object>> result = new ArrayList<>();
            for(int i = 2; i< strings.length; i++){
                String[] keyValue;
                for(Map<String,Object> map: data ) {
                    if (strings[i].equalsIgnoreCase("WHERE")) {
                        String[] operators = {"=", "!=", "like", "ilike", ">=", "<=", "<", ">"};
                        for (String operator : operators) {
                            if (strings[i + 1].contains(operator))
                                switch (operator) {
                                    case "=": {
                                        keyValue = strings[i + 1].split(operator);
                                        if (map.containsKey(keyValue[0]))
                                            map.put(keyValue[0], keyValue[1]);
                                    }
                                    case "!=": {

                                    }
                                    case "like":
                                    case "ilike":
                                    case ">=":
                                    case "<=":
                                    case "<":
                                    case ">":
                                }
                        }


                    } else {
                        keyValue = strings[i].split("=");

                    }
                }
            }

            String[] whereParts = strings[4].split("=");
            String whereKey = whereParts[0].replaceAll("'", "");
            String whereValue = whereParts[1].replaceAll("'", "");
            for (Map<String, Object> item : data) {
                if (item.containsKey(whereKey) && item.get(whereKey).toString().equals(whereValue)) {
                    for (String updatePart : updateParts) {
                        String[] keyValue = updatePart.split("=");
                        String key = keyValue[0].replaceAll("'", "");
                        String value = keyValue[1].replaceAll("'", "");
                        if (key.equals("id")) {
                            item.put(key, Long.parseLong(value));
                        } else if (key.equals ("lastName")) {
                            item.put(key, value);
                        } else if (key.equals("age")) {
                            item.put(key, Integer.parseInt(value));
                        } else if (key.equals("cost")) {
                            item.put(key, Double.parseDouble(value));
                        } else if (key.equals("active")) {
                            item.put(key, Boolean.parseBoolean(value));
                        } else {
                            throw new IllegalArgumentException("Unsupported field: " + key);
                        }
                    }
                    result.add(item);
                }
            }
            return result;
        }




    private List<Map<String, Object>> select(String... strings){
        List<Map<String, Object>> result = new ArrayList<>();
        List<Map<String, Object>> filteredData = data;
        if (strings.length > 1) {
            filteredData = filter(strings[1].substring("WHERE".length()));
        }
        if (strings[0].toUpperCase().equals("SELECT")) {
            String[] selectParts = strings[1].substring("WHERE".length()).split(",");
            for (Map<String, Object> item : filteredData) {
                Map<String, Object> selectedFields = new HashMap<>();
                for (String selectPart : selectParts) {
                    String key = selectPart.replaceAll("'", "");
                    if (item.containsKey(key)) {
                        selectedFields.put(key, item.get(key));
                    } else {
                        throw new IllegalArgumentException("Unsupported field: " + key);
                    }
                }
                result.add(selectedFields);
            }
        }
        return result;
    }

    private List<Map<String, Object>> delete(String... strings){
        List<Map<String, Object>> result = new ArrayList<>();
        List<Map<String, Object>> filteredData = data;
        if (strings.length > 1) {
            filteredData = filter(strings[1].substring("WHERE".length()));
        }
        for (Map<String, Object> item : filteredData) {
            data.remove(item);
            result.add(item);
        }
        return result;
    }

    private List<Map<String, Object>> filter(String condition) {
        List<Map<String, Object>> result = new ArrayList<>();
        String[] strings = condition.split("AND");
        for (Map<String, Object> item : data) {
            boolean matched = true;
            for (String part : strings) {
                String[] keyValue = part.split("=");
                String key = keyValue[0].replaceAll("'", "");
                String value = keyValue[1].replaceAll("'", "");
                if (key.equals("id")) {
                    matched &= item.containsKey(key) && item.get(key).toString().equals(value);
                } else if (key.equals("lastName")) {
                    matched &= item.containsKey(key) && item.get(key).toString().toLowerCase().contains(value.toLowerCase());
                } else if (key.equals("age")) {
                    matched &= item.containsKey(key) && Integer.parseInt(item.get(key).toString()) >= Integer.parseInt(value);
                } else if (key.equals("cost")) {
                    matched &= item.containsKey(key) && Double.parseDouble(item.get(key).toString()) == Double.parseDouble(value);
                } else if (key.equals("active")) {
                    matched &= item.containsKey(key) && item.get(key).toString().equals(value);
                } else {
                    throw new IllegalArgumentException("Unsupported field: " + key);
                }
            }
            if (matched) {
                result.add(item);
            }
        }
        return result;
    }

    @Override
    public String toString() {
        return "JavaSchoolStarter{" +
                "data=" + data +
                '}';
    }
}
