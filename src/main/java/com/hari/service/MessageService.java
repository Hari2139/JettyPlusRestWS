package com.hari.service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;

/**
 * Description: 
 * File: $ MessageService.java $
 * Module:  com.hari.service
 * Created: Oct 12, 2013
 * @author Hari 
 * @version $Revision:  $
 * Last Changed: $Date: Oct 12, 2013 2:36:37 PM $
 * Last Changed By: $ Hari $
 */
@Path("/message")
public class MessageService {
	@GET
	@Path("/{name}")
	public Response getMessage(@PathParam("name") String name) {
		return Response
				.ok(new GenericEntity <String>("Hello " + name + "!",
						String.class)).build();
	}
}
/**
 *  Modification History:
 *
 *  $Log:  $
 *
 */
