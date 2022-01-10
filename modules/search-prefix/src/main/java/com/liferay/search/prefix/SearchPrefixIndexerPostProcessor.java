package com.liferay.search.prefix;

import com.liferay.journal.model.JournalArticle;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.search.BooleanClauseOccur;
import com.liferay.portal.kernel.search.BooleanQuery;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.IndexerPostProcessor;
import com.liferay.portal.kernel.search.Query;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.Summary;
import com.liferay.portal.kernel.search.filter.BooleanFilter;
import com.liferay.portal.kernel.search.generic.MatchQuery;
import com.liferay.portal.kernel.search.generic.MatchQuery.Type;
import com.liferay.portal.kernel.search.generic.StringQuery;
import com.liferay.portal.kernel.search.generic.WildcardQueryImpl;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.LocalizationUtil;

import org.osgi.service.component.annotations.Component;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Peter Richards
 */
@Component(immediate = true, property = "indexer.class.name=com.liferay.journal.model.JournalArticle", service = IndexerPostProcessor.class)
public class SearchPrefixIndexerPostProcessor implements IndexerPostProcessor {

	@Override
	public void postProcessContextBooleanFilter(BooleanFilter booleanFilter, SearchContext searchContext)
			throws Exception {
		if (_logger.isInfoEnabled()) {
			_logger.info("postProcessContextBooleanFilter");
		}
	}

	@Override
	public void postProcessDocument(Document document, Object object) throws Exception {
		if (_logger.isInfoEnabled()) {
			_logger.info("postProcessDocument");
		}
		JournalArticle journalArticle = (JournalArticle) object;

		for (Locale locale : LanguageUtil.getAvailableLocales(journalArticle.getGroupId())) {
			final String languageId = LocaleUtil.toLanguageId(locale);
			final String content = journalArticle.getContentByLocale(languageId);

			document.addText(LocalizationUtil.getLocalizedName(SearchPrefixConstants.RAW_CONTENT_FIELD, languageId),
					content);
		}
	}

	@Override
	public void postProcessFullQuery(BooleanQuery fullQuery, SearchContext searchContext) throws Exception {
		if (_logger.isInfoEnabled()) {
			_logger.info("postProcessFullQuery");
		}

	}

	@Override
	public void postProcessSearchQuery(BooleanQuery searchQuery, BooleanFilter booleanFilter,
			SearchContext searchContext) throws Exception {
		if (_logger.isInfoEnabled()) {
			_logger.info("postProcessSearchQuery");
		}

		final Query query = GetQuery(searchContext);

		searchQuery.add(query, BooleanClauseOccur.SHOULD);
	}

	private Query GetQuery(SearchContext searchContext) {
		final String fieldName = getLocalizedFieldName(searchContext.getLocale(),
				SearchPrefixConstants.RAW_CONTENT_FIELD);
		final String value = searchContext.getKeywords();

//		final String wildcardQuery = value + "*";
//		final Query query = new WildcardQueryImpl(fieldName, wildcardQuery);

		final MatchQuery matchQuery = new MatchQuery(fieldName, value);
		matchQuery.setType(Type.PHRASE_PREFIX);
		final Query query = matchQuery;

		query.setBoost(100);
		return query;
	}

	@Override
	public void postProcessSummary(Summary summary, Document document, Locale locale, String snippet) {
		if (_logger.isInfoEnabled()) {
			_logger.info("postProcessSummary");
		}
	}

	private String getLocalizedFieldName(Locale locale, String fieldName) {
		final String languageId = LocaleUtil.toLanguageId(locale);
		return LocalizationUtil.getLocalizedName(SearchPrefixConstants.RAW_CONTENT_FIELD, languageId);
	}

	private static final Logger _logger = LoggerFactory.getLogger(SearchPrefixIndexerPostProcessor.class);
}
