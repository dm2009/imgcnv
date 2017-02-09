package org.imgcnv.rest;

import org.imgcnv.concurrent.JobExecutor;
import org.imgcnv.entity.ImageResource;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

/**
 * Created by Dmitry_Slepchenkov on 2/1/2017.
 */

@Path("/")
public class RestImageResource {

    private JobExecutor executor;

    public JobExecutor getExecutor() {
        return executor;
    }

    public void setExecutor(JobExecutor executor) {
        this.executor = executor;
    }

 
    @POST
    @Path("/linklists")
    public final Response postLinkLists(String params) {
        return postImages(params);
    }

    @POST
    @Path("/jobs")
    public final Response postJobs(String params) {
        return postImages(params);
    }

    
    public final Response postImages(String params) {
        List<ImageResource> ob = new ArrayList<ImageResource>(ImageResource.ImageResourceSetFromString(params));

        JobExecutor executor = JobExecutor.getInstance();

        Long job = executor.addToExecutor(ob);
        return Response.status(Response.Status.OK).entity("<b>Your job nomber is:</b> <br>" + job.toString()
                + "</br><b>Server receive list:</b> " + ob.toString()).build();
    }

 
    @GET
    @Path("/linklists/{id}")
    public final Response getLinkListsInfo(@PathParam("id") String id) {
        return ImagesInfo(id);
    }

    @GET
    @Path("/jobs/{id}")
    public final Response getJobsInfo(@PathParam("id") String id) {
        return ImagesInfo(id);
    }

    
    public final Response ImagesInfo(String id) {

        JobExecutor executor = JobExecutor.getInstance();
        String output = "Your job: " + id + " is finished: " + executor.isReadyJob(Long.valueOf(id));
        return Response.status(Response.Status.OK).entity(output).build();
    }

    @GET
    @Path("/hello/{hello}")
    public Response getMsg(@PathParam("hello") String msg) {
        String output = "Jersey say : " + msg;
        return Response.status(Response.Status.OK).entity(output).build();

    }
}