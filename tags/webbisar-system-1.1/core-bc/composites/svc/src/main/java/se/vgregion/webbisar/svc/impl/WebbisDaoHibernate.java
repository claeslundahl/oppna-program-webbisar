/**
 * Copyright 2010 Västra Götalandsregionen
 *
 *   This library is free software; you can redistribute it and/or modify
 *   it under the terms of version 2.1 of the GNU Lesser General Public
 *   License as published by the Free Software Foundation.
 *
 *   This library is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public
 *   License along with this library; if not, write to the
 *   Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 *   Boston, MA 02111-1307  USA
 *
 */

package se.vgregion.webbisar.svc.impl;

import static org.hibernate.search.Search.*;
import static org.hibernate.search.jpa.Search.*;

import java.util.List;
import java.util.StringTokenizer;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TermQuery;
import org.hibernate.CacheMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.ejb.HibernateEntityManager;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaCallback;
import org.springframework.orm.jpa.JpaTemplate;
import org.springframework.stereotype.Repository;

import se.vgregion.webbisar.svc.WebbisDao;
import se.vgregion.webbisar.types.Hospital;
import se.vgregion.webbisar.types.Webbis;

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

    /**
     * {@inheritDoc}
     */
    public void delete(Webbis webbis) {
        getJpaTemplate().remove(webbis);
    }

    /**
     * {@inheritDoc}
     */
    public Webbis get(Long id) {
        return getJpaTemplate().find(Webbis.class, id);
    }

    /**
     * {@inheritDoc}
     */
    public Webbis getDetached(final Long id) {
        Object o = getJpaTemplate().execute(new JpaCallback() {
            public Object doInJpa(EntityManager em) throws PersistenceException {
                Webbis webbis = em.find(Webbis.class, id);
                getHibernateSession(em).evict(webbis);
                return webbis;
            }
        });
        return (Webbis) o;
    }

    public void save(Webbis webbis) {
        getJpaTemplate().persist(webbis);
    }

    public Webbis merge(Webbis webbis) {
        return getJpaTemplate().merge(webbis);
    }

    /**
     * Creating a full text query for given criteria, matching e.g. name, parent name and birthdate
     * 
     * @param criteria
     * @param includeDisabled
     * @param em
     * @return FullTextQuery object
     */
    private FullTextQuery createSearchQuery(final String criteria, boolean includeDisabled, EntityManager em) {
        FullTextEntityManager fullTextEntityManager = getFullTextEntityManager(em);

        BooleanQuery query = new BooleanQuery();

        StringTokenizer st = new StringTokenizer(criteria);
        while (st.hasMoreTokens()) {
            String token = st.nextToken().toLowerCase();

            BooleanClause.Occur clause = BooleanClause.Occur.SHOULD;
            if (token.startsWith("+") && token.length() > 1) {
                token = token.substring(1);
                clause = BooleanClause.Occur.MUST;
            } else if (token.startsWith("-") && token.length() > 1) {
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
        if (!includeDisabled) {
            fq.enableFullTextFilter("enabledWebbis");
        }
        return fq;
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<Webbis> searchWebbis(final String criteria, final int firstResult, final int maxResults,
            final boolean includeDisabled) {
        Object results = getJpaTemplate().execute(new JpaCallback() {
            public Object doInJpa(EntityManager em) throws PersistenceException {

                FullTextQuery fq = createSearchQuery(criteria, includeDisabled, em);
                fq.setSort(new Sort(new SortField[] { SortField.FIELD_SCORE, new SortField("birthDate", true) }));
                fq.setFirstResult(firstResult);
                fq.setMaxResults(maxResults);
                return fq.getResultList();
            }
        });
        return (List<Webbis>) results;
    }

    /**
     * {@inheritDoc}
     */
    public long getNumberOfWebbisar() {
        Object results = getJpaTemplate().execute(new JpaCallback() {
            public Object doInJpa(EntityManager em) throws PersistenceException {
                Query q = em.createQuery("select count(*) from Webbis w where w.disabled = false");
                q.setHint("org.hibernate.cacheable", true);
                return q.getSingleResult();
            }
        });
        return (Long) results;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<Webbis> getWebbisar(final int firstResult, final int maxResult) {
        Object results = getJpaTemplate().execute(new JpaCallback() {
            public Object doInJpa(EntityManager em) throws PersistenceException {
                FullTextEntityManager fullTextEntityManager = getFullTextEntityManager(em);

                MatchAllDocsQuery query = new MatchAllDocsQuery();

                FullTextQuery fq = fullTextEntityManager.createFullTextQuery(query, Webbis.class);
                fq.enableFullTextFilter("enabledWebbis");
                fq.setFirstResult(firstResult);
                fq.setMaxResults(maxResult);
                fq.setSort(new Sort(new SortField("birthTime", true)));
                fq.setHint("org.hibernate.cacheable", true);

                return fq.getResultList();
            }
        });
        return (List<Webbis>) results;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<Webbis> findAllWebbis() {
        Object results = getJpaTemplate().execute(new JpaCallback() {
            public Object doInJpa(EntityManager em) throws PersistenceException {
                Query q = em.createQuery("from Webbis");
                return q.getResultList();
            }
        });
        return (List<Webbis>) results;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<Webbis> getWebbisarForAuthorId(final String authorId) {
        Object results = getJpaTemplate().execute(new JpaCallback() {
            public Object doInJpa(EntityManager em) throws PersistenceException {
                Query q = em
                        .createQuery("SELECT w FROM Webbis w WHERE w.authorId = :author AND w.disabled = false AND w.multipleBirthMainWebbis IS NULL");
                q.setParameter("author", authorId);
                q.setHint("org.hibernate.cacheable", true);
                return q.getResultList();
            }
        });
        return (List<Webbis>) results;
    }

    /**
     * {@inheritDoc}
     */
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
                ScrollableResults results = fullTextSession.createQuery("from Webbis w").scroll();

                int index = 0;
                while (results.next()) {
                    index++;
                    fullTextSession.index(results.get(0)); // index each element
                    if (index % BATCH_SIZE == 0) {
                        fullTextSession.flushToIndexes(); // apply changes to indexes
                        fullTextSession.clear(); // clear since the queue is processed
                    }
                }
                return null;
            }
        });
    }

    private Session getHibernateSession(EntityManager em) {
        return ((HibernateEntityManager) em).getSession();
    }

    /**
     * Get latest active webbis
     * 
     * @inheritDoc
     */
    @SuppressWarnings("unchecked")
    public List<Webbis> getLastestWebbis(final int maxResult) {
        Object results = getJpaTemplate().execute(new JpaCallback() {
            public Object doInJpa(EntityManager em) throws PersistenceException {
                FullTextEntityManager fullTextEntityManager = getFullTextEntityManager(em);

                MatchAllDocsQuery query = new MatchAllDocsQuery();

                FullTextQuery fq = fullTextEntityManager.createFullTextQuery(query, Webbis.class);
                fq.setSort(new Sort(new SortField("birthTime", true)));
                fq.enableFullTextFilter("enabledWebbis");
                fq.setHint("org.hibernate.cacheable", true);
                fq.setMaxResults(maxResult);

                return fq.getResultList();
            }
        });
        return (List<Webbis>) results;

    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<Webbis> getLastestWebbis(final Hospital hospital, final int maxResult) {
        Object results = getJpaTemplate().execute(new JpaCallback() {
            public Object doInJpa(EntityManager em) throws PersistenceException {

                FullTextEntityManager fullTextEntityManager = getFullTextEntityManager(em);

                TermQuery query = new TermQuery(new Term("hospitalConstant", hospital.name()));

                FullTextQuery fq = fullTextEntityManager.createFullTextQuery(query, Webbis.class);
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
