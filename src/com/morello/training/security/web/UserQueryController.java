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

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import com.morello.training.security.data.ConnectionFactory;
import com.morello.training.security.data.User;

public class UserQueryController extends HttpServlet {

  private static final long serialVersionUID = 1L;

  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    response.setContentType("text/html");
    String username = (String) request.getParameter("username");

    User searchUser = new User();
    searchUser.setUserName(username);
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    Validator validator = factory.getValidator();
    Set<ConstraintViolation<User>> errors = validator.validate(searchUser);
    
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

    ResultSet rs = null;
    Statement sqlStatement = null;
    try {
      sqlStatement = conn.createStatement();
      String sql = "select * from MEMBER WHERE USERNAME = '" + searchUser.getUserName() + "'";
      System.out.println(sql);
      rs = sqlStatement.executeQuery(sql);
      while (rs.next()) {
        // Setup the empty bean
        User user = new User();

        // Populate the bean with the request parameters
        user.setUserName(rs.getString("USERNAME"));
        user.setFullName(rs.getString("FULLNAME"));
        user.setStreet(rs.getString("STREET"));
        user.setCity(rs.getString("CITY"));
        user.setZipcode(rs.getString("ZIPCODE"));
        user.setCcn(rs.getString("CCN"));

        // Add the bean to the request
        request.setAttribute("user", user);
      }
    } catch (SQLException e) {
      e.printStackTrace();
      response.sendRedirect("index.html");
      return;
    } finally {
      Statement[] statements = new Statement[1];
      statements[0] = sqlStatement;
      ConnectionFactory.closeConnection(conn, statements);
    }

    request.getRequestDispatcher("JSPs/UserResults.jsp").forward(request,
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
