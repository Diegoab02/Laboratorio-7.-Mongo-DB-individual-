/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.services;

import com.example.PersistenceManager;
import com.example.models.Competitor;
import com.example.models.CompetitorDTO;
import com.example.models.Producto;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.json.simple.JSONObject;

/**
 *
 * @author Mauricio
 */
@Path("/competitors")
@Produces(MediaType.APPLICATION_JSON)
public class CompetitorService {

    @PersistenceContext(unitName = "mongoPU")
    EntityManager entityManager;

    @PostConstruct
    public void init() {
        try {
            entityManager = PersistenceManager.getInstance().getEntityManagerFactory().createEntityManager();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ---------- CONSULTA SIN PARAMETROS ----------
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        Query q = entityManager.createQuery("select u from Competitor u order by u.surname ASC");
        List<Competitor> competitors = q.getResultList();
        return Response.status(200).header("Access-Control-Allow-Origin", "*").entity(competitors).build();
    }

    // ---------- CREAR COMPETIDOR (sin vehiculo) ----------
    @POST
    @Path("/add")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createCompetitor(CompetitorDTO competitor) {
        Competitor c = new Competitor();
        JSONObject rta = new JSONObject();
        c.setAddress(competitor.getAddress());
        c.setAge(competitor.getAge());
        c.setCellphone(competitor.getCellphone());
        c.setCity(competitor.getCity());
        c.setCountry(competitor.getCountry());
        c.setName(competitor.getName());
        c.setSurname(competitor.getSurname());
        c.setTelephone(competitor.getTelephone());
        c.setVehicle(competitor.getVehicle());
        // Actividad: el producto es OBLIGATORIO
        c.setProducto(competitor.getProducto());

        try {
            entityManager.getTransaction().begin();
            entityManager.persist(c);
            entityManager.getTransaction().commit();
            entityManager.refresh(c);
            rta.put("competitor_id", c.getId());
        } catch (Throwable t) {
            t.printStackTrace();
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            c = null;
        }
        return Response.status(200).header("Access-Control-Allow-Origin", "*").entity(rta.toJSONString()).build();
    }

    // ---------- CREAR COMPETIDOR CON VEHICULO ----------
    @POST
    @Path("/vehicle")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createCompetitorWithVehicle(CompetitorDTO competitor) {
        Competitor c = new Competitor();
        JSONObject rta = new JSONObject();
        c.setAddress(competitor.getAddress());
        c.setAge(competitor.getAge());
        c.setCellphone(competitor.getCellphone());
        c.setCity(competitor.getCity());
        c.setCountry(competitor.getCountry());
        c.setName(competitor.getName());
        c.setSurname(competitor.getSurname());
        c.setTelephone(competitor.getTelephone());
        c.setVehicle(competitor.getVehicle());
        c.setProducto(competitor.getProducto());

        try {
            entityManager.getTransaction().begin();
            entityManager.persist(c);
            entityManager.getTransaction().commit();
            entityManager.refresh(c);
            rta.put("competitor_id", c.getId());
        } catch (Throwable t) {
            t.printStackTrace();
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            c = null;
        }
        return Response.status(200).header("Access-Control-Allow-Origin", "*").entity(rta.toJSONString()).build();
    }

    // ---------- CONSULTA CON PARAMETRO: competidores por nombre ----------
    @GET
    @Path("/name/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCompetitorsByName(@PathParam("name") String name) {
        TypedQuery<Competitor> query = (TypedQuery<Competitor>)
                entityManager.createQuery("SELECT c FROM Competitor c WHERE c.name = :name");
        List<Competitor> competitors = query.setParameter("name", name).getResultList();
        return Response.status(200).header("Access-Control-Allow-Origin", "*").entity(competitors).build();
    }

    // ===================== ACTIVIDAD =====================

    // Actividad 3: listar todos los productos almacenados
    @GET
    @Path("/productos")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllProductos() {
        Query q = entityManager.createQuery("SELECT c.producto FROM Competitor c WHERE c.producto IS NOT NULL");
        List<Producto> productos = q.getResultList();
        return Response.status(200).header("Access-Control-Allow-Origin", "*").entity(productos).build();
    }

    // Actividad 4: competidores cuyo nombre empieza por 'A'
    @GET
    @Path("/startsWithA")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCompetitorsStartingWithA() {
        TypedQuery<Competitor> query = (TypedQuery<Competitor>)
                entityManager.createQuery("SELECT c FROM Competitor c WHERE c.name LIKE :letra");
        List<Competitor> competitors = query.setParameter("letra", "A%").getResultList();
        return Response.status(200).header("Access-Control-Allow-Origin", "*").entity(competitors).build();
    }

    // ---------- CORS ----------
    @OPTIONS
    public Response cors(@javax.ws.rs.core.Context HttpHeaders requestHeaders) {
        return Response.status(200)
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
                .header("Access-Control-Allow-Headers", "AUTHORIZATION, content-type, accept")
                .build();
    }
}
