package com.techgeekme.sis;
import java.io.Serializable;
import java.util.ArrayList;

public class Course implements Serializable{
	private static final long serialVersionUID = 1L;
	public String courseName;
	public String courseCode;
	public String courseType;
	public String credits;
	public int attendancePercent;
	public int classesAttended;
	public int classesHeld;
	public ArrayList<String> tests = new ArrayList<String>();
}
