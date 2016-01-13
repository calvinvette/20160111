package com.morello.training.security.web;

/**
 * <p>
 * This component and its source code representation are copyright protected
 * and proprietary to Kim Morello, Worldwide
 *
 * This component and source code may be used for instructional and
 * evaluation purposes only. No part of this component or its source code
 * may be sold, transferred, or publicly posted, nor may it be used in a
 * commercial or production environment, without the express written consent
 * of Trivera Technologies, LLC
 *
 * Copyright (c) 2015 Kim Morello
 * </p>
 * @author Kim Morello.
 */

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Set;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import com.morello.training.security.data.ConnectionFactory;
import com.morello.training.security.data.User;

public class UserUpdateController extends HttpServlet {

  private static final long serialVersionUID = 1L;

  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    response.setContentType("text/html");

    // Setup the empty bean
    User newUser = new User();

    // Populate the bean with the request parameters
    newUser.setUserName(request.getParameter("userName"));
    newUser.setFullName(request.getParameter("fullName"));
    newUser.setStreet(request.getParameter("street"));
    newUser.setCity(request.getParameter("city"));
    newUser.setZipcode(request.getParameter("zip"));
    newUser.setCcn(request.getParameter("ccn"));
    
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    Validator validator = factory.getValidator();
    Set<ConstraintViolation<User>> errors = validator.validate(newUser);  
    
    if (errors != null && errors.size() > 0) {
    	String msg = "";
    	for ( ConstraintViolation<User> error : errors) {
    		msg += error.getPropertyPath() + ": " + error.getMessage() + "<br/>"; // embedded HTML - ick!
    	}
    	genBadIdPage(msg, request, response);
    	//genBadIdPage(errors.toString(), request, response);
    }

    Connection conn = ConnectionFactory.getConnection();
    if (conn == null) {
      response.sendRedirect("index.html");
      return;
    }
    Statement sqlStatement = null;
    try {
      sqlStatement = conn.createStatement();
      String sql = "UPDATE MEMBER SET FULLNAME = '" + newUser.getFullName()
          + "', STREET = '" + newUser.getStreet() + "', CITY = '"
          + newUser.getCity() + "', ZIPCODE = '" + newUser.getZipcode()
          + "', CCN = '" + newUser.getCcn() + "' WHERE USERNAME = '" + newUser.getUserName()
          + "'";
      System.out.println(sql);
      sqlStatement.execute(sql);
    } catch (SQLException e) {
      e.printStackTrace();
      response.sendRedirect("index.html");
      return;
    } finally {
      Statement[] statements = new Statement[1];
      statements[0] = sqlStatement;
      ConnectionFactory.closeConnection(conn, statements);
    }

    // Add the bean to the request
    request.setAttribute("user", newUser);
    request.getRequestDispatcher("JSPs/UserUpdateSuccess.jsp").forward(request,
        response);
  }

  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    doPost(request, response);
  }

    /** ***************************************************************** */
    /** ***********************Utilities********************************* */
    /** ***************************************************************** */

    private void genBadIdPage(String message, HttpServletRequest request,
        HttpServletResponse response) throws ServletException, IOException {
      request.setAttribute("message", message);
      request.getRequestDispatcher("ErrorHandler").forward(request, response);
    }

}
