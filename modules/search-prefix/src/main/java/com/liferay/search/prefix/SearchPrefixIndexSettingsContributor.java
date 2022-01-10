package com.liferay.search.prefix;

import com.liferay.portal.search.elasticsearch7.settings.IndexSettingsContributor;
import com.liferay.portal.search.elasticsearch7.settings.IndexSettingsHelper;
import com.liferay.portal.search.elasticsearch7.settings.TypeMappingsHelper;

import org.osgi.service.component.annotations.Component;

/**
 * @author Peter Richards
 */
@Component(
	    immediate = true,
	    service = IndexSettingsContributor.class
)
public class SearchPrefixIndexSettingsContributor implements IndexSettingsContributor {

	@Override
	public void contribute(
	    String indexName, TypeMappingsHelper typeMappingsHelper) {
	    try {
	        String mappings = ResourceUtil.readResouceAsString(
	            "META-INF/resources/mappings/index-type-mappings.json");

	        typeMappingsHelper.addTypeMappings(indexName, mappings);
	    }
	    catch (Exception e) {
	        e.printStackTrace();
	    }
	}

	@Override
	public int compareTo(IndexSettingsContributor o) {
		return -1;
	}

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public void populate(IndexSettingsHelper indexSettingsHelper) {
	}
}