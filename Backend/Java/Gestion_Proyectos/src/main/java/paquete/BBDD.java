package paquete;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;



public class BBDD {
	 static String usuarioWeb;
	 static String idProyectoActual;
	
	 /**
	     * Pasamos por parametro la informaci�n que ha introducido el usuario en el formulario de registro y
	     * ejecutamos un query en nuestra base de datos en la tabla de usuarios para insertar toda esta informaci�n
	     * @param id
	     * @param nombre
	     * @param apellidos
	     * @param email
	     * @param contrasena
	     * @return existe
	     * @throws SQLException
	     * @throws ClassNotFoundException
	     */
	    public static boolean insertUsuario(String id, String nombre, String apellidos, String email, String contrasena ) throws SQLException, ClassNotFoundException {
	        Class.forName("com.mysql.cj.jdbc.Driver");
	        String url = "jdbc:mysql://localhost:3306/proyecto";
	        String query = "INSERT INTO usuario (id, nombre, apellidos, email, contrase�a) VALUES ('"+id+"','"+nombre+"','"+apellidos+"','"+email+"','"+contrasena+"')";
	        Connection con = DriverManager.getConnection(url, "root", "mysql");
	        Statement st = con.createStatement();

	        ArrayList<Persona> listaPersonas = recuperarPersonas();

	        int i = 0;
	        boolean existe = false;

	        while(!existe && i<listaPersonas.size()) {
	            if(email.equals(listaPersonas.get(i).getEmail())) {
	                existe = true;
	            }else {
	                i++;
	            }
	        }

	        if(!existe) {
	            st.executeUpdate(query);
	        }

	        return existe;
	    }

	    /**
	     * Metodo que selecciona todas las filas de la tabla usuario de la base de datos, fila por fila
	     * va creando un nuevo objeto persona con la informaci�n y lo mete en un arraylist de personas.
	     * Resumidamente metemos en un arraylist todas los usuarios de la base de datos para usarla posteriormente en otros metodos
	     * @return listaPersonas
	     * @throws ClassNotFoundException
	     * @throws SQLException
	     */
	    public static ArrayList<Persona> recuperarPersonas() throws ClassNotFoundException, SQLException {

	        ArrayList<Persona> listaPersonas = new ArrayList<>();

	        try {
	            String query = "SELECT * FROM usuario";
	            Class.forName("com.mysql.cj.jdbc.Driver");
	            String url = "jdbc:mysql://localhost:3306/proyecto";
	            Connection con = DriverManager.getConnection(url, "root", "mysql");
	            Statement st = con.createStatement();
	            ResultSet rs= st.executeQuery(query);

	            while (rs.next()) {

	                String id = rs.getString("id");
	                String nombre = rs.getString("nombre");
	                String apellidos = rs.getString("apellidos");
	                String email = rs.getString("email");
	                String contrasena = rs.getString("contrase�a");
	                listaPersonas.add(new Persona(id, nombre, apellidos, email, contrasena));

	            }
	        }catch(SQLException e) {
	            System.out.println("Error al recuperar las personas");
	        }
	        return listaPersonas;
	    }
	    
	    /**
	     *Metodo que selecciona todas las filas de la tabla proyecto de la base de datos, fila por fila
	     * va creando un nuevo objeto proyecto con la informaci�n y lo mete en un arraylist de proyectos.
	     * Resumidamente metemos en un arraylist todas los proyectos de la base de datos para usarla posteriormente en otros metodos
	     * @return
	     * @throws ClassNotFoundException
	     */
	    public static ArrayList<Proyecto> recuperarProyectos() throws ClassNotFoundException{

	        ArrayList<Proyecto> listaProyectos = new ArrayList<>();

	        try {
	            String query = "SELECT * FROM proyecto";
	            Class.forName("com.mysql.cj.jdbc.Driver");
	            String url = "jdbc:mysql://localhost:3306/proyecto";
	            Connection con = DriverManager.getConnection(url, "root", "mysql");
	            Statement st = con.createStatement();
	            ResultSet rs= st.executeQuery(query);

	            while (rs.next()) {

	                String id = rs.getString("id");
	                String nombre = rs.getString("nombre");
	                String descripcion = rs.getString("descripcion");
	                String idDirector = rs.getString("idDirector");

	                listaProyectos.add(new Proyecto(id, nombre, descripcion, idDirector));

	            }
	        }catch(SQLException e) {
	            System.out.println("Error al recuperar las personas");
	        }

	        return listaProyectos;

	    }

	    
	    /**
	     * Metodo que carga la informacion almacenada en la base de datos
	     *  de los proyectos creados por el usuario y lo va concatenando con html,
	     *  para luego enviarlo a la pagina principal
	     * @return
	     * @throws ClassNotFoundException
	     * @throws SQLException
	     */
	    public static String trabajosDirector() throws ClassNotFoundException, SQLException {
	        String resultado = "";
	        String id = usuarioWeb;
	        String query = "SELECT * FROM proyecto.proyecto WHERE idDirector='"+id+"'";
	        Class.forName("com.mysql.cj.jdbc.Driver");
	        String url = "jdbc:mysql://localhost:3306/proyecto";
	        Connection con = DriverManager.getConnection(url, "root", "mysql");
	        Statement st = con.createStatement();
	        ResultSet rs= st.executeQuery(query);

	        resultado = "<h2>Proyectos propios</h2>";

	        while (rs.next()) {
	            resultado = resultado
	                    + "<div onclick=\"enviaridProyecto("+rs.getString("id")+")\"><a href=\"proyecto.html\"><h3>"+rs.getString("nombre")+" <span >"+rs.getString("id")+"</span>| "
	                    +rs.getString("tipo")+" | "
	                    +rs.getString("estado")+"|</h3></a></div>";
	        }


	        return resultado;
	    }

	    /**
	     * Metodo que carga la informacion almacenada en la base de datos
	     *  de los proyectos donde el usuario participa y lo va concatenando con html,
	     *  para luego enviarlo a la pagina principal
	     * @return
	     * @throws ClassNotFoundException
	     * @throws SQLException
	     */
	    public static String trabajosParticipante() throws ClassNotFoundException, SQLException{
	        String resultado = "";
	        String id = usuarioWeb;
	        String query = "SELECT p.id , p.nombre, p.tipo, p.estado FROM proyecto p JOIN grupo_participantes gp ON gp.idProyecto = p.id WHERE gp.idUsuario='"+id+"' GROUP BY p.id" ;
	        Class.forName("com.mysql.cj.jdbc.Driver");
	        String url = "jdbc:mysql://localhost:3306/proyecto";
	        Connection con = DriverManager.getConnection(url, "root", "mysql");
	        Statement st = con.createStatement();
	        ResultSet rs= st.executeQuery(query);

	        resultado = "<h2>Proyectos participando</h2>";

	        while (rs.next()) {
	            resultado = resultado
	                    + "<div onclick=\"enviaridProyecto("+rs.getString("id")+")\"><a href=\"proyecto.html\"><h3>"+rs.getString("nombre")+" <span>"+rs.getString("id")+"</span>| "
	                    +rs.getString("tipo")+" | "
	                    +rs.getString("estado")+"|</h3></a></div>";
	        }


	        return resultado;
	    }

	    /**
	     * Metodo que carga la informacion del usuario y los concatena con html
	     * para luego enviarlo a la pagina
	     * @return
	     * @throws ClassNotFoundException
	     * @throws SQLException
	     */
	    public static String informacionUsuario() throws ClassNotFoundException, SQLException {
	        String resultado = "";
	        String id = usuarioWeb;
	        String query = "SELECT id, nombre, apellidos, email FROM usuario  WHERE id='"+id+"'";
	        Class.forName("com.mysql.cj.jdbc.Driver");
	        String url = "jdbc:mysql://localhost:3306/proyecto";
	        Connection con = DriverManager.getConnection(url, "root", "mysql");
	        Statement st = con.createStatement();
	        ResultSet rs= st.executeQuery(query);

	        resultado = "<h1>Cuenta</h1>";

	        while(rs.next()) {
	            resultado = resultado + "<h4> ID |"+rs.getString("id")+"</h4>"
	                    +"<h4>"+rs.getString("nombre")+"</h4>"
	                    +"<h4>"+rs.getString("apellidos")+"</h4>"
	                    +"<h4>"+rs.getString("email")+"</h4>";
	        }
	        return resultado;
	    }
	    
	    /**
	     *Seleccionamos toda la informaci�n en la base de datos del proyecto seleccionado para luego incrustarlo en un html 
	     *este metodo devolvera un String que contendra el html que se enviara de vuelta a la p�gina web
	     * @return
	     * @throws ClassNotFoundException
	     * @throws SQLException
	     */
	    public static String cargarInfoProyecto() throws ClassNotFoundException, SQLException {
	        String resultado = "";
	        String id = idProyectoActual;

	        String query = "SELECT id, nombre, tipo, estado , descripcion, fechaInicio, fechaFinal FROM proyecto  WHERE id='"+id+"'";
	        Class.forName("com.mysql.cj.jdbc.Driver");
	        String url = "jdbc:mysql://localhost:3306/proyecto";
	        Connection con = DriverManager.getConnection(url, "root", "mysql");
	        Statement st = con.createStatement();
	        ResultSet rs= st.executeQuery(query);


	        while(rs.next()) {
	            resultado = resultado + "<div><h3>Id | <h2>"+rs.getString("id")+"</h2></h3><br><br>"
	                    +"<h3>Nombre | <h2>"+rs.getString("nombre")+"</h2></h3><br><br>"
	                    +"<h3>Tipo | <h2>"+rs.getString("tipo")+"</h2></h3><br><br>"
	                    +"<h3>Estado | <h2>"+rs.getString("estado")+"</h2></h3><br><br></div>"
	                    +"<div><h3>Descripcion | <h2>"+rs.getString("descripcion")+"</h2></h3><br><br>"
	                    +"<h3>Fecha de inicio | <h2>"+rs.getString("fechaInicio")+"</h2></h3><br><br>"
	                    +"<h3>Fecha de terminio | <h2>"+rs.getString("fechaFinal")+"</h2></h3><br><br></div>"
	                    +"<div><a href=\"documento.html\" target=\"_blank\"><i class=\"icon icon-file\" id=\"icon\"></i></a></div>";
	        }
	        return resultado;
	    }

	    
	    /**
	     *Metodo que insertara un nuevo proyecto en la base de datos con toda la informaci�n pasada por parametro
	     * @param id
	     * @param nombre
	     * @param idDirector
	     * @param descripcion
	     * @param tipo
	     * @throws ClassNotFoundException
	     * @throws SQLException
	     */
	    public static void insertProyecto(String id, String nombre, String idDirector, String descripcion, String tipo, String fechaI, String fechaF) throws ClassNotFoundException, SQLException {

	        Class.forName("com.mysql.cj.jdbc.Driver");
	        String url = "jdbc:mysql://localhost:3306/proyecto";
	        String query = "INSERT INTO proyecto (id, nombre, descripcion, tipo, idDirector, fechaInicio, fechaFinal) VALUES ('"+id+"','"+nombre+"','"+descripcion+"','"+tipo+"','"+idDirector+"','"+fechaI+"','"+fechaF+"')";
	        Connection con = DriverManager.getConnection(url, "root", "mysql");
	        Statement st = con.createStatement();
	        try {
	            st.executeUpdate(query);
	        } catch (SQLException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
	    }

	    /**
	     *Metodo que a�adira una fase a la base de datos con la informaci�n pasada por parametro
	     *el id del proyecto ser� el del abierto por el usuario
	     *La fase se a�ade dentro de un proyecto ya creado
	     *Cada vez que elegimos y abrimos un proyecto cogemos el id del proyecto y lo almacenamos en atributo estatico
	     *para luego utilizarlo en otros metodos
	     * @param idProyecto
	     * @param nombre
	     * @param descripcion
	     * @param estado
	     * @param importancia
	     * @throws ClassNotFoundException
	     * @throws SQLException
	     */
	    public static void insertFase( String nombre, String descripcion, String fechaI, String fechaF) throws ClassNotFoundException, SQLException {
	    	       
	    	Class.forName("com.mysql.cj.jdbc.Driver");
	        String url = "jdbc:mysql://localhost:3306/proyecto";
	        String query = "INSERT INTO fase (idProyecto, nombre, descripcion, fecha_inicio, fecha_final) VALUES ('"+BBDD.idProyectoActual+"','"+nombre+"','"+descripcion+"','"+fechaI+"','"+fechaF+"')";
	        Connection con = DriverManager.getConnection(url, "root", "mysql");
	        Statement st = con.createStatement();
	        try {
	            st.executeUpdate(query);
	        } catch (SQLException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
	    }

	    /**
	     *Metodo que a�adira un usuario (el id se pasa por parametro) a la tabla de grupo de los participantes en el proyecto
	     *Otra vez el id del proyecto se coje del atributo estatico
	     * @param idUsuario
	     * @throws ClassNotFoundException
	     * @throws SQLException
	     */
	    public static void insertParticipanteProyecto(String idUsuario) throws ClassNotFoundException, SQLException {

	        Class.forName("com.mysql.cj.jdbc.Driver");
	        String url = "jdbc:mysql://localhost:3306/proyecto";
	        String query = "INSERT INTO grupo_participantes VALUES ('"+BBDD.idProyectoActual+"','"+idUsuario+"')";
	        Connection con = DriverManager.getConnection(url, "root", "mysql");
	        Statement st = con.createStatement();
	        try {
	            st.executeUpdate(query);
	        } catch (SQLException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
	    }

	    /**
	     *Se pasa por parametro el id del usuario y el de la fase para luego insertar un nuevo participante en la tabla de
	     * grupo de participantes por fase en el proyecto
	     * Otra vez el id del proyecto se coje del atributo estatico
	     * @param idUsuario
	     * @param idFase
	     * @throws ClassNotFoundException
	     * @throws SQLException
	     */
	    public static void insertParticipanteFase(String idUsuario, int idFase) throws ClassNotFoundException, SQLException {

	        Class.forName("com.mysql.cj.jdbc.Driver");
	        String url = "jdbc:mysql://localhost:3306/proyecto";
	        String query = "INSERT INTO grupo_participantes_fase VALUES ('"+BBDD.idProyectoActual+"','"+idUsuario+"','"+idFase+"')";
	        Connection con = DriverManager.getConnection(url, "root", "mysql");
	        Statement st = con.createStatement();
	        try {
	            st.executeUpdate(query);
	        } catch (SQLException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
	    }
	    
	    /**
	     * Este metodo carga todas las fases de un proyecto para mostrarlas 
	     * en los formularios de a�adir tareas 
	     * Otra vez el id del proyecto se coje del atributo estatico
	     * devolvera un String con el html que se incrustara en la p�gina web
	     * @return
	     * @throws ClassNotFoundException
	     * @throws SQLException
	     */
	    public static String cargarFasesForm() throws ClassNotFoundException, SQLException {
	        String resultado = "";

	        String query = "SELECT id, nombre FROM fase WHERE idProyecto='"+BBDD.idProyectoActual+"'" ;
	        Class.forName("com.mysql.cj.jdbc.Driver");
	        String url = "jdbc:mysql://localhost:3306/proyecto";
	        Connection con = DriverManager.getConnection(url, "root", "mysql");
	        Statement st = con.createStatement();
	        ResultSet rs= st.executeQuery(query);

	        while(rs.next()) {
	            resultado = resultado + "<option value= \""+rs.getString("id")+"\">"+rs.getString("nombre")+"</option>";
	        }

	        return resultado;
	    }
	    
	   /**
	    *  Este metodo carga todas los participantes de un proyecto para mostrarlos 
	     * en los formularios de a�adir tareas
	     * Otra vez el id del proyecto se coje del atributo estatico
	     * devolvera un String con el html que se incrustara en la p�gina web
	    * @return
	    * @throws ClassNotFoundException
	    * @throws SQLException
	    */
	    public static String inforParticipanteForm() throws ClassNotFoundException, SQLException {
	        String resultado = "";

	        String query = "SELECT idUsuario FROM grupo_participantes WHERE idProyecto='"+BBDD.idProyectoActual+"' AND idUsuario IS NOT NULL" ;
	        Class.forName("com.mysql.cj.jdbc.Driver");
	        String url = "jdbc:mysql://localhost:3306/proyecto";
	        Connection con = DriverManager.getConnection(url, "root", "mysql");
	        Statement st = con.createStatement();
	        ResultSet rs= st.executeQuery(query);

	        while(rs.next()) {
	            resultado = resultado + "<option value= \""+rs.getString("idUsuario")+"\">"+rs.getString("idUsuario")+"</option>";
	        }

	        return resultado;
	    }

	    /**
	     *Metodo que a�adira una tarea nueva a la base de datos con toda la informaacion pasada por parametro
	     *Otra vez el id del proyecto se coje del atributo estatico
	     * @param idFase
	     * @param nombre
	     * @param descripcion
	     * @param idUsuario
	     * @param fecha
	     * @throws ClassNotFoundException
	     * @throws SQLException
	     */
	    public static void insertTarea( int idFase, String nombre, String descripcion, String idUsuario, String fecha) throws ClassNotFoundException, SQLException {
	        Class.forName("com.mysql.cj.jdbc.Driver");
	        String url = "jdbc:mysql://localhost:3306/proyecto";
	        String query = "INSERT INTO tarea(idFase, idProyecto, nombre, descripcion, idUsuario, fechaEntrega) VALUES ('"+idFase+"','"+BBDD.idProyectoActual+"','"+nombre+"','"+descripcion+"','"+idUsuario+"','"+fecha+"')";
	        Connection con = DriverManager.getConnection(url, "root", "mysql");
	        Statement st = con.createStatement();
	        try {
	            st.executeUpdate(query);
	        } catch (SQLException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
	    }
	    
	    /**
	     *Metodo para cojer toda la informaci�n de todas las tareas de un proyecto en la base de datos
	     *Retornara un String con la informaci�n incrustada en un html, 
	     *el cual se enviara a la p�gina web
	     * @return
	     * @throws ClassNotFoundException
	     * @throws SQLException
	     */
	    public static String infoTarea() throws ClassNotFoundException, SQLException {
	        String resultado = "";

	        String query = "SELECT nombre, estado, fechaEntrega FROM tarea WHERE idProyecto='"+BBDD.idProyectoActual+"'" ;
	        Class.forName("com.mysql.cj.jdbc.Driver");
	        String url = "jdbc:mysql://localhost:3306/proyecto";
	        Connection con = DriverManager.getConnection(url, "root", "mysql");
	        Statement st = con.createStatement();
	        ResultSet rs= st.executeQuery(query);

	        resultado = "<h5> Tareas </h5> <h6> | Nombre | Estado | Fecha entrega |<h6>";

	        while(rs.next()) {
	            resultado = resultado + "<h6>"+rs.getString("nombre")+" : "+rs.getString("estado")+ " : " + rs.getString("fechaEntrega")+"</h6>";
	        }

	        return resultado;
	    }

	    /**
	     *Metodo para cojer toda la informaci�n de todos los participantes de un proyecto en la base de datos
	     *Retornara un String con la informaci�n incrustada en un html, 
	     *el cual se enviara a la p�gina web
	     * @return
	     * @throws ClassNotFoundException
	     * @throws SQLException
	     */
	    public static String infoParticipante() throws ClassNotFoundException, SQLException {
	        String resultado = "";

	        String query = "SELECT gp.idUsuario, u.nombre FROM grupo_participantes gp JOIN usuario u ON gp.idUsuario = u.id WHERE gp.idProyecto='"+BBDD.idProyectoActual+"'" ;
	        Class.forName("com.mysql.cj.jdbc.Driver");
	        String url = "jdbc:mysql://localhost:3306/proyecto";
	        Connection con = DriverManager.getConnection(url, "root", "mysql");
	        Statement st = con.createStatement();
	        ResultSet rs= st.executeQuery(query);

	        resultado = "<h5> Partcipantes </h5> <h6> | ID | Nombre | <h6>";

	        while(rs.next()) {
	            resultado = resultado + "<h6>"+rs.getString("idUsuario")+" : "+rs.getString("nombre")+ "</h6>";
	        }

	        return resultado;
	    }

	    /**
	     *Metodo para cojer toda la informaci�n de todas las fases de un proyecto en la base de datos
	     *Retornara un String con la informaci�n incrustada en un html, 
	     *el cual se enviara a la p�gina web
	     * @return
	     * @throws ClassNotFoundException
	     * @throws SQLException
	     */
	    public static String infoFase() throws ClassNotFoundException, SQLException {
	        String resultado = "";

	        String query = "SELECT nombre, estado, fecha_final FROM fase WHERE idProyecto='"+BBDD.idProyectoActual+"'" ;
	        Class.forName("com.mysql.cj.jdbc.Driver");
	        String url = "jdbc:mysql://localhost:3306/proyecto";
	        Connection con = DriverManager.getConnection(url, "root", "mysql");
	        Statement st = con.createStatement();
	        ResultSet rs= st.executeQuery(query);

	        resultado = "<h5> Fases </h5> <h6> | Nombre | Estado | Fecha terminio <h6>";

	        while(rs.next()) {
	            resultado = resultado + "<h6>"+rs.getString("nombre")+" : "+rs.getString("estado")+" : "+rs.getString("fecha_final")+"</h6>";
	        }

	        return resultado;
	    }
	    
	    /**
	     *Metodo que cojera la informaci�n de las fases y tareas donde partcicpa el usuario logueado
	     *por cada fase llamara al metodo infoTareaUsuario, al cual se le mandara por parametro la fase para obetener
	     *cada tarea que tiene endiente el usuario en esa fase
	     *El metodo devuele un String que contiene toda la  informaci�n incrustada en un html que se enviara a la p�gina web
	     * @return
	     * @throws ClassNotFoundException
	     * @throws SQLException
	     */
	    public static String infoTareasUsuario() throws ClassNotFoundException, SQLException {
	        String resultado = "";

	        String query = "SELECT gpf.idFase, f.nombre FROM grupo_participantes_fase gpf JOIN fase f ON gpf.idFase = f.id WHERE f.idProyecto='"+BBDD.idProyectoActual+"' AND idUsuario='"+BBDD.usuarioWeb+"'" ;
	        Class.forName("com.mysql.cj.jdbc.Driver");
	        String url = "jdbc:mysql://localhost:3306/proyecto";
	        Connection con = DriverManager.getConnection(url, "root", "mysql");
	        Statement st = con.createStatement();
	        ResultSet rs= st.executeQuery(query);

	        while(rs.next()) {

	            resultado = resultado + "<button class=\"fase_drop\" onmouseover=\"drop()\">"+rs.getString("nombre")+"<i class=\"icon icon-angle-double-down\"></i></button>\r\n"
	                    + "                            <ul class=\"tareas_drop\">"+BBDD.infoTareaFaseUsuario(rs.getString("idFase"))+"</ul>";


	        }
	        return resultado;
	    }

	    /**
	     *Metodo que a traves de un id de fase pasado por parametro devuelve todas las tareas que tiene 
	     *el usuario actual pendiente en la fase
	     *Devuelve un String de la infromaci�n con html incrustado
	     * @param idFase
	     * @return
	     * @throws ClassNotFoundException
	     * @throws SQLException
	     */
	    public static String infoTareaFaseUsuario(String idFase) throws ClassNotFoundException, SQLException {
	        String resultado = "";


	        String query = "SELECT distinct(idTarea) ,nombre, descripcion, fechaEntrega FROM tarea WHERE idProyecto='"+BBDD.idProyectoActual+"' AND idFase='"+idFase+"' AND idUsuario='"+BBDD.usuarioWeb+"' AND estado = 'Pendiente'" ;
	        Class.forName("com.mysql.cj.jdbc.Driver");
	        String url = "jdbc:mysql://localhost:3306/proyecto";
	        Connection con = DriverManager.getConnection(url, "root", "mysql");
	        Statement st = con.createStatement();
	        ResultSet rs= st.executeQuery(query);

	        while(rs.next()) {
	            resultado = resultado + "<h4 id="+rs.getString("idTarea")+">"+rs.getString("nombre")+" | entrega : "+rs.getString("fechaEntrega")+"<i class=\"icon icon-square-o\" onclick=\"actualizarEstadoTarea("+rs.getString("idTarea")+", "+idFase+")\"></i><br><span> Descripci�n | "+rs.getString("descripcion")+"</span></h4>";
	        }

	        return resultado;
	    }
	    
	    /**
	     *Metodo que inserta en la base de datos una reclamacion hecha por el usuario logueado
	     * @param id
	     * @param titulo
	     * @param tipo
	     * @param idProyecto
	     * @param descripcion
	     * @throws ClassNotFoundException
	     * @throws SQLException
	     */
	    public static void infoReclamacion(String id, String titulo, String tipo, String descripcion) throws ClassNotFoundException, SQLException {

	        Class.forName("com.mysql.cj.jdbc.Driver");
	        String url = "jdbc:mysql://localhost:3306/proyecto";
	        String query = "INSERT INTO reclamacion (id, nombre, descripcion, idUsuario_re) VALUES ('"+id+"','"+titulo+"','"+descripcion+"','"+usuarioWeb+"')";
	        Connection con = DriverManager.getConnection(url, "root", "mysql");
	        Statement st = con.createStatement();
	        try {
	            st.executeUpdate(query);
	        } catch (SQLException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
	    }

	    /**
	     *Metodo que selecciona todas las filas de la tabla reclamacion de la base de datos, fila por fila
	     * va creando un nuevo objeto reclamacion con la informaci�n y lo mete en un arraylist de reclamaciones.
	     * Resumidamente metemos en un arraylist todas las reclamaciones de la base de datos para usarla posteriormente en otros metodos
	     * @return
	     * @throws ClassNotFoundException
	     * @throws SQLException
	     */
	    public static ArrayList<Reclamacion> listaReclamacion() throws ClassNotFoundException, SQLException{

	        ArrayList<Reclamacion> listaReclamaciones = new ArrayList<>();

	        try {

	            String query = "SELECT * FROM reclamacion";
	            Class.forName("com.mysql.cj.jdbc.Driver");
	            String url = "jdbc:mysql://localhost:3306/proyecto";
	            Connection con = DriverManager.getConnection(url, "root", "mysql");
	            Statement st = con.createStatement();
	            ResultSet rs= st.executeQuery(query);

	            while (rs.next()) {

	                String id= rs.getString("id");
	                String idProyecto= rs.getString("idProyecto_re");
	                String nombre = rs.getString("nombre");
	                String descripcion = rs.getString("descripcion");
	                String idUsuario = rs.getString("idUsuario_re");
	                String estado = rs.getString("estado");

	                listaReclamaciones.add(new Reclamacion(id, idProyecto, nombre, descripcion, idUsuario, estado));

	            }
	        }catch(SQLException e) {
	            System.out.println("Error al recuperar las reclamaciones");
	        }
	        return listaReclamaciones;
	    }
	    

	    /**
	     *Metodo que actualiza el estado de un proyecto a En proceso.
	     *Este es llamado cuando se a�ade la primera fase del proyecto
	     * @throws ClassNotFoundException
	     * @throws SQLException
	     */
	    public static void actualizarEstadoProyecto() throws ClassNotFoundException, SQLException {

	        String query = "UPDATE proyecto SET estado = \"En proceso\" WHERE id='"+BBDD.idProyectoActual+"' " ;
	        Class.forName("com.mysql.cj.jdbc.Driver");
	        String url = "jdbc:mysql://localhost:3306/proyecto";
	        Connection con = DriverManager.getConnection(url, "root", "mysql");
	        Statement st = con.createStatement();
	        try {
	            st.executeUpdate(query);
	        } catch (SQLException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
	    }
	    
	    /**
	     * Metodo que coje todas las tareas de ka base de datos de un proyecto especifico
	     * lo incrusta en html , devuelve un String del codigo html que se introducira en los formularios para elegir una tarea
	     * @return
	     * @throws ClassNotFoundException 
	     * @throws SQLException 
	     */
	    public static String cargarTareasForm() throws ClassNotFoundException, SQLException {
	    
	    String resultado = "";
	    
	    String query = "SELECT idTarea, nombre, idUsuario FROM tarea WHERE idProyecto='"+BBDD.idProyectoActual+"' " ;
        Class.forName("com.mysql.cj.jdbc.Driver");
        String url = "jdbc:mysql://localhost:3306/proyecto";
        Connection con = DriverManager.getConnection(url, "root", "mysql");
        Statement st = con.createStatement();
        ResultSet rs= st.executeQuery(query);

        while(rs.next()) {
            resultado = resultado + "<option value= \""+rs.getString("idTarea")+"\">"+rs.getString("nombre")+"| Participante : "+rs.getString("idUsuario")+"</option>";
        }
	    
	    return resultado;
	    	
	    }
	    
	    /**
	     * El metodo recibe por parametro el id de una fase, se ejecuta un query que elimina la fase de la base de datos
	     * @param idFase
	     * @throws ClassNotFoundException
	     * @throws SQLException
	     */
		public static void eliminarFase(String idFase) throws ClassNotFoundException, SQLException{			
			String query = "DELETE FROM fase WHERE id='"+idFase+"'";
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://localhost:3306/proyecto";
			Connection con = DriverManager.getConnection(url, "root", "mysql");
			Statement st = con.createStatement();
			try {
	            st.executeUpdate(query);
	        } catch (SQLException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
			

		}
		
		/**
		 * Se pasa por parametro el id de una fase
		 * este metodo elimina todas las tareas de una fase,
		 *en este caso es usado a la vez con el metodo anterior para cuando eliminemos una fase tambien se elimine todo lo dependiente (tareas)
		 * @param idFase
		 * @throws ClassNotFoundException
		 * @throws SQLException
		 */
		public static void eliminarTareasFase(String idFase) throws ClassNotFoundException, SQLException {
			String query = "DELETE FROM tarea WHERE idFase='"+idFase+"'";
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://localhost:3306/proyecto";
			Connection con = DriverManager.getConnection(url, "root", "mysql");
			Statement st = con.createStatement();
			try {
	            st.executeUpdate(query);
	        } catch (SQLException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
		}
	
		/**
	     * se pasa por parmetro el id del participante en cuestion para luego borrarlo de la base de datos del grupo de participantes en el proyecto
	     * @param idFase
	     * @throws ClassNotFoundException
	     * @throws SQLException
	     */
		public static void eliminarParticipanteProyecto(String idUsuario) throws ClassNotFoundException, SQLException{			
			String query = "DELETE FROM grupo_participantes WHERE idUsuario='"+idUsuario+"' AND idProyecto = '"+BBDD.idProyectoActual+"'";
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://localhost:3306/proyecto";
			Connection con = DriverManager.getConnection(url, "root", "mysql");
			Statement st = con.createStatement();
			try {
	            st.executeUpdate(query);
	        } catch (SQLException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
		}
		
		/**
	     * Este metodo esta correlacionado con el anterior 
	     * se pasa por parmetro el id del participante en cuestion para luego borrarlo de la base de datos del grupo de participantes en fases de un proyecto
	     * Cunado eliminamos un participantes queremos que ya no se muestre en ningun grupo ni
	     * @param idFase
	     * @throws ClassNotFoundException
	     * @throws SQLException
	     */
		public static void eliminarParticipanteFase(String idUsuario) throws ClassNotFoundException, SQLException{			
			String query = "DELETE FROM grupo_participantes_fase WHERE idUsuario='"+idUsuario+"' AND idProyecto = '"+BBDD.idProyectoActual+"'";
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://localhost:3306/proyecto";
			Connection con = DriverManager.getConnection(url, "root", "mysql");
			Statement st = con.createStatement();
			try {
	            st.executeUpdate(query);
	        } catch (SQLException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
		}
		
		/**
		 * Este metodo tambien esta relacionado con el tema de liminar participantes
		 * cuando quitamos a un participante de l proyecto necesitamso que las tareas que tenia asignadas tampoco se muestren
		 * @param idUsuario
		 * @throws ClassNotFoundException
		 * @throws SQLException
		 */
		public static void eliminarParticipanteTareas(String idUsuario) throws ClassNotFoundException, SQLException {
			String query = "DELETE FROM tarea WHERE idUsuario='"+idUsuario+"' AND idProyecto = '"+BBDD.idProyectoActual+"'";
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://localhost:3306/proyecto";
			Connection con = DriverManager.getConnection(url, "root", "mysql");
			Statement st = con.createStatement();
			try {
	            st.executeUpdate(query);
	        } catch (SQLException e) {
	            // TODO Auto-generated catch block

	            e.printStackTrace();
	        }
		}
		
		/**
	     * Se pasa por parametro el id de la tarea
	     * ejecutamos un query que eliminara la respectiva tarea del proyecto actual
	     * @param idFase
	     * @throws ClassNotFoundException
	     * @throws SQLException
	     */
		public static void eliminarTarea(String id) throws ClassNotFoundException, SQLException{			
			String query = "DELETE FROM tarea WHERE idTarea='"+id+"' AND idProyecto = '"+BBDD.idProyectoActual+"'";
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://localhost:3306/proyecto";
			Connection con = DriverManager.getConnection(url, "root", "mysql");
			Statement st = con.createStatement();
			try {
	            st.executeUpdate(query);
	        } catch (SQLException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
		}
		
		/**
		 * Se pasa por parametro el id de la tarea
		 * Este metodo se ejecuta cuando terminamos una tarea y se le da al tick
		 * cambiara de estado a finalizado y ya no se mostrara en las tareas de usuario
		 * @param id
		 * @throws ClassNotFoundException
		 * @throws SQLException
		 */
		public static void actualizarEstadoTarea(String id) throws ClassNotFoundException, SQLException {
			String query = "UPDATE tarea SET estado = \"Finalizado\" WHERE idTarea='"+id+"'" ;
	        Class.forName("com.mysql.cj.jdbc.Driver");
	        String url = "jdbc:mysql://localhost:3306/proyecto";
	        Connection con = DriverManager.getConnection(url, "root", "mysql");
	        Statement st = con.createStatement();
	        try {
	            st.executeUpdate(query);
	        } catch (SQLException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
		}
		
		/**
		 * Metodo que se ejecuta cuando eleiminamos un pryecto
		 * en este caso esta borrando los participantes del proyecto en la base de datos
		 * @throws ClassNotFoundException
		 * @throws SQLException
		 */
		public static void eliminarProyectoGrupo() throws ClassNotFoundException, SQLException {
			String query = "DELETE FROM grupo_participantes WHERE idProyecto = '"+BBDD.idProyectoActual+"'";
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://localhost:3306/proyecto";
			Connection con = DriverManager.getConnection(url, "root", "mysql");
			Statement st = con.createStatement();
			try {
	            st.executeUpdate(query);
	        } catch (SQLException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
		}
		
		/**
		 * Metodo que se ejecuta cuando eleiminamos un pryecto
		 * en este caso esta borrando los participantes de fases del proyecto en la base de datos
		 * @throws ClassNotFoundException
		 * @throws SQLException
		 */
		public static void eliminarProyectoGrupoFases() throws ClassNotFoundException, SQLException {
			String query = "DELETE FROM grupo_participantes_fase WHERE idProyecto = '"+BBDD.idProyectoActual+"'";
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://localhost:3306/proyecto";
			Connection con = DriverManager.getConnection(url, "root", "mysql");
			Statement st = con.createStatement();
			try {
	            st.executeUpdate(query);
	        } catch (SQLException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
		}
		
		/**
		 * Metodo que se ejecuta cuando eleiminamos un pryecto
		 * en este caso esta borrando el proyecto de la tabla de proyectos en la base de datos
		 * @throws ClassNotFoundException
		 * @throws SQLException
		 */
		public static void eliminarProyecto() throws ClassNotFoundException, SQLException {
			String query = "DELETE FROM proyecto WHERE id = '"+BBDD.idProyectoActual+"'";
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://localhost:3306/proyecto";
			Connection con = DriverManager.getConnection(url, "root", "mysql");
			Statement st = con.createStatement();
			try {
	            st.executeUpdate(query);
	        } catch (SQLException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
		}
		
		/**
		 * Metodo que se ejecuta cuando eleiminamos un pryecto
		 * en este caso esta borrando todas las tareas del proyecto en la base de datos
		 * @throws ClassNotFoundException
		 * @throws SQLException
		 */
		public static void eliminarProyectoTareas() throws ClassNotFoundException, SQLException {
			String query = "DELETE FROM tarea WHERE idProyecto = '"+BBDD.idProyectoActual+"'";
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://localhost:3306/proyecto";
			Connection con = DriverManager.getConnection(url, "root", "mysql");
			Statement st = con.createStatement();
			try {
	            st.executeUpdate(query);
	        } catch (SQLException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
		}
		
		/**
		 * Metodo que se ejecuta cuando eleiminamos un pryecto
		 * en este caso esta borrando todas las fases del proyecto en la base de datos
		 * @throws ClassNotFoundException
		 * @throws SQLException
		 */
		public static void eliminarProyectoFases() throws ClassNotFoundException, SQLException {
			String query = "DELETE FROM fase WHERE idProyecto = '"+BBDD.idProyectoActual+"'";
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://localhost:3306/proyecto";
			Connection con = DriverManager.getConnection(url, "root", "mysql");
			Statement st = con.createStatement();
			try {
	            st.executeUpdate(query);
	        } catch (SQLException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
		}
		
		/**
		 * Este metodo incrusta en html la informacion de las fases de un proyecto especifico
		 * este metodo se usa para mandar la informacion al documento detallado
		 * @return
		 * @throws ClassNotFoundException
		 * @throws SQLException
		 */
		public static String cargarFasesDoc() throws ClassNotFoundException, SQLException {
			String resultado = "";
			
			String query = "SELECT * FROM fase  WHERE idProyecto='"+BBDD.idProyectoActual+"'" ;
	        Class.forName("com.mysql.cj.jdbc.Driver");
	        String url = "jdbc:mysql://localhost:3306/proyecto";
	        Connection con = DriverManager.getConnection(url, "root", "mysql");
	        Statement st = con.createStatement();
	        ResultSet rs= st.executeQuery(query);

	        resultado = "<h5><i class=\"icon icon-sitemap\"></i> Fases </h5> <h6> | ID | Nombre | Estado | Fecha Inicio | Fecha Final | <h6>";

	        while(rs.next()) {
	            resultado = resultado + "<h6>"+rs.getString("id")+" : "+rs.getString("nombre")+ " : "+rs.getString("estado")+ " : "+rs.getString("fecha_inicio")+ " : "+rs.getString("fecha_final")+ "</h6>";
	        }
			
			return resultado;
		}
		
		/**
		 * Metodo que carga todos los participantes de un proyecto
		 * este metodo se usa para mandar la informacion al documento detallado
		 * @return
		 * @throws ClassNotFoundException
		 * @throws SQLException
		 */
		public static String cargarParDoc() throws ClassNotFoundException, SQLException {
			String resultado = "";
			
			String query = "SELECT gp.idUsuario, u.nombre, u.apellidos FROM grupo_participantes gp JOIN usuario u ON gp.idUsuario = u.id WHERE gp.idProyecto='"+BBDD.idProyectoActual+"'" ;
	        Class.forName("com.mysql.cj.jdbc.Driver");
	        String url = "jdbc:mysql://localhost:3306/proyecto";
	        Connection con = DriverManager.getConnection(url, "root", "mysql");
	        Statement st = con.createStatement();
	        ResultSet rs= st.executeQuery(query);

	        resultado = "<h5><i class=\"icon icon-users\"></i> Partcipantes </h5> <h6> | ID | Nombre | Appelidos | <h6>";

	        while(rs.next()) {
	            resultado = resultado + "<h6>"+rs.getString("idUsuario")+" : "+rs.getString("nombre")+ " : "+rs.getString("apellidos")+ "</h6>";
	        }
			
			return resultado;
		}
		
		/**
		 * Este metodo carga todas las teras de un proyecto
		 * este metodo se usa para mandar la informacion al documento detallado
		 * @return
		 * @throws ClassNotFoundException
		 * @throws SQLException
		 */
		public static String cargarTareasDoc() throws ClassNotFoundException, SQLException {
			String resultado = "";
			
			String query = "SELECT * FROM tarea  WHERE idProyecto='"+BBDD.idProyectoActual+"'" ;
	        Class.forName("com.mysql.cj.jdbc.Driver");
	        String url = "jdbc:mysql://localhost:3306/proyecto";
	        Connection con = DriverManager.getConnection(url, "root", "mysql");
	        Statement st = con.createStatement();
	        ResultSet rs= st.executeQuery(query);

	        resultado = "<h5> Partcipantes </h5> <h6> | ID | Nombre | Estado | Fecha Entrega | ID Persona Asiganda | ID Fase | <h6>";

	        while(rs.next()) {
	            resultado = resultado + "<h6>"+rs.getString("idTarea")+" : "+rs.getString("nombre")+" : "+rs.getString("estado")+ " : "+rs.getString("fechaEntrega")+ " : "+rs.getString("idUsuario")+ " : "+rs.getString("idFase")+"</h6>";
	        }
			
			return resultado;
		}
		
		/**
		 *pasamos por parametro las fechas de la fase que se ha creado en el formulario de la apgina web y retorna un booleano
		 *cargamos las fechas del proyecto actual en unas variables y los comparamos
		 *si la fase no esta temporalmente dentro del rango del proyecto el metodo devovera el booleano falso y por lo tanto no se insertara en la base de datos 
		 * @param fechaI
		 * @param fechaF
		 * @return
		 * @throws ClassNotFoundException
		 * @throws SQLException
		 * @throws ParseException
		 */
		public static boolean comrpobarFecha(String fechaI, String fechaF) throws ClassNotFoundException, SQLException, ParseException {
			boolean mal = false;
			String fechaIn = "";
			String fechaFi = "";
			
			String query = "SELECT * FROM proyecto WHERE id='"+BBDD.idProyectoActual+"'" ;
	        Class.forName("com.mysql.cj.jdbc.Driver");
	        String url = "jdbc:mysql://localhost:3306/proyecto";
	        Connection con = DriverManager.getConnection(url, "root", "mysql");
	        Statement st = con.createStatement();
	        ResultSet rs= st.executeQuery(query);
	        
	        while (rs.next()) {
	        	fechaIn = rs.getString("fechaInicio");
	        	fechaFi = rs.getString("fechaFinal");	        	
	        }
	        
	        SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");
	        
	        Date fechaIfase = formato.parse(fechaI);
	        Date fechaFfase = formato.parse(fechaF);
	        Date fechaIproyect = formato.parse(fechaIn);
	        Date fechaFproyect = formato.parse(fechaFi);
	        
	        if(fechaIfase.compareTo(fechaIproyect) >= 0) {
	        	if(fechaFfase.compareTo(fechaFproyect) <= 0) {
	        		mal = true;
	        	}
	        }
			
			return mal;
		}


}




