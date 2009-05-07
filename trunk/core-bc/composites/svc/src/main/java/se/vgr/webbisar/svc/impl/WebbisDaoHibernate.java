package se.vgr.webbisar.svc.impl;

import static org.hibernate.search.Search.getFullTextSession;
import static org.hibernate.search.jpa.Search.getFullTextEntityManager;

import static org.apache.commons.lang.StringUtils.*;

import java.util.List;
import java.util.StringTokenizer;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TermQuery;
import org.hibernate.CacheMode;
import org.hibernate.FetchMode;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.ejb.HibernateEntityManager;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.transform.DistinctResultTransformer;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.transform.RootEntityResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaCallback;
import org.springframework.orm.jpa.JpaTemplate;
import org.springframework.stereotype.Repository;

import antlr.StringUtils;

import se.vgr.webbisar.svc.WebbisDao;
import se.vgr.webbisar.types.Hospital;
import se.vgr.webbisar.types.Webbis;

/**
 * Declare POJO Spring Component DAO
 */
@Repository("webbisDao")
public class WebbisDaoHibernate implements WebbisDao {

	protected final Log log = LogFactory.getLog(getClass());

	JpaTemplate jpaTemplate;

	@Autowired
	public WebbisDaoHibernate(EntityManagerFactory entityManagerFactory) {
		this.jpaTemplate = new JpaTemplate(entityManagerFactory);
	}

	public JpaTemplate getJpaTemplate() {
		return jpaTemplate;
	}

	public void setJpaTemplate(JpaTemplate jpaTemplate) {
		this.jpaTemplate = jpaTemplate;
	}
	
	public void delete(Webbis webbis) {
		getJpaTemplate().remove(webbis);
	}

	public Webbis get(Long id) {
		return getJpaTemplate().find(Webbis.class, id);
	}

	public Webbis getDetached(final Long id) {
		Object o = getJpaTemplate().execute(new JpaCallback() {
			public Object doInJpa(EntityManager em) throws PersistenceException {
				Webbis webbis = em.find(Webbis.class, id);
				getHibernateSession(em).evict(webbis);
				return webbis;
			}
		});
		return (Webbis)o;
	}
	
	public void save(Webbis webbis) {		
		getJpaTemplate().persist(webbis);
	}

	public Webbis merge(Webbis webbis) {
		return getJpaTemplate().merge(webbis);
	}

	private FullTextQuery createSearchQuery(final String criteria, boolean includeDisabled, EntityManager em) {
		FullTextEntityManager fullTextEntityManager = getFullTextEntityManager(em);

		BooleanQuery query = new BooleanQuery();
		
		StringTokenizer st = new StringTokenizer(criteria);
		while(st.hasMoreTokens()) {
			String token = st.nextToken().toLowerCase();
			
			BooleanClause.Occur clause = BooleanClause.Occur.SHOULD;
			if(token.startsWith("+") && token.length() > 1) {
				token = token.substring(1);
				clause = BooleanClause.Occur.MUST;
			} else if(token.startsWith("-") && token.length() > 1) {
				token = token.substring(1);
				clause = BooleanClause.Occur.MUST_NOT;
			}
			
			BooleanQuery bq = new BooleanQuery();
			
			TermQuery tq = new TermQuery(new Term("name", token));
			tq.setBoost(2.0f);
			bq.add(new BooleanClause(tq, BooleanClause.Occur.SHOULD));
			tq = new TermQuery(new Term("parents.firstName", token));
			bq.add(new BooleanClause(tq, BooleanClause.Occur.SHOULD));
			tq = new TermQuery(new Term("parents.lastName", token));
			bq.add(new BooleanClause(tq, BooleanClause.Occur.SHOULD));
			tq = new TermQuery(new Term("birthDate", token));
			bq.add(new BooleanClause(tq, BooleanClause.Occur.SHOULD));
			tq = new TermQuery(new Term("hospital", token));
			bq.add(new BooleanClause(tq, BooleanClause.Occur.SHOULD));	
			tq = new TermQuery(new Term("home", token));
			bq.add(new BooleanClause(tq, BooleanClause.Occur.SHOULD));	
			
			query.add(new BooleanClause(bq, clause));

		}

		FullTextQuery fq = fullTextEntityManager.createFullTextQuery(query, Webbis.class);
		if (!includeDisabled){ 
			fq.enableFullTextFilter("enabledWebbis");
		}
		return fq;
	}
	
	public Integer getNumberOfMatchesFor(final String criteria, final boolean includeDisabled) {
		Object result = getJpaTemplate().execute(new JpaCallback() {
			public Object doInJpa(EntityManager em) throws PersistenceException {

				FullTextQuery fq = createSearchQuery(criteria, includeDisabled, em);
				fq.setHint("org.hibernate.cacheable", true);
				return fq.getResultSize();
			}

		});
		return (Integer) result;
	}

	@SuppressWarnings("unchecked")
	public List<Webbis> searchWebbis(final String criteria, final int firstResult, final int maxResults, final boolean includeDisabled) {
		Object results = getJpaTemplate().execute(new JpaCallback() {
			public Object doInJpa(EntityManager em) throws PersistenceException {

				FullTextQuery fq = createSearchQuery(criteria, includeDisabled, em);
				fq.setSort(new Sort(new SortField[]{
										SortField.FIELD_SCORE, 
										new SortField("birthDate",true)}));
				fq.setFirstResult(firstResult); 
				fq.setMaxResults(maxResults); 
				return fq.getResultList();
			}
		});
		return (List<Webbis>) results;
	}

	public long getNumberOfWebbisar() {
		Object results = getJpaTemplate().execute(new JpaCallback() {
			public Object doInJpa(EntityManager em) throws PersistenceException {
				Query q = em.createQuery("select count(*) from Webbis w where w.disabled = false");
				q.setHint("org.hibernate.cacheable", true);
				return q.getSingleResult();
			}
		});
		return (Long)results;
	}
	
	@SuppressWarnings("unchecked")
	public List<Webbis> getWebbisar(final int firstResult, final int maxResult) {
		Object results = getJpaTemplate().execute(new JpaCallback() {
			public Object doInJpa(EntityManager em) throws PersistenceException {
				FullTextEntityManager fullTextEntityManager = getFullTextEntityManager(em);
				
				MatchAllDocsQuery query = new MatchAllDocsQuery();
				
				FullTextQuery fq = fullTextEntityManager.createFullTextQuery( query, Webbis.class );
				fq.enableFullTextFilter("enabledWebbis");
				fq.setFirstResult(firstResult); 
				fq.setMaxResults(maxResult);
				fq.setSort(new Sort(new SortField("birthTime", true)));
				fq.setHint("org.hibernate.cacheable", true);
				
				return fq.getResultList();
			}
		});
		return (List<Webbis>)results;
	}
	
	@SuppressWarnings("unchecked")
	public List<Webbis> findAllWebbis() {
		Object results = getJpaTemplate().execute(new JpaCallback() {
			public Object doInJpa(EntityManager em) throws PersistenceException {
				Query q = em.createQuery("from Webbis");
				return q.getResultList();
			}
		});
		return (List<Webbis>)results;
	}

	@SuppressWarnings("unchecked")
	public List<Webbis> getWebbisarForAuthorId(final String authorId) {
		Object results = getJpaTemplate().execute(new JpaCallback() {
			public Object doInJpa(EntityManager em) throws PersistenceException {
				FullTextEntityManager fullTextEntityManager = getFullTextEntityManager(em);
				
				BooleanQuery query = new BooleanQuery();
				TermQuery tq = new TermQuery(new Term("authorId", authorId));
				query.add(new BooleanClause(tq, BooleanClause.Occur.MUST));
				
				FullTextQuery fq = fullTextEntityManager.createFullTextQuery( query, Webbis.class );
				fq.enableFullTextFilter("enabledWebbis");
				
				return fq.getResultList();
			}
		});
		return (List<Webbis>)results;
	}
	
	public void reindex() {
		getJpaTemplate().execute(new JpaCallback() {
			public Object doInJpa(EntityManager em) throws PersistenceException {
				FullTextSession fullTextSession = getFullTextSession(getHibernateSession(em));
				fullTextSession.purgeAll(Webbis.class);

				// Do not update the second level cache. It will just slow things down.
				fullTextSession.setCacheMode(CacheMode.GET);
				
				// Read 5000 entries at a time.
				final int BATCH_SIZE = 5000;
	
				// Due to a bug in Hibernate (HHH-1283) a join does not work here.
				// See http://opensource.atlassian.com/projects/hibernate/browse/HHH-1283
				ScrollableResults results = fullTextSession.createQuery(
						"from Webbis w").scroll();

				int index = 0;
				while( results.next() ) {
					index++;
					Webbis w = (Webbis)results.get(0);
					fullTextSession.index( results.get(0) ); //index each element
					if (index % BATCH_SIZE == 0) {
						fullTextSession.flushToIndexes(); //apply changes to indexes
						fullTextSession.clear(); //clear since the queue is processed
					}
				}
				return null;
			}
		});	
	}
		
	private Session getHibernateSession(EntityManager em) {
		return ((HibernateEntityManager)em).getSession();
	}

	@SuppressWarnings("unchecked")
	public List<Webbis> getLastestWebbis(final int maxResult) {
		Object results = getJpaTemplate().execute(new JpaCallback() {
			public Object doInJpa(EntityManager em) throws PersistenceException {
				FullTextEntityManager fullTextEntityManager = getFullTextEntityManager(em);
				
				MatchAllDocsQuery query = new MatchAllDocsQuery();
				
				FullTextQuery fq = fullTextEntityManager.createFullTextQuery( query, Webbis.class );
				fq.setSort(new Sort(new SortField("birthTime", true)));
				fq.setHint("org.hibernate.cacheable", true);
				fq.setMaxResults(maxResult);
				
				return fq.getResultList();
			}
		});
		return (List<Webbis>)results;
		
	}
	
	@SuppressWarnings("unchecked")
	public List<Webbis> getLastestWebbis(final Hospital hospital, final int maxResult) {
		Object results = getJpaTemplate().execute(new JpaCallback() {
			public Object doInJpa(EntityManager em) throws PersistenceException {
				
				FullTextEntityManager fullTextEntityManager = getFullTextEntityManager(em);
				
				TermQuery query = new TermQuery(new Term("hospitalConstant", hospital.name()));
				
				FullTextQuery fq = fullTextEntityManager.createFullTextQuery( query, Webbis.class );
				fq.enableFullTextFilter("enabledWebbis");
				fq.setSort(new Sort(new SortField("birthTime", true)));
				fq.setHint("org.hibernate.cacheable", true);
				fq.setMaxResults(maxResult);
				return fq.getResultList();
			}
		});
		return (List<Webbis>) results;
	}
	
}
