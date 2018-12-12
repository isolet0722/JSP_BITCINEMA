package bitcinema.mvc.model;

import java.sql.*;
import java.util.UUID;

import javax.naming.*;
import javax.servlet.http.HttpServletRequest;
import javax.sql.*;

import bitcinema.mvc.model.SigninDTO;
import bitcinema.mvc.model.SQL;

public class SigninDAO
{
	DataSource ds;
	
	SigninDAO(){
		try{
			Context initContext = new InitialContext();
			Context envContext  = (Context)initContext.lookup("java:/comp/env");
			ds = (DataSource)envContext.lookup("jdbc/myoracle");
		}catch(NamingException n){}
	}
	
	SigninDTO signin(HttpServletRequest request) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmt2 = null;
		ResultSet rs = null;
		String email = request.getParameter("email");
		String pw = request.getParameter("pw");
		String kakao_email = request.getParameter("kakao_email");
		
		try{
			conn = ds.getConnection();
			pstmt = conn.prepareStatement(SQL.PW_CHECK);
			pstmt.setString(1, email);
			rs = pstmt.executeQuery();
			
			if(!kakao_email.equals("empty")){
				String ranPw = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 10);
				SigninDTO signin_dto = new SigninDTO(kakao_email, ranPw);
				
				pstmt2 = conn.prepareStatement(SQL.SIGNUP);
				pstmt2.setString(1, kakao_email);
				pstmt2.setString(2, "īī��");
				pstmt2.setString(3, ranPw);
				pstmt2.execute();
				
				return signin_dto;
			}
			
			if(rs.next()){
				if(rs.getString("PWD").equals(pw)){
					SigninDTO signin_dto = new SigninDTO(email, pw);
					return signin_dto;
				}else{
					SigninDTO signin_dto = new SigninDTO(email, null);
					return signin_dto;
				}
			}else{
				SigninDTO signin_dto = new SigninDTO(null, null);
				return signin_dto;
			}
		}catch(SQLException s){
			System.out.println(s);
			SigninDTO signin_dto = new SigninDTO(null, null);
			return signin_dto;
		}finally{
			try{
				if(rs != null) rs.close();
				if(pstmt2 != null) pstmt2.close();
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
			}catch(SQLException se){}
		}
	}
	
	int signup(HttpServletRequest request) {
		Connection conn = null;
		Statement stmt = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String email = request.getParameter("email");
		
		try{
			conn = ds.getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(SQL.EMAIL_CHECK);
			
			while(rs.next()){
				if(!rs.getString("EMAIL").equalsIgnoreCase(email)){
					pstmt = conn.prepareStatement(SQL.SIGNUP);
					pstmt.setString(1, email);
					pstmt.setString(2, request.getParameter("name"));
					pstmt.setString(3, request.getParameter("pw"));
					pstmt.executeUpdate();
					return 0;
				}
			}
			return 1;
		}catch(SQLException s){
			System.out.print(s);
			return 1;
		}finally{
			try{
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}catch(SQLException se){}
		}
	}
	
	void signout() {
		Connection conn = null;
		Statement stmt = null;
		
		try{
			conn = ds.getConnection();
			stmt = conn.createStatement();
			stmt.execute(SQL.SIGNOUT);
		}catch(SQLException s){
			System.out.print(s);
		}finally{
			try{
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}catch(SQLException se){}
		}
	}
}