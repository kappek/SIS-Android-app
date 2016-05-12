package com.techgeekme.sis;
// Import required java libraries
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// Extend HttpServlet class
public class SisServer extends HttpServlet {
 
	public void init() throws ServletException {
		
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		String usn = request.getParameter("usn");
		String dob = request.getParameter("dob");
		ServletOutputStream servletOutputStream = response.getOutputStream();
		if (usn == null || dob == null) {
			PrintWriter pw = new PrintWriter(servletOutputStream);
			pw.write("Missing input data");
			pw.close();
			return;
		}
		Student s = ScrapeTool.fetchData(usn, dob);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(s);
		byte[] serializedBytes = baos.toByteArray();
		servletOutputStream.write(serializedBytes);      
	}
  
	public void destroy() {
      // do nothing.
	}
}