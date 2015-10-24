package com.michir.projects.arxiving.endpoints;

import java.io.IOException;
import java.nio.file.Path;

import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.michir.projects.arxiving.api.Arxiv;
import com.michir.projects.arxiving.api.Uploaded;
import com.michir.projects.arxiving.core.Arxiver;
import com.michir.projects.arxiving.core.Organizer;

@WebServlet("/upload")
@MultipartConfig
public class UploadFile extends HttpServlet {

	/**
	 * UID.
	 */
	private static final long serialVersionUID = -6138113413341875775L;

	@Inject
	private Organizer organizer;

	@Inject
	private Arxiver arxiver;

	@Inject @Uploaded
	private Event<Arxiv> event;
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			req.getParts()
			.stream()
			.forEach(e -> {
					try {
						Path path = organizer.save(e.getInputStream());
						arxiver.arxiv(path, e.getSubmittedFileName(), null);
						
						// fire uploaded
						Arxiv arg0 = new Arxiv();
						arg0.setPath(path.toFile().getAbsolutePath());
						arg0.setName(e.getSubmittedFileName());
						
						event.fire(arg0);
						
					} catch (Exception e1) {
						throw new RuntimeException(e1);
					}
				});
		} catch (IOException | ServletException e) {
			throw new RuntimeException(e);
		}
	}

}
