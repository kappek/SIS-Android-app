package com.techgeekme.sis;


import java.io.Serializable;
import java.util.ArrayList;

public class Student implements Serializable {
    private static final long serialVersionUID = 1L;
    public String studentName;
    public String usn;
    public ArrayList<Course> courses = new ArrayList<Course>();
}
