package br.ufal.ic.academico.person;

import br.ufal.ic.academico.BasicDAO;
import br.ufal.ic.academico.subject.Subject;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;

import java.util.ArrayList;

public class PersonDAO extends BasicDAO<Person> {

	public PersonDAO(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	public ArrayList<Person> getAll() {
		return(ArrayList<Person>) currentSession().createQuery("from Person").list();
	}
}
