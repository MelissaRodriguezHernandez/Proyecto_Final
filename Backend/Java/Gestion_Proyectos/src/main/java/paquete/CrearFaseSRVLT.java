package paquete;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class CrearFaseSRVLT
 */
@WebServlet("/CrearFaseSRVLT")
public class CrearFaseSRVLT extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("metodo crear fase get");
		
		try {
			String nombre = request.getParameter("nombre");
			String descripcion = request.getParameter("descripcion");
			String fechaI = request.getParameter("fechaI");
			String fechaF = request.getParameter("fechaF");
			
			
			BBDD.insertFase(nombre, descripcion, fechaI, fechaF);
			
								
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();}		
	
	}
	

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("metodo crear fase");
				
		try {
			String nombre = request.getParameter("nombre");
			String descripcion = request.getParameter("descripcion");
			String fechaI = request.getParameter("fechaI");
			String fechaF = request.getParameter("fechaF");
			
			boolean fechaMal = BBDD.comrpobarFecha(fechaI, fechaF);
			
			if(String.valueOf(fechaMal).equals("true")) {
				BBDD.insertFase(nombre, descripcion, fechaI, fechaF);
			}else {
				response.addHeader("Access-Control-Allow-Origin", "*");
				response.getWriter().append(String.valueOf("Las fechas para la fase no son correctas respecto a los limites del proyecto"));
			}
						
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
 