package com.techgeekme.sis;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.util.Base64;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class ScrapeTool {

    public static Response getResponse(String usn, String dob) {
        try {

            Response res1 = Jsoup.connect("http://parents.msrit.edu/index.php").timeout(0).method(Method.GET).execute();
            Document doc1 = res1.parse();
            Element e = doc1.select("input").last();
            String magicKey = e.attr("name");
            System.out.println(e.attr("name"));
            Map<String, String> cookie = res1.cookies();
            System.out.println(cookie);
            String encodedDob = "";
            for (int i = 0; i < dob.length(); i++) {
                encodedDob += dob.charAt(i) + "  ";
            }
            Base64.Encoder encoder = Base64.getEncoder();
            String encodedPassword = encoder.encodeToString(encodedDob.getBytes());

            Response res = Jsoup.connect("http://parents.msrit.edu/index.php").data("username", usn)
                    .data("passwd", encodedPassword).data("option", "com_user")
                    .data("task", "login").data(magicKey, "1").followRedirects(true).cookies(cookie).timeout(0)
                    .method(Method.POST).execute();
            return res;
        } catch (IOException ex) {
            Logger.getLogger(ScrapeTool.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public static Student fetchData(String usn, String dob) throws IOException{
    	Response res =  getResponse(usn, dob);
        System.out.println("code: " + res.headers());
        // System.out.println("Body: " + res.body());
        Document doc = res.parse();
        Student s = new Student();
        String name = doc.getElementsByClass("largebluefont").first().text();
        System.out.println("Name: " + name);
        s.studentName = name;
        Elements bigContainers = doc.getElementsByClass("big_container");
        for (Element bigContainer : bigContainers) {
        	Course c = new Course();
            Element boxLeft = bigContainer.getElementsByClass("boxleft").first();
            Elements courseDetails = boxLeft.getElementsByClass("content");
            c.courseCode = bigContainer.getElementsByClass("top_back1").eq(1).text();
            c.courseName = bigContainer.getElementsByClass("top_back1").eq(2).text();
            c.credits = courseDetails.eq(0).text().split("\\.", 2)[0];
            c.courseType = courseDetails.eq(1).text();
            System.out.println("Code: " + c.courseCode +"\n" + "Name " + c.courseName);
            System.out.println("Credits: " + c.credits+"\n" + "Course Type: " + c.courseType);

            Element boxMiddle = bigContainer.getElementsByClass("boxmiddle").first();
            Elements attendanceDetails = boxMiddle.getElementsByClass("content");

            String attendancePercentString = attendanceDetails.get(0).text();
            c.attendancePercent = Integer.parseInt(attendancePercentString.substring(0, attendancePercentString.length()-1));
            Element attendance = attendanceDetails.get(1);
            c.classesAttended = Integer.parseInt(attendance.child(0).text());
            c.classesHeld = Integer.parseInt(attendance.child(1).text());
            System.out.println("Percent: " + c.attendancePercent + "\nAttendance: " + c.classesAttended + "/" + c.classesHeld);

            Element boxRight = bigContainer.getElementsByClass("boxright").first();
            Elements tests = boxRight.getElementsByClass("content");
            for (int i = 0; i < tests.size(); i++) {
            	if (i % 2 == 1) {
            		String testMarks = tests.get(i).text().split("\\.", 2)[0];
            		c.tests.add(testMarks);
            	}
            }
            s.courses.add(c);
        }
        return s;
    }
}