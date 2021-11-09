package medical.m2i.api;

import dao.DbConnection;
import entities.PatientEntity;
import entities.VilleEntity;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/patient")
public class PatientRESTAPI {
    EntityManager   em = DbConnection.getInstance();

    private PatientEntity getPatient( int id ){
        PatientEntity v = em.find(PatientEntity.class , id);
        if(  v == null ){
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return v;
    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("")
    public List<PatientEntity> getAll(){

        List<PatientEntity> p=em.createNativeQuery("SELECT * FROM patient",PatientEntity.class).getResultList();
        return p;
    }







    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public PatientEntity getOne(@PathParam("id") int id){
        return em.find(PatientEntity.class, id) ;
    }




    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("")
    public void addPatient(PatientEntity p) {

        // Récupération d’une transaction
        EntityTransaction tx = em.getTransaction();
        // Début des modifications
        try {
            tx.begin();
            em.persist(p);
            tx.commit();
        } catch (Exception e) {

            tx.rollback();
        } finally {
            // em.close();
            // emf.close();
        }

    }


    @DELETE
    @Path("/{id}")
    public void DeletePatient(@PathParam("id") int id) {
        PatientEntity p = em.find(PatientEntity.class, id) ;
    if ( p == null ){
        throw new WebApplicationException(Response.Status.NOT_FOUND);
    }
        // Récupération d’une transaction
        EntityTransaction tx = em.getTransaction();
        // Début des modifications

        try {
            tx.begin();
            em.remove(p);
            tx.commit();
        } catch (IllegalArgumentException e) {
            tx.rollback();
        } finally {
            // em.close();
            // emf.close();
        }
    }

    @PUT
    @Path("/{id}")
    @Consumes( MediaType.APPLICATION_JSON )
    public void updatePatient( @PathParam("id") int id , PatientEntity vparam ){

        PatientEntity v = getPatient(id);

        v.setNom( vparam.getNom() );
        v.setAdresse( vparam.getAdresse() );
        v.setDatenaissance( vparam.getDatenaissance() );
        v.setPrenom( vparam.getPrenom());
        v.setVille( vparam.getVille() );

        EntityTransaction tx = em.getTransaction();
        // Début des modifications
        try {
            tx.begin();
            em.persist( v );
            tx.commit();
            // }catch ( IllegalArgumentException e ){
            //    throw new WebApplicationException(Response.Status.NOT_FOUND); // sol 2
        } catch (Exception e) {
            tx.rollback();
            System.out.println("Exception " + e.getMessage() );
            throw e;
        }
    }


}













