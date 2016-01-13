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

package com.morello.training.security.auth;

/**
 * This class is handles login requests.
 * 
 * @version Training Course
 */
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.morello.training.security.data.ConnectionFactory;

public class AgentRegistry {
	/** ***************************************************************** */
	/** ***********************Variables********************************* */
	/** ***************************************************************** */

	/** ***************************************************************** */
	/** ***********************Constructor******************************* */
	/** ***************************************************************** */

	/**
	 * Constructor initializes registry.
	 */
	public AgentRegistry() {
		super();

	}

	/** ***************************************************************** */
	/** ****************Methods****************************** */
	/** ***************************************************************** */

	public int checkAgent(String candidateName, String candidatePassword) {
		if (candidateName == null || candidatePassword == null)
			return 0;

		Connection conn = ConnectionFactory.getConnection();
		if (conn == null)
			return 0;
		ResultSet rs = null;
		String password = null;

		Statement sqlStatement = null;
		try {
			sqlStatement = conn.createStatement();
			String sql = "select PASSWORD from MEMBER WHERE USERNAME = '"
					+ candidateName + "'";
			System.out.println(sql);
			rs = sqlStatement.executeQuery(sql);
			while (rs.next()) {
				password = rs.getString(1);
			}
			;
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		} finally {
			Statement[] statements = new Statement[1];
			statements[0] = sqlStatement;
			ConnectionFactory.closeConnection(conn, statements);
		}

		if (password == null) {
			System.out.println("No password returned");
			return 0;
		}
		if (candidatePassword.equals(password)) {
			return 2; // Passwords are the same, good username/password combo
		} else {
			return 1; // Passwords not the same, good username but bad password
		}
	}
}
