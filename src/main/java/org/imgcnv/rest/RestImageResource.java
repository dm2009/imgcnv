package org.imgcnv.rest;

import org.imgcnv.entity.ImageResource;
import org.imgcnv.service.concurrent.JobMapWrapper;
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
    private Producer<ImageResource> producer;

    /**
     * JobMapConfig for class.
     */
    @Autowired
    private JobMapWrapper jobMap;

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
        List<ImageResource> ob = new ArrayList<ImageResource>(
                ImageResource.imageResourceSetFromString(params));

        Long jobId = producer.addToProducer(ob);

        StringBuilder output = new StringBuilder("Your job number is: ")
                .append(jobId.toString())
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
        StringBuilder output = new StringBuilder("Your job: ")
                .append(id)
                .append(" is finished: ")
                .append(jobMap.isReadyJob(Long.valueOf(id)));
        return Response.status(Response.Status.OK).entity(output.toString())
                .build();
    }

}
