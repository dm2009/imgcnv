package org.imgcnv.rest;

import org.imgcnv.entity.ImageResource;
import org.imgcnv.service.concurrent.JobMapConfig;
import org.imgcnv.service.concurrent.Producer;
import org.springframework.beans.factory.annotation.Autowired;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

/**
 * Class implements REST API using Java Jersey. Created by Dmitry_Slepchenkov on
 * 2/1/2017.
 */
@Path("/rest")
@Api(value = "/rest", description = "APIs for working with images")
@Produces(MediaType.APPLICATION_JSON)
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
    @ApiOperation(value = "Return value of job", httpMethod = "POST",
    notes = "Post urls as string with ;"
            + " delimiter for download and convert images.",
    response = String.class)
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

        Long job = producer.addToProducer(ob);

        StringBuilder output = new StringBuilder("Your job nomber is: ")
                .append(job.toString())
                .append(" Server receive list: ")
                .append(ob.toString());

        return Response.status(Response.Status.OK)
                .entity(output.toString()).build();
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
    @ApiOperation(value = "Return status of job ready or not ready",
    httpMethod = "GET",
    notes = "For return status of job",
    response = String.class)
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

        StringBuilder output = new StringBuilder("Your job: ")
                .append(id)
                .append(" is finished: ")
                .append(jobMap.isReadyJob(Long.valueOf(id)));
        return Response.status(Response.Status.OK).entity(output.toString())
                .build();
    }

    /**
     * Test endpoint with path /hello/{hello}.
     *
     * @param msg
     *            String message for display
     * @return Response
     */
    /*
    @GET
    @Path("/hello/{hello}")
    public final Response getMsg(@PathParam("hello") final String msg) {
        String output = "Jersey say : " + msg;
        return Response.status(Response.Status.OK).entity(output).build();

    }
    */
}
