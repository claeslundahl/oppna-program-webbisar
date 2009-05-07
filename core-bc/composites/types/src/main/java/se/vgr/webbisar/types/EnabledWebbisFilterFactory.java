package se.vgr.webbisar.types;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.QueryWrapperFilter;
import org.apache.lucene.search.TermQuery;
import org.hibernate.search.annotations.Factory;
import org.hibernate.search.filter.CachingWrapperFilter;

public class EnabledWebbisFilterFactory {

	@Factory
    public Filter getFilter() {
        Filter enabledWebbisFilter = new QueryWrapperFilter(new TermQuery(new Term("disabled", "false")));
        return new CachingWrapperFilter(enabledWebbisFilter);
    }
}
