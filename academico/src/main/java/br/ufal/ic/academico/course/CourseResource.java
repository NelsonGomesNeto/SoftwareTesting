package br.ufal.ic.academico.course;

import br.ufal.ic.academico.subject.Subject;
import br.ufal.ic.academico.subject.SubjectDAO;
import io.dropwizard.hibernate.UnitOfWork;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;

@Path("course")
@Slf4j
@RequiredArgsConstructor
@Produces(MediaType.APPLICATION_JSON)
public class CourseResource {

	private final CourseDAO courseDAO;
	private final SubjectDAO subjectDAO;

	@GET
	@UnitOfWork
	public Response getAll() {

		log.info("getAll");
		ArrayList<Course> courses = courseDAO.getAll();
		return(Response.ok(courses).build());
	}

	@GET
	@Path("/{id}")
	@UnitOfWork
	public Response getById(@PathParam("id") Long id) {

		log.info("getById: id={}", id);
		Course p = courseDAO.get(id);
		if (p == null) return(Response.status(Response.Status.NOT_FOUND)
			.entity(String.format("There's no course with id %d", id)).build());
		return(Response.ok(p).build());
	}

	@POST
	@UnitOfWork
	@Consumes("application/json")
	public Response save(CourseDTO entity) {

		log.info("save: {}", entity);
		if (entity == null)
			return(Response.status(Response.Status.BAD_REQUEST)
				.entity("json doesn't have a course").build());
		if (courseDAO.getByNameAndDegreeLevel(entity.name, entity.degreeLevel) != null)
			return(Response.status(Response.Status.BAD_REQUEST)
				.entity(String.format("Course with name %s and %s degree level already exists", entity.name, entity.degreeLevel)).build());

		ArrayList<Subject> subjects = new ArrayList<>();
		entity.subjectsCodes.forEach((code) -> {
			Subject aux = subjectDAO.getByCode(code);
			if (aux != null) subjects.add(aux);
		});
		Course c = new Course(entity.name, subjects, entity.degreeLevel);
		return(Response.ok(courseDAO.persist(c)).build());
	}

	@Getter
	@AllArgsConstructor
	@RequiredArgsConstructor
	@ToString
	public static class CourseDTO {

		private String name;
		private ArrayList<String> subjectsCodes;
		private String degreeLevel;
	}
}
