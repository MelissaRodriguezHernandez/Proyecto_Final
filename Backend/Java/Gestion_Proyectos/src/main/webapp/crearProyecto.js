function crearFase() {
    var http;
    http = new XMLHttpRequest();

    http.onreadystatechange = function () {
        if (http.readyState == 4 && http.status == 200) {
            alert(http.responseText);
        }

        http.open("GET", "http://localhost:8080/Gestion_Proyectos/prueba", false);
        http.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
        http.send();

    }
}

function drop() {
    var fase_drop = document.getElementsByClassName('fase_drop');
    var i;

    for (i = 0; i < fase_drop.length; i++) {
        fase_drop[i].addEventListener("click", function() {
            this.classList.toggle("active");
            var dropdownContent = this.nextElementSibling;
            if (dropdownContent.style.display === "block") {
                dropdownContent.style.display = "none";
            } else {
                dropdownContent.style.display = "block";
            }
        });
    }
}



let monthNames = ['Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio', 'Julio', 'Agosto', 'Septiembre','Octubre', 'Noviembre', 'Deciembre'];

let currentDate = new Date();
let currentDay = currentDate.getDate();
let monthNumber = currentDate.getMonth();
let currentYear = currentDate.getFullYear();

let dates = document.getElementById('dates');
let month = document.getElementById('month');
let year = document.getElementById('year');

let prevMonthDOM = document.getElementById('prev-month');
let nextMonthDOM = document.getElementById('next-month');

month.textContent = monthNames[monthNumber];
year.textContent = currentYear.toString();

prevMonthDOM.addEventListener('click', ()=>lastMonth());
nextMonthDOM.addEventListener('click', ()=>nextMonth());



const writeMonth = (month) => {

    for(let i = startDay(); i>0;i--){
        dates.innerHTML += ` <div class="calendar__date calendar__item calendar__last-days">
            ${getTotalDays(monthNumber-1)-(i-1)}
        </div>`;
    }

    for(let i=1; i<=getTotalDays(month); i++){
        if(i===currentDay) {
            dates.innerHTML += ` <div class="calendar__date calendar__item calendar__today">${i}</div>`;
        }else{
            dates.innerHTML += ` <div class="calendar__date calendar__item">${i}</div>`;
        }
    }
}

const getTotalDays = month => {
    if(month === -1) month = 11;

    if (month == 0 || month == 2 || month == 4 || month == 6 || month == 7 || month == 9 || month == 11) {
        return  31;

    } else if (month == 3 || month == 5 || month == 8 || month == 10) {
        return 30;

    } else {

        return isLeap() ? 29:28;
    }
}

const isLeap = () => {
    return ((currentYear % 100 !==0) && (currentYear % 4 === 0) || (currentYear % 400 === 0));
}

const startDay = () => {
    let start = new Date(currentYear, monthNumber, 1);
    return ((start.getDay()-1) === -1) ? 6 : start.getDay()-1;
}

const lastMonth = () => {
    if(monthNumber !== 0){
        monthNumber--;
    }else{
        monthNumber = 11;
        currentYear--;
    }

    setNewDate();
}

const nextMonth = () => {
    if(monthNumber !== 11){
        monthNumber++;
    }else{
        monthNumber = 0;
        currentYear++;
    }

    setNewDate();
}

const setNewDate = () => {
    currentDate.setFullYear(currentYear,monthNumber,currentDay);
    month.textContent = monthNames[monthNumber];
    year.textContent = currentYear.toString();
    dates.textContent = '';
    writeMonth(monthNumber);
}

writeMonth(monthNumber);


////////////////////////////////////////////////////////////////////////////////////////////////////

function crearProyecto(){
    var http;
    http = new XMLHttpRequest();

    http.open("POST","http://localhost:8080/Gestion_Proyectos/CrearProyectoSRVLT",false);
    http.setRequestHeader("Content-type","application/x-www-form-urlencoded");
    http.send("nombre="+document.getElementById('nombre_proyecto').value+"&&descripcion="+document.getElementById('descripcion_proyecto').value+"&&tipo="+document.getElementById('tipo_proyecto').value+"&&fechaI="+document.getElementById('fecha_inicio').value+"&&fechaF="+document.getElementById('fehca_final').value);
}

function cargarProyecto(){
    cargarInfoProyecto();
    cargarTareas();
    cargarParticipantes();
    cargarFases();
    cargarTareasUsuario();
}

function cargarInfoProyecto(){
    var http;
    http = new XMLHttpRequest();

    http.onreadystatechange = function (){
        if (http.readyState==4 && http.status==200){
            document.getElementById('infoProyecto').innerHTML =http.responseText;
        }

    }
    http.open("POST","http://localhost:8080/Gestion_Proyectos/CargarInfoProyectoSRVLT",false);
    http.setRequestHeader("Content-type","application/x-www-form-urlencoded");
    http.send();

}



    function crearParticipante() {
        var http;
        http = new XMLHttpRequest();

        http.open("POST", "http://localhost:8080/Gestion_Proyectos/CrearParticipanteSRVLT", false);
        http.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
        http.send("idUsuario=" + document.getElementById('participante').value);
    }

    function crearTarea() {
        var http;
        http = new XMLHttpRequest();

        http.open("POST", "http://localhost:8080/Gestion_Proyectos/CrearTareaSRVLT", false);
        http.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
        http.send("nombre=" + document.getElementById('nombre_tarea').value + "&&descripcion=" + document.getElementById('descripcion_tarea').value + "&&idFase=" + document.getElementById('fases').value + "&&idUsuario=" + document.getElementById('id_participante').value + "&&fecha=" + document.getElementById('fecha_tarea').value);
    }

    function cargarFases() {
        var http;
        http = new XMLHttpRequest();

        http.onreadystatechange = function () {
            if (http.readyState == 4 && http.status == 200) {
                document.getElementById('fases_proyecto').innerHTML = http.responseText;
            }

        }
        http.open("POST", "http://localhost:8080/Gestion_Proyectos/CargarFasesSRVLT", false);
        http.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
        http.send();
    }

    function cargarParticipantes() {
        var http;
        http = new XMLHttpRequest();

        http.onreadystatechange = function () {
            if (http.readyState == 4 && http.status == 200) {
                document.getElementById('participantes_proyecto').innerHTML = http.responseText;
            }

        }
        http.open("POST", "http://localhost:8080/Gestion_Proyectos/CargarParticipantesSRVLT", false);
        http.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
        http.send();
    }


    function cargarTareas() {
        var http;
        http = new XMLHttpRequest();

        http.onreadystatechange = function () {
            if (http.readyState == 4 && http.status == 200) {
                document.getElementById('tarea_proyecto').innerHTML = http.responseText;
            }

        }
        http.open("POST", "http://localhost:8080/Gestion_Proyectos/CargarTareasSRVLT", false);
        http.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
        http.send();
    }

    function cargarTareasUsuario() {
        var http;
        http = new XMLHttpRequest();

        http.onreadystatechange = function () {
            if (http.readyState == 4 && http.status == 200) {
                document.getElementById('tareasUsuario').innerHTML = http.responseText;
            }

        }
        http.open("POST", "http://localhost:8080/Gestion_Proyectos/CargarTareasUsuarioSRVLT", false);
        http.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
        http.send();
    }

    function cargarInfoForm() {
        cargarFasesForm();
        cargarParticipantesForm();
        cargarParticipantesFormEliminar();
        cargarFasesFormEliminar();
        cargarTareasFormEliminar();
    }

    function cargarFasesForm() {
        var http;
        http = new XMLHttpRequest();

        http.onreadystatechange = function () {
            if (http.readyState == 4 && http.status == 200) {
                document.getElementById('fases').innerHTML = http.responseText;
            }

        }
        http.open("POST", "http://localhost:8080/Gestion_Proyectos/CargarFasesFromSRVLT", false);
        http.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
        http.send();
    }

    function cargarParticipantesForm() {
        var http;
        http = new XMLHttpRequest();

        http.onreadystatechange = function () {
            if (http.readyState == 4 && http.status == 200) {
                document.getElementById('id_participante').innerHTML = http.responseText;
            }

        }
        http.open("POST", "http://localhost:8080/Gestion_Proyectos/CargarParticipanteFormSRVLT", false);
        http.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
        http.send();
    }


    function cargarParticipantesFormEliminar() {
        var http;
        http = new XMLHttpRequest();

        http.onreadystatechange = function () {
            if (http.readyState == 4 && http.status == 200) {
                document.getElementById('eliminar_p').innerHTML = http.responseText;
            }

        }
        http.open("POST", "http://localhost:8080/Gestion_Proyectos/CargarParticipanteFormSRVLT", false);
        http.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
        http.send();
    }

    function cargarFasesFormEliminar() {
        var http;
        http = new XMLHttpRequest();

        http.onreadystatechange = function () {
            if (http.readyState == 4 && http.status == 200) {
                document.getElementById('eliminar_f').innerHTML = http.responseText;
            }

        }
        http.open("POST", "http://localhost:8080/Gestion_Proyectos/CargarFasesFromSRVLT", false);
        http.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
        http.send();
    }

    function cargarTareasFormEliminar() {
        var http;
        http = new XMLHttpRequest();

        http.onreadystatechange = function () {
            if (http.readyState == 4 && http.status == 200) {
                document.getElementById('eliminar_t').innerHTML = http.responseText;
            }

        }
        http.open("POST", "http://localhost:8080/Gestion_Proyectos/CargarTareasFromSRVLT", false);
        http.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
        http.send();
    }

    function borrarFase() {
        var http;
        http = new XMLHttpRequest();

        http.open("POST", "http://localhost:8080/Gestion_Proyectos/BorrarFaseSRVLT", false);
        http.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
        http.send("idFase=" + document.getElementById('eliminar_f').value);
    }

    function borrarParticipante() {
        var http;
        http = new XMLHttpRequest();

        http.open("POST", "http://localhost:8080/Gestion_Proyectos/BorrarParticipanteSRVLT", false);
        http.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
        http.send("idParticipante=" + document.getElementById('eliminar_p').value);
    }

    function borrarTarea() {
        var http;
        http = new XMLHttpRequest();

        http.open("POST", "http://localhost:8080/Gestion_Proyectos/BorrarTareaSRVLT", false);
        http.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
        http.send("idTarea=" + document.getElementById('eliminar_t').value);
    }

    function actualizarEstadoTarea(idTarea, idFase) {
        document.getElementById(idTarea).remove();

        var http;
        http = new XMLHttpRequest();

        http.open("POST", "http://localhost:8080/Gestion_Proyectos/ActualizarTareaSRVLT", false);
        http.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
        http.send("id=" + idTarea);
    }

    function eliminarProyecto() {
        var http;
        http = new XMLHttpRequest();

        http.open("POST", "http://localhost:8080/Gestion_Proyectos/EliminarProyectoSRVLT", false);
        http.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
        http.send();
    }

    function cargarDocu() {
        cargarFasDoc();
        cargarParDoc();
        cargarTareaDoc();
    }

    function cargarParDoc() {
        var http;
        http = new XMLHttpRequest();

        http.onreadystatechange = function () {
            if (http.readyState == 4 && http.status == 200) {
                document.getElementById('participantes_doc').innerHTML = http.responseText;
            }

        }
        http.open("POST", "http://localhost:8080/Gestion_Proyectos/CargarParDocSRVLT", false);
        http.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
        http.send();
    }

    function cargarFasDoc() {
        var http;
        http = new XMLHttpRequest();

        http.onreadystatechange = function () {
            if (http.readyState == 4 && http.status == 200) {
                document.getElementById('fases_doc').innerHTML = http.responseText;
            }

        }
        http.open("POST", "http://localhost:8080/Gestion_Proyectos/CargarFaseDocSRVLT", false);
        http.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
        http.send();
    }

    function cargarTareaDoc() {
        var http;
        http = new XMLHttpRequest();

        http.onreadystatechange = function () {
            if (http.readyState == 4 && http.status == 200) {
                document.getElementById('tareas_doc').innerHTML = http.responseText;
            }

        }
        http.open("POST", "http://localhost:8080/Gestion_Proyectos/CargarTareaDocSRVLT", false);
        http.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
        http.send();
    }

    function fase() {
        var http;
        http = new XMLHttpRequest();

        http.open("POST", "http://localhost:8080/Gestion_Proyectos/FaseSRVLT", false);
        http.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
        http.send("nombre=" + document.getElementById('nombre_fase').value + "&&descripcion=" + document.getElementById('descripcion_fase').value + "&&fechaI=" + document.getElementById('fecha_inicio_fase').value + "&&fechaF=" + document.getElementById('fehca_final_fase').value);
    }