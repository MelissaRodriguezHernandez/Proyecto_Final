package paquete;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ReclamacionSRVLT
 */
@WebServlet("/ReclamacionSRVLT")
public class ReclamacionSRVLT extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		try {
			String id = Reclamacion.asignarId();
			
			String titulo = request.getParameter("titulo");
			
			String tipo = request.getParameter("tipo");
			
			String descripcion = request.getParameter("descripcion");
			
			BBDD.infoReclamacion(id, titulo, tipo, descripcion);
			
			response.addHeader("Access-Control-Allow-Origin", "*");
			response.getWriter().append(String.valueOf("Se ha enviado correctamente"));
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
