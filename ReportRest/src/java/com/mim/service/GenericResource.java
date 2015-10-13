/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mim.service;

import com.google.gson.Gson;
import com.mim.model.Actividades;
import com.mim.model.Reporte2;
import com.mim.model.Trabajo;
import com.mim.model.Usuario2;
import dtos.Reporte2DTO;
import dtos.TrabajoDTO;
import dtos.Usuario2DTO;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.FormDataParam;

/**
 * REST Web Service
 *
 * @author NORE
 */
@Path("generic")
public class GenericResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of GenericResource
     */
    public GenericResource() {
    }

    /**
     * Retrieves representation of an instance of
     * com.mim.service.GenericResource
     *
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("application/json")
    public String getJson() {
        //TODO return proper representation object
        return System.getenv("OPENSHIFT_DATA_DIR");
    }

    @GET
    @Path("/dir")
    @Produces("application/json")
    public String build() {
        File file = new File(System.getenv("OPENSHIFT_DATA_DIR") + "imagenes/");
        String status = "hola";
        if (!file.exists()) {
            if (file.mkdir()) {
                status = "Directory is created!";
            } else {
                status = "Failed to create directory!";
            }
        }
        return status;
    }

    @POST
    @Path("/prime")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public void saveImagen2(FormDataMultiPart multiPart) {

        String jobId = null;
        InputStream stream = null;

        Map<String, List<FormDataBodyPart>> list = multiPart.getFields();
        for (Map.Entry<String, List<FormDataBodyPart>> entrySet : list.entrySet()) {
            String key = entrySet.getKey();
            List<FormDataBodyPart> value = entrySet.getValue();
            //System.out.println("Llave: " + key);
            if (key.equals("id")) {
                for (FormDataBodyPart val : value) {
                    jobId = val.getValue();

                }
            } else if (key.equals("file")) {
                for (FormDataBodyPart val : value) {
                    stream = val.getValueAs(InputStream.class);
                }
            }
        }

        if ((jobId != null) && (stream != null)) {
            //System.out.println("Procces data");
            proccessStream(stream, jobId);

        }

    }

    private void proccessStream(InputStream val, String jobId) {
        Random r = new Random();
        int i1 = r.nextInt(200 - 70) + 17;
        int i2 = r.nextInt(67 - 12) - 6;
        int i3 = r.nextInt(173 - 60) + i2;
        String name = "JPEG_" + i1 + i2 + "compressed_" + i3 + ".jpg";

        writeImage(val, name);
        findEntityJobById(jobId, name);

    }

    private void findEntityJobById(String jobId, String name) {

        //System.out.println("Valor: " + jobId);
        if (jobId != null) {
            EntityManagerFactory factory = Persistence.createEntityManagerFactory("ReportRestPU");
            EntityManager em = factory.createEntityManager();

            try {

                TypedQuery<Trabajo> query = em.createQuery("SELECT c FROM Trabajo c WHERE c.idtrabajo = :id", Trabajo.class);
                query.setParameter("id", Integer.parseInt(jobId));
                Trabajo job = query.getSingleResult();
                job.setImagen(name);
                em.getTransaction().begin();
                em.merge(job);
                em.getTransaction().commit();
            } catch (NoResultException ex) {
                Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NumberFormatException ex) {
                Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                em.close();
                factory.close();
            }
        }

    }

    @POST
    @Path("/subir/imagen")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public void saveImagen(@FormDataParam("file") InputStream stream) {
        //writeImage(stream);
    }

    private void writeImage(InputStream stream, String name) {
        //System.out.println("ID DEL TRABAJO: " + id);
        String path = "C:\\Users\\NORE\\Desktop\\prueba\\";
        OutputStream os = null;
        try {
            //File fileToUpload = new File(path + name);  LOCAL
            File fileToUpload = new File(System.getenv("OPENSHIFT_DATA_DIR") + "imagenes/" + name); // OPENSHIFT
            os = new FileOutputStream(fileToUpload);
            byte[] b = new byte[2048];
            int length;
            while ((length = stream.read(b)) != -1) {
                os.write(b, 0, length);
            }
        } catch (IOException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                os.close();
            } catch (IOException ex) {
                Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @GET
    @Path("/reptest")
    @Produces("application/json")
    public Reporte2 getReportTest() {
        //TODO return proper representation object
        Reporte2 rep = new Reporte2();
        //rep.setFecha(new Date());
        rep.setSitio("tecate");
        rep.setTrabajo("bombas remodelacion electrica");

        Trabajo trabajo = new Trabajo();
        trabajo.setDescripcion("instalar tableros");

        List<Actividades> list = new ArrayList<Actividades>();

        Actividades act = new Actividades();
        act.setDescripcion("actividades");

        Actividades act2 = new Actividades();
        act2.setDescripcion("tuberia");

        list.add(act);
        list.add(act2);
        trabajo.setActividadesList(list);

        List<Trabajo> trabajos = new ArrayList<Trabajo>();
        trabajos.add(trabajo);
        rep.setTrabajoList(trabajos);
        rep.setEstatus(0);
        return rep;
    }

    @POST
    @Path("/report/{user}")
    @Produces("application/json")
    public String persistRepo(@PathParam("user") String usuario, Reporte2 reporte) {
        if (usuario != null && reporte != null) {

            EntityManagerFactory factory = Persistence.createEntityManagerFactory("ReportRestPU");
            EntityManager em = factory.createEntityManager();
            try {
                TypedQuery<Usuario2> query = em.createQuery("SELECT c FROM Usuario2 c WHERE c.usuario = :usr", Usuario2.class);
                query.setParameter("usr", usuario);
                Usuario2 user = query.getSingleResult();
                EntityTransaction tran = em.getTransaction();
                tran.begin();
                reporte.setUsuarioIdusuario(user);
                reporte.setFecha(new Date());
                List<Trabajo> trabajos = new ArrayList<Trabajo>(reporte.getTrabajoList());

                reporte.getTrabajoList().clear();
                em.persist(reporte);

                //em.refresh(reporte);
                for (Trabajo tb : trabajos) {
                    // EntityTransaction tranJob = em.getTransaction();
                    //tranJob.begin();
                    tb.setReporteIdreporte(reporte);

                    List<Actividades> actividadesList = new ArrayList<Actividades>(tb.getActividadesList());
                    tb.getActividadesList().clear();

                    em.persist(tb);
                    //tranJob.commit();
                    //em.refresh(tb);

                    for (Actividades ac : actividadesList) {
                        try {
                            //EntityTransaction tranAct = em.getTransaction();
                            //tranAct.begin();
                            ac.setTrabajoIdtrabajo(tb);
                            em.persist(ac);
                            // tranAct.commit();
                        } catch (Exception e) {

                        }
                    }
                    tb.setActividadesList(actividadesList);
                }
                tran.commit();

                //System.out.println("I SHOULD RETURN THE DATA!!!!!!!!!!");
                Gson gson = new Gson();
                //System.out.println("Reporte ID: " + reporte.getIdreporte());
                Reporte2 repJson = new Reporte2();
                repJson.setIdreporte(reporte.getIdreporte());

                List<Trabajo> trabajoJsonList = new ArrayList<Trabajo>();
                repJson.setTrabajoList(trabajoJsonList);
                for (Trabajo trabajo : trabajos) {
                    //System.out.println("Trabajo ID: " + trabajo.getIdtrabajo());
                    Trabajo trabajoJson = new Trabajo(trabajo.getIdtrabajo());
                    trabajoJsonList.add(trabajoJson);
                    List<Actividades> actJsonList = new ArrayList<Actividades>();
                    trabajoJson.setActividadesList(actJsonList);
                    for (Actividades act : trabajo.getActividadesList()) {
                        //System.out.println("Actividad ID: " + act.getIdactividades());
                        actJsonList.add(new Actividades(act.getIdactividades()));
                    }
                }
                return gson.toJson(repJson);
            } catch (NoResultException e) {
                System.out.println("EXACTLY WHAT IS THE FUCKING FLAW HERE????");
                System.out.println(e);
                return null;
            } finally {
                em.close();
                factory.close();
            }
        } else {
            return null;
        }
    }

    /**
     * Retrieves representation of an instance of com.mim.entity.Usuario
     *
     *
     * @return
     */
    @GET
    @Path("/usuarios")
    @Produces("application/json")
    public List<Usuario2> findAll() {
        //TODO return proper representation object
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("ReportRestPU");
        EntityManager em = factory.createEntityManager();

        List<Usuario2> users = em.createNamedQuery("Usuario2.findAll").getResultList();

        em.close();
        factory.close();
        return users;
    }

    /**
     * PUT method for updating or creating an instance of GenericResource
     *
     * @param content representation for the resource
     */
    @PUT
    @Consumes("application/json")
    public void putJson(String content) {
    }

    @POST
    @Path("/test/{id}")
    @Consumes("application/json")
    @Produces("application/json")
    public Response postTest(Usuario2 content, @PathParam("id") int id) {

        return Response.ok(new Usuario2(id)).build();
    }

    @POST
    @Consumes("application/json")
    public Response registerUser(Usuario2 content) {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("ReportRestPU");
        EntityManager em = factory.createEntityManager();

        System.out.println("Usuario: -> " + content.getUsuario().toLowerCase());
        TypedQuery<Usuario2> query = em.createQuery("SELECT c FROM Usuario2 c WHERE c.usuario = :us", Usuario2.class);
        query.setParameter("us", content.getUsuario().toLowerCase());

        Usuario2 user = null;
        try {
            user = query.getSingleResult();
            System.out.println(user.getNombre());
            return Response.status(Response.Status.CONFLICT).build();
        } catch (NoResultException e) {
            System.out.println("Guardando usuario nuevo");
            EntityTransaction trans = em.getTransaction();
            trans.begin();
            em.persist(content);

            trans.commit();
            return Response.status(Response.Status.OK).build();
        } finally {
            em.close();
            factory.close();
        }

    }

    @GET
    @Path("/pdf/{id}")
    @Produces("application/json")
    public String getReportPDF(@PathParam("id") Integer id) {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("ReportRestPU");
        EntityManager em = factory.createEntityManager();

        TypedQuery<Reporte2> query = em.createQuery("SELECT c FROM Reporte2 c WHERE c.idreporte = :id", Reporte2.class);
        query.setParameter("id", id);

        try {
            Gson gson = new Gson();
            if (id != null) {
                Reporte2 repo = query.getSingleResult();

                Reporte2DTO temp = new Reporte2DTO(repo.getIdreporte());
                temp.setEstatus(repo.getEstatus());
                temp.setFecha(repo.getFecha());
                temp.setSitio(repo.getSitio());
                temp.setTrabajo(repo.getTrabajo());

                Usuario2 user = repo.getUsuarioIdusuario();

                Usuario2DTO tempUser = new Usuario2DTO(user.getIdusuario());
                tempUser.setApellidos(user.getApellidos());
                tempUser.setNombre(user.getNombre());
                tempUser.setUsuario(user.getUsuario());

                temp.setUsuarioIdusuario(tempUser);

                temp.setTrabajoList(TrabajoDTO.convertList(repo.getTrabajoList()));

                return gson.toJson(temp);
            } else {
                System.out.println("viene nulo");
                return Response.status(Response.Status.CONFLICT).build().toString();
            }

        } catch (NoResultException e) {
            return Response.status(Response.Status.CONFLICT).build().toString();
            //return null;
        } finally {
            //factory.close();
            //em.close();
        }

    }

    @GET
    @Path("/api/{id}")
    @Produces("image/jpg")
    public Response getFile(@PathParam("id") Integer id) {
        if (id != null) {

            EntityManagerFactory factory = Persistence.createEntityManagerFactory("ReportRestPU");
            EntityManager em = factory.createEntityManager();

            Query query = em.createQuery("SELECT c.imagen FROM Trabajo c where c.idtrabajo = :id");
            query.setParameter("id", id);

            String name = (String) query.getSingleResult();

            if (name != null) {
                System.out.println("es nulo");
                //File file = new File("C:\\Users\\NORE\\Pictures\\imagenesVolante\\cople.jpg");
                File file = new File(System.getenv("OPENSHIFT_DATA_DIR") + "imagenes/" + name); // OPENSHIFT
                ResponseBuilder response = Response.ok((Object) file);
                response.header("Content-Disposition",
                        "attachment; filename=" + name);
                response.header("nombre", name);

                //factory.close();
                //em.close();

                return response.build();
            } else {
                //factory.close();
                //em.close();
                return Response.noContent().build();
            }

        } else {
            return Response.noContent().build();
        }
    }
}
