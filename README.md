# fiddler-acelerator

Este repositorio contiene un Servidor proxy HTTP construido en java y es multihilo. Consta de dos clases un Main que iniciativa la app creando el socket a donde llegan todas las peticiones. Estas son tratadas logueadas y por ultimo, se crea el request al destino original para obtener, loguear y devolver el response.

El proxy puede no funcionar para todas las peticiones, contempla de dos tipos imágenes y peticiones de texto. Cabe destacar que las imágenes no son logradas.

Los los quedan almacenados en la carpeta donde se ejecuta la app. En un archivo con el nombre de la fecha en la cual se corre.

Para correr la app se debe ejecutar desde una consola el comando java -jar fiddlerAcelerator.jar

Consideraciones:

- Se debe tener instalado previamente Java (1.8)
- Para chrome se debe setear en preferencias del sistema, el Proxy de web (HTTP) con los siguientes valores: Servidor: localhost y puerto: 8888
- Para firefox se debe ir Preferencias -> Proxy de red -> Configuración. Setear el check Configuración manual de proxy -> Proxy HTTP con localhost y puerto 8888. (Se debe desactivar la configuración de proxy a nivel de sistema y solo sesearlo en firefox)
