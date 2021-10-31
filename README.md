### Requerimiento 1
<hr />

Se pide hacer un programa cliente-servidor con sockets, dicho programa consistirá en crear una aplicación que gestione una serie de libros de una biblioteca virtual.

Los libros tendrán un ISBN, un título, un autor y un precio. Se encontrarán alojados en el servidor. Dicho servidor cuando arranque tendrá 5 libros preestablecidos con todos los datos rellenos. Los libros se guardarán en memoria en cualquier tipo de estructura de datos (como puede ser un lista). Se valorará que el servidor esté preparado para que interactúen con él varios clientes.

La aplicación cliente mostrará un menú como el que sigue:

    Consultar libro por ISBN
    Consultar libro por titulo
    Salir de la aplicación

La aplicación se ejecutará hasta que el cliente decida pulsar la opción de “salir de la aplicación”.

El cliente deberá de recoger todos los datos del usuario necesarios y mandarlos al servidor en un solo envio.


### Requerimiento 2
<hr />

Se pide añadir otra opción que sea “Consultar libros por autor”. En este caso hay que tener en cuenta que puede haber varios libros por autor, por lo que el servidor puede devolver una lista de libros. Se recomienda pensar en grupo el formato de envio de información.


### Requerimiento 3
<hr />

Se pide añadir otra opción que sea “Añadir libro”. En este caso el cliente pedirá todos los datos del libro y los enviará al servidor para que este lo guarde en el servidor.

El cliente deberá de recoger todos los datos del usuario y mandarlos al servidor en un solo envio.

