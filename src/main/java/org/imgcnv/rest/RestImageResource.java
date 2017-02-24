package org.imgcnv.rest;

import org.imgcnv.entity.ImageResource;
import org.imgcnv.service.concurrent.JobMapConfig;
import org.imgcnv.service.concurrent.Producer;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

/**
 * Class implements REST API using Java Jersey. Created by Dmitry_Slepchenkov on
 * 2/1/2017.
 */
@Path("/rest")
public class RestImageResource {

    /**
     * Producer for class.
     */
    @Autowired
    private Producer producer;

    /**
     * JobMapConfig for class.
     */
    @Autowired
    private JobMapConfig jobMap;

     /**
     * Create endpoint with path /jobs. Process url for download images and
     * transform its.
     *
     * @param params
     *            String with url list with delimiter
     * @return Response
     */
    @POST
    @Path("/jobs")
    public final Response postJobs(final String params) {
        return postImages(params);
    }

    /**
     * Process url for download images and transform its.
     *
     * @param params
     *            String with url list with delimiter
     * @return Response
     */
    public final Response postImages(final String params) {
        List<ImageResource> ob = new ArrayList<ImageResource>(
                ImageResource.imageResourceSetFromString(params));


        Long job = producer.addToProducer(ob); //executor.addToExecutor(ob);

        return Response.status(Response.Status.OK)
                .entity("Your job nomber is: " + job.toString()
                + " Server receive list: " + ob.toString()
                ).build();
    }

    /**
     * Create endpoint with path /linklists/{id}. Get status for job.
     *
     * @param id
     *            String with representation of job id.
     * @return Response
     */
    @GET
    @Path("/jobs/{id}")
    public final Response getJobsInfo(@PathParam("id") final String id) {
        return imagesInfo(id);
    }

    /**
     * Get status for job.
     *
     * @param id
     *            String with representation of job id.
     * @return Response
     */
    public final Response imagesInfo(final String id) {

        String output = "Your job: " + id + " is finished: "
        + jobMap.isReadyJob(Long.valueOf(id));
        return Response.status(Response.Status.OK).entity(output).build();
    }

    /**
     * Test endpoint with path /hello/{hello}.
     *
     * @param msg
     *            String message for display
     * @return Response
     */
    @GET
    @Path("/hello/{hello}")
    public final Response getMsg(@PathParam("hello") final String msg) {
        String output = "Jersey say : " + msg;
        return Response.status(Response.Status.OK).entity(output).build();

    }
}
