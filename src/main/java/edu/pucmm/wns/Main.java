package edu.pucmm.wns;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.pucmm.wns.encapsulaciones.Usuario;
import edu.pucmm.wns.init.BootStrap;
import edu.pucmm.wns.servicios.OperacionesService;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.template.freemarker.FreeMarkerEngine;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static spark.Spark.*;

/**
 * Aplicación para fines de demostración de los aspectos de seguridad
 */
public class Main {


    /**
     *
     * @param model
     * @param templatePath
     * @return
     */
    public static String render(Map<String, Object> model, String templatePath) {
        return new FreeMarkerEngine().render(new ModelAndView(model, templatePath));
    }

    private static void validarPermiso(Request request, Response response){
        if(request.session().attribute("usuario") == null) {
            halt(401, "No tienes permisos");
        }
    }

    /**
     *
     * @param args
     */
    public static void main(String[] args) throws SQLException {
        System.out.println("Aplicación Web No Segura");
        Gson gson = new Gson();

        BootStrap.init();

        before("/admin/*", Main::validarPermiso);
        before("/csrf", Main::validarPermiso);

        get("/", (req, res) ->{
            Map<String, Object> model = new HashMap<>();
            return render(model, "index.ftl");
        });

        get("/sqli", (req, res) ->{
            Map<String, Object> model = new HashMap<>();
            if(req.queryParams("error")!=null){
                model.put("error", 1);
            }
            return render(model, "sqli.ftl");
        });

        get("/csrf", (req, res) ->{
            Map<String, Object> model = new HashMap<>();
            if(req.queryParams("usuario")!=null &&
                    req.queryParams("nombre")!=null &&
                    req.queryParams("clave")!=null ){
                System.out.println("Creando los usuarios.");
                OperacionesService.getInstancia().crearUsuario(new Usuario(req.queryParams("usuario"),
                        req.queryParams("nombre"),
                        req.queryParams("clave")));
            }
            return render(model, "csrf.ftl");
        });

        get("/admin/", (req, res) ->{
            Map<String, Object> model = new HashMap<>();
            return render(model, "/admin/admin.ftl");
        });



        /**
         * Recibe los parametros de usuario y clave vía post
         * llamando al metodo vulnerable.
         */
        post("/procesarSqliVulnerable", (req, res) ->{

            //
            String usuario = req.queryParams("usuario");
            String clave = req.queryParams("clave");

            //
            Usuario objUsuario = OperacionesService.getInstancia().getUsuarioPorClaveNoSeguro(usuario, clave);
            if(objUsuario!=null){
                req.session().attribute("usuario", objUsuario);
                res.redirect("/admin/");
            } else{
                res.redirect("/sqli?error=1");
            }
            return "";
        });

        /**
         * Consulta de la información de usuario dado su usuario por el metodo vulnerable.
         */
        get("/consultaUsuarioVulnerable", (req, res) ->{
            return OperacionesService.getInstancia().getUsuarioPorIdVulnerable(req.queryParams("id"));
        }, gson::toJson);

        /**
         * Recibe los parametros de usuario y clave vía post
         * llamando al metodo NO vulnerable.
         */
        post("/procesarSqliNoVulnerable", (req, res) ->{

            //
            String usuario = req.queryParams("usuario");
            String clave = req.queryParams("clave");

            //
            Usuario objUsuario = OperacionesService.getInstancia().getUsuarioPorClaveSeguro(usuario, clave);
            if(objUsuario!=null){
                req.session().attribute("usuario", objUsuario);
                res.redirect("/admin/");
            } else{
                res.redirect("/sqli?error=1");
            }
            return "";
        });

        /**
         *
         */
        get("/xss", (req, res) ->{
            Map<String, Object> model = new HashMap<>();
            model.put("lista", OperacionesService.getInstancia().getListaUsuarios());
            return render(model, "xss.ftl");
        });

        post("/crearUsuarioVulnerable", (req, res) ->{

            //
            String usuario = req.queryParams("usuario");
            String nombre = req.queryParams("nombre");
            String clave = req.queryParams("clave");

            //
            boolean ok = OperacionesService.getInstancia().crearUsuarioVulnerable(new Usuario(usuario, nombre, clave));

            res.redirect("/xss"+ (ok ? "" : "?error=1"));
            return "";
        });
    }

    @Override
    protected void finalize() throws Throwable {
        System.out.println("Parando aplicación");
        BootStrap.stopDb();
    }
}
