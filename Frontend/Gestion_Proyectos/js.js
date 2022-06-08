 
 function enviarRegistro() {
    var http1;
    http1 = new XMLHttpRequest();
    http1.onreadystatechange = function (){
        if (http1.readyState==4 && http1.status==200){
            if(http1.responseText == "false"){
                window.location.href ="http://localhost:80/Gestion_Proyectos/login.html";
            }
            if(http1.responseText == "true"){
                alert("El email ya esta en uso");
            }
        }
    }
    http1.open("POST", "http://localhost:8080/Gestion_Proyectos/RegistroSRVLT", false);
    http1.setRequestHeader("Content-type","application/x-www-form-urlencoded");
    http1.send("nombre="+document.getElementById('nombre').value+"&&apellidos="+document.getElementById('apellidos').value+"&&email="+document.getElementById('email_primero').value+"&&contrasena="+document.getElementById('contraseña_primero').value);

}
    

function respuestaLogin(){

        var http;
        http = new XMLHttpRequest;

        http.onreadystatechange = function (){
            if (http.readyState==4 && http.status==200){
                if(http.responseText == "3"){
                    window.location.href ="http://localhost:80/Gestion_Proyectos/home.html";

                }else{
                    alert(http.responseText);
                }
            }
        }

        http.open("POST","http://localhost:8080/Gestion_Proyectos/LoginSRVLT",true);
        http.setRequestHeader("Content-type","application/x-www-form-urlencoded");
        http.send("email1="+document.getElementById('email').value+"&&contrasena1="+document.getElementById('contraseña').value);
}


function cargar(){
	proyectos_propios();
	proyectos_participe();
	informacion_usuario();
}


function proyectos_propios(){
	var http;
    http = new XMLHttpRequest;
    	
	http.onreadystatechange = function (){
        if (http.readyState==4 && http.status==200){
			document.getElementById('trabajos_propios').innerHTML = http.responseText;		
        }
    }
      
	 http.open("POST","http://localhost:8080/Gestion_Proyectos/ProyectosPropios",false);
	 http.setRequestHeader("Content-type","application/x-www-form-urlencoded");
	 http.send();
}

function proyectos_participe(){
	var http;
    http = new XMLHttpRequest;
    	
	http.onreadystatechange = function (){
        if (http.readyState==4 && http.status==200){
			document.getElementById('trabajos_participante').innerHTML = http.responseText;			
			}
     
		}
	 http.open("POST","http://localhost:8080/Gestion_Proyectos/ProyectosParticipante",false);
	 http.setRequestHeader("Content-type","application/x-www-form-urlencoded");
	 http.send();
}

function enviar_fase(){
    var http;
    http = new XMLHttpRequest();

    http.onreadystatechange = function (){
        if (http.readyState==4 && http.status==200){
        }

    }

    http.open("GET","http://localhost:8080/Gestion_Proyectos/FaseSRVLT",true);
    http.setRequestHeader("Content-type","application/x-www-form-urlencoded");
    http.send("nombre="+document.getElementById('nombre_fase').value+"&&descripcion="+document.getElementById('descripcion_fase').value+"&&fecha_inicio="+document.getElementById('fecha_inicio_fase').value+"&&fecha_final="+document.getElementById('fehca_final_fase').value+"&&importancia="+document.getElementById('importancia_fase').value);
}

function informacion_usuario(){
    var http;
    http = new XMLHttpRequest();

    http.onreadystatechange = function (){
        if (http.readyState==4 && http.status==200){
            document.getElementById('info').innerHTML =http.responseText;
        }

    }
    http.open("POST","http://localhost:8080/Gestion_Proyectos/InfoUsuarioSRVLT",false);
    http.setRequestHeader("Content-type","application/x-www-form-urlencoded");
    http.send();
}

function enviar_reclamacion(){
	var http;
    http = new XMLHttpRequest();

    http.onreadystatechange = function (){
        if (http.readyState==4 && http.status==200){
            alert(http.responseText);
        }

    }
    http.open("POST","http://localhost:8080/Gestion_Proyectos/ReclamacionSRVLT",false);
    http.setRequestHeader("Content-type","application/x-www-form-urlencoded");
    http.send("titulo="+document.getElementById('titulo').value+"&&tipo="+document.getElementById('tipo_problema').value+"&&descripcion="+document.getElementById('texto').value);
}


function enviaridProyecto(id){
	var http;
    http = new XMLHttpRequest();

    http.open("POST","http://localhost:8080/Gestion_Proyectos/CargarIdProyectoSRVLT",false);
    http.setRequestHeader("Content-type","application/x-www-form-urlencoded");
    http.send("id="+id);
	
}



