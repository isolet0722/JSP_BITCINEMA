package bitcinema.mvc.ctr;

import java.io.*;
import java.util.ArrayList;
import javax.servlet.*;
import javax.servlet.http.*;

import bitcinema.mvc.model.SigninDTO;
import bitcinema.mvc.model.SigninMgr;
import bitcinema.mvc.model.DTO;
import bitcinema.mvc.model.Mgr;

import com.google.gson.JsonArray;

@SuppressWarnings("serial")
public class Control extends HttpServlet
{
	public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String param = request.getParameter("page");
		
		if(param != null){
			if(param.equals("signin")){
				signin(request, response);
			}else if(param.equals("signup")){
				signup(request, response);
			}else if(param.equals("signout")){
				signout(request, response);
			}else if(param.equals("signinCheck")){
				signinCheck(request, response);
			}else if(param.equals("signupCheck")){
				signupCheck(request, response);
			}else if(param.equals("select")){
				select(request, response);
			}else if(param.equals("select_genre")){
				select_genre(request, response);
			}else if(param.equals("select_director")){
				select_director(request, response);
			}else if(param.equals("select_actor")){
				select_actor(request, response);
			}else if(param.equals("select_materials")){
				select_materials(request, response);
			}else if(param.equals("main")){
				main(request, response);
			}else if(param.equals("film_detail")){
				film_detail(request, response);
			}else if(param.equals("reviewInsert")){
				reviewInsert(request, response);
			}else if(param.equals("reviewDelete")){
				reviewDelete(request, response);
			}else if(param.equals("search")){
				search(request, response);
			}else if(param.equals("search_convert")){
				search_convert(request, response);
			}else if(param.equals("curator")){
				curator(request, response);
			}else{
				index(request, response);
			}
		}else{
			index(request, response);
		}
	}
	
	private void index(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher("bitcinema/index.jsp");
		rd.forward(request, response);
	}
	
	private void signin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher("bitcinema/signin.jsp");
		rd.forward(request, response);
	}
	
	private void signout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {	
		SigninMgr mgr = SigninMgr.getInstance();
		mgr.signoutM();
		HttpSession session = request.getSession();
		session.invalidate();

		response.sendRedirect("index.do");
	}
	
	private void signup(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher("bitcinema/signup.jsp");
		rd.forward(request, response);
	}
	
	private void signinCheck(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter pw = response.getWriter();
		SigninMgr mgr = SigninMgr.getInstance();
		SigninDTO dto = mgr.signinM(request);

		pw.println("<script>");
		if(dto.getEmail() == null){
			pw.println("alert('�������� �ʴ� ���̵��Դϴ�.\\nȸ�������� ���� ������ �ּ���.');");
			pw.println("location.href='bitcinema.do?page=signup';");
			pw.println("</script>");
			return;
		}else if(dto.getPw() == null){
			pw.println("alert('�н����尡 Ʋ�Ƚ��ϴ�.\\n���� ������ �ٽ� �� �� Ȯ���� �ּ���.');");
			pw.println("location.href='bitcinema.do?page=signin';");
			pw.println("</script>");
			return;
		}else{
			HttpSession session = request.getSession();
			session.setAttribute("signin_dto", dto);
			pw.println("location.href='bitcinema.do?page=select';");
		}
		pw.println("</script>");
	}
	
	private void signupCheck(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter pw = response.getWriter();
		SigninMgr mgr = SigninMgr.getInstance();
		int result = mgr.signupM(request);
		
		pw.println("<script>");
		if(result == 0){
			pw.println("alert('��Ʈ�ó׸� ������ ȯ���մϴ�.\\n���񽺸� �̿��Ͻ÷��� �α����� �ּ���.');");
			pw.println("location.href='bitcinema.do?page=signin';");
		}else if(result == 1){
			pw.println("alert('�̹� ���ԵǾ� �ִ� ȸ���Դϴ�.\\n���� ������ �ٽ� �� �� Ȯ���� �ּ���.');");
			pw.println("location.href='bitcinema.do?page=signup';");
		}
		pw.println("</script>");
	}
	
	private void select(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher("bitcinema/select.jsp");
		rd.forward(request, response);
	}
	
	private void select_genre(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher("bitcinema/select_genre.jsp");
		rd.forward(request, response);
	}
	
	private void select_director(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher("bitcinema/select_director.jsp");
		rd.forward(request, response);
	}
	
	private void select_actor(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher("bitcinema/select_actor.jsp");
		rd.forward(request, response);
	}
	
	private void select_materials(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher("bitcinema/select_materials.jsp");
		rd.forward(request, response);
	}
	
	private void main(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		Mgr mgr = Mgr.getInstance();
		
		if(request.getParameterValues("genre") != null){
			String[] values_genre = request.getParameterValues("genre");
			for(int i=0; i<values_genre.length; i++) session.setAttribute("genre_" + i, values_genre[i]);
			ArrayList<DTO> result = mgr.resultM("genre", values_genre[0], values_genre[1], values_genre[2]);
			request.setAttribute("result", result);
		}else if(request.getParameterValues("director") != null){
			String[] values_director = request.getParameterValues("director");
			for(int i=0; i<values_director.length; i++) session.setAttribute("director_" + i, values_director[i]);
			ArrayList<DTO> result = mgr.resultM("director", values_director[0], values_director[1], values_director[2]);
			request.setAttribute("result", result);
		}else if(request.getParameterValues("actor") != null){
			String[] values_actor = request.getParameterValues("actor");
			for(int i=0; i<values_actor.length; i++) session.setAttribute("actor_" + i, values_actor[i]);
			ArrayList<DTO> result = mgr.resultM("actor", values_actor[0], values_actor[1], values_actor[2]);
			request.setAttribute("result", result);
		}else if(request.getParameterValues("materials") != null){
			String[] values_materials = request.getParameterValues("materials");
			for(int i=0; i<values_materials.length; i++) session.setAttribute("materials_" + i, values_materials[i]);
			ArrayList<DTO> result = mgr.resultM("materials", values_materials[0], values_materials[1], values_materials[2]);
			request.setAttribute("result", result);
		}
		
		RequestDispatcher rd = request.getRequestDispatcher("bitcinema/main.jsp");
		rd.forward(request, response);
	}
	
	private void film_detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html; charset=UTF-8");
		Mgr mgr = Mgr.getInstance();
		ArrayList<DTO> film_detail = mgr.film_detailM(request);
		ArrayList<DTO> reviewList = mgr.reviewListM(request);
		request.setAttribute("film_detail", film_detail);
		request.setAttribute("reviewList", reviewList);
		
		RequestDispatcher rd = request.getRequestDispatcher("bitcinema/film_detail.jsp");
		rd.forward(request, response);
	}
	
	private void reviewInsert(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html; charset=UTF-8");
		String code = request.getParameter("film_id");
		Mgr mgr = Mgr.getInstance();
		boolean flag = mgr.reviewInsertM(request);
		
		PrintWriter pw = response.getWriter();
		if(flag){
			pw.println("<script>location.href='bitcinema.do?page=film_detail&code=" + code + "';</script>");
		}else{
			pw.println("<script>alert('��ȭ �� �ϳ��� �򰡸� �ۼ��� �� �ֽ��ϴ�.');");
			pw.println("location.href='bitcinema.do?page=film_detail&code=" + code + "';</script>");
		}
		pw.close();
	}
	
	private void reviewDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html; charset=UTF-8");
		String code = request.getParameter("film_id");
		Mgr mgr = Mgr.getInstance();
		boolean flag = mgr.reviewDeleteM(request);
		
		PrintWriter pw = response.getWriter();
		if(flag){
			pw.println("<script>location.href='bitcinema.do?page=film_detail&code=" + code + "';</script>");
		}else{
			pw.println("<script>alert('��ȭ�� ���� �� ������ �߻��Ͽ����ϴ�. �ٽ� �� �� �õ��� �ּ���.');");
			pw.println("location.href='bitcinema.do?page=film_detail&code=" + code + "';</script>");
		}
		pw.close();
	}
	
	private void search(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html; charset=UTF-8");
		Mgr mgr = Mgr.getInstance();
		ArrayList<DTO> search = mgr.searchM(request);
		
		JsonArray array = new JsonArray();
		for(int i=0; i<search.size(); i++) array.add(search.get(i).getFilm_title());
		PrintWriter pw = response.getWriter();
		pw.print(array.toString());
		pw.close();
	}
	
	private void search_convert(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html; charset=UTF-8");
		Mgr mgr = Mgr.getInstance();
		int code = mgr.search_convertM(request.getParameter("title"));

		PrintWriter pw = response.getWriter();
		pw.println("<script>location.href='bitcinema.do?page=film_detail&code=" + code + "';</script>");
		pw.close();
	}
	
	private void curator(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html; charset=UTF-8");
		Mgr mgr = Mgr.getInstance();
		ArrayList<DTO> curating_board = mgr.curating_boardM();
		request.setAttribute("curating_board", curating_board);
		
		RequestDispatcher rd = request.getRequestDispatcher("bitcinema/curator.jsp");
		rd.forward(request, response);
	}
}