package com.digdes.school;

public class Main {
    public static void main(String[] args) {
        JavaSchoolStarter starter = new JavaSchoolStarter();
        starter.execute("INSERT VALUES 'lastName'='Федоров' , 'id'=3, 'age'=40, 'active'=true");
        starter.execute("UPDATE VALUES 'active'=false, 'cost'=10.1 where 'id'=3");
       /* starter.execute("UPDATE VALUES ‘active’=true  where ‘active’=false");
        starter.execute("SELECT WHERE ‘age’>=30 and ‘lastName’ ilike ‘%п%’");
        starter.execute("DELETE WHERE ‘id’=3");*/

        starter.execute("Select");
        System.out.println(starter);
    }
}